package com.yu;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.yu.entity.Report;
import com.yu.util.JsonContext;

public class ReportReader {
	private final BufferedReader rdr;
	private String line;
	
	public ReportReader(String filePath) throws IOException {
		rdr = new BufferedReader(new FileReader(filePath));
		line = rdr.readLine();
	}
	
	public boolean hasNext(){
		return line !=null;
	}
	
	public Report next() throws Exception{
		Report rpt = JsonContext.mapper.readValue(line, Report.class);
		line = rdr.readLine();
		return rpt;
	}

	
	
}
