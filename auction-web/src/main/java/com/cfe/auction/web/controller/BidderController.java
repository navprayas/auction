package com.cfe.auction.web.controller;

import java.util.ArrayList;
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
import com.cfe.auction.model.persist.BidItem;
import com.cfe.auction.model.persist.BidderCategory;
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

	@RequestMapping(value = { "/home", "/index" }, method = RequestMethod.GET)
	public String modelerHome(ModelMap model, HttpSession session) {
		User user = (User) session.getAttribute(SessionConstants.USER_INFO);
		System.out.println("Auction Id"
				+ AuctionCacheManager.getActiveAuctionId());

		if (AuctionCacheManager.getActiveAuctionId() != null) {
			List<BidderCategory> bidderCategoryList = bidderCategoryService
					.getBidderCategory(user.getId(),
							AuctionCacheManager.getActiveAuctionId());
			LOG.debug("Category Id" + bidderCategoryList);

			List<BidItem> bidItems = AuctionCacheManager.getBidItems();
			System.out.println("BidItems" + bidItems);
			List<Integer> categoryIds = getCategoryIdList(bidderCategoryList);
			model.put("bidItems", bidItemFilterService
					.getBidItemListForActiveMarket(bidItems, categoryIds));
			model.put("timeextention", 3);

		}
		return "bidderhome";
	}

	@RequestMapping(value = "/marketlist", method = RequestMethod.GET)
	public String getMarketList(ModelMap model, HttpSession session) {
		User user = (User) session.getAttribute(SessionConstants.USER_INFO);
		if (AuctionCacheManager.getActiveAuctionId() != null) {
			List<BidderCategory> bidderCategoryList = bidderCategoryService
					.getBidderCategory(user.getId(),
							AuctionCacheManager.getActiveAuctionId());
			LOG.debug("Category Id" + bidderCategoryList);

			List<BidItem> bidItems = AuctionCacheManager.getBidItems();
			System.out.println("BidItems" + bidItems);
			List<Integer> categoryIds = getCategoryIdList(bidderCategoryList);
			model.put("bidItems", bidItemFilterService
					.getBidItemListForActiveMarket(bidItems, categoryIds));
			model.put("timeextention", 3);
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
	public String getActiveMarketList(ModelMap model) {
		BidItem bidItem = AuctionCacheService.getActiveBidItem(AuctionCacheService.getActiveBidItemId());
		List<BidItem> bidItems = new ArrayList<BidItem>();
		bidItems.add(bidItem);
		model.put("bidItems",
				bidItemFilterService.getBidItemListForActiveMarket(bidItems, 2));
		return "bidderactivemarket";
	}

	@RequestMapping(value = "/closedmarketlist", method = RequestMethod.GET)
	public String getClosedMarket(ModelMap model) {
		List<BidItem> bidItems = AuctionCacheManager.getBidItems();
		model.put("bidItems", bidItemFilterService
				.getBidItemListForClosedMarket(bidItems, "2"));
		return "bidderclosedmarket";
	}

	@RequestMapping(value = "/saveautobid", method = RequestMethod.POST)
	public String saveAutoBid(
			@RequestParam(value = "bidItemId", required = true) Long bidItemId,
			@RequestParam(value = "autoBidAmount", required = true) Double bidAmount,
			@RequestParam(value = "categoryId", required = true) Long categoryId,
			ModelMap modelMap, HttpSession session) {
		session.getAttribute(CommonConstants.USER_NAME);

		String userName = session.getAttribute(CommonConstants.USER_NAME)
				.toString();
		List<BidItem> bidItemsList = null;
		System.out.println("bidItemId" + bidItemId);
		try {
			autoBidService.saveAutoBid(userName, categoryId, bidItemId,
					bidAmount, "No Comments",
					AuctionCacheManager.getActiveAuctionId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		/* bidItemsList = bidderService.saveAutoBid(); */
		LOG.debug(" For category: BidItems List::" + bidItemsList);
		modelMap.addAttribute("bidItemsList", bidItemsList);
		// List<BidderCategory> categoryList = getCategoryIdList(userName);
		// modelMap.addAttribute("bidderCategoryList", categoryList);
		// LOG.debug(" For category: bidderCategoryList List::" + categoryList);
		return "redirect:/bidder/home";

	}

	@RequestMapping(value = "/bid", method = RequestMethod.GET)
	public @ResponseBody
	String doBid(
			@RequestParam(value = "bidItemId", required = true) Long bidItemId,
			@RequestParam(value = "bidType", required = true) Integer bidType,
			@RequestParam(value = "bidAmount", required = true) Double bidAmount,
			@RequestParam(value = "comments", required = true) String comments,
			ModelMap model, HttpSession session)

	{

		String userName = session.getAttribute(CommonConstants.USER_NAME)
				.toString();
		boolean returnVal = false;//bidderService.doBid(bidItemId.intValue(), bidType,
				//new Double(bidAmount), userName, comments);

		if (bidType == 2) {
			BidItem bidItem = null;//bidItemsCacheService.getBidItem(bidItemId);

			Map<Long, String> bidItemWithRanks = new HashMap<Long, String>();
			Map<Long, String> bidItemWithAutoBidFlag = new HashMap<Long, String>();
			Bidder tempBidder = new Bidder();
			tempBidder.setBidderName(userName);
			List<Bidder> bidders = bidItem.getCurrentBidderList();
			int rank = bidders.indexOf(tempBidder) + 1;
			bidItemWithRanks.put(bidItem.getBidItemId(), rank + "");
			if (bidItem.isAutoBidFlag() && rank > 0
					&& bidders.get(rank - 1).isAutoBid()) {
				bidItemWithAutoBidFlag.put(bidItem.getBidItemId(), "2");
			} else {
				bidItemWithAutoBidFlag.put(bidItem.getBidItemId(), "1");
			}
			model.addAttribute("bidItemWithRanks", bidItemWithRanks);
			model.addAttribute("bidItemWithAutoBidFlag", bidItemWithAutoBidFlag);
			return "bidder/bidder_active";
		}

		LOG.debug("In doBid Method returnVal: " + returnVal);
		HashMap<String, String> h = new HashMap<String, String>();
		h.put("returnVal", "" + returnVal);
		return "Hello World";
	}
	
	
	
}
