package com.cfe.auction.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
import com.cfe.auction.service.impl.BidItemFilterServiceImpl;
import com.cfe.auction.web.constants.CommonConstants;
import com.cfe.auction.web.constants.SessionConstants;

@Controller
@RequestMapping("/bidder/**")
public class BidderController {

	@Autowired
	IAuctionService auctionService;

	@Autowired
	private BidItemService bidItemService;

	@Autowired
	private BidItemFilterServiceImpl bidItemFilterServiceImpl;

	@Autowired
	private BidderCategoryService bidderCategoryService;

	@RequestMapping(value = "/marketlist", method = RequestMethod.GET)
	public String getMarketList(ModelMap model, HttpSession session) {
		User user = (User) session.getAttribute(SessionConstants.USER_INFO);
		List<BidderCategory> bidderCategoryList = bidderCategoryService
				.getBidderCategory(user.getId(), 76);
		List<BidItem> bidItems = auctionService.getActiveAuctionBidItem();
		model.put("bidItems", bidItemFilterServiceImpl
				.getBidItemListForcategoryId(bidItems, "2"));
		return "marketlist";
	}

	@RequestMapping(value = "/activemarketlist", method = RequestMethod.GET)
	public String getActiveMarketList(ModelMap model) {
		List<BidItem> bidItems = auctionService.getActiveAuctionBidItem();
		model.put("bidItems", bidItemFilterServiceImpl
				.getBidItemListForActiveMarket(bidItems, "2"));
		return "activemarket";
	}

	@RequestMapping(value = "/closedmarketlist", method = RequestMethod.GET)
	public String getClosedMarket(ModelMap model) {
		List<BidItem> bidItems = auctionService.getActiveAuctionBidItem();
		model.put("bidItems", bidItemFilterServiceImpl
				.getBidItemListForClosedMarket(bidItems, "2"));
		return "closedmarket";
	}

}
