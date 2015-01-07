package com.zentral.netty.websocket.pingpong;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Command<T> implements Serializable
{
	private static final long serialVersionUID = -5785581298237888541L;
	private CommandEnum name;
	private Map<String,String> headers = new HashMap<String,String>();
	private T body;
	public CommandEnum getName() {
		return name;
	}
	public void setName(CommandEnum name) {
		this.name = name;
	}
	public Map<String, String> getHeaders() {
		return headers;
	}
	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}
	public T getBody() {
		return body;
	}
	public void setBody(T body) {
		this.body = body;
	}
	
	
}
