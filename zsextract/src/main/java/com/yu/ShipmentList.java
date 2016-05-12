package com.yu;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.LinkedList;
import java.util.Queue;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.yu.entity.Device;
import com.yu.entity.DeviceResponse;
import com.yu.entity.Shipment;
import com.yu.entity.ShipmentResponse;
import com.yu.util.JsonContext;
import com.yu.util.ZsConfig;

public class ShipmentList extends Listing <Shipment> {
	String PATH = "v1/shipments/active";
	WebTarget targetRoot = client.target(ZsConfig.URL).path(PATH);
	WebTarget target = null;
	Writer wr = new PrintWriter("/tmp/ZSExtract.log");
	
	public ShipmentList() throws Exception{
		getList();
	}

	void getList() throws Exception {
		Params params = new Params();
		params.add("clientkey", ZsConfig.ClientKey);
		params.add("_t", System.currentTimeMillis());
		params.add("rows", ROWS);
		params.add("offset", offset);
		target = params.addToTarget(targetRoot);
		System.out.println(target);
		target = target.queryParam("sign", params.sign("GET"));
		String RespStr = target.request(MediaType.TEXT_PLAIN_TYPE).get(String.class);
		System.out.println(RespStr);
		Response response = target.request(MediaType.TEXT_PLAIN_TYPE).get();
		System.out.println("response: " + response);
		System.out.println("Ent from resp: " + response.readEntity(String.class));
		ShipmentResponse resp = JsonContext.mapper.readValue(RespStr, ShipmentResponse.class);
		totalCount = resp.totalCount;
		offset = resp.offset + resp.rows;
		System.out.println("response.status: " + resp.status);
		elementQueue = new LinkedList<Shipment>();
		for (Shipment shp: resp.data){
			elementQueue.add(shp);
		}
		wr.flush();
		wr.write("offset: " + offset + "; totalCount: " + totalCount + "+++++++++++++++++\n");
		wr.flush();
	}

	public Shipment next() throws Exception {
		if (elementQueue.size() > 0) {
			return elementQueue.poll();
		} else if (offset < totalCount) {
			getList();
			return elementQueue.poll();
		}
		return null;
	}

	public static void main(String[] args) throws Exception {
		ShipmentList list = new ShipmentList();
		int count=0;
		Writer wr = new PrintWriter("/tmp/ZSextract-Shipments.txt");
		while (list.hasNext()){
			Shipment  shp = list.next();
			System.out.println("" + count++ + ": " + shp);
			wr.write("" + count + ": " + shp + '\n');
			wr.flush();

		}
		
		wr.close();
	}

}
