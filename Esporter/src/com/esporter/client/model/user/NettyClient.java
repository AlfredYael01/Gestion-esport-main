package com.esporter.client.model.user;

import com.esporter.both.socket.Command;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

public class NettyClient {
	
	private Channel channel;
	private User user;
	
	public NettyClient(User user) {
		this.user = user;
	}

	public void run(String host, int port) throws InterruptedException {

        EventLoopGroup workerGroup = new NioEventLoopGroup();


        Bootstrap b = new Bootstrap();
        b.group(workerGroup);
        b.channel(NioSocketChannel.class);
        b.option(ChannelOption.SO_KEEPALIVE, true);
        b.handler(new ChannelInitializer<SocketChannel>() {
 
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new ObjectEncoder(), 
                new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)), new ClientProcessingHandler(user));
            }
        });

        ChannelFuture f = b.connect(host, port).sync();
        
        channel = f.channel();
    }
	
	public void send(Command command) {
		channel.writeAndFlush(command);
	}
}