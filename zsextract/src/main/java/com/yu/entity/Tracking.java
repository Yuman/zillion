package com.yu.entity;

import java.text.ParseException;
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
	private WGS84Point previousPoint;
	private final String sourceType, id;

	public Tracking(String sourceType, String id) {
		this.sourceType = sourceType;
		this.id = id;
	}

	public void addReport(double lat, double lon, int location_type, long time, List<Alarm> alarms) throws Exception {
		String geo;
		Passage psg = track.peekLast();
		if (location_type == 0) {
			geo = psg.getGeoHash();
		} else {
			geo = GeoHash.geoHashStringWithCharacterPrecision(lat, lon, 5);
			psg = compactGeo(geo);
		}

		if (psg == null || !psg.getGeoHash().equals(geo)) {// at the start or
															// entering new
															// place
			Passage psgNew = new Passage(geo);
			//WGS84Point currPoint = new WGS84Point(lat, lon);
			if (psg != null) { // not at starting point
				int dist = distanceBetween(psg.getGeoHash(), geo);
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

		psg = track.peekLast(); // the tail may have changed.
		psg.addReport(time); // staying in place
		if (alarms != null) {
			psg.addAlarms(alarms);
		}

		if (minTime == 0) {
			minTime = time;
		}
		if (maxTime < time) {
			maxTime = time;
		}
	}

	private Passage compactGeo(String geo) {// handles the tailing
											// A(p2)-B(p1)-A(p0) sequence
		if (track.size() < 2) {
			return track.peekLast();
		}
		Passage p1 = track.pollLast();
		Passage p2 = track.peekLast();
		if (geo.equals(p2.getGeoHash())) { // dropped p1
			p2.setAlarmCount(p2.getAlarmCount() + p1.getAlarmCount());
			return p2;
		} else {
			track.offerLast(p1);
			return p1;
		}
	}

	private int distanceBetween(String geo1, String geo2) {
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
		boolean extended;
		iter = track.listIterator(iterCurrPos);
		while (iter.hasNext()) {
			Passage p = iter.next();
			extended = rt.extend(new Cell(p.getGeoHash(), p.getDurationMin()));
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
		sb.append("Max Speed (km/hr): " + speedStats().getMax() + '\n');
		return sb.toString();
	}

}
