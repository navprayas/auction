package com.cfe.auction.web.controller;

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
import com.cfe.auction.model.persist.User;
import com.cfe.auction.service.BidItemService;
import com.cfe.auction.service.BidderCategoryService;
import com.cfe.auction.service.IAuctionService;
import com.cfe.auction.service.IBidItemFilterService;
import com.cfe.auction.web.cache.manager.AuctionCacheManager;
import com.cfe.auction.web.constants.SessionConstants;

@Controller
@RequestMapping("/bidder/**")
public class BidderController {

    private static final Logger LOG = LoggerFactory
            .getLogger(BidderController.class);

	@Autowired
	IAuctionService auctionService;

	@Autowired
	private BidItemService bidItemService;

	@Autowired
	private IBidItemFilterService bidItemFilterService;

	@Autowired
	private BidderCategoryService bidderCategoryService;

	@RequestMapping(value = "/marketlist", method = RequestMethod.GET)
	public String getMarketList(ModelMap model, HttpSession session) {
		User user = (User) session.getAttribute(SessionConstants.USER_INFO);
		if(AuctionCacheManager.getActiveAuctionId() != null) {
			List<BidderCategory> bidderCategoryList = bidderCategoryService
					.getBidderCategory(user.getId(), AuctionCacheManager.getActiveAuctionId());
			LOG.debug("Category Id" + bidderCategoryList);
	
			List<BidItem> bidItems = AuctionCacheManager.getBidItems();
			System.out.println("BidItems" + bidItems);
			if(bidderCategoryList != null && bidderCategoryList.size() > 0 ) {
				model.put("bidItems", bidItemFilterService.getBidItemListForActiveMarket(bidItems,bidderCategoryList.get(0).getCategoryId()));
			}
		}
		return "marketlist";
	}

	@RequestMapping(value = "/activemarketlist", method = RequestMethod.GET)
	public String getActiveMarketList(ModelMap model) {
		List<BidItem> bidItems = auctionService.getActiveAuctionBidItem();
		model.put("bidItems", bidItemFilterService
				.getBidItemListForActiveMarket(bidItems, 2));
		return "activemarket";
	}

	@RequestMapping(value = "/closedmarketlist", method = RequestMethod.GET)
	public String getClosedMarket(ModelMap model) {
		List<BidItem> bidItems = auctionService.getActiveAuctionBidItem();
		model.put("bidItems", bidItemFilterService
				.getBidItemListForClosedMarket(bidItems, "2"));
		return "closedmarket";
	}

}
