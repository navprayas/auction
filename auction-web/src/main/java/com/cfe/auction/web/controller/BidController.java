package com.cfe.auction.web.controller;

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
import com.cfe.auction.model.persist.ClientDetails;
import com.cfe.auction.service.AutoBidService;
import com.cfe.auction.service.cache.manager.AuctionCacheManager;
import com.cfe.auction.web.constants.CommonConstants;
import com.cfe.auction.web.constants.SessionConstants;

/**
 * 
 * @author Vikas Anand
 *
 */
@Controller
@RequestMapping("/bidder/**")
public class BidController {
	
	private static final Logger LOG = LoggerFactory
			.getLogger(BidController.class);
	
	@Autowired
	AutoBidService autoBidService;
	
	@RequestMapping(value = "/saveautobid", method = RequestMethod.POST)
	public String saveAutoBid(
			@RequestParam(value = "bidItemId", required = true) Long bidItemId,
			@RequestParam(value = "autoBidAmount", required = true) Double bidAmount,
			@RequestParam(value = "categoryId", required = true) Long categoryId,
			ModelMap modelMap, HttpSession session) {
		session.getAttribute(CommonConstants.USER_NAME);

		String userName = session.getAttribute(CommonConstants.USER_NAME)
				.toString();
		
		ClientDetails clientDetails = (ClientDetails) session
				.getAttribute(SessionConstants.CLIENT_INFO);
		
		AuctionSearchBean auctionSearchBean = new AuctionSearchBean(clientDetails.getSchemaKey());
		auctionSearchBean.setClientId(clientDetails.getId());
		auctionSearchBean.setSchemaName(clientDetails.getSchemaKey());
		auctionSearchBean.setAuctionId(AuctionCacheManager.getActiveAuctionId(auctionSearchBean));

		System.out.println("bidItemId" + bidItemId);
		try {
			autoBidService.saveAutoBid(userName, categoryId, bidItemId,
					bidAmount, "No Comments",
					auctionSearchBean);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
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


}
