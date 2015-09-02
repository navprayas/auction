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
import org.springframework.web.servlet.ModelAndView;

import com.cfe.auction.model.auction.persist.AuctionSearchBean;
import com.cfe.auction.model.persist.Bids;
import com.cfe.auction.model.persist.ClientDetails;
import com.cfe.auction.model.persist.User;
import com.cfe.auction.service.BidsService;
import com.cfe.auction.service.cache.manager.AuctionCacheManager;
import com.cfe.auction.web.constants.SessionConstants;
import com.cfe.auction.web.util.ExcelWriterUtils;

@Controller
@RequestMapping("/observer/**")
public class ObserverReportController {
	Logger logger = LoggerFactory.getLogger(ObserverReportController.class);
	@Autowired
	private BidsService bidsService;

	@RequestMapping(value = "/observerreport", method = RequestMethod.GET)
	public String getObserverReportSummary(ModelMap model, HttpSession session) {

		logger.debug("In getObserverReportSummary Method::");
		User user = (User) session.getAttribute(SessionConstants.USER_INFO);

		ClientDetails clientDetails = (ClientDetails) session
				.getAttribute(SessionConstants.CLIENT_INFO);

		AuctionSearchBean auctionSearchBean = new AuctionSearchBean(
				clientDetails.getSchemaKey());
		auctionSearchBean.setClientId(clientDetails.getId());
		auctionSearchBean.setAuctionId(AuctionCacheManager
				.getActiveAuctionId(auctionSearchBean));

		List<Bids> bidsList = bidsService.getReportSummary(auctionSearchBean);
		System.out.println("Bids Items" + bidsList);

		model.addAttribute("bidsreport", bidsList);
		/*
		 * List<Bids> bidsList = reportService.getReportSummary1(userName,
		 * null); List<BidderCategory> bidCategoryList = commonService
		 * .getCategoryList(userName); Set<String> bidsStatusSet =
		 * reportService.getLotsList(userName);
		 * 
		 * ReportsTotal reportsTotal = getReportsTotal(bidsList);
		 * 
		 * Date d1 = new Date(); SimpleDateFormat sdf = new
		 * SimpleDateFormat(DATE_FORMAT); String date = sdf.format(d1);
		 * 
		 * modelMap.addAttribute("dateFromStr", date);
		 * modelMap.addAttribute("dateToStr", date);
		 * 
		 * modelMap.addAttribute("BidsList", bidsList);
		 * modelMap.addAttribute("bidderCategoryList", bidCategoryList);
		 * modelMap.addAttribute("reportsTotal", reportsTotal);
		 * modelMap.addAttribute("bidsStatusSet", bidsStatusSet);
		 * modelMap.addAttribute("bidsStat", 0);
		 */
		return "observerreport";
	}

	@RequestMapping(value = "/excelreport", method = RequestMethod.GET)
	public ModelAndView getObserverSummary1ReportExcel(ModelMap modelMap,
			HttpSession session) {

		logger.debug("In getObserverReportSummary Method::");

		ClientDetails clientDetails = (ClientDetails) session
				.getAttribute(SessionConstants.CLIENT_INFO);

		AuctionSearchBean auctionSearchBean = new AuctionSearchBean(
				clientDetails.getSchemaKey());
		auctionSearchBean.setClientId(clientDetails.getId());
		auctionSearchBean.setAuctionId(AuctionCacheManager
				.getActiveAuctionId(auctionSearchBean));

		List<Bids> bidsList = bidsService.getReportSummary(auctionSearchBean);
		System.out.println("Bids Items" + bidsList);
		ExcelWriterUtils.getExcelFileFromList(bidsList);
		return new ModelAndView("ExcelObserverReportSummary1", "list", bidsList);
	}

	@RequestMapping(value = "/excelreport", method = RequestMethod.GET)
	public ModelAndView getObserverSummary1ReportPDF(ModelMap modelMap,
			HttpSession session) {

		logger.debug("In getObserverReportSummary Method::");
		ClientDetails clientDetails = (ClientDetails) session
				.getAttribute(SessionConstants.CLIENT_INFO);

		AuctionSearchBean auctionSearchBean = new AuctionSearchBean(
				clientDetails.getSchemaKey());
		auctionSearchBean.setClientId(clientDetails.getId());
		auctionSearchBean.setAuctionId(AuctionCacheManager
				.getActiveAuctionId(auctionSearchBean));

		List<Bids> bidsList = bidsService.getReportSummary(auctionSearchBean);
		System.out.println("Bids Items" + bidsList);

		return new ModelAndView("PDfObserverSummary1ReportView", "list",
				bidsList);

	}

}
