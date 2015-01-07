package com.zentral.netty.websocket.pingpong;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CheckWorker implements Runnable{
	
	public final String originator;
	
	public CheckWorker(String originator)
	{
		this.originator = originator;
	}
	
	public void run()
	{
		Map<URI, ChannelHandlerContext> map = Store.getMapInstance();
		List<ChannelFuture> futures = new ArrayList<ChannelFuture>();
		for (Entry<URI, ChannelHandlerContext> entry:map.entrySet())
		{
			ChannelHandlerContext context = entry.getValue();
			URI uri = entry.getKey();
			ObjectMapper objectMapper = new ObjectMapper();
			HeartBeatCommand request = new HeartBeatCommand();
			request.setName(CommandEnum.HEARTBEAT);
			request.getHeaders().put(Constants.COMMAND_HEADER_SOURCE, this.originator);
			request.getHeaders().put(Constants.COMMAND_HEADER_DESTINATION, uri.toString());
			request.setBody(Constants.COMMAND_HEARTBEAT_MESSAGE);
			try {
				context.channel().writeAndFlush(new TextWebSocketFrame(objectMapper.writeValueAsString(request)));
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}				
		}	
		for (ChannelFuture future:futures)
		{
			try {
				future.sync();
			} catch (InterruptedException e) {
				future.cause().printStackTrace();
			}
		}
	}
}
