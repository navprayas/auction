package com.cfe.auction.web.controller;

import java.security.Principal;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;



import com.cfe.auction.model.persist.User;
import com.cfe.auction.service.UserService;
import com.cfe.auction.web.constants.SessionConstants;

@Controller
@RequestMapping("/")
public class LoginController {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginWelcome(ModelMap model) {
		return "login";
	}

	@RequestMapping(value = "/loginfailed", method = RequestMethod.GET)
	public String loginFailed(HttpServletRequest request, ModelMap model) {
		if (request.getParameter("j_username") != null)
			logger.info("Login attempt failed for user: "
					+ request.getParameter("j_username"));
		return "login";
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String root(ModelMap model) {
		model.addAttribute("message", "This is / (root) page" + " Company is: ");
		return "index";
	}

	// @RequestMapping(value="/home", method = RequestMethod.GET)
	public String home(HttpServletRequest request, Principal principal) {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute(SessionConstants.USER_INFO);
		if (user == null) {
			String userName = principal.getName();
			logger.info("Login attempt failed for user: " + userName
					+ ". Loading user info in session");
			user = userService.getUserByUserName(userName);
			session.setAttribute(SessionConstants.USER_INFO, user);
			user.setLastLoginTime(new Date());
			userService.update(user);
		}
		return "index";
		// return "redirect:" +"/admin/home";
	}

	@RequestMapping(value = "/welcome/{name}", method = RequestMethod.GET)
	public String welcomeName(@PathVariable String name, ModelMap model) {
		model.addAttribute("message", "Maven Web Project + Spring 3 MVC - "
				+ name);
		return "index";
	}

}
