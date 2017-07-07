package com.yu;

import java.io.PrintWriter;
import java.io.Writer;
import java.text.ParseException;

import com.yu.entity.GeoStop;
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
 * 
 * @author Kenny
 *
 */
public class ReportXformer {
	static Iso8601Util iso = new Iso8601Util();

	public static void main(String[] args) throws Exception {
		int shipId = 10297;
		ReportReader rr = new JsonReader("/tmp/shipmentRpts" + shipId + ".txt");		
		Tracking trkg =processReports(rr, String.valueOf(shipId));
		Writer wr = new PrintWriter("/tmp/track" + shipId + ".txt");
		wr.write(trkg.toString());
		wr.flush();
		wr.close();

	}

	public static Tracking processReports(ReportReader rr, String id) throws Exception {
		Tracking trkg = new Tracking("Shipment", id);
		while (rr.hasNext()) {
			Report rpt = rr.next();

			trkg.addReport(rpt.u_latitude, rpt.u_longitude, rpt.u_locationType, iso.parseTime(rpt.dateAcquired),
					rpt.alarms);			
		}
		return trkg;
	}
}
