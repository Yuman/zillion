package com.yu.aggreg;

import java.util.Deque;
import java.util.LinkedList;

/**
 * Examines time-series data input and detects abnormal surges.
 * 
 * @author Kenny
 *
 */
public class MovingStats {
	// https://en.wikipedia.org/wiki/Algorithms_for_calculating_variance#Online_algorithm
	private final int lengthLimit;
	private double stabilityThreshold = 1.0;
	private int length = 0;
	private Deque<Double> measures; // sliding window containing the raw data
									// for the mean/average
	private Deque<Double> variances; // sliding window containing variance
										// components for varSum
	private Deque<Long> times;
	private double sum;
	double varSum;
	private long timeSpan;
	private double stdev;

	/**
	 * @param len
	 *            Size of the moving data window.
	 */
	public MovingStats(int len) {
		lengthLimit = len;
		measures = new LinkedList<Double>();
		times = new LinkedList<Long>();
		variances = new LinkedList<Double>();
	}

	/**
	 * @param time
	 * @param measure
	 * @return A value > 1.0 signals abnormal surge for temperature. A negative
	 *         value signals the surge reversal.
	 */
	public Stability addData(long time, double measure) {
		double var = 0.0;
		times.addLast(time);
		measures.addLast(measure);

		if (length < lengthLimit) {
			length++;
			var = measure - sum / length;
			sum += measure;
			var *= (measure - sum / length);
		} else { // keep length
			timeSpan = time - times.pollFirst();
			var = measure - sum / length;
			sum += measure;
			sum -= measures.pollFirst();
			var *= (measure - sum / length);
			varSum -= variances.pollFirst();
		}
		varSum += var;
		variances.addLast(var);

		return isNormal(time, variances.peekFirst(), variances.peekLast(), sum / length, measure);
	}

	private Stability isNormal(long time, double startVar, double endVar, double mean, double measure) {
		if (startVar <= 0 || endVar <= 0) {
			return new Stability(time, false, false, mean, measure);
		}
		double bump = (measure - mean) / Math.sqrt(varSum / length);
		return new Stability(time, bump > stabilityThreshold, bump < 0, mean, measure);
	}

	public long getTimeSpan() {
		return timeSpan;
	}

	public double getStdev() {
		return stdev;
	}

	public double getSum() {
		return sum;
	}

	public static void main(String[] args) {
		MovingStats stats = new MovingStats(13);
		Stability st;
		double bump, measure;
		for (int i = 0; i < 17; i++) {
			measure = 19.5 + Math.random();
			st = stats.addData(i, measure);
			System.out.println(
					"flat add: mean=" + stats.getSum() / stats.length + " measure : " + measure + "; st=" + st);
		}
		for (int i = 0; i < 17; i++) {
			measure = 19.5 + Math.random() + Math.pow(1.2, i);
			st = stats.addData(i, measure);
			System.out.println(
					"asceding add: mean=" + stats.getSum() / stats.length + " measure : " + measure + "; st=" + st);
		}
		measure = stats.getSum() / stats.length;
		for (int i = 0; i < 17; i++) {
			measure = measure - i * Math.random();
			st = stats.addData(i, measure);
			System.out.println("desceding add: mean=" + stats.getSum() / stats.length + " measure : " + measure
					+ "; st=" + st);
		}
	}

}
