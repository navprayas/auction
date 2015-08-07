package com.cfe.auction.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.cfe.auction.model.persist.Auction;
import com.cfe.auction.model.persist.User;
import com.cfe.auction.service.CategoryService;
import com.cfe.auction.service.IAuctionService;
import com.cfe.auction.service.UserService;
import com.cfe.auction.web.constants.CommonConstants;

@Controller
@RequestMapping("/admin/**")
public class AdminController {
	Logger logger = LoggerFactory.getLogger(AdminController.class);
	@Autowired
	private IAuctionService iAuctionService;
	@Autowired
	private UserService userService;
	@Autowired
	private CategoryService categoryService;

	@RequestMapping(value = "/auctionmanagement", method = RequestMethod.GET)
	public String auctionManegement(ModelMap model) {

		List<Auction> auctionList = iAuctionService.getAuctionList();
		Integer aunctionRunningOrClosedPresent = 0;
		if (auctionList != null) {
			for (Auction auction : auctionList) {
				if ("END".equalsIgnoreCase(auction.getStatus())
						|| "Running".equalsIgnoreCase(auction.getStatus())) {
					aunctionRunningOrClosedPresent++;
					break;
				}
			}
		}
		model.addAttribute("auctionlist", iAuctionService.getAuctionList());
		model.addAttribute("aunctionRunningOrClosedPresent",
				aunctionRunningOrClosedPresent);

		return "auctionmanagement";
	}

	@RequestMapping(value = "/userauctionmap", method = RequestMethod.GET)
	public String getUserAuctionMap(ModelMap modelMap) {
		List<Auction> auctionList = iAuctionService.getAuctionList();
		System.out.println("Auction list" + auctionList);
		List<Auction> newAuctionList = new ArrayList<Auction>();
		for (Auction auction : auctionList) {
			if ("Start".equalsIgnoreCase(auction.getStatus())
					|| "Running".equalsIgnoreCase(auction.getStatus())) {
				newAuctionList.add(auction);
			}
		}
		modelMap.addAttribute("auctionlist", newAuctionList);
		modelMap.addAttribute("userlist", userService.listAll());
		modelMap.addAttribute("categorylist", categoryService.listAll());
		return "userauctionmap";
	}

	@RequestMapping(value = "/userauctionmap", method = RequestMethod.POST)
	public String setUserAuctionMap(
			@RequestParam(value = "selectedAuctionId", required = true) Long selectedAuctionId,
			@RequestParam(value = "selectedCategoryIdList", required = true) String selectedCategoryIdList,
			@RequestParam(value = "selectedUserIdList", required = true) String selectedUserIdList) {
		String msg = null;
		try {
			logger.debug("selectedAuctionId: " + selectedAuctionId);
			logger.debug("selectedCategoryIdList: " + selectedCategoryIdList);
			logger.debug("selectedUserIdList: " + selectedUserIdList);
			// commonService.setUserAuctionMap(selectedAuctionId,selectedCategoryIdList,
			// selectedUserIdList);
			msg = "All selected Users are now allowed for the auction "
					+ selectedAuctionId;
		} catch (Exception e) {
			e.printStackTrace();
			msg = "Mapping Not successful";
		}
		return "userauctionmap";
	}

	/*@RequestMapping(value = "/changepassword", method = RequestMethod.GET)
	public String changePassword() {
		return "adminchangepassword";
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
		return "adminchangepassword";
	}*/

	@RequestMapping(value = "/createauction", method = RequestMethod.GET)
	public String createAuction() {
		return "createauction";
	}

	@RequestMapping(value = "/createauction", method = RequestMethod.POST)
	public String createAuction(@ModelAttribute Auction auction, Model model) {
		iAuctionService.create(auction);
		return "createauction";
	}

	@RequestMapping(value = "/registeruser", method = RequestMethod.GET)
	public String registerUser() {
		return "userregisteration";
	}

	@RequestMapping(value = "/registeruser", method = RequestMethod.POST)
	public String registerUser(@ModelAttribute User user, Model model) {
		userService.addUser(user);
		return "userregisteration";
	}

}
