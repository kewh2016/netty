package com.kewh.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kewh.common.RemoteApi;

public class EchoClient {
	private static final Logger logger = LoggerFactory.getLogger(EchoClient.class);

	public static void start(String host,int port, RemoteApi remoteApi) throws Exception {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group) // 注册线程池
					.channel(NioSocketChannel.class) // 使用NioSocketChannel来作为连接用的channel类
					.remoteAddress(new InetSocketAddress(host, port)) // 绑定连接端口和host信息
					.handler(new ChannelInitializer<SocketChannel>() { // 绑定连接初始化器
								@Override
								protected void initChannel(SocketChannel ch)
										throws Exception {
									ch.pipeline().addLast(
											new EchoClientHandler(remoteApi));
								}
							});
			logger.info("created..");

			ChannelFuture cf = b.connect().sync(); // 异步连接服务器
			logger.info("connected..."); // 连接完成

			cf.channel().closeFuture().sync(); // 异步等待关闭连接channel
			logger.info("closed.."); // 关闭完成
		} finally {
			group.shutdownGracefully().sync(); // 释放线程池资源
		}
	}
}