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
import com.yu.util.JsonContext;
import com.yu.util.ZsConfig;

public class DeviceList extends Listing <Device> {
	String PATH = "v1/devices";
	WebTarget targetRoot = client.target(ZsConfig.URL).path(PATH);
	WebTarget target = null;
	Writer wr = new PrintWriter(JsonContext.logPath);
	
	public DeviceList() throws Exception{
		super();
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
		//System.out.println("response: " + response);
		//System.out.println("Ent from resp: " + response.readEntity(String.class));
		DeviceResponse resp = JsonContext.mapper.readValue(RespStr, DeviceResponse.class);
		totalCount = resp.totalCount;
		offset = resp.offset + resp.rows;
		System.out.println("response.status: " + resp.status);
		elementQueue = new LinkedList<Device>();
		for (Device dvc: resp.data){
			elementQueue.add(dvc);
		}
		//wr.flush();
		//wr.write("offset: " + offset + "; totalCount: " + totalCount + "+++++++++++++++++\n");
		//wr.flush();
	}

	public static void main(String[] args) throws Exception {
		DeviceList list = new DeviceList();
		int count=0;
		Writer wr = new PrintWriter("/tmp/ZSextract-Devices.txt");
		while (list.hasNext()){
			Device  dev = list.next();
			//System.out.println("" + count++ + ": " + dev);
			wr.write("" + count++ + ": " + dev + '\n');
			wr.flush();
			//ReportList rpt = new ReportList(dev);	
			//System.out.println("Report: " + rpt.next());
		}
		
		wr.close();
	}

}
