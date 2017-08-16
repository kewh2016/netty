package com.kewh;

import com.kewh.server.EchoServer;

public class ServerMain {
	public static void main(String[] args) throws Exception {
		EchoServer.start(65535); // 启动
	}
}
