package com.yu;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

public interface WsClient {
	Client client = ClientBuilder.newClient();
}
