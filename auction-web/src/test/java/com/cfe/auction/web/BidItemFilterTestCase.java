package com.cfe.auction.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.cfe.auction.model.persist.BidItem;
import com.cfe.auction.model.persist.BidderCategory;
import com.cfe.auction.service.BidderCategoryService;
import com.cfe.auction.service.IAuctionService;
import com.cfe.auction.service.IBidItemFilterService;
import com.cfe.auction.service.cache.manager.AuctionCacheManager;
import com.cfe.auction.service.cache.manager.AuctionCacheService;

import junit.framework.TestCase;

public class BidItemFilterTestCase extends TestCase {
	ApplicationContext context;

	public void setUp() {

		context = new ClassPathXmlApplicationContext("spring-beans.xml");
		System.out.println(context);

	}

/*	public void testBidItemFilterService() {

		BidderCategoryService bidderCategoryService = (BidderCategoryService) context
				.getBean("bidderCategoryServiceImpl");
		IBidItemFilterService iBidItemFilterService = (IBidItemFilterService) context
				.getBean("bidItemFilterServiceImpl");
		System.out.println("Running Auction "
				+ AuctionCacheManager.getActiveAuctionId());
		if (AuctionCacheManager.getActiveAuctionId() != null) {
			List<BidderCategory> bidderCategoryList = bidderCategoryService
					.getBidderCategory(2,
							AuctionCacheManager.getActiveAuctionId());
			List<BidItem> bidItems = AuctionCacheManager.getBidItems();
			if (bidItems != null) {
				System.out.println("BidItems" + bidItems);
				List<Integer> categoryIds = getCategoryIdList(bidderCategoryList);
				System.out
						.println("Bidder Categiory List" + bidderCategoryList);
				System.out.println("Bid sequence"
						+ AuctionCacheService.getActiveBidSequenceId());
				System.out.println(iBidItemFilterService
						.getBidItemListForMarketList(bidItems, categoryIds,
								AuctionCacheService.getActiveBidSequenceId()));
			}

		}

	}*/

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
