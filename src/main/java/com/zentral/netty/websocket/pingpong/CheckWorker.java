package com.zentral.netty.websocket.pingpong;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class CheckWorker implements Runnable{
	
	public static final String CHECK_MESSAGE = "HALO";
	
	public void run()
	{
		Map<URI, ChannelHandlerContext> map = Store.getMapInstance();
		List<ChannelFuture> futures = new ArrayList<ChannelFuture>();
		for (Entry<URI, ChannelHandlerContext> entry:map.entrySet())
		{
			ChannelHandlerContext context = entry.getValue();
            WebSocketFrame frame = new TextWebSocketFrame(CHECK_MESSAGE + " " + entry.getKey());
            ChannelFuture future = context.channel().writeAndFlush(frame);
            futures.add(future);
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
