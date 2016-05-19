package com.yu.entity;

import java.util.List;

import com.yu.util.Iso8601Util;

public class Passage {
	static Iso8601Util iso = new Iso8601Util();
	private final String geoHash;
	private long firstTime;
	private long lastTime;

	private int durationMin;
	private int distanceFromLast;
	private int speedMperHR;
	private int alarmCount = 0;

	public int getSpeedMperHR() {
		return speedMperHR;
	}

	public long getLastTime() {
		return lastTime;
	}

	public void setSpeedMperHR(int speedMperHR) {
		this.speedMperHR = speedMperHR;
	}

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
	
	public void addAlarms(List<Alarm> alarms){
		for (Alarm alm : alarms){
			alarmCount++;
			if (alm.rules != null){
				alarmCount += alm.rules.size();
			}
		}
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
		return geoHash.toString() + ": lastTime=" + iso.format(lastTime) + " durationMin=" + durationMin + " dist: "
				+ distanceFromLast + " Speed m/hr: " + speedMperHR + " alarms*rules: " + alarmCount;
	}

}
