package com.zentral.netty.websocket.pingpong;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.util.SelfSignedCertificate;

import java.net.InetAddress;

public class PingPongServer {

	public final String originator;
	public final String host;
	public final int port;
	public final boolean useSSL;
	
	public PingPongServer(String originator, String host, int port, boolean useSSL)
	{
		this.originator = originator;
		this.host = host;
		this.port = port;
		this.useSSL = useSSL;
	}
	
	public void start() throws Exception
	{
		// Configure SSL.
		final SslContext sslCtx;
		if (useSSL) {
			SelfSignedCertificate ssc = new SelfSignedCertificate();
			sslCtx = SslContext.newServerContext(ssc.certificate(), ssc.privateKey());
		} else {
			sslCtx = null;
		}		
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup)
			.channel(NioServerSocketChannel.class)
			.handler(new LoggingHandler(LogLevel.INFO))
			.childHandler(new PingPongServerInitializer(sslCtx));

			InetAddress inetAddress = InetAddress.getByName(host);
			Channel ch = b.bind(inetAddress,port).sync().channel();

			System.out.println("Open your web browser and navigate to " +
					(useSSL? "https" : "http") + "://"+host+":" + port + '/');

			ch.closeFuture().sync();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}		
	}
	
	/**
	public static void main(String[] args) throws Exception {
		String ip = System.getenv("OPENSHIFT_INTERNAL_IP");
		if(ip == null) {
			ip = "localhost";
		}
		String ports = System.getenv("OPENSHIFT_INTERNAL_PORT");
		if(ports == null) {
			ports = "8080";
		}    	

		// Configure SSL.
		final SslContext sslCtx;
		if (SSL) {
			SelfSignedCertificate ssc = new SelfSignedCertificate();
			sslCtx = SslContext.newServerContext(ssc.certificate(), ssc.privateKey());
		} else {
			sslCtx = null;
		}

		if (args.length > 1)
		{
			PingPongClient client = new PingPongClient(args[0]);
			URI uri = new URI(args[1]);
			client.connect(uri);
		}

		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup)
			.channel(NioServerSocketChannel.class)
			.handler(new LoggingHandler(LogLevel.INFO))
			.childHandler(new PingPongServerInitializer(sslCtx, args));

			InetAddress inetAddress = InetAddress.getByName(ip);
			int port = Integer.decode(ports);
			Channel ch = b.bind(inetAddress,port).sync().channel();

			System.out.println("Open your web browser and navigate to " +
					(SSL? "https" : "http") + "://"+ip+":" + port + '/');

			ch.closeFuture().sync();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
	*/
}
