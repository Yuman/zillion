package com.yu.entity;

public class Cell {
	public Cell(String geoHash, int durationMin) {
		this.geoHash = geoHash;
		this.durationMin = durationMin;
	}
	String geoHash;
	int durationMin;
}
