package com.cfe.auction.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cfe.auction.service.BidItemService;

@Controller
@RequestMapping("/bidder/**")
public class BidderController {
	@Autowired
	private BidItemService bidItemService;

	@RequestMapping(value = "/marketlist", method = RequestMethod.GET)
	public String getMarketList(ModelMap model) {
		return "marketlist";
	}

	@RequestMapping(value = "/activemarketlist", method = RequestMethod.GET)
	public String getActiveMarketList() {
		return "activemarket";
	}

	@RequestMapping(value = "/closedmarketlist", method = RequestMethod.GET)
	public String getClosedMarket() {
		return "closedmarket";
	}

}
