package com.yu;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import com.yu.entity.GeoStop;

import ch.hsr.geohash.GeoHash;

public class KnownGeoStops {
	private static HashMap<String, GeoStop> stops = new HashMap<String, GeoStop>();
	static {load();}
//	public static void main(String[] args) throws IOException {
//
//		load ();
//	}
	
	static void load() {
		try {
			//loadFromFile("resources/airports-extended.csv");
			loadFromFile("resources/european-train-stations-lean.csv");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private static void loadFromFile(String file) throws IOException{
		String filePath = KnownGeoStops.class.getClassLoader().getResource(file).getPath();
		Reader in = new FileReader(filePath);
		Iterable<CSVRecord> records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
		GeoStop stop;
		for (CSVRecord rcd : records){
			stop = build(rcd);
			stops.put(stop.geohash, stop);
		}
		in.close();
	}
	
	private static GeoStop build(CSVRecord rcd){
		GeoStop stop = new GeoStop();
		stop.id = rcd.get("id");
		stop.name = rcd.get("name");
		//stop.city = rcd.get("city");
		stop.country = rcd.get("country");
		//stop.type = rcd.get("type");
		stop.geohash = GeoHash.geoHashStringWithCharacterPrecision(Double.valueOf(rcd.get("lat")), Double.valueOf(rcd.get("lng")), 5);
		return stop;
	}
	
	public static GeoStop findOne(double lat, double lng){		
		return stops.get(GeoHash.geoHashStringWithCharacterPrecision(lat, lng, 5));
	}
	
	public static GeoStop findOne(String geohash){
		return stops.get(geohash);
	}

}
