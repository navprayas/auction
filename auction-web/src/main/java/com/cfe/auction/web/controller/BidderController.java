package com.cfe.auction.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cfe.auction.model.persist.BidItem;
import com.cfe.auction.service.BidItemService;
import com.cfe.auction.service.IAuctionService;
import com.cfe.auction.service.impl.BidItemFilterServiceImpl;

@Controller
@RequestMapping("/bidder/**")
public class BidderController {
	
	@Autowired
	IAuctionService auctionService;
	
	@Autowired
	private BidItemService bidItemService;
	
	@Autowired
	private BidItemFilterServiceImpl bidItemFilterServiceImpl;

	@RequestMapping(value = "/marketlist", method = RequestMethod.GET)
	public String getMarketList(ModelMap model) {
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		String name = auth.getName();
		List<BidItem> bidItems = auctionService.getActiveAuctionBidItem();
		model.put("bidItems" ,bidItemFilterServiceImpl.getBidItemListForcategoryId(bidItems, "1"));
		return "bidder/bidder_marketlist";
	}

	@RequestMapping(value = "/activemarketlist", method = RequestMethod.GET)
	public String getActiveMarketList() {
		bidItemService.getBidItemActiveMarket();
		return "activemarket";
	}

	@RequestMapping(value = "/closedmarketlist", method = RequestMethod.GET)
	public String getClosedMarket() {
		bidItemService.getBidItemClosedMarket();
		return "closedmarket";
	}

}
