package com.yu;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.yu.entity.Report;
import com.yu.util.JsonContext;

public class JsonReader implements ReportReader {
	private final BufferedReader rdr;
	private String line;
	
	public JsonReader(String filePath) throws IOException {
		rdr = new BufferedReader(new FileReader(filePath));
		line = rdr.readLine();
	}
	
	/* (non-Javadoc)
	 * @see com.yu.ReportReader#hasNext()
	 */
	public boolean hasNext(){
		return line !=null;
	}
	
	/* (non-Javadoc)
	 * @see com.yu.ReportReader#next()
	 */
	public Report next() throws Exception{
		Report rpt = JsonContext.mapper.readValue(line, Report.class);
		line = rdr.readLine();
		return rpt;
	}

	
	
}
