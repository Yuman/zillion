package com.yu;

import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.yu.entity.Report;
import com.yu.entity.Tracking;
import com.yu.util.JsonContext;

public class BulkJsonReader implements ReportReader {
	List<Report> rptls = new ArrayList<Report>(0);
	Iterator<Report> rpts;

	public BulkJsonReader(String filePath) throws IOException {
		String lines = new String(Files.readAllBytes(Paths.get(filePath)));
		Report[] rtpArray = JsonContext.mapper.readValue(lines, Report[].class);
		rpts = Arrays.asList(rtpArray).iterator();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yu.ReportReader#hasNext()
	 */
	public boolean hasNext() {
		return rpts.hasNext();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yu.ReportReader#next()
	 */
	public Report next() throws Exception {
		return rpts.next();
	}

	public static void main(String[] args) throws Exception {
		String dir = "C:/Users/ky073u/Documents/ZS/YXO";
		String dirOut = "C:/Users/ky073u/Documents/ZS/YXO/track";
		FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File dir, String name) {				
				return name.endsWith(".json");
			}
		};
		for (String inputFile : new File(dir).list(filter)) {
			System.out.println(inputFile);
			processFile(dir, inputFile, dirOut);
		}
	}

	static void processFile(String dir, String file, String dirOut) throws Exception {
		ReportReader rr = new BulkJsonReader(dir + "/" + file);
		Tracking trkg = ReportXformer.processReports(rr, file);
		FileWriter fw = new FileWriter(dirOut + "/" + file + ".txt");
		fw.write(trkg.toString());
		fw.close();
	}

}
