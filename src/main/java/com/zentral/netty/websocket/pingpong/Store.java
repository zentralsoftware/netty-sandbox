package com.zentral.netty.websocket.pingpong;

import io.netty.channel.ChannelHandlerContext;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Store {

	private static Map<URI, ChannelHandlerContext> contextMap;
	
	public static Map<URI, ChannelHandlerContext> getMapInstance()
	{
		if (contextMap == null)
		{
			synchronized(Store.class)
			{
				contextMap = new ConcurrentHashMap<URI, ChannelHandlerContext>();
			}
		}
		return contextMap;
	}
}
