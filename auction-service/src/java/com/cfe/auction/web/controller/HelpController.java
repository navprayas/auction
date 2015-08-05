package com.cfe.auction.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping({"/admin/**","/user/**"})
public class HelpController {
	@RequestMapping(value = "/help")
	public String helppage(ModelMap model, HttpServletRequest request) {
		HttpSession session = request.getSession();
		String userType = (String) session.getAttribute("category");
		System.out.println(userType);
		
		if ("ROLE_ADMIN".equalsIgnoreCase(userType)) {
			return "adminhelp";
		}
		else if ("ROLE_USER".equalsIgnoreCase(userType)) {

			return "userhelp";
		}
		return "login";
	}
}
