package com.cfe.auction.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cfe.auction.common.Bidder;
import com.cfe.auction.model.auction.persist.AuctionCacheBean;
import com.cfe.auction.model.auction.persist.AuctionSearchBean;
import com.cfe.auction.model.persist.BidItem;
import com.cfe.auction.model.persist.BidderCategory;
import com.cfe.auction.model.persist.ClientDetails;
import com.cfe.auction.model.persist.User;
import com.cfe.auction.service.AutoBidService;
import com.cfe.auction.service.BidItemService;
import com.cfe.auction.service.BidderCategoryService;
import com.cfe.auction.service.IBidItemFilterService;
import com.cfe.auction.service.UserService;
import com.cfe.auction.service.cache.manager.AuctionCacheManager;
import com.cfe.auction.service.cache.manager.AuctionCacheService;
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

	@RequestMapping(value = { "/marketlist", "/home", "/index" }, method = RequestMethod.GET)
	public String getMarketList(ModelMap model, HttpSession session) {
		User user = (User) session.getAttribute(SessionConstants.USER_INFO);
		ClientDetails clientDetails = (ClientDetails) session
				.getAttribute(SessionConstants.CLIENT_INFO);
		AuctionSearchBean auctionSearchBean = new AuctionSearchBean(clientDetails.getSchemaKey());
		auctionSearchBean.setClientId(clientDetails.getId());
		auctionSearchBean.setSchemaName(clientDetails.getSchemaKey());
		
		System.out.println("clientDetails" + clientDetails.getId());
		AuctionCacheBean auctionCacheBean = AuctionCacheManager.getActiveAuctionCacheBean(auctionSearchBean);
		if (auctionCacheBean != null && auctionCacheBean.getAuctionId() != null) {
			
			auctionSearchBean.setAuctionId(auctionCacheBean.getAuctionId());
			
			List<BidderCategory> bidderCategoryList = bidderCategoryService
					.getBidderCategory(user.getId(),auctionSearchBean);
			System.out.println("bidderCategoryList" + bidderCategoryList);
			LOG.debug("Category Id" + bidderCategoryList);

			List<BidItem> bidItems = AuctionCacheManager.getBidItems(auctionSearchBean);
			Date currDate = new Date();
			long refreshTime = auctionCacheBean.getAuctionStartTime().getTime() - currDate.getTime();
			refreshTime = (long) (refreshTime / 1000);
			LOG.debug("*****refreshTime::" + refreshTime);
			if (refreshTime <= 0) {
				refreshTime = 0;
			}
			if (bidItems != null) {
				System.out.println("BidItems" + bidItems);
				List<Integer> categoryIds = getCategoryIdList(bidderCategoryList);
				Long activeBidSeuenceId = AuctionCacheManager.getActiveBidSequenceId(auctionSearchBean);
				List<BidItem> bidItemsFinal = bidItemFilterService
						.getBidItemListForMarketList(bidItems, categoryIds,
								activeBidSeuenceId);
				model.put("bidItems", bidItemsFinal);
				if(bidItemsFinal != null) {
					for (BidItem bidItem : bidItemsFinal) {
						LOG.debug("bidItem : " + bidItem.getBidItemId() + " "
								+ bidItem.getBidSpan() + " " + refreshTime);
						refreshTime += bidItem.getBidSpan();
						bidItem.setTimeLeft(refreshTime);
						if (bidItem.getCurrentMarketPrice() == null) {
							bidItem.setCurrentMarketPrice(bidItem.getMinBidPrice());
						}
					}
				}
				model.put("timeextention", 30);
			}
		}
		return "biddermarketlist";
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
		User user = (User) session.getAttribute(SessionConstants.USER_INFO);
		
		ClientDetails clientDetails = (ClientDetails) session
				.getAttribute(SessionConstants.CLIENT_INFO);
		
		AuctionSearchBean auctionSearchBean = new AuctionSearchBean(clientDetails.getSchemaKey());
		auctionSearchBean.setClientId(clientDetails.getId());
		auctionSearchBean.setSchemaName(clientDetails.getSchemaKey());
		
		auctionSearchBean.setAuctionId(AuctionCacheManager.getActiveAuctionId(auctionSearchBean));
		
		List<BidderCategory> bidderCategoryList = bidderCategoryService
				.getBidderCategory(user.getId(), auctionSearchBean);
		BidItem bidItem = AuctionCacheManager.getActiveBidItem(auctionSearchBean);
		if (bidItem != null) {
			List<BidItem> bidItems = new ArrayList<BidItem>();
			bidItems.add(bidItem);
			
			List<Integer> categoryIds = getCategoryIdList(bidderCategoryList);
			model.put("bidItems", bidItemFilterService
					.getBidItemListForActiveMarketList(bidItems, categoryIds, bidItem.getSeqId()));
		}
		return "bidderactivemarket";
	}

	@RequestMapping(value = "/closedmarketlist", method = RequestMethod.GET)
	public String getClosedMarket(ModelMap model, HttpSession session) {
		
		User user = (User) session.getAttribute(SessionConstants.USER_INFO);
		
		ClientDetails clientDetails = (ClientDetails) session
				.getAttribute(SessionConstants.CLIENT_INFO);
		
		AuctionSearchBean auctionSearchBean = new AuctionSearchBean(clientDetails.getSchemaKey());
		auctionSearchBean.setClientId(clientDetails.getId());
		auctionSearchBean.setSchemaName(clientDetails.getSchemaKey());
		auctionSearchBean.setAuctionId(AuctionCacheManager.getActiveAuctionId(auctionSearchBean));
		
		List<BidderCategory> bidderCategoryList = bidderCategoryService
				.getBidderCategory(user.getId(), auctionSearchBean);
		List<BidItem> bidItems = AuctionCacheManager.getBidItems(auctionSearchBean);
		List<Integer> categoryIds = getCategoryIdList(bidderCategoryList);
		model.put("bidItems", bidItemFilterService
				.getBidItemListForClosedMarketList(bidItems, categoryIds,
						AuctionCacheManager.getActiveBidSequenceId(auctionSearchBean)));
		return "bidderclosedmarket";
	}
}
