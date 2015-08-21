package com.cfe.auction.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cfe.auction.model.persist.BidItem;
import com.cfe.auction.model.persist.BidderCategory;
import com.cfe.auction.model.persist.ClientDetails;
import com.cfe.auction.service.BidItemService;
import com.cfe.auction.service.BidderCategoryService;
import com.cfe.auction.service.IAuctionService;
import com.cfe.auction.service.IBidItemFilterService;
import com.cfe.auction.service.UserService;
import com.cfe.auction.service.cache.manager.AuctionCacheManager;
import com.cfe.auction.service.cache.manager.AuctionCacheService;
import com.cfe.auction.service.impl.BidItemFilterServiceImpl;
import com.cfe.auction.web.constants.SessionConstants;

@Controller
@RequestMapping("/observer/**")
public class ObserverController {
	private static final Logger LOG = LoggerFactory
			.getLogger(ObserverController.class);
	@Autowired
	IAuctionService auctionService;

	@Autowired
	private BidItemService bidItemService;

	@Autowired
	private BidItemFilterServiceImpl bidItemFilterServiceImpl;

	@Autowired
	private BidderCategoryService bidderCategoryService;
	@Autowired
	private UserService userService;

	@Autowired
	IBidItemFilterService bidItemFilterService;

	@RequestMapping(value = { "/marketlist", "/home", "/index" }, method = RequestMethod.GET)
	public String getMarketList(ModelMap model, HttpSession session) {
		// User user = (User) session.getAttribute(SessionConstants.USER_INFO);
		ClientDetails clientDetails = (ClientDetails) session
				.getAttribute(SessionConstants.CLIENT_INFO);
		if (AuctionCacheManager.getActiveAuctionId() != null) {
			List<BidderCategory> bidderCategoryList = bidderCategoryService
					.getAllCategory(AuctionCacheManager.getActiveAuctionId());
			LOG.debug("Category Id" + bidderCategoryList);

			List<BidItem> bidItems = AuctionCacheManager.getBidItems();
			if (bidItems != null) {
				System.out.println("BidItems" + bidItems);
				List<Integer> categoryIds = getCategoryIdList(bidderCategoryList);
				model.put("bidItems", bidItemFilterService
						.getBidItemListForMarketList(bidItems, categoryIds,
								AuctionCacheService.getActiveBidSequenceId()));
				model.put("timeextention", 180);
			}
		}
		return "observermarketlist";
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

	@RequestMapping(value = "/activemarketlist", method = RequestMethod.GET)
	public String getActiveMarketList(ModelMap model, HttpSession session) {
		// User user = (User) session.getAttribute(SessionConstants.USER_INFO);
		ClientDetails clientDetails = (ClientDetails) session
				.getAttribute(SessionConstants.CLIENT_INFO);
		List<BidderCategory> bidderCategoryList = bidderCategoryService
				.getAllCategory(AuctionCacheManager.getActiveAuctionId());
		BidItem bidItem = AuctionCacheService
				.getActiveBidItem(AuctionCacheService.getActiveBidItemId());
		List<BidItem> bidItems = new ArrayList<BidItem>();
		bidItems.add(bidItem);

		List<Integer> categoryIds = getCategoryIdList(bidderCategoryList);
		model.put("bidItems", bidItemFilterService
				.getBidItemListForActiveMarketList(bidItems, categoryIds,
						AuctionCacheService.getActiveBidSequenceId()));
		return "observeractivemarket";
	}

	@RequestMapping(value = "/closedmarketlist", method = RequestMethod.GET)
	public String getClosedMarket(ModelMap model, HttpSession session) {
		// User user = (User) session.getAttribute(SessionConstants.USER_INFO);
		ClientDetails clientDetails = (ClientDetails) session
				.getAttribute(SessionConstants.CLIENT_INFO);
		List<BidderCategory> bidderCategoryList = bidderCategoryService
				.getAllCategory(AuctionCacheManager.getActiveAuctionId());
		List<BidItem> bidItems = AuctionCacheManager.getBidItems();
		List<Integer> categoryIds = getCategoryIdList(bidderCategoryList);
		model.put("bidItems", bidItemFilterService
				.getBidItemListForClosedMarketList(bidItems, categoryIds,
						AuctionCacheService.getActiveBidSequenceId()));
		return "observerclosedmarket";
	}

}
