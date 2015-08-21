package com.cfe.auction.web;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.cfe.auction.model.auction.persist.AuctionSearchBean;
import com.cfe.auction.service.BidItemService;
import com.cfe.auction.service.cache.manager.AuctionCacheManager;

import junit.framework.TestCase;

public class BidItemTestCase extends TestCase {
	ApplicationContext context;

	public void setUp() {

		context = new ClassPathXmlApplicationContext("spring-beans.xml");
		System.out.println(context);

	}
	public void testBIdItemTestCase() {
		AuctionSearchBean auctionSearchBean = new AuctionSearchBean();
		auctionSearchBean
				.setAuctionId(AuctionCacheManager.getActiveAuctionId());
		BidItemService bidItemService = (BidItemService) context
				.getBean("bidItemServiceImpl");
		System.out.println(bidItemService
				.getBidItemsbyAuctionId(auctionSearchBean));
	}
}
