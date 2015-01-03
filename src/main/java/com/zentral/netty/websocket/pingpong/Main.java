package com.zentral.netty.websocket.pingpong;

import java.net.URI;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {

	public static void main(String[] args) throws Exception {
		String originator = args[0];
		String host = args[1];
		int port = Integer.parseInt(args[2]);
		
		if (args.length > 3)
		{
			PingPongClient client = new PingPongClient(originator);
			URI uri = new URI(args[3]);
			client.connect(uri);			
		}		
		
		ScheduledExecutorService executorService = Executors.newScheduledThreadPool(3);
		executorService.scheduleAtFixedRate(new CheckWorker(), 10, 10, TimeUnit.SECONDS);
		
		PingPongServer server = new PingPongServer(originator, host, port, false);
		server.start();

	}
}
