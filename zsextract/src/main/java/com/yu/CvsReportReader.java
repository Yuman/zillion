package com.yu;

import java.io.FileReader;
import java.io.Reader;
import java.util.Deque;
import java.util.LinkedList;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import com.yu.entity.Report;

public class CvsReader implements ReportReader{
	private final Reader in;
	private Deque<Report> reports = new LinkedList<Report>();
	
	public CvsReader(String filePath) throws Exception{
		in = new FileReader(filePath);
		Iterable<CSVRecord> records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
		for (CSVRecord rcd : records){
			reports.offerFirst(convert(rcd));
		}
		in.close();
	}
	
	
	public static void main(String[] args) throws Exception {
		CvsReader rd = new CvsReader("C:/Users/446549/Documents/ZS/ShpData/ShipmentReports 模板 日期 杭州CDC-广州RDC.csv");
		while (rd.hasNext()) {
			System.out.println(rd.next());
		}
	}

	private static Report convert(CSVRecord record) {
		Report rpt = new Report();
		rpt.clientId = 0;
		rpt.dateAcquired = record.get("Acquired Time");
		rpt.dateReceived = record.get("Received Time");
		rpt.u_tag = record.get("Device Tag");
		String accel = record.get("Acceleration (g)").substring(0, 5);
		rpt.u_acceleration = Float.parseFloat(accel);
		rpt.u_locationType = record.get("Location Type").equals("Unknown") ? 0 : 1;
		if (rpt.u_locationType != 0) {
			rpt.u_latitude = Float.parseFloat(record.get("Latitude"));
			rpt.u_longitude = Float.parseFloat(record.get("Longitude"));
		}
		rpt.u_humidity = Float.parseFloat(record.get("Humidity (%)"));
		rpt.u_light = Float.parseFloat(record.get("Light (Lux)"));
		rpt.u_pressure = Float.parseFloat(record.get("Pressure (Pa)"));
		rpt.u_temperature = Float.parseFloat(record.get("Temperature (C)"));

		return rpt;
	}


	public boolean hasNext() {
		return !reports.isEmpty();
	}

	public Report next() throws Exception {		
		return reports.pollFirst();
	}

}
