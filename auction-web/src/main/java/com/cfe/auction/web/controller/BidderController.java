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

	/*
	 * @RequestMapping(value = { }, method = RequestMethod.GET) public String
	 * modelerHome(ModelMap model, HttpSession session) { User user = (User)
	 * session.getAttribute(SessionConstants.USER_INFO);
	 * System.out.println("Auction Id" +
	 * AuctionCacheManager.getActiveAuctionId());
	 * 
	 * if (AuctionCacheManager.getActiveAuctionId() != null) {
	 * List<BidderCategory> bidderCategoryList = bidderCategoryService
	 * .getBidderCategory(user.getId(),
	 * AuctionCacheManager.getActiveAuctionId()); LOG.debug("Category Id" +
	 * bidderCategoryList);
	 * 
	 * List<BidItem> bidItems = AuctionCacheManager.getBidItems();
	 * System.out.println("BidItems" + bidItems); List<Integer> categoryIds =
	 * getCategoryIdList(bidderCategoryList); model.put("bidItems",
	 * bidItemFilterService .getBidItemListForActiveMarket(bidItems,
	 * categoryIds,AuctionCacheService.getActiveBidSequenceId()));
	 * model.put("timeextention", 3);
	 * 
	 * } return "bidderhome"; }
	 */

	@RequestMapping(value = { "/marketlist", "/home", "/index" }, method = RequestMethod.GET)
	public String getMarketList(ModelMap model, HttpSession session) {
		User user = (User) session.getAttribute(SessionConstants.USER_INFO);
		ClientDetails clientDetails = (ClientDetails) session
				.getAttribute(SessionConstants.CLIENT_INFO);
		if (AuctionCacheManager.getActiveAuctionId() != null) {
			List<BidderCategory> bidderCategoryList = bidderCategoryService
					.getBidderCategory(user.getId(),
							AuctionCacheManager.getActiveAuctionId());
			System.out.println("bidderCategoryList" + bidderCategoryList);
			LOG.debug("Category Id" + bidderCategoryList);

			List<BidItem> bidItems = AuctionCacheManager.getBidItems();
			if (bidItems != null) {
				System.out.println("BidItems" + bidItems);
				List<Integer> categoryIds = getCategoryIdList(bidderCategoryList);
				model.put("bidItems", bidItemFilterService
						.getBidItemListForMarketList(bidItems, categoryIds,
								AuctionCacheService.getActiveBidSequenceId()));
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
		List<BidderCategory> bidderCategoryList = bidderCategoryService
				.getBidderCategory(user.getId(),
						AuctionCacheManager.getActiveAuctionId());
		BidItem bidItem = AuctionCacheService
				.getActiveBidItem(AuctionCacheService.getActiveBidItemId());
		List<BidItem> bidItems = new ArrayList<BidItem>();
		bidItems.add(bidItem);

		List<Integer> categoryIds = getCategoryIdList(bidderCategoryList);
		model.put("bidItems", bidItemFilterService
				.getBidItemListForActiveMarketList(bidItems, categoryIds,
						AuctionCacheService.getActiveBidSequenceId()));

		return "bidderactivemarket";
	}

	@RequestMapping(value = "/closedmarketlist", method = RequestMethod.GET)
	public String getClosedMarket(ModelMap model, HttpSession session) {
		User user = (User) session.getAttribute(SessionConstants.USER_INFO);
		ClientDetails clientDetails = (ClientDetails) session
				.getAttribute(SessionConstants.CLIENT_INFO);
		List<BidderCategory> bidderCategoryList = bidderCategoryService
				.getBidderCategory(user.getId(),
						AuctionCacheManager.getActiveAuctionId());
		List<BidItem> bidItems = AuctionCacheManager.getBidItems();
		List<Integer> categoryIds = getCategoryIdList(bidderCategoryList);
		model.put("bidItems", bidItemFilterService
				.getBidItemListForClosedMarketList(bidItems, categoryIds,
						AuctionCacheService.getActiveBidSequenceId()));
		return "bidderclosedmarket";
	}

	@RequestMapping(value = "/saveautobid", method = RequestMethod.POST)
	public String saveAutoBid(
			@RequestParam(value = "bidItemId", required = true) Long bidItemId,
			@RequestParam(value = "autoBidAmount", required = true) Double bidAmount,
			@RequestParam(value = "categoryId", required = true) Long categoryId,
			ModelMap modelMap, HttpSession session) {
		session.getAttribute(CommonConstants.USER_NAME);
AuctionSearchBean auctionSearchBean=new AuctionSearchBean();
		String userName = session.getAttribute(CommonConstants.USER_NAME)
				.toString();
		ClientDetails clientDetails = (ClientDetails) session
				.getAttribute(SessionConstants.CLIENT_INFO);
		auctionSearchBean.setAuctionId(AuctionCacheManager.getActiveAuctionId());
		List<BidItem> bidItemsList = null;
		System.out.println("bidItemId" + bidItemId);
		try {
			autoBidService.saveAutoBid(userName, categoryId, bidItemId,
					bidAmount, "No Comments",
					auctionSearchBean);
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
			ModelMap model, HttpSession session) {
		System.out.println(bidItemId + " " + bidType + " " + bidAmount + ""
				+ comments);
		String userName = session.getAttribute(CommonConstants.USER_NAME)
				.toString();
		ClientDetails clientDetails = (ClientDetails) session
				.getAttribute(SessionConstants.CLIENT_INFO);
		System.out.println(userName);
		boolean returnVal = false;// bidderService.doBid(bidItemId.intValue(),
									// bidType,
		// new Double(bidAmount), userName, comments);

		if (bidType == 2) {
			BidItem bidItem = null;// bidItemsCacheService.getBidItem(bidItemId);

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
			return "success";
		}

		LOG.debug("In doBid Method returnVal: " + returnVal);
		HashMap<String, String> h = new HashMap<String, String>();
		h.put("returnVal", "" + returnVal);
		return "success";
	}

	@RequestMapping(value = "/bidderreport", method = RequestMethod.GET)
	public String getwonLists(ModelMap modelMap, HttpSession session)
			throws Exception {
		AuctionSearchBean auctionSearchBean = new AuctionSearchBean();
		User user = (User) session.getAttribute(SessionConstants.USER_INFO);
		ClientDetails clientDetails = (ClientDetails) session
				.getAttribute(SessionConstants.CLIENT_INFO);
		auctionSearchBean.setSchemaName(clientDetails.getSchemaKey());
		LOG.debug("UserName" + user.getUsername());

		List<BidItem> wonList = bidItemService.getWonList(user.getUsername(),
				auctionSearchBean);
		LOG.debug("closedbids List::" + wonList);
		modelMap.addAttribute("wonList", wonList);
		List<BidderCategory> categoryList = bidderCategoryService
				.getAllCategory(AuctionCacheManager.getActiveAuctionId());
		modelMap.addAttribute("bidderCategoryList", categoryList);
		LOG.debug(" For category: bidderCategoryList List::" + categoryList);
		return "bidderreport";
	}
}
