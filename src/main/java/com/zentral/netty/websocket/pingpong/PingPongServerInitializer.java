package com.zentral.netty.websocket.pingpong;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslContext;

public class PingPongServerInitializer extends ChannelInitializer<SocketChannel> {
	
    private final SslContext sslCtx;
    
    public PingPongServerInitializer(SslContext sslCtx) throws Exception {
        this.sslCtx = sslCtx;
    }
    
    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        boolean useSSL = false;
        if (sslCtx != null) {
            pipeline.addLast(sslCtx.newHandler(ch.alloc()));
            useSSL = true;
        }
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new HttpObjectAggregator(65536));
        pipeline.addLast(new PingPongServerHandler(useSSL));
    }

}
