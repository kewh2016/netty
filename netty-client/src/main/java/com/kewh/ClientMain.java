package com.kewh;

import com.kewh.client.EchoClient;
import com.kewh.common.RemoteApi;

public class ClientMain {

	public static void main(String[] args) throws Exception {
		RemoteApi remoteApi = new RemoteApi();
		remoteApi.setServiceName("com.kewh.service.impl.HelloServiceImpl");
		remoteApi.setMethodName("sayHello");
		remoteApi.setParam("kewh");
		EchoClient.start("127.0.0.1", 65535,remoteApi ); // 连接127.0.0.1/65535，并启动
	}
}
