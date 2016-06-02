package com.yu.aggreg;

public class Stability {	

	final boolean hasSurged;
	final boolean hasDropped;
	final long time;
	double mean;
	double measure;
	
	public double getMean() {
		return mean;
	}

	public double getMeasure() {
		return measure;
	}

	public boolean hasSurged() {
		return hasSurged;
	}

	public boolean hadDropped() {
		return hasDropped;
	}
	
	public long getTime(){
		return time;
	}

	public Stability(long time, boolean hasSurged, boolean hasDropped, double mean, double measure) {
		this.time = time;
		this.hasSurged = hasSurged;
		this.hasDropped = hasDropped;
		this.mean = mean;
		this.measure = measure;
	}
	
	@Override
	public String toString() {
		return "Stability [hasSurged=" + hasSurged + ", hasDropped=" + hasDropped + ", mean=" + mean + ", measure="
				+ measure + "]";
	}
}
