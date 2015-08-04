package com.cfe.auction.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorController {
	@RequestMapping(value = "/errorpage")
	public String errorpage(ModelMap model, HttpServletRequest request) {
		HttpSession session = request.getSession();
		String userType = (String) session.getAttribute("category");
		System.out.println(userType);
		String errorType = request.getParameter("error");
		String errorMessage = "";
		if ("500".equalsIgnoreCase(errorType)) {
			errorMessage += " Try with another Input.";
		} else if ("400".equalsIgnoreCase(errorType)) {
			errorMessage += " As Page you are looking for not found.";
		}
		model.addAttribute("ErrorMessage", errorMessage);
		if ("ROLE_ADMIN".equalsIgnoreCase(userType)) {
			return "adminerror";
		} else if ("ROLE_BIDDER".equalsIgnoreCase(userType)) {

			return "biddererror";
		} else if ("ROLE_OBSERVER".equalsIgnoreCase(userType)) {

			return "observererror";
		}

		else {

			return "error";
		}
	}
}
