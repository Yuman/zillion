package com.yu;

import java.util.Deque;
import java.util.LinkedList;

import org.glassfish.jersey.client.ClientProperties;

import com.yu.util.Iso8601Util;

public abstract class Listing<E> implements WsClient {

	protected int ROWS = 100;
	protected int rowsFetched;
	protected int offset = 0;
	protected int totalCount = 0;
	protected Deque<E> elementQueue = new LinkedList<E>();
	protected String lastDateAcquired = (new Iso8601Util()).back(-1);
	protected String endDate = (new Iso8601Util()).now();

	Listing() {
		client.property(ClientProperties.CONNECT_TIMEOUT, 5000);
		client.property(ClientProperties.READ_TIMEOUT, 30000);
	}

	boolean hasNext() {
		if (totalCount > 0) {
			return (elementQueue.size() > 0 || offset < totalCount);
		} else {
			return elementQueue.size() > 0 || rowsFetched == ROWS; // if select by date, totalCount==0
		}
	}

	E next() throws Exception {
		if (elementQueue.size() == 0) {
			getList();
		}
		return elementQueue.poll();
	}

	abstract void getList() throws Exception;
}