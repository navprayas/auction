package com.cfe.auction.web.controller;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cfe.auction.model.auction.persist.AuctionCacheBean;
import com.cfe.auction.model.auction.persist.AuctionSearchBean;
import com.cfe.auction.model.persist.ClientDetails;
import com.cfe.auction.model.persist.User;
import com.cfe.auction.service.AutoBidService;
import com.cfe.auction.service.BidItemService;
import com.cfe.auction.service.BidderCategoryService;
import com.cfe.auction.service.BiditemUtilService;
import com.cfe.auction.service.BidsService;
import com.cfe.auction.service.IBidItemFilterService;
import com.cfe.auction.service.UserService;
import com.cfe.auction.service.cache.manager.AuctionCacheManager;
import com.cfe.auction.web.constants.CommonConstants;
import com.cfe.auction.web.constants.SessionConstants;

@Controller
@RequestMapping("/bidder/**")
public class BidderController {
	private static final Logger LOG = LoggerFactory
			.getLogger(BidderController.class);
	@Autowired
	private BidItemService bidItemService;

	@Autowired
	private IBidItemFilterService bidItemFilterService;

	@Autowired
	private BidderCategoryService bidderCategoryService;

	@Autowired
	private UserService userService;

	@Autowired
	private AutoBidService autoBidService;

	@Autowired
	private BidsService bidsService;

	@Autowired
	private BiditemUtilService biditemUtilService;

	@RequestMapping(value = { "/marketlist", "/home", "/index" }, method = RequestMethod.GET)
	public String getMarketList(ModelMap model, HttpSession session) {
		User user = (User) session.getAttribute(SessionConstants.USER_INFO);
		ClientDetails clientDetails = (ClientDetails) session
				.getAttribute(SessionConstants.CLIENT_INFO);
		AuctionSearchBean auctionSearchBean = new AuctionSearchBean(
				clientDetails.getSchemaKey());
		auctionSearchBean.setClientId(clientDetails.getId());
		auctionSearchBean.setSchemaName(clientDetails.getSchemaKey());

		System.out.println("clientDetails" + clientDetails.getId());
		AuctionCacheBean auctionCacheBean = AuctionCacheManager
				.getActiveAuctionCacheBean(auctionSearchBean);
		if (auctionCacheBean != null && auctionCacheBean.getAuctionId() != null
				&& !auctionCacheBean.isAuctionClosed()) {

			auctionSearchBean.setAuctionId(auctionCacheBean.getAuctionId());
			auctionSearchBean.setUserId(user.getId());
			auctionSearchBean.setAuctionStartTime(auctionCacheBean
					.getAuctionStartTime());
			auctionSearchBean.setRole(session.getAttribute(
					CommonConstants.USER_ROLE).toString());

			model.put("bidItems",
					biditemUtilService.getBidItemsMarketList(auctionSearchBean));
			model.put("timeextention", 120);

		}
		return "biddermarketlist";
	}

	@RequestMapping(value = "/activemarketlist", method = RequestMethod.GET)
	public String getActiveMarketList(ModelMap model, HttpSession session) {
		User user = (User) session.getAttribute(SessionConstants.USER_INFO);

		ClientDetails clientDetails = (ClientDetails) session
				.getAttribute(SessionConstants.CLIENT_INFO);

		AuctionSearchBean auctionSearchBean = new AuctionSearchBean(clientDetails.getSchemaKey());
		auctionSearchBean.setClientId(clientDetails.getId());
		auctionSearchBean.setAuctionId(AuctionCacheManager
				.getActiveAuctionId(auctionSearchBean));

		AuctionCacheBean auctionCacheBean = AuctionCacheManager
				.getActiveAuctionCacheBean(auctionSearchBean);
		if (auctionCacheBean != null && auctionCacheBean.getAuctionId() != null
				&& !auctionCacheBean.isAuctionClosed()) {
			auctionSearchBean.setUserId(user.getId());
			auctionSearchBean.setRole(session.getAttribute(
					CommonConstants.USER_ROLE).toString());
			model.put("bidItems", biditemUtilService
					.getBidItemsActiveMarketList(auctionSearchBean));
		}

		return "bidderactivemarket";
	}

	@RequestMapping(value = "/closedmarketlist", method = RequestMethod.GET)
	public String getClosedMarket(ModelMap model, HttpSession session) {

		User user = (User) session.getAttribute(SessionConstants.USER_INFO);

		ClientDetails clientDetails = (ClientDetails) session
				.getAttribute(SessionConstants.CLIENT_INFO);

		AuctionSearchBean auctionSearchBean = new AuctionSearchBean(
				clientDetails.getSchemaKey());
		auctionSearchBean.setClientId(clientDetails.getId());
		auctionSearchBean.setSchemaName(clientDetails.getSchemaKey());
		auctionSearchBean.setAuctionId(AuctionCacheManager
				.getActiveAuctionId(auctionSearchBean));
		AuctionCacheBean auctionCacheBean = AuctionCacheManager
				.getActiveAuctionCacheBean(auctionSearchBean);
		if (auctionCacheBean != null && auctionCacheBean.getAuctionId() != null
				&& !auctionCacheBean.isAuctionClosed()) {
			auctionSearchBean.setUserId(user.getId());
			auctionSearchBean.setRole(session.getAttribute(
					CommonConstants.USER_ROLE).toString());
			model.put("bidItems", biditemUtilService
					.getBidItemsClosedMarketList(auctionSearchBean));
		}
		return "bidderclosedmarket";
	}
}
