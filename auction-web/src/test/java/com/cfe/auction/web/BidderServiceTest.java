package com.cfe.auction.web;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.cfe.auction.model.persist.BidItem;
import com.cfe.auction.model.persist.BidderCategory;
import com.cfe.auction.service.BidderCategoryService;
import com.cfe.auction.service.IBidItemFilterService;
import com.cfe.auction.service.cache.manager.AuctionCacheManager;
import com.cfe.auction.service.cache.manager.AuctionCacheService;

public class BidderServiceTest extends TestCase {

	ApplicationContext context;
	BidderCategoryService bidderCategoryService;
	IBidItemFilterService bidItemFilterService;

	public void setUp() {

		context = new ClassPathXmlApplicationContext("spring-beans.xml");

		bidderCategoryService = (BidderCategoryService) context
				.getBean("bidderCategoryServiceImpl");
		bidItemFilterService = (IBidItemFilterService) context
				.getBean("bidItemFilterServiceImpl");
	}

	public void testBidderService() {

		List<BidderCategory> bidderCategoryList = bidderCategoryService
				.getBidderCategory(2, AuctionCacheManager.getActiveAuctionId());
		BidItem bidItem = AuctionCacheService
				.getActiveBidItem(AuctionCacheService.getActiveBidItemId());
		List<BidItem> bidItems = new ArrayList<BidItem>();
		bidItems.add(bidItem);
		System.out.println("Bid Item" + bidItem);
		List<Integer> categoryIds = getCategoryIdList(bidderCategoryList);
		System.out.println("Category Id" + categoryIds);

		System.out.println("Active Bid Item"
				+ bidItemFilterService.getBidItemListForMarketList(
						bidItems, categoryIds,
						AuctionCacheService.getActiveBidSequenceId()));

		/*
		 * System.out.println("Closed Bid Item"+bidItemFilterService
		 * .getBidItemListForClosedMarketList(bidItems, categoryIds,
		 * AuctionCacheService.getActiveBidSequenceId()));
		 */
	}

	private List<Integer> getCategoryIdList(
			List<BidderCategory> bidderCategoryList) {
		List<Integer> categoryIds = new ArrayList<Integer>();
		if (bidderCategoryList != null && !bidderCategoryList.isEmpty()) {
			for (BidderCategory category : bidderCategoryList) {
				categoryIds.add(category.getCategoryId());
			}
		}
		return categoryIds;
	}

}
