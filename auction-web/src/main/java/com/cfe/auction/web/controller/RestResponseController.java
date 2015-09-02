package com.cfe.auction.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cfe.auction.common.BidItemUi;
import com.cfe.auction.model.auction.persist.AuctionSearchBean;
import com.cfe.auction.model.persist.BidItem;
import com.cfe.auction.model.persist.BidderCategory;
import com.cfe.auction.model.persist.ClientDetails;
import com.cfe.auction.model.persist.User;
import com.cfe.auction.service.BidderCategoryService;
import com.cfe.auction.service.IBidItemFilterService;
import com.cfe.auction.service.cache.manager.AuctionCacheManager;
import com.cfe.auction.web.constants.SessionConstants;
import com.fasterxml.jackson.databind.ObjectMapper;

@RequestMapping({ "/bidder/**", "/observer/**" })
@Controller
public class RestResponseController {
	private static final Logger LOG = LoggerFactory
			.getLogger(RestResponseController.class);
	@Autowired
	private BidderCategoryService bidderCategoryService;

	@Autowired
	private IBidItemFilterService bidItemFilterService;

	@RequestMapping(value = "/marketlistajaxcall", method = RequestMethod.GET)
	public @ResponseBody
	String getMarketListAjax(ModelMap model, HttpSession session) {
		ObjectMapper mapper = new ObjectMapper();
		String result = null;
		List<BidItemUi> filteredbidItems = new ArrayList<BidItemUi>();
		User user = (User) session.getAttribute(SessionConstants.USER_INFO);
		ClientDetails clientDetails = (ClientDetails) session
				.getAttribute(SessionConstants.CLIENT_INFO);
		AuctionSearchBean auctionSearchBean = new AuctionSearchBean(clientDetails.getSchemaKey());
		auctionSearchBean.setClientId(clientDetails.getId());
		auctionSearchBean.setSchemaName(clientDetails.getSchemaKey());
		if (AuctionCacheManager.getActiveAuctionId(auctionSearchBean) != null) {
			List<BidderCategory> bidderCategoryList = bidderCategoryService
					.getBidderCategory(user.getId(),auctionSearchBean);
			LOG.debug("Category Id" + bidderCategoryList);
			List<BidItem> bidItems = AuctionCacheManager.getBidItems(auctionSearchBean);
			System.out.println("Ajax bid items" + bidItems);
			if (bidderCategoryList != null && bidderCategoryList.size() > 0) {
				List<Integer> categoryIds = getCategoryIdList(bidderCategoryList);
				filteredbidItems = bidItemFilterService
						.getBidItemListForMarketListAjax(bidItems, categoryIds,
								AuctionCacheManager.getActiveBidSequenceId(auctionSearchBean));
			}
			// System.out.println("MArket List Item"+filteredbidItems.get(0).getCreatedTime());
			try {
				result = mapper.writeValueAsString(filteredbidItems);
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
		}
		return result;
	}

	@RequestMapping(value = "/activemarketlistajaxcall", method = RequestMethod.GET)
	public @ResponseBody
	String getActiveMarketListAjax(ModelMap model, HttpSession session) {
		ClientDetails clientDetails = (ClientDetails) session
				.getAttribute(SessionConstants.CLIENT_INFO);
		AuctionSearchBean auctionSearchBean = new AuctionSearchBean(clientDetails.getSchemaKey());
		auctionSearchBean.setClientId(clientDetails.getId());
		auctionSearchBean.setSchemaName(clientDetails.getSchemaKey());
		BidItem bidItem =  AuctionCacheManager.getActiveBidItem(auctionSearchBean);
		User user = (User) session.getAttribute(SessionConstants.USER_INFO);
		ObjectMapper mapper = new ObjectMapper();
		String result = null;
		List<BidItemUi> filteredbidItems = new ArrayList<BidItemUi>();
		List<BidItem> bidItems = AuctionCacheManager.getBidItems(auctionSearchBean);
		List<BidderCategory> bidderCategoryList = bidderCategoryService
				.getBidderCategory(user.getId(),auctionSearchBean);
		bidItems.add(bidItem);
		List<Integer> categoryIds = getCategoryIdList(bidderCategoryList);
		filteredbidItems = bidItemFilterService
				.getBidItemListForActiveMarketAjax(bidItems, categoryIds,
						AuctionCacheManager.getActiveBidSequenceId(auctionSearchBean));
		try {
			result = mapper.writeValueAsString(filteredbidItems);
			System.out.println("Ajax filtered data got result " + result);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}

		return result;
	}

	@RequestMapping(value = "/closedmarketlistajaxcall", method = RequestMethod.GET)
	public @ResponseBody
	String getClosedMarketListAjax(ModelMap model, HttpSession session) {
		ObjectMapper mapper = new ObjectMapper();
		User user = (User) session.getAttribute(SessionConstants.USER_INFO);
		ClientDetails clientDetails = (ClientDetails) session
				.getAttribute(SessionConstants.CLIENT_INFO);
		AuctionSearchBean auctionSearchBean = new AuctionSearchBean(clientDetails.getSchemaKey());
		auctionSearchBean.setClientId(clientDetails.getId());
		auctionSearchBean.setSchemaName(clientDetails.getSchemaKey());
		
		String result = null;
		System.out.println("Make ajax call");
		List<BidItemUi> filteredbidItems = new ArrayList<BidItemUi>();
		List<BidItem> bidItems = AuctionCacheManager.getBidItems(auctionSearchBean);
		List<BidderCategory> bidderCategoryList = bidderCategoryService
				.getBidderCategory(user.getId(),
						auctionSearchBean);
		List<Integer> categoryIds = getCategoryIdList(bidderCategoryList);
		filteredbidItems = bidItemFilterService
				.getBidItemListForClosedMarketAjax(bidItems, categoryIds,
						AuctionCacheManager.getActiveBidSequenceId(auctionSearchBean));

		try {
			result = mapper.writeValueAsString(filteredbidItems);
			System.out.println("Ajax filtered data got result " + result);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}

		return result;
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
}
