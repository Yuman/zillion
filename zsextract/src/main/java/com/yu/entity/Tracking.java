package com.yu.entity;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import com.yu.stats.Stats;
import com.yu.util.Iso8601Util;

import ch.hsr.geohash.GeoHash;
import ch.hsr.geohash.WGS84Point;
import ch.hsr.geohash.util.VincentyGeodesy;

/**
 * The trajectory of a device/shipment
 * 
 * @author Kenny
 *
 */
public class Tracking {
	private LinkedList<Passage> track = new LinkedList<Passage>();
	private ListIterator<Passage> iter;
	private int iterCurrPos = 0;
	private long minTime, maxTime; // start and end times of tracking
	private final String sourceType, id;

	public Tracking(String sourceType, String id) {
		this.sourceType = sourceType;
		this.id = id;
	}

	public void addReport(double lat, double lon, int location_type, long time, List<Alarm> alarms) throws Exception {
		String geo;
		Passage lastPsg = track.peekLast();
		if (location_type == 0 && lastPsg != null) { // no current position,
			geo = lastPsg.getGeoHash(); // rolls back
		} else if (location_type == 0 && lastPsg == null) { // starting with no
															// position, nothing
															// to do
			return;
		} else {// with position and not starting
			geo = GeoHash.geoHashStringWithCharacterPrecision(lat, lon, 5);
			lastPsg = compactGeo(geo);
		}

		if (lastPsg == null || !lastPsg.getGeoHash().equals(geo)) {// at the
																	// start or
			// entering new
			// place
			Passage psgNew = new Passage(geo);
			// WGS84Point currPoint = new WGS84Point(lat, lon);
			if (lastPsg != null) { // not at starting point
				int dist = distanceInMeterBetween(lastPsg.getGeoHash(), geo);
				psgNew.setDistanceFromLast(dist);
				long lastTime = track.peekLast().getLastTime();
				double min = (time - lastTime) / 60000;
				if (min > 2 && dist > 500) {
					int speed = (int) (dist * 60.0 / min);
					psgNew.setSpeedMperHR(speed);
				}
			}
			track.offerLast(psgNew);
		}

		lastPsg = track.peekLast(); // the tail may have changed.
		lastPsg.addReport(time); // update lastTime
		if (alarms != null) {
			lastPsg.addAlarms(alarms);
		}

		if (minTime == 0) {
			minTime = time;
		}
		if (maxTime < time) {
			maxTime = time;
		}
	}

	/**
	 * Compaction is needed when a non-moving device is near a border, or a
	 * corner, and generates geopoints that, due to inaccuracy, fall in two or
	 * three geohashes.
	 */
	private Passage compactGeo(String geo) {// handles the tailing
											// A(p2)-B(p1)-A(p0) sequence,
											// inaccurate positioning
											// causing swing between two
											// geohashes
		if (track.size() < 3) {
			return track.peekLast();
		}
		Passage p1 = track.pollLast();
		Passage p2 = track.pollLast();
		Passage p3 = track.peekLast();

		// https://gis.stackexchange.com/questions/18330/using-geohash-for-proximity-searches
		if (geo.equals(p2.getGeoHash())) { // dropping p1, A-B-A compacting into
											// A
			p2.setAlarmCount(p2.getAlarmCount() + p1.getAlarmCount());
			p2.mergeDuration(p1.getDurationMin());
			return p2;
		} else if (geo.equals(p3.getGeoHash())) {// at corner, dropping p1 and
													// p2
			//A-B-C-A compacting into A
			p3.setAlarmCount(p3.getAlarmCount() + p1.getAlarmCount());
			p3.mergeDuration(p1.getDurationMin());
			p3.setAlarmCount(p3.getAlarmCount() + p2.getAlarmCount());
			p3.mergeDuration(p2.getDurationMin());
			return p3;
		} else {
			track.offerLast(p2);
			track.offerLast(p1);
			return p1;
		}

	}

	private int distanceInMeterBetween(String geo1, String geo2) {
		return (int) VincentyGeodesy.distanceInMeters(GeoHash.fromGeohashString(geo1).getBoundingBoxCenterPoint(),
				GeoHash.fromGeohashString(geo2).getBoundingBoxCenterPoint());
	}

	public int totalTimeHR() {
		return (int) ((maxTime - minTime) / 3600000);
	}

	public Stats speedStats() {
		Stats spd = new Stats("speed");
		for (Passage psg : track) {
			spd.input(psg.getSpeedMperHR());
		}
		return spd;
	}

	public int totalDistanceKM() {
		int dist = 0;
		for (Passage psg : track) {
			dist += psg.getDistanceFromLast();
		}
		return dist / 1000;
	}

	public Route learnRoute() {
		// a route can't go backward
		Route rt = new Route();
		iter = track.listIterator(iterCurrPos);
		while (iter.hasNext()) {
			Passage p = iter.next();
			rt.extend(new Cell(p.getGeoHash(), p.getDurationMin()));
		}
		iterCurrPos = iter.nextIndex();
		return rt;
	}

	@Override
	public String toString() {
		Iso8601Util iso = new Iso8601Util();
		StringBuilder sb = new StringBuilder();
		sb.append("http://www.movable-type.co.uk/scripts/geohash.html\n");
		sb.append("Tracking " + sourceType + " " + id + ": \n");
		sb.append(iso.format(minTime));
		sb.append("-->");
		sb.append(iso.format(maxTime));
		sb.append('\n');
		for (Passage psg : track) {
			sb.append(psg);
			sb.append('\n');
		}
		sb.append("Total Time (hr): " + totalTimeHR() + '\n');
		sb.append("Total Dist (km): " + totalDistanceKM() + '\n');
		sb.append("Max Speed (km/hr): " + speedStats().getMax() / 1000 + '\n');
		return sb.toString();
	}

}
