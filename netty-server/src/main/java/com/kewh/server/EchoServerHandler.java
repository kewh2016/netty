package com.kewh.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.kewh.common.RemoteApi;

public class EchoServerHandler extends ChannelInboundHandlerAdapter {
	private static final Logger logger = LoggerFactory
			.getLogger(EchoServerHandler.class);

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		ByteBuf buf = (ByteBuf) msg;
		String string = buf.toString(CharsetUtil.UTF_8);
		logger.info("接收到数据：" + string);
		RemoteApi remoteApi = JSON.parseObject(string, RemoteApi.class);
		String result = hander(remoteApi);
		logger.info("返回的数据：" + result);
		byte[] bytes = result.getBytes();
		ByteBuf resultBuf = Unpooled.buffer(bytes.length);
		resultBuf.writeBytes(bytes);
		ctx.writeAndFlush(resultBuf);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private String hander(RemoteApi remoteApi) throws ClassNotFoundException {

		Class cls = Class.forName(remoteApi.getServiceName());

		Method m = null;
		try {
			m = cls.getDeclaredMethod(remoteApi.getMethodName(), String.class);
		} catch (Exception e) {
			throw new RuntimeException();
		}
		Object result = null;
		try {
			result = m.invoke(cls.newInstance(), remoteApi.getParam());
		} catch (Exception e) {
			throw new RuntimeException();
		}
		return JSON.toJSONString(result);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		logger.info("server channelReadComplete..");
		// 第一种方法：写一个空的buf，并刷新写出区域。完成后关闭sock channel连接。
		ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(
				ChannelFutureListener.CLOSE);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		logger.info("server occur exception:" + cause.getMessage());
		cause.printStackTrace();
		ctx.close(); // 关闭发生异常的连接
	}
}