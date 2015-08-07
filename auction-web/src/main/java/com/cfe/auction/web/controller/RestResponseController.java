package com.cfe.auction.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cfe.auction.model.persist.BidItem;
import com.cfe.auction.model.persist.BidderCategory;
import com.cfe.auction.model.persist.User;
import com.cfe.auction.service.BidderCategoryService;
import com.cfe.auction.service.IBidItemFilterService;
import com.cfe.auction.web.cache.manager.AuctionCacheManager;
import com.cfe.auction.web.constants.SessionConstants;
import com.fasterxml.jackson.databind.ObjectMapper;

@RequestMapping("/bidder/**")
public class RestResponseController {
	private static final Logger LOG = LoggerFactory
			.getLogger(RestResponseController.class);
	@Autowired
	private BidderCategoryService bidderCategoryService;

	@Autowired
	private IBidItemFilterService bidItemFilterService;

	@RequestMapping(value = "/marketlistajaxcall", produces = {
			"application/xml", "application/json" }, method = RequestMethod.GET)
	public @ResponseBody
	String getMarketListAjax(ModelMap model, HttpSession session) {
		ObjectMapper mapper = new ObjectMapper();
		String result = null;
		System.out.println("Make ajax call");
		List<BidItem> filteredbidItems = new ArrayList<BidItem>();
		User user = (User) session.getAttribute(SessionConstants.USER_INFO);
		if (AuctionCacheManager.getActiveAuctionId() != null) {
			List<BidderCategory> bidderCategoryList = bidderCategoryService
					.getBidderCategory(user.getId(),
							AuctionCacheManager.getActiveAuctionId());
			LOG.debug("Category Id" + bidderCategoryList);

			List<BidItem> bidItems = AuctionCacheManager.getBidItems();
			if (bidderCategoryList != null && bidderCategoryList.size() > 0) {
				filteredbidItems = bidItemFilterService
						.getBidItemListForActiveMarket(bidItems,
								bidderCategoryList.get(0).getCategoryId());
				System.out.println("filtered Item " + filteredbidItems);
			}

			try {
				result = mapper.writeValueAsString(filteredbidItems);
				System.out.println("Ajax filtered data got result " + result);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return result;
	}
}