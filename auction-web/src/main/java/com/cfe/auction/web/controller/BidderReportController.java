package com.cfe.auction.web.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cfe.auction.model.auction.persist.AuctionSearchBean;
import com.cfe.auction.model.persist.BidItem;
import com.cfe.auction.model.persist.BidderCategory;
import com.cfe.auction.model.persist.ClientDetails;
import com.cfe.auction.model.persist.User;
import com.cfe.auction.service.BidItemService;
import com.cfe.auction.service.BidderCategoryService;
import com.cfe.auction.service.cache.manager.AuctionCacheManager;
import com.cfe.auction.web.constants.SessionConstants;

/**
 * 
 * @author Vikas Anand
 *
 */
@Controller
@RequestMapping("/bidder/**")
public class BidderReportController {
	
	private static final Logger LOG = LoggerFactory
			.getLogger(BidderReportController.class);
	
	@Autowired
	BidItemService bidItemService;
	
	@Autowired
	BidderCategoryService bidderCategoryService;
	
	@RequestMapping(value = "/bidderreport", method = RequestMethod.GET)
	public String getwonLists(ModelMap modelMap, HttpSession session)
			throws Exception {

		User user = (User) session.getAttribute(SessionConstants.USER_INFO);
		
		ClientDetails clientDetails = (ClientDetails) session
				.getAttribute(SessionConstants.CLIENT_INFO);
		
		AuctionSearchBean auctionSearchBean = new AuctionSearchBean(clientDetails.getSchemaKey());
		auctionSearchBean.setClientId(clientDetails.getId());
		auctionSearchBean.setSchemaName(clientDetails.getSchemaKey());
		
		LOG.debug("UserName" + user.getUsername());
		List<BidItem> wonList = bidItemService.getWonList(user.getUsername(), auctionSearchBean);
		LOG.debug("closedbids List::" + wonList);
		modelMap.addAttribute("wonList", wonList);
		List<BidderCategory> categoryList = bidderCategoryService
				.getAllCategory(AuctionCacheManager.getActiveAuctionId(auctionSearchBean));
		modelMap.addAttribute("bidderCategoryList", categoryList);
		LOG.debug(" For category: bidderCategoryList List::" + categoryList);
		return "bidderreport";
	}
}
