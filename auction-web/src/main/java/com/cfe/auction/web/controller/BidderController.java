package com.cfe.auction.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.cfe.auction.model.persist.BidItem;
import com.cfe.auction.model.persist.BidderCategory;
import com.cfe.auction.model.persist.ClientDetails;
import com.cfe.auction.model.persist.User;
import com.cfe.auction.service.AutoBidService;
import com.cfe.auction.service.BidItemService;
import com.cfe.auction.service.BidderCategoryService;
import com.cfe.auction.service.BidsService;
import com.cfe.auction.service.IBidItemFilterService;
import com.cfe.auction.service.UserService;
import com.cfe.auction.service.cache.manager.AuctionCacheManager;
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
		if (auctionCacheBean != null && auctionCacheBean.getAuctionId() != null) {

			auctionSearchBean.setAuctionId(auctionCacheBean.getAuctionId());

			List<BidderCategory> bidderCategoryList = bidderCategoryService
					.getBidderCategory(user.getId(), auctionSearchBean);
			System.out.println("bidderCategoryList" + bidderCategoryList);
			LOG.debug("Category Id" + bidderCategoryList);

			List<BidItem> bidItems = AuctionCacheManager
					.getBidItems(auctionSearchBean);
			Date currDate = new Date();
			long refreshTime = auctionCacheBean.getAuctionStartTime().getTime()
					- currDate.getTime();
			refreshTime = (long) (refreshTime / 1000);
			LOG.debug("*****refreshTime::" + refreshTime);
			if (refreshTime <= 0) {
				//it means auction has already started
				BidItem bidItem = AuctionCacheManager
						.getActiveBidItem(auctionSearchBean);
				if (bidItem != null) {
					refreshTime = bidItem.getTimeLeft();
				}
			}
			if (bidItems != null) {
				System.out.println("BidItems" + bidItems);
				List<Integer> categoryIds = getCategoryIdList(bidderCategoryList);
				Long activeBidSeuenceId = AuctionCacheManager
						.getActiveBidSequenceId(auctionSearchBean);
				List<BidItem> bidItemsFinal = bidItemFilterService
						.getBidItemListForMarketList(bidItems, categoryIds,
								activeBidSeuenceId);
				model.put("bidItems", bidItemsFinal);
				if (bidItemsFinal != null) {
					for (BidItem bidItem : bidItemsFinal) {
						LOG.debug("bidItem : " + bidItem.getBidItemId() + " "
								+ bidItem.getBidSpan() + " " + refreshTime);
						bidItem.setTimeCounter(refreshTime);
						refreshTime += bidItem.getBidSpan();
						if (bidItem.getCurrentMarketPrice() == null) {
							bidItem.setCurrentMarketPrice(bidItem
									.getMinBidPrice());
						}
					}
				}
				model.put("timeextention", 60);
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

		AuctionSearchBean auctionSearchBean = new AuctionSearchBean(
				clientDetails.getSchemaKey());
		auctionSearchBean.setClientId(clientDetails.getId());
		auctionSearchBean.setSchemaName(clientDetails.getSchemaKey());

		auctionSearchBean.setAuctionId(AuctionCacheManager
				.getActiveAuctionId(auctionSearchBean));

		List<BidderCategory> bidderCategoryList = bidderCategoryService
				.getBidderCategory(user.getId(), auctionSearchBean);
		BidItem bidItem = AuctionCacheManager
				.getActiveBidItem(auctionSearchBean);
		if (bidItem != null) {
			List<BidItem> bidItems = new ArrayList<BidItem>();
			bidItems.add(bidItem);
			List<Integer> categoryIds = getCategoryIdList(bidderCategoryList);
			model.put("bidItems", bidItemFilterService
					.getBidItemListForActiveMarketList(bidItems, categoryIds,
							bidItem.getSeqId()));
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

		List<BidderCategory> bidderCategoryList = bidderCategoryService
				.getBidderCategory(user.getId(), auctionSearchBean);
		List<BidItem> bidItems = AuctionCacheManager
				.getBidItems(auctionSearchBean);
		List<Integer> categoryIds = getCategoryIdList(bidderCategoryList);
		model.put("bidItems", bidItemFilterService
				.getBidItemListForClosedMarketList(bidItems, categoryIds,
						AuctionCacheManager
								.getActiveBidSequenceId(auctionSearchBean)));
		return "bidderclosedmarket";
	}

	/*@RequestMapping(value = "/saveautobid", method = RequestMethod.POST)
	public String saveAutoBid(
			@RequestParam(value = "bidItemId", required = true) Long bidItemId,
			@RequestParam(value = "autoBidAmount", required = true) Double bidAmount,
			@RequestParam(value = "categoryId", required = true) Long categoryId,
			ModelMap modelMap, HttpSession session) {
		session.getAttribute(CommonConstants.USER_NAME);
		AuctionSearchBean auctionSearchBean = new AuctionSearchBean();
		String userName = session.getAttribute(CommonConstants.USER_NAME)
				.toString();
		ClientDetails clientDetails = (ClientDetails) session
				.getAttribute(SessionConstants.CLIENT_INFO);
		auctionSearchBean.setAuctionId(AuctionCacheManager
				.getActiveAuctionId(auctionSearchBean));
		auctionSearchBean.setSchemaName(clientDetails.getSchemaKey());
		List<BidItem> bidItemsList = null;
		try {
			autoBidService.saveAutoBid(userName, categoryId, bidItemId,
					bidAmount, "No Comments", auctionSearchBean);
		} catch (Exception e) {
			e.printStackTrace();
		}
		 bidItemsList = bidderService.saveAutoBid(); 
		LOG.debug(" For category: BidItems List::" + bidItemsList);
		modelMap.addAttribute("bidItemsList", bidItemsList);
		// List<BidderCategory> categoryList = getCategoryIdList(userName);
		// modelMap.addAttribute("bidderCategoryList", categoryList);
		// LOG.debug(" For category: bidderCategoryList List::" + categoryList);
		return "redirect:/bidder/home";

	}
*/
	/*@RequestMapping(value = "/bid", method = RequestMethod.GET)
	public @ResponseBody
	String doBid(
			@RequestParam(value = "bidItemId", required = true) Long bidItemId,
			@RequestParam(value = "bidType", required = true) Integer bidType,
			@RequestParam(value = "bidAmount", required = true) Double bidAmount,
			@RequestParam(value = "comments", required = true) String comments,
			ModelMap model, HttpSession session) {
		System.out.println(bidItemId + " " + bidType + " " + bidAmount + ""
				+ comments);
		session.getAttribute(CommonConstants.USER_NAME);
		AuctionSearchBean auctionSearchBean = new AuctionSearchBean();
		String userName = session.getAttribute(CommonConstants.USER_NAME)
				.toString();
		ClientDetails clientDetails = (ClientDetails) session
				.getAttribute(SessionConstants.CLIENT_INFO);
		
		 * auctionSearchBean.setSchemaName(clientDetails.getSchemaKey());
		 * System.out.println(userName); boolean returnVal = false; Bid bid =
		 * new Bid(); bid.setBidId(bidItemId); bid.setBidType(bidType);
		 * bid.setBidAmount(bidAmount); bid.setBidderName(userName);
		 * bid.setComments(comments); bid.setAuctionId(AuctionCacheManager
		 * .getActiveAuctionId(auctionSearchBean));
		 * bidsService.createBidService(bid,auctionSearchBean);
		 

		auctionSearchBean.setSchemaName("PersistenceUnitB");
		Bid bid = new Bid();
		bid.setBidId(80L);
		bid.setBidType(1);
		bid.setBidAmount(34000.0);
		bid.setBidderName("bidder");
		bid.setComments("comments");
		bid.setAuctionId(80);
		bid.setBidItemId(211L);
		bidsService.createBidService(bid, auctionSearchBean);

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

		
		 * LOG.debug("In doBid Method returnVal: " + returnVal); HashMap<String,
		 * String> h = new HashMap<String, String>(); h.put("returnVal", "" +
		 * returnVal);
		 return "success";
	}
*/
	/*@RequestMapping(value = "/bidderreport", method = RequestMethod.GET)
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
				.getAllCategory(AuctionCacheManager
						.getActiveAuctionId(auctionSearchBean));
		modelMap.addAttribute("bidderCategoryList", categoryList);
		LOG.debug(" For category: bidderCategoryList List::" + categoryList);
		return "bidderreport";
	}*/
}
