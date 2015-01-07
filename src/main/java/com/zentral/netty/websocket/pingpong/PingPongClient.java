package com.zentral.netty.websocket.pingpong;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

import java.net.URI;

import com.fasterxml.jackson.databind.ObjectMapper;

public class PingPongClient {
	
	private final String originator;
	
	public PingPongClient(String originator)
	{
		this.originator = originator;
	}
	
	public void connect(final URI uri) throws Exception
	{
		String scheme = uri.getScheme();
		final String host = uri.getHost();
		final int port = uri.getPort();
		if (!"ws".equalsIgnoreCase(scheme) && !"wss".equalsIgnoreCase(scheme)) {
			throw new Exception("Only WS(S) is supported.");
		}

		final boolean ssl = "wss".equalsIgnoreCase(scheme);
		final SslContext sslCtx;
		if (ssl) {
			sslCtx = SslContext.newClientContext(InsecureTrustManagerFactory.INSTANCE);
		} else {
			sslCtx = null;
		}

		EventLoopGroup group = new NioEventLoopGroup();
		try {
			HttpHeaders headers = new DefaultHttpHeaders();
			headers.add(Constants.HTTP_HEADER_X_RIKIT_ORIGINATOR, getOriginator());
			final PingPongClientHandler handler =
					new PingPongClientHandler(
							WebSocketClientHandshakerFactory.newHandshaker(
									uri, WebSocketVersion.V13, null, false, headers));

			Bootstrap b = new Bootstrap();
			b.group(group)
			.channel(NioSocketChannel.class)
			.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) {
					ChannelPipeline p = ch.pipeline();
					if (sslCtx != null) {
						p.addLast(sslCtx.newHandler(ch.alloc(), host, port));
					}
					p.addLast(
							new HttpClientCodec(),
							new HttpObjectAggregator(8192),
							handler);
				}
			});

			System.out.println("Open connection to " + uri);
			Channel ch = b.connect(uri.getHost(), port).sync().channel();
            handler.handshakeFuture().sync();
            System.out.println("Send ping");
            WebSocketFrame frame = new PingWebSocketFrame(Unpooled.wrappedBuffer(new byte[] { 8, 1, 8, 1 }));
            ch.writeAndFlush(frame).sync();
            ObjectMapper objectMapper = new ObjectMapper();
			JoinCommand request = new JoinCommand();
			request.setName(CommandEnum.JOIN);
			request.getHeaders().put(Constants.COMMAND_HEADER_SOURCE, getOriginator());
			request.getHeaders().put(Constants.COMMAND_HEADER_DESTINATION, uri.toString());
            ChannelFuture future = ch.writeAndFlush(new TextWebSocketFrame(objectMapper.writeValueAsString(request)));
            future.addListener(new ChannelFutureListener() {
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                	if (channelFuture.isSuccess()) {
                		System.out.println("Successfully join with " + uri);
                		Store.getMapInstance().put(uri, handler.getContext());
                	}                	
                }
            });
		} finally {
           // group.shutdownGracefully();
        }
	}

	public String getOriginator() {
		return originator;
	}
}
