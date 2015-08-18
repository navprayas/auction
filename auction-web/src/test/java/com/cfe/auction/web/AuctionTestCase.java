package com.cfe.auction.web;

import java.util.List;

import junit.framework.TestCase;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.cfe.auction.model.persist.Auction;
import com.cfe.auction.service.IAuctionService;

public class AuctionTestCase extends TestCase {
	ApplicationContext context;

	public void setUp() {
		context = new ClassPathXmlApplicationContext("spring-beans.xml");
		System.out.println(context);

	}

	public void testAuction() {

		IAuctionService iAuctionService = (IAuctionService) context
				.getBean("auctionServiceImpl");

		List<Auction> auctions = iAuctionService.findAll(Auction.class);
		for (Auction auction : auctions) {
			System.out.println(auction.getName());
		}
	}

}
