package com.zentral.netty.websocket.pingpong;

import java.net.URI;

public class Node {
	private String identifier;
	private URI endpoint;

	public Node() {
		
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public URI getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(URI endpoint) {
		this.endpoint = endpoint;
	}

}
