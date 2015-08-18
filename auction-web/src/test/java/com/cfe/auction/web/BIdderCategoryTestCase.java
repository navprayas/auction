package com.cfe.auction.web;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import junit.framework.TestCase;

public class BIdderCategoryTestCase extends TestCase {
	ApplicationContext context;

	public void setUp() {

		context = new ClassPathXmlApplicationContext("spring-beans.xml");
		System.out.println(context);

	}

	public void testBidderCategory() {

	}
}
