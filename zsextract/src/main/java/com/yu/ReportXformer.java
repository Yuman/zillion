package com.yu;

import java.io.PrintWriter;
import java.io.Writer;

import com.yu.entity.Report;
import com.yu.entity.Tracking;
import com.yu.util.Iso8601Util;
import com.yu.util.JsonContext;

import ch.hsr.geohash.BoundingBox;
import ch.hsr.geohash.GeoHash;
import ch.hsr.geohash.WGS84Point;
import ch.hsr.geohash.util.VincentyGeodesy;

/**
 * http://www.movable-type.co.uk/scripts/geohash.html
 * @author Kenny
 *
 */
public class ReportXformer {
	static Iso8601Util iso = new Iso8601Util();

	public static void main(String[] args) throws Exception {
		int shipId = 10297;
		Tracking trkg = new Tracking("Shipment", String.valueOf(shipId));		
		ReportReader rr = new ReportReader("/tmp/shipmentRpts" + shipId + ".txt");
		Writer wr = new PrintWriter("/tmp/track" + shipId + ".txt");
		int counter = 1;
		while (rr.hasNext()) {
			Report rpt = rr.next();
			System.out.println("" + counter++ + ": " + rpt);
			String geo = GeoHash.geoHashStringWithCharacterPrecision(rpt.u_latitude, rpt.u_longitude, 5);
			GeoHash hsh = GeoHash.fromGeohashString(geo);
			BoundingBox box = hsh.getBoundingBox();
			box.getLatitudeSize();
			float distanceLat = (float) VincentyGeodesy.distanceInMeters(
					new WGS84Point(box.getMaxLat(), box.getMaxLon()), new WGS84Point(box.getMaxLat(), box.getMinLon()));
			float distanceLon = (float) VincentyGeodesy.distanceInMeters(
					new WGS84Point(box.getMaxLat(), box.getMaxLon()), new WGS84Point(box.getMinLat(), box.getMaxLon()));
			String distance = "Lat*Lon: " + distanceLat + "*" + distanceLon;
			System.out.println("Geohash: " + geo + " latSize: " + box.getLatitudeSize() + " lonSize: " + box.getLongitudeSize()
					+ " Distance M: " + distance + '\n');
			
			trkg.addReport(rpt.u_latitude, rpt.u_longitude, rpt.u_locationType, iso.parseTime(rpt.dateAcquired), rpt.alarms);
			wr.flush();

		}
		wr.write(trkg.toString());
		wr.flush();
		wr.close();

	}
}
