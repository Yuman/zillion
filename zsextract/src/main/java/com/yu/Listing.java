package com.yu;

import java.util.Deque;
import org.glassfish.jersey.client.ClientProperties;

public abstract class Listing <E> implements WsClient { 

	protected int ROWS = 100;
	protected int offset = 0;
	protected int totalCount = 0;
	protected Deque<E> elementQueue;

	Listing() {
		client.property(ClientProperties.CONNECT_TIMEOUT, 5000);
	    client.property(ClientProperties.READ_TIMEOUT,    30000);
	}
	
	boolean hasNext() {
		return (elementQueue.size() > 0 || offset < totalCount);
	}
	
	E next() throws Exception {
		return elementQueue.poll();
	}
	
	abstract void getList() throws Exception;
}