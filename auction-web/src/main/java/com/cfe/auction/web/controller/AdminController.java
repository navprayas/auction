package com.cfe.auction.web.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import com.cfe.auction.service.cache.IBidItemsCacheService;
import com.cfe.auction.service.cache.manager.AuctionCacheManager;
import com.cfe.auction.service.cache.manager.AuctionCacheService;

@Controller
@RequestMapping("/admin/**")
public class AdminController {
	
	Logger logger = LoggerFactory.getLogger(AdminController.class);
	
	private static final String DD_MM_YYYY_HH_MM_SS = "dd/MM/yyyy HH:mm:ss";
	
	@Autowired
	private IAuctionService auctionService;
	@Autowired
	private UserService userService;
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	IBidItemsCacheService bidItemsCacheService;
	
	@Autowired
	AuctionCacheManager auctionCacheManager;

	@RequestMapping(value = "/auctionmanagement", method = RequestMethod.GET)
	public String auctionManegement(ModelMap model) {

		List<Auction> auctionList = auctionService.getAuctionList();
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
		model.addAttribute("auctionlist", auctionService.getAuctionList());
		model.addAttribute("aunctionRunningOrClosedPresent",
				aunctionRunningOrClosedPresent);

		return "auctionmanagement";
	}

	@RequestMapping(value = "/userauctionmap", method = RequestMethod.GET)
	public String getUserAuctionMap(ModelMap modelMap) {
		List<Auction> auctionList = auctionService.getAuctionList();
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

	@RequestMapping(value = "/createauction", method = RequestMethod.GET)
	public String createAuction() {
		return "createauction";
	}

	@RequestMapping(value = "/createauction", method = RequestMethod.POST)
	public String createAuction(@ModelAttribute Auction auction, Model model) {
		auctionService.create(auction);
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
	
	@RequestMapping(value = "/closeAuction", method = RequestMethod.GET)
	public String closeAuction(
			@RequestParam(value = "auctionId", required = true) Integer auctionId,
			ModelMap modelMap, HttpServletRequest httpServletRequest) {
		String msg = null;
		try {
			auctionService.closeAuction(auctionId);
			msg = "Auction Closed - " + auctionId;
			httpServletRequest.setAttribute("SuccessMessage", "Auction Closed");
			AuctionCacheService.flushCache();
			AuctionCacheManager.flushCache();
		} catch (Exception e) {
			e.printStackTrace();
			msg = "Auction Could not be Closed - " + auctionId;
		}
		return "redirect:/admin/superAdmin?Message=" + msg;
		// }

	}
	
	@RequestMapping(value = "/initcache", method = RequestMethod.GET)
	public String initcache(
			@RequestParam(value = "auctionStartTime", required = false) String auctionStart,
			@RequestParam(value = "auctionId", required = true) String auctionIdStr,
			@RequestParam(value = "auctionTimeExt", required = false) String auctionTimeExt,
			ModelMap modelMap, HttpServletRequest httpServletRequest) {
		Integer auctionId = null;
		if (auctionIdStr != null && !auctionIdStr.equals("")) {
			auctionId = Integer.parseInt(auctionIdStr);
		}
		if (auctionId == null || !auctionService.isValidAuction(auctionId)) {
			String msg = "Auction Not Present - " + auctionId;
			return "redirect:/admin/auctionmanagement?Message=" + msg;
		}

		AuctionCacheService.flushCache();
		AuctionCacheManager.flushCache();
		AuctionCacheManager.setActiveAuctionId(auctionId);
		auctionCacheManager.refreshAuctionCache();
		//KilimEngineGenerator.getAuctioneer().restart();

		try {
			Date actualAuctionStartTime = null;
			if (auctionStart != null && auctionStart.length() > 0) {
				SimpleDateFormat sdf = new SimpleDateFormat(DD_MM_YYYY_HH_MM_SS);
				actualAuctionStartTime = sdf.parse(auctionStart);
			} else if (auctionTimeExt != null && auctionTimeExt.length() > 0) {
				Calendar cal = Calendar.getInstance();
				int time = Integer.parseInt(auctionTimeExt);
				cal.add(Calendar.MINUTE, time + 3);
				actualAuctionStartTime = cal.getTime();
			} else {
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.MINUTE, 3);
				actualAuctionStartTime = cal.getTime();
			}
			bidItemsCacheService.setAuctionStartTime(actualAuctionStartTime);
			auctionService.setActualAuctionStartTime(auctionId,
					actualAuctionStartTime);
		} catch (ParseException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
		bidItemsCacheService.initCache();
		String msg = "Auction Started - " + auctionId;
		return "redirect:/admin/auctionmanagement?Message=" + msg;
	}


}
