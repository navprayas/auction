package com.cfe.auction.web.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.cfe.auction.common.Bid;
import com.cfe.auction.model.auction.persist.AuctionSearchBean;
import com.cfe.auction.model.persist.ClientDetails;
import com.cfe.auction.model.persist.User;
import com.cfe.auction.service.BidsService;
import com.cfe.auction.service.cache.manager.AuctionCacheManager;
import com.cfe.auction.web.constants.SessionConstants;
import com.cfe.auction.web.util.ExcelWriterUtils;
import com.cfe.auction.web.util.PDFWriterUtils;
import com.itextpdf.text.DocumentException;

@Controller
@RequestMapping("/observer/**")
public class ObserverReportController {
	Logger logger = LoggerFactory.getLogger(ObserverReportController.class);
	@Autowired
	private BidsService bidsService;

	@RequestMapping(value = "/observerreport", method = RequestMethod.GET)
	public String getObserverReportSummary(ModelMap model, HttpSession session,
			@RequestParam(required = false) String fromDate,
			@RequestParam(required = false) String toDate) {

		logger.debug("In getObserverReportSummary Method::");
		User user = (User) session.getAttribute(SessionConstants.USER_INFO);

		ClientDetails clientDetails = (ClientDetails) session
				.getAttribute(SessionConstants.CLIENT_INFO);

		AuctionSearchBean auctionSearchBean = new AuctionSearchBean(
				clientDetails.getSchemaKey());
		auctionSearchBean.setClientId(clientDetails.getId());
		auctionSearchBean.setAuctionId(AuctionCacheManager
				.getActiveAuctionId(auctionSearchBean));
		auctionSearchBean.setFromDate(fromDate);
		auctionSearchBean.setToDate(toDate);

		List<Bid> bidsList = bidsService.getReportSummary(auctionSearchBean);
		System.out.println(fromDate + " " + toDate);
		model.addAttribute("bidsreport", bidsList);

		return "observerreport";
	}

	@RequestMapping(value = "/excelreport", method = RequestMethod.GET)
	public void getObserverSummary1ReportExcel(ModelMap modelMap,
			HttpSession session, HttpServletResponse response,
			@RequestParam(required = false) String fromDate,
			@RequestParam(required = false) String toDate) {

		logger.debug("In getObserverReportSummary Method::");

		ClientDetails clientDetails = (ClientDetails) session
				.getAttribute(SessionConstants.CLIENT_INFO);

		AuctionSearchBean auctionSearchBean = new AuctionSearchBean(
				clientDetails.getSchemaKey());
		auctionSearchBean.setClientId(clientDetails.getId());
		auctionSearchBean.setAuctionId(AuctionCacheManager
				.getActiveAuctionId(auctionSearchBean));
		auctionSearchBean.setFromDate(fromDate);
		auctionSearchBean.setToDate(toDate);

		List<Bid> bidList = bidsService.getReportSummary(auctionSearchBean);

		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition",
				"attachment; filename=ExcelReport.xls");
		Workbook wb = ExcelWriterUtils.getExcelFile(bidList);
		try {
			javax.servlet.ServletOutputStream out = response.getOutputStream();
			wb.write(out);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@RequestMapping(value = "/pdfreport", method = RequestMethod.GET)
	public void getObserverSummary1ReportPDF(ModelMap modelMap,
			HttpSession session, HttpServletResponse response,
			@RequestParam(required = false) String fromDate,
			@RequestParam(required = false) String toDate)
			throws DocumentException, IOException {

		logger.debug("In getObserverReportSummary Method::");
		ClientDetails clientDetails = (ClientDetails) session
				.getAttribute(SessionConstants.CLIENT_INFO);
		AuctionSearchBean auctionSearchBean = new AuctionSearchBean(
				clientDetails.getSchemaKey());
		auctionSearchBean.setClientId(clientDetails.getId());
		auctionSearchBean.setAuctionId(AuctionCacheManager
				.getActiveAuctionId(auctionSearchBean));
		auctionSearchBean.setFromDate(fromDate);
		auctionSearchBean.setToDate(toDate);
		List<Bid> bidsList = bidsService.getReportSummary(auctionSearchBean);
		response.setContentType("application/pdf");
		response.setHeader("content-disposition",
				"attachment; filename=PDFReport.pdf");
		javax.servlet.ServletOutputStream out = response.getOutputStream();
		PDFWriterUtils.getPDFFile(bidsList, out);
	}

}
