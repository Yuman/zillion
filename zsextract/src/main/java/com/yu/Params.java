package com.yu;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Map;
import java.util.TreeMap;

import javax.ws.rs.client.WebTarget;

import com.yu.util.ZsConfig;

public class Params {
	static class Path {
		String name, value;
	}

	private TreeMap<String, String> map = new TreeMap<String, String>();
	private Path path;
	private String fullPath;

	/**
	 * @param name, id
	 * @param value, idValue
	 */
	void addPath(String name, String value) {
		path = new Path();
		path.name = name;
		path.value = value;
	}

	void add(String key, String value) {
		map.put(key, value);
	}

	void add(String key, long value) {
		map.put(key, String.valueOf(value));
	}

	/**
	 * Add the parameter to the target after '?'.
	 * @param targetIn Contains the base path, excluding the variable path (such as :id)
	 * @return
	 */
	WebTarget addToTarget(WebTarget targetIn) {
		WebTarget target = targetIn;
		if (path != null) {
			target = target.path(path.value);
		}
		fullPath = target.getUri().getPath();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			target = target.queryParam(entry.getKey(), entry.getValue());
		}
		return target;
	}

	public String sign(String method) throws Exception {
		StringBuilder builder = new StringBuilder();
		builder.append(ZsConfig.ClientSecret);
		for (Map.Entry<String, String> entry : map.entrySet()) {
			builder.append(entry.getKey()).append(entry.getValue());
		}
		builder.append(method).append(fullPath);
		builder.append(ZsConfig.ClientSecret);
		String sig = builder.toString();
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.reset();
		md.update(sig.getBytes(), 0, sig.length());
		sig = String.format("%032x", new BigInteger(1, md.digest()));
		return sig;
	}
}
