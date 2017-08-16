package com.kewh.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.kewh.common.RemoteApi;

public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

	private static final Logger logger = LoggerFactory
			.getLogger(EchoClientHandler.class);

	private RemoteApi remoteApi;

	public EchoClientHandler(RemoteApi remoteApi) {
		this.remoteApi = remoteApi;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		logger.info("client channelActive..");
		String str = JSON.toJSONString(remoteApi);
		ByteBuf copiedBuffer = Unpooled.copiedBuffer(str, 0, str.length(),
				CharsetUtil.UTF_8);
		ctx.writeAndFlush(copiedBuffer); // 必须有flush
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg)
			throws Exception {
		logger.info("client channelRead..");
		ByteBuf buf = msg.readBytes(msg.readableBytes());
		System.out.println("Client received:"
				+ buf.toString(Charset.forName("utf-8")));
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

}