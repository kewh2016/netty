package com.kewh.service.impl;

import com.kewh.service.HelloService;

public class HelloServiceImpl implements HelloService {

	@Override
	public String sayHello(String name) {
		return "hello " + name + " from server!";
	}

}
