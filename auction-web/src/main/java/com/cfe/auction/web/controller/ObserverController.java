package com.cfe.auction.web.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.cfe.auction.model.persist.BidItem;
import com.cfe.auction.model.persist.BidderCategory;
import com.cfe.auction.model.persist.User;
import com.cfe.auction.service.BidItemService;
import com.cfe.auction.service.BidderCategoryService;
import com.cfe.auction.service.IAuctionService;
import com.cfe.auction.service.UserService;
import com.cfe.auction.service.impl.BidItemFilterServiceImpl;
import com.cfe.auction.web.constants.CommonConstants;
import com.cfe.auction.web.constants.SessionConstants;

@Controller
@RequestMapping("/observer/**")
public class ObserverController {
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

	@RequestMapping(value = "/marketlist", method = RequestMethod.GET)
	public String getMarketList(ModelMap model, HttpSession session) {
		User user = (User) session.getAttribute(SessionConstants.USER_INFO);
		List<BidderCategory> bidderCategoryList = bidderCategoryService
				.getBidderCategory(user.getId(), 76);
		System.out.println("Category Id" + bidderCategoryList);

		List<BidItem> bidItems = auctionService.getActiveAuctionBidItem();
		model.put("bidItems", bidItemFilterServiceImpl
				.getBidItemListForcategoryId(bidItems, "2"));
		return "marketlist";
	}

	@RequestMapping(value = "/activemarketlist", method = RequestMethod.GET)
	public String getActiveMarketList(ModelMap model) {
		List<BidItem> bidItems = auctionService.getActiveAuctionBidItem();
		model.put("bidItems", bidItemFilterServiceImpl
				.getBidItemListForActiveMarket(bidItems, 2));
		return "activemarket";
	}

	@RequestMapping(value = "/closedmarketlist", method = RequestMethod.GET)
	public String getClosedMarket(ModelMap model) {
		List<BidItem> bidItems = auctionService.getActiveAuctionBidItem();
		model.put("bidItems", bidItemFilterServiceImpl
				.getBidItemListForClosedMarket(bidItems, "2"));
		return "closedmarket";
	}

	@RequestMapping(value = "/changepassword", method = RequestMethod.GET)
	public String changePassword() {
		return "observerchangepassword";
	}

	@RequestMapping(value = "/changepassword", method = RequestMethod.POST)
	public String changePassword(@RequestParam String oldPassword,
			@RequestParam String newPassword, ModelMap model,
			HttpSession session) {
		String message = "Password Changed Successfully";
		User user = userService.getUserByUserName(session.getAttribute(
				CommonConstants.USER_NAME).toString());
		if (user.getPassword().equals(userService.eccodePassword(oldPassword))) {
			user.setPassword(userService.eccodePassword(newPassword));
			userService.update(user);
		} else {
			message = "You have entered wrong current password. Please enter correct current password";
		}
		model.addAttribute("message", message);
		return "observerchangepassword";
	}
}