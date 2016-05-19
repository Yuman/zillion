package com.yu.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 *
 */
public class JsonContext {
	public static final String logPath = "/tmp/ZSextract.log";
	public static final ObjectMapper mapper = new ObjectMapper();
	static {
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}
	
	public static List<?> read(String filePath) throws Exception{
		ArrayList<?> data = mapper.readValue(new File(filePath), ArrayList.class);
		return data;
	}
	
	public static void write(List<String> data, String filePath ) throws Exception{
		mapper.writeValue(new File(filePath), data);
	}
	
	public static void main(String[] args) throws Exception {
		List<String> data = new ArrayList<String>();
		data.add("one");
		data.add("two");
		data.add("three");
		write(data, "/tmp/Strings.txt");
		System.out.println(data.getClass());
		System.out.println(read("/tmp/Strings.txt"));
	}
}
