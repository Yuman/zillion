package com.yu.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * This, and the libs jackson-*-2.x.jar, are used by the test package only 
 * and should be removed from the final deployment.
 *
 */
public class JsonContext {
	public static final String logPath = "/tmp/ZSextract.log";
	public static final ObjectMapper mapper = new ObjectMapper();
	static {
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}
}
