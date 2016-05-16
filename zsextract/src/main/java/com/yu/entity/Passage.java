package com.yu.entity;

public class Passage {
	private final String geoHash;
	private long firstTime;
	private long lastTime;
	private int durationMin;
	private int distanceFromLast;
	
	public int getDistanceFromLast() {
		return distanceFromLast;
	}

	public void setDistanceFromLast(int distanceFromLast) {
		this.distanceFromLast = distanceFromLast;
	}

	public int getDurationMin() {
		return durationMin;
	}

	public Passage(String geoHash) {
		this.geoHash = geoHash;
	}
	
	public String getGeoHash() {
		return geoHash;
	}

	public void addReport(long time) {
		if (firstTime == 0 || time < firstTime) {
			firstTime = time;
			durationMin = 0;
		}
		if (time > lastTime) {
			lastTime = time;
		}
		durationMin = (int) (lastTime - firstTime) / 60000;
	}

	@Override
	public String toString() {
		return geoHash.toString() + ": lastTime (sec)=" + lastTime/1000 + " durationMin=" + durationMin + " dist: " + distanceFromLast;
	}

}
