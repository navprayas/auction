package com.cfe.auction.web;

import junit.framework.TestCase;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.cfe.auction.common.Bid;
import com.cfe.auction.model.auction.persist.AuctionSearchBean;
import com.cfe.auction.service.BidsService;
import com.cfe.auction.service.cache.manager.AuctionCacheManager;

public class BidsTestCase extends TestCase {

	ApplicationContext context;
	BidsService bidsService = null;

	public void setUp() {
		try {
			context = new ClassPathXmlApplicationContext("spring-beans.xml");
			
			System.out.println(bidsService);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void testBidsTestCase() {

		bidsService = (BidsService) context.getBean("bidsServiceImpl");
		AuctionSearchBean auctionSearchBean = new AuctionSearchBean();
		try {
			auctionSearchBean.setSchemaName("PersistenceUnitB");
			auctionSearchBean.setClientId(101);
			Bid bid = new Bid();
			bid.setBidId(80L);
			bid.setBidType(1);
			bid.setBidAmount(34000.0);
			bid.setBidderName("bidder");
			bid.setComments("comments");
			bid.setAuctionId(Long.parseLong(AuctionCacheManager
					.getActiveAuctionId(auctionSearchBean).toString()));
			bidsService.createBidService(bid, auctionSearchBean);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
