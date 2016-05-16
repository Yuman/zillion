package com.yu.entity;

import java.text.ParseException;
import java.util.LinkedList;
import java.util.ListIterator;
import com.yu.util.Iso8601Util;

import ch.hsr.geohash.GeoHash;
import ch.hsr.geohash.WGS84Point;
import ch.hsr.geohash.util.VincentyGeodesy;

/**
 * The trajectory of a device
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

	public void addReport(double lat, double lon, int location_type, long time) throws Exception {
		String geo;
		Passage psg = track.peekLast();
		if (location_type == 0) {
			geo = psg.getGeoHash();
		} else {
			geo = GeoHash.geoHashStringWithCharacterPrecision(lat, lon, 5);
		}

		if (psg == null || !psg.getGeoHash().equals(geo)) {
			psg = new Passage(geo);
			WGS84Point currPoint = new WGS84Point(lat, lon);
			if (previousPoint != null) {
				psg.setDistanceFromLast((int) VincentyGeodesy.distanceInMeters(previousPoint, currPoint));
			}
			psg.addReport(time);
			track.offerLast(psg);
			previousPoint = currPoint;
		} else {
			psg.addReport(time);
		}

		if (minTime == 0) {
			minTime = time;
		}
		if (maxTime < time) {
			maxTime = time;
		}
	}
	
	public int totalTimeHR(){
		return (int)((maxTime - minTime)/60000/60);
	}
	
	public int totalDistanceKM(){
		int dist = 0;
		for (Passage psg : track){
			dist += psg.getDistanceFromLast();
		}
		return dist/1000;
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
		sb.append("Tracking: \n");
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
		return sb.toString();
	}

}
