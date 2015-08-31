package com.cfe.auction.web.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
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

import com.cfe.auction.model.auction.persist.AuctionCacheBean;
import com.cfe.auction.model.auction.persist.AuctionSearchBean;
import com.cfe.auction.model.persist.Auction;
import com.cfe.auction.model.persist.ClientDetails;
import com.cfe.auction.model.persist.User;
import com.cfe.auction.service.CategoryService;
import com.cfe.auction.service.IAuctionService;
import com.cfe.auction.service.UserService;
import com.cfe.auction.service.cache.IBidItemsCacheService;
import com.cfe.auction.service.cache.manager.AuctionCacheManager;
import com.cfe.auction.service.cache.manager.AuctionCacheService;
import com.cfe.auction.web.constants.SessionConstants;

@Controller
@RequestMapping("/admin/**")
public class AdminController {

	Logger logger = LoggerFactory.getLogger(AdminController.class);

	private static final String DD_MM_YYYY_HH_MM_SS = "dd/MM/yyyy HH:mm:ss";

	@Autowired
	private IAuctionService iAuctionService;

	@Autowired
	private UserService userService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	IBidItemsCacheService bidItemsCacheService;

	@Autowired
	AuctionCacheManager auctionCacheManager;

	@RequestMapping(value = { "/home", "/index", "/auctionmanagement" }, method = RequestMethod.GET)
	public String adminHome(ModelMap model, HttpSession session) {

		ClientDetails clientDetails = (ClientDetails) session
				.getAttribute(SessionConstants.CLIENT_INFO);
		if(clientDetails != null && clientDetails.getSchemaKey() != null) {
			List<Auction> auctionList = iAuctionService.getAuctionList(clientDetails.getSchemaKey());
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
			model.addAttribute("auctionlist", auctionList);
			model.addAttribute("aunctionRunningOrClosedPresent",
					aunctionRunningOrClosedPresent);
		}
		return "auctionmanagement";
	}


	@RequestMapping(value = "/userauctionmap", method = RequestMethod.GET)
	public String getUserAuctionMap(ModelMap modelMap,
			@RequestParam(required = false) String msg, HttpSession session) {
		System.out.println("Message" + msg);
		ClientDetails clientDetails = (ClientDetails) session
				.getAttribute(SessionConstants.CLIENT_INFO);
		if(clientDetails != null && clientDetails.getSchemaKey() != null) {
		
			List<Auction> auctionList = iAuctionService.getAuctionList(clientDetails.getSchemaKey());
	
			List<Auction> newAuctionList = new ArrayList<Auction>();
			for (Auction auction : auctionList) {
				if ("Start".equalsIgnoreCase(auction.getStatus())
						|| "Running".equalsIgnoreCase(auction.getStatus())) {
					newAuctionList.add(auction);
				}
			}
			modelMap.addAttribute("auctionlist", newAuctionList);
			modelMap.addAttribute("userlist", userService.findAll(User.class));
			modelMap.addAttribute("categorylist", categoryService.listAll());
			modelMap.addAttribute("message", msg);
		}
		return "userauctionmap";
	}

	@RequestMapping(value = "/userauctionmap", method = RequestMethod.POST)
	public String setUserAuctionMap(
			@RequestParam(value = "selectedAuctionId", required = true) Long selectedAuctionId,
			@RequestParam(value = "selectedCategoryIdList", required = true) String selectedCategoryIdList,
			@RequestParam(value = "selectedUserIdList", required = true) String selectedUserIdList) {
		String msg = null;
		System.out.println(selectedAuctionId + "   " + selectedCategoryIdList
				+ "   " + selectedUserIdList);

		try {
			logger.debug("selectedAuctionId: " + selectedAuctionId);
			logger.debug("selectedCategoryIdList: " + selectedCategoryIdList);
			logger.debug("selectedUserIdList: " + selectedUserIdList);
			userService.setAuctionMapping(selectedAuctionId,
					selectedCategoryIdList, selectedUserIdList);

			userService.setAuctionMapping(selectedAuctionId,
					selectedCategoryIdList, selectedUserIdList);
			msg = "All selected Users are now allowed for the auction "
					+ selectedAuctionId;
		} catch (Exception e) {
			e.printStackTrace();
			msg = "Mapping Not successful";
		}
		return "redirect:/admin/userauctionmap?msg=" + msg;
	}

	/*@RequestMapping(value = "/closeauction", method = RequestMethod.GET)
	public String closeRunningAuction(@RequestParam Integer auctionId,
			ModelMap model, HttpServletRequest httpServletRequest,
			HttpSession session) {
		ClientDetails clientDetails = (ClientDetails) session
				.getAttribute(SessionConstants.CLIENT_INFO);
		String msg = null;
		AuctionSearchBean auctionSearchBean = new AuctionSearchBean(clientDetails.getSchemaKey());
		auctionSearchBean.setClientId(clientDetails.getId());
		auctionSearchBean.setSchemaName(clientDetails.getSchemaKey());
		auctionSearchBean.setAuctionId(auctionId);
		try {
			msg = closeAuction(auctionSearchBean, httpServletRequest);
		} catch (Exception e) {
			e.printStackTrace();
			msg = "Auction Could not be Closed - " + auctionId;
		}
		return "redirect:/admin/auctionmanagement?Message=" + msg;
	}
*/
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

	@RequestMapping(value = {"/closeAuction","/closeauction"}, method = RequestMethod.GET)
	public String closeAuction(
			@RequestParam(value = "auctionId", required = true) Integer auctionId,
			ModelMap modelMap, HttpServletRequest httpServletRequest, HttpSession session) {
		String msg = null;
		ClientDetails clientDetails = (ClientDetails) session
				.getAttribute(SessionConstants.CLIENT_INFO);
		AuctionSearchBean auctionSearchBean = new AuctionSearchBean(clientDetails.getSchemaKey());
		auctionSearchBean.setClientId(clientDetails.getId());
		auctionSearchBean.setSchemaName(clientDetails.getSchemaKey());
		auctionSearchBean.setAuctionId(auctionId);
		try {
			msg = closeAuction(auctionSearchBean, httpServletRequest);
		} catch (Exception e) {
			e.printStackTrace();
			msg = "Auction Could not be Closed - " + auctionId;
		}
		return "redirect:/admin/auctionmanagement?Message=" + msg;
	}

	private String closeAuction(AuctionSearchBean auctionSearchBean,
			HttpServletRequest httpServletRequest) {
		iAuctionService.closeAuction(auctionSearchBean);
		String msg = "Auction Closed - " + auctionSearchBean.getAuctionId();
		httpServletRequest.setAttribute("SuccessMessage", "Auction Closed");
		AuctionCacheService.flushCache();
		AuctionCacheManager.flushCache();
		return msg;
	}

	@RequestMapping(value = "/initcache", method = RequestMethod.GET)
	public String initcache(
			@RequestParam(value = "auctionStartTime", required = false) String auctionStart,
			@RequestParam(value = "auctionId", required = true) String auctionIdStr,
			@RequestParam(value = "auctionTimeExt", required = false) String auctionTimeExt,
			ModelMap modelMap, HttpServletRequest httpServletRequest, HttpSession session) {
		Integer auctionId = null;
		ClientDetails clientDetails = (ClientDetails) session
				.getAttribute(SessionConstants.CLIENT_INFO);
		if (auctionIdStr != null && !auctionIdStr.equals("")) {
			auctionId = Integer.parseInt(auctionIdStr);
		}
		if (auctionId == null ){ //|| !iAuctionService.isValidAuction(auctionId)) {
			String msg = "Auction Not Present - " + auctionId;
			return "redirect:/admin/auctionmanagement?Message=" + msg;
		}

		AuctionCacheService.flushCache();
		AuctionCacheManager.flushCache();
		//AuctionCacheManager.setActiveAuctionId(auctionId);
		
		AuctionCacheBean auctionCacheBean = new AuctionCacheBean();
		auctionCacheBean.setAuctionId(auctionId);
		auctionCacheBean.setClientId(clientDetails.getId());
		auctionCacheBean.setSchemaName(clientDetails.getSchemaKey());
		AuctionCacheManager.setActiveAuctionId(auctionCacheBean);
		
		auctionCacheManager.refreshAuctionCache(auctionCacheBean);
		// KilimEngineGenerator.getAuctioneer().restart();

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
			auctionCacheBean.setAuctionStartTime(actualAuctionStartTime);
			iAuctionService.setActualAuctionStartTime(auctionId,
					actualAuctionStartTime);
		} catch (ParseException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
		bidItemsCacheService.initCache(auctionCacheBean);
		String msg = "Auction Started - " + auctionId;
		return "redirect:/admin/auctionmanagement?Message=" + msg;
	}

}
