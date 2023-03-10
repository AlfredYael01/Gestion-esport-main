package com.esporter.server.main;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class NettyServer {

    private int port;
	private ChannelFuture f;
	private NioEventLoopGroup bossGroup;
	private NioEventLoopGroup workerGroup;

    // constructor
    public NettyServer(int port) {
		// TODO Auto-generated constructor stub
    	this.port = port;
    	
	}

    public void run() throws InterruptedException {
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();

        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup)
          .channel(NioServerSocketChannel.class)
          .childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) 
              throws Exception {
                ch.pipeline().addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)), 
                  new ObjectEncoder(), 
                  new ProcessingHandler());
            }
        }).option(ChannelOption.SO_BACKLOG, 128)
          .childOption(ChannelOption.SO_KEEPALIVE, true)
          .handler(new LoggingHandler(LogLevel.INFO));

        f = b.bind(port).sync();
        
            f.channel().closeFuture().sync();
            
        
    }
    
    public Channel getChannel() {
    	return f.channel();
    }
    
    public void shutdownGracefully() {
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
    }
}