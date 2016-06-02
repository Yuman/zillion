package com.yu.stats;

public class Stats {
	private final String metric;
	private double max, min, mean, sum;
	private int count;
	
	public Stats(String mtc){
		metric = mtc;
	}
	
	public String getMetric() {
		return metric;
	}

	public double getMax() {
		return max;
	}

	public double getMin() {
		return min;
	}

	public double getMean() {
		mean = sum / count;
		return mean;
	}

	public int getCount() {
		return count;
	}

	public void input(double val) {
		if (val > max) {
			max = val;
		} else if (val < min) {
			min = val;
		}
		sum += val;
		count++;
	}
}
