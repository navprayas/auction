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
import com.cfe.auction.service.BidItemService;
import com.cfe.auction.service.BidderCategoryService;
import com.cfe.auction.service.BiditemUtilService;
import com.cfe.auction.service.IAuctionService;
import com.cfe.auction.service.IBidItemFilterService;
import com.cfe.auction.service.UserService;
import com.cfe.auction.service.cache.manager.AuctionCacheManager;
import com.cfe.auction.service.impl.BidItemFilterServiceImpl;
import com.cfe.auction.web.constants.CommonConstants;
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
		auctionSearchBean.setRole(session.getAttribute(
				CommonConstants.USER_ROLE).toString());

		auctionSearchBean.setUserId(user.getId());
		AuctionCacheBean auctionCacheBean = AuctionCacheManager
				.getActiveAuctionCacheBean(auctionSearchBean);
		if (auctionCacheBean != null && auctionCacheBean.getAuctionId() != null
				&& !auctionCacheBean.isAuctionClosed()) {
			auctionSearchBean.setAuctionId(auctionCacheBean.getAuctionId());
			auctionSearchBean.setAuctionStartTime(auctionCacheBean
					.getAuctionStartTime());
			model.put("bidItems",
					biditemUtilService.getBidItemsMarketList(auctionSearchBean));
			model.put("timeextention", 180);
		}
		
		LOG.debug("Market List");
		

		return "observermarketlist";
	}

	@RequestMapping(value = "/activemarketlist", method = RequestMethod.GET)
	public String getActiveMarketList(ModelMap model, HttpSession session) {
		User user = (User) session.getAttribute(SessionConstants.USER_INFO);
		ClientDetails clientDetails = (ClientDetails) session
				.getAttribute(SessionConstants.CLIENT_INFO);
		AuctionSearchBean auctionSearchBean = new AuctionSearchBean(
				clientDetails.getSchemaKey());
		auctionSearchBean.setClientId(clientDetails.getId());
		auctionSearchBean.setSchemaName(clientDetails.getSchemaKey());
		auctionSearchBean.setRole(session.getAttribute(
				CommonConstants.USER_ROLE).toString());
		AuctionCacheBean auctionCacheBean = AuctionCacheManager
				.getActiveAuctionCacheBean(auctionSearchBean);
		if (auctionCacheBean != null && auctionCacheBean.getAuctionId() != null
				&& !auctionCacheBean.isAuctionClosed()) {
		auctionSearchBean.setUserId(user.getId());
		model.put("bidItems", biditemUtilService
				.getBidItemsActiveMarketList(auctionSearchBean));
		}
		return "observeractivemarket";
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
		auctionSearchBean.setRole(session.getAttribute(
				CommonConstants.USER_ROLE).toString());
		AuctionCacheBean auctionCacheBean = AuctionCacheManager
				.getActiveAuctionCacheBean(auctionSearchBean);
		if (auctionCacheBean != null && auctionCacheBean.getAuctionId() != null
				&& !auctionCacheBean.isAuctionClosed()) {
		auctionSearchBean.setUserId(user.getId());
		model.put("bidItems", biditemUtilService
				.getBidItemsClosedMarketList(auctionSearchBean));
		}
		return "observerclosedmarket";
	}

}
