package com.cfe.auction.web;

import junit.framework.TestCase;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.cfe.auction.service.UserService;

public class UserServiceTestCase extends TestCase {
	ApplicationContext context;

	public void setUp() {

		context = new ClassPathXmlApplicationContext("spring-beans.xml");
		System.out.println(context);

	}

	public void testUserServiceTestCase() {

		UserService userService = (UserService) context
				.getBean("userServiceImpl");
		userService.getUserByUserName("bidder");
	}
}
