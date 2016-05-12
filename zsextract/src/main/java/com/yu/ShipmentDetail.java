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
import com.yu.entity.Shipment;
import com.yu.entity.ShipmentResponse;
import com.yu.entity.Tracking;
import com.yu.entity.ZsResponse;
import com.yu.util.Iso8601Util;
import com.yu.util.JsonContext;
import com.yu.util.ZsConfig;

import ch.hsr.geohash.BoundingBox;
import ch.hsr.geohash.GeoHash;
import ch.hsr.geohash.WGS84Point;
import ch.hsr.geohash.util.VincentyGeodesy;

public class ShipmentDetail implements WsClient{
	String PATH = "v1/shipments";
	WebTarget targetRoot = client.target(ZsConfig.URL).path(PATH);
	WebTarget target = null;
	String shipmentId;
	String lastDateAcquired = (new Iso8601Util()).back(-1);

	public ShipmentDetail(String shipId) throws Exception {
		shipmentId = shipId;
	}

	void getReport() throws Exception {
		Params params = new Params();
		params.add("clientkey", ZsConfig.ClientKey);
		params.add("_t", System.currentTimeMillis());
		params.add("shipmentIdType", 1);
		params.addPath("id", shipmentId);
		target = params.addToTarget(targetRoot);
		System.out.println(target);
		target = target.queryParam("sign", params.sign("GET"));
		String RespStr = target.request(MediaType.TEXT_PLAIN_TYPE).get(String.class);
		System.out.println(RespStr);
		Response response = target.request(MediaType.TEXT_PLAIN_TYPE).get();
		System.out.println("response: " + response);
		System.out.println("Ent from resp: " + response.readEntity(String.class));
		ZsResponse resp = JsonContext.mapper.readValue(RespStr, ZsResponse.class);
		System.out.println("response.status: " + resp.status);

	}

	public static void main(String[] args) throws Exception {
		int counter = 0;
		Writer wr = new PrintWriter("/tmp/shipment" + 10592 + ".txt");
		ShipmentDetail rpt = new ShipmentDetail("10592");
		rpt.getReport();
		Iso8601Util iso = new Iso8601Util();

		wr.write('\n');
		wr.flush();
		wr.close();

	}

}
