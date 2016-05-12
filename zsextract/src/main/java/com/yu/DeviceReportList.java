package com.yu;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.yu.entity.Device;
import com.yu.entity.Report;
import com.yu.entity.ReportResponse;
import com.yu.entity.Tracking;
import com.yu.util.Iso8601Util;
import com.yu.util.JsonContext;
import com.yu.util.ZsConfig;

import ch.hsr.geohash.BoundingBox;
import ch.hsr.geohash.GeoHash;
import ch.hsr.geohash.WGS84Point;
import ch.hsr.geohash.util.VincentyGeodesy;

public class DeviceReportList extends Listing <Report> {
	String PATH = "v1/device/reportdata";
	WebTarget targetRoot = client.target(ZsConfig.URL).path(PATH);
	WebTarget target = null;
	Device dvc;
	String lastDateAcquired = (new Iso8601Util()).back(-1);

	public DeviceReportList(Device device) throws Exception {
		dvc = device;
		getList();
	}

	void getList() throws Exception {
		Params params = new Params();
		params.add("clientkey", ZsConfig.ClientKey);
		params.add("_t", System.currentTimeMillis());
		params.add("rows", ROWS);
		params.add("deviceIdType", dvc.deviceTypeId);
		params.add("deviceid", dvc.id);// was spelled deviceId
		params.add("begindate", lastDateAcquired);
		// params.add("enddate", dvc.id);
		target = params.addToTarget(targetRoot);
		System.out.println(target);
		target = target.queryParam("sign", params.sign("GET"));
		String RespStr = target.request(MediaType.TEXT_PLAIN_TYPE).get(String.class);
		System.out.println(RespStr);
		Response response = target.request(MediaType.TEXT_PLAIN_TYPE).get();
		System.out.println("response: " + response);
		System.out.println("Ent from resp: " + response.readEntity(String.class));
		ReportResponse resp = JsonContext.mapper.readValue(RespStr, ReportResponse.class);
		System.out.println("response.status: " + resp.status);
		elementQueue = new LinkedList<Report>();
		for (Report rpt : resp.data) {
			elementQueue.add(rpt);
		}
		if (elementQueue.peekLast() != null) {
			lastDateAcquired = elementQueue.peekLast().dateAcquired;
		}
	}

	public static void main(String[] args) throws Exception {
		int counter = 0;
		Device dev = new Device();
		dev.id = "335";
		dev.tag = "010200036";
		Writer wr = new PrintWriter("/tmp/dev" + dev.tag + ".txt");
		DeviceReportList rpts = new DeviceReportList(dev);
		Tracking trkg = new Tracking();
		Iso8601Util iso = new Iso8601Util();
		while (rpts.hasNext()) {
			Report rpt = rpts.next();
			System.out.println("" + counter++ + ": " + rpt);
			wr.write("" + counter + ": " + rpt + '\n');
			String geo = GeoHash.geoHashStringWithCharacterPrecision(rpt.u_latitude, rpt.u_longitude, 6);
			GeoHash hsh = GeoHash.fromGeohashString(geo);
			BoundingBox box = hsh.getBoundingBox();
			box.getLatitudeSize();
			float distanceLat = (float) VincentyGeodesy.distanceInMeters(
					new WGS84Point(box.getMaxLat(), box.getMaxLon()), new WGS84Point(box.getMaxLat(), box.getMinLon()));
			float distanceLon = (float) VincentyGeodesy.distanceInMeters(
					new WGS84Point(box.getMaxLat(), box.getMaxLon()), new WGS84Point(box.getMinLat(), box.getMaxLon()));
			String distance = "Lat*Lon: " + distanceLat + "*" + distanceLon;
			wr.write("Geohash: " + geo + " latSize: " + box.getLatitudeSize() + " lonSize: " + box.getLongitudeSize()
					+ " Distance M: " + distance + '\n');
			trkg.addReport(rpt.u_latitude, rpt.u_longitude, iso.parseTime(rpt.dateAcquired));

			// break;
			// System.out.print(rpt.dateAcquired);
			// Iso8601Util iso = new Iso8601Util();
			// System.out.println("; time: " + iso.parseTime(rpt.dateAcquired));
			// long roughTime = iso.parseTime(rpt.dateAcquired) >>20;
			// System.out.println("rough: "+iso.format(roughTime <<20));
		}
		wr.write('\n');
		wr.write(trkg.toString());
		wr.flush();
		wr.close();
		System.out.println(trkg);
	}

}
