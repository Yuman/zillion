package com.yu.entity;

public class GeoStop {
	public String id;
	public String name;
	public String geohash;
	public double lat;
	public double lng;
	public String city;
	public String country;
	public String type;
	@Override
	public String toString() {
		return "GeoStop [name=" + name + ", geohash=" + geohash + ", city=" + city + ", country=" + country + ", type="
				+ type + "]";
	}
	
	
	
}
