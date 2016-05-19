package com.yu;

import java.io.PrintWriter;
import java.io.Writer;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.yu.entity.Report;
import com.yu.entity.ReportResponse;
import com.yu.entity.Shipment;
import com.yu.entity.Tracking;
import com.yu.util.Iso8601Util;
import com.yu.util.JsonContext;
import com.yu.util.ZsConfig;

public class ShipReportList extends Listing<Report> {
	String PATH = "v1/shipments/reportdata";
	WebTarget targetRoot = client.target(ZsConfig.URL).path(PATH);
	WebTarget target = null;
	Shipment shp;
	Iso8601Util iso = new Iso8601Util();

	public ShipReportList(Shipment sh) throws Exception {
		super();
		shp = sh;
		getList();
	}

	void getList() throws Exception {
		Params params = new Params();
		params.add("clientkey", ZsConfig.ClientKey);
		params.add("_t", System.currentTimeMillis());
		params.add("rows", ROWS);
		params.add("shipmentIdType", shp.shipmentIdType);
		params.add("id", shp.id);
		params.add("begindate", lastDateAcquired);
		// params.add("enddate", dvc.id);
		target = params.addToTarget(targetRoot);
		target = target.queryParam("sign", params.sign("GET"));
		System.out.println(target);
		String RespStr = target.request(MediaType.APPLICATION_JSON).get(String.class);
		// System.out.println(RespStr);
		//Response response = target.request(MediaType.TEXT_PLAIN_TYPE).get();

		//.out.println("response MediaType: " + response.getMediaType());
		//System.out.println("response header: " + response.getHeaders());
		// System.out.println("Ent from resp: " +
		// response.readEntity(String.class));
		ReportResponse resp = JsonContext.mapper.readValue(RespStr, ReportResponse.class);
		rowsFetched = resp.data.size();
		System.out.println("response.status: " + resp.status + " Rows:" + resp.rows + " offset:" + resp.offset
				+ " total:" + resp.totalCount + " rowsFetched: " + rowsFetched);
		for (Report rpt : resp.data) {
			elementQueue.add(rpt);
		}
		if (elementQueue.peekLast() != null) {			
			lastDateAcquired = iso.format(iso.parseTime(elementQueue.peekLast().dateAcquired)+ 60000);
		}
	}

	public static void main(String[] args) throws Exception {
		int counter = 0;
		Shipment shp = new Shipment();
		// 10281, 10297, 10298, 10299, 10300, 10301, 10302, 10303, 10364
		shp.id = "10300";
		Writer wr = new PrintWriter("/tmp/shipmentRpts" + shp.id + ".txt");
		ShipReportList rpts = new ShipReportList(shp);

		while (rpts.hasNext()) {
			Report rpt = rpts.next();
			// System.out.println("" + counter++ + ": " + rpt);
			// wr.write( rpt.toString());
			wr.write(JsonContext.mapper.writeValueAsString(rpt));
			wr.write("\r\n");
			// String geo =
			// GeoHash.geoHashStringWithCharacterPrecision(rpt.u_latitude,
			// rpt.u_longitude, 6);
			// GeoHash hsh = GeoHash.fromGeohashString(geo);
			// BoundingBox box = hsh.getBoundingBox();
			// box.getLatitudeSize();
			// float distanceLat = (float) VincentyGeodesy.distanceInMeters(
			// new WGS84Point(box.getMaxLat(), box.getMaxLon()), new
			// WGS84Point(box.getMaxLat(), box.getMinLon()));
			// float distanceLon = (float) VincentyGeodesy.distanceInMeters(
			// new WGS84Point(box.getMaxLat(), box.getMaxLon()), new
			// WGS84Point(box.getMinLat(), box.getMaxLon()));
			// String distance = "Lat*Lon: " + distanceLat + "*" + distanceLon;
			// wr.write("Geohash: " + geo + " latSize: " + box.getLatitudeSize()
			// + " lonSize: " + box.getLongitudeSize()
			// + " Distance M: " + distance + '\n');
			// trkg.addReport(rpt.u_latitude, rpt.u_longitude,
			// iso.parseTime(rpt.dateAcquired));
			wr.flush();
			// break;
			// System.out.print(rpt.dateAcquired);
			// Iso8601Util iso = new Iso8601Util();
			// System.out.println("; time: " + iso.parseTime(rpt.dateAcquired));
			// long roughTime = iso.parseTime(rpt.dateAcquired) >>20;
			// System.out.println("rough: "+iso.format(roughTime <<20));
		}
		// wr.write(trkg.toString());
		wr.flush();
		wr.close();
		System.out.println(wr.toString());
	}

}
