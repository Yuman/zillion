package com.yu.entity;

import java.util.LinkedList;
import java.util.ListIterator;
import com.yu.util.Iso8601Util;

import ch.hsr.geohash.GeoHash;

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
	private long minTime, maxTime;

	public void addReport(float lat, float lon, long time) {
		String geo = GeoHash.geoHashStringWithCharacterPrecision(lat, lon, 6);
		Passage psg = track.peekLast();
		if (psg == null || !psg.getGeoHash().equals(geo)) {
			psg = new Passage(geo);
			psg.addReport(time);
			track.offerLast(psg);
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
	
	public Route learnRoute(){
		// a route can't go backward
		Route rt = new Route();
		boolean extended;
		iter = track.listIterator(iterCurrPos);
		while (iter.hasNext()){
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
		return sb.toString();
	}

}
