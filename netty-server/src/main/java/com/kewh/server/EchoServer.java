package com.kewh.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EchoServer {

	private static final Logger logger = LoggerFactory
			.getLogger(EchoServer.class);

	public static void start(int port) throws Exception {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap sb = new ServerBootstrap();

			sb.group(bossGroup, workerGroup)// 绑定线程池
					.channel(NioServerSocketChannel.class) // 指定使用的channel
					.localAddress(port)// 绑定监听端口
					.childHandler(new ChannelInitializer<SocketChannel>() { // 绑定客户端连接时候触发操作
								@Override
								protected void initChannel(SocketChannel ch)
										throws Exception {
									logger.info("connected...; Client:"
											+ ch.remoteAddress());
									ch.pipeline().addLast(
											new EchoServerHandler()); // 客户端触发操作
								}
							});
			ChannelFuture cf = sb.bind().sync(); // 服务器异步创建绑定
			logger.info(EchoServer.class + " started and listen on "
					+ cf.channel().localAddress());
			cf.channel().closeFuture().sync(); // 关闭服务器通道
		} finally {
			bossGroup.shutdownGracefully().sync(); // 释放线程池资源
			workerGroup.shutdownGracefully().sync(); // 释放线程池资源
		}
	}
}