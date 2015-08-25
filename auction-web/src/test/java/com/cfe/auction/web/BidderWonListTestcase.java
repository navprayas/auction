package com.cfe.auction.web;

import java.util.List;

import junit.framework.TestCase;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.cfe.auction.model.persist.BidItem;
import com.cfe.auction.model.persist.BidderCategory;
import com.cfe.auction.service.BidItemService;
import com.cfe.auction.service.BidderCategoryService;
import com.cfe.auction.service.cache.manager.AuctionCacheManager;

public class BidderWonListTestcase extends TestCase {
	ApplicationContext context;
	BidderCategoryService bidderCategoryService;
	BidItemService bidItemService;

	public void setUp() {

		context = new ClassPathXmlApplicationContext("spring-beans.xml");

		bidItemService = (BidItemService) context.getBean("bidItemServiceImpl");
		bidderCategoryService=(BidderCategoryService)context.getBean("bidderCategoryServiceImpl");
	}

	public void testBidderWonListService() {
		try {
			List<BidItem> wonList = bidItemService.getWonList("bidder");
	/*		List<BidderCategory> categoryList = bidderCategoryService
					.getAllCategory(AuctionCacheManager.getActiveAuctionId());

			System.out.println("Wonlist" + wonList);
			System.out.println("Category Test" + categoryList);
*/		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
