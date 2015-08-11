package com.cfe.auction.web.controller;

import java.security.Principal;
import java.util.Collection;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cfe.auction.model.persist.User;
import com.cfe.auction.service.UserService;
import com.cfe.auction.web.constants.CommonConstants;
import com.cfe.auction.web.constants.SessionConstants;


@Controller
/* @RequestMapping({"/","/admin/**","/business/**","/user/**","/modeler/**"}) */
public class HomeController {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private UserService userService;

	@RequestMapping(value = { "/home", "/index" }, method = RequestMethod.GET)
	public String login(ModelMap model, HttpServletRequest httpServletRequest,
			Principal principal) {

		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();

		Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) auth
				.getAuthorities();
		String name = principal.getName();
		GrantedAuthority au = (authorities.iterator().next());
		String category = au.getAuthority();

		HttpSession session = httpServletRequest.getSession();
		session.setAttribute(CommonConstants.USER_NAME, name);
		session.setAttribute("category", category);
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

		String pageToForward = null;

		for (GrantedAuthority authority : authorities) {

			// if (category != null &&
			// category.equals(authority.getAuthority())) {

			if (authority.getAuthority().equalsIgnoreCase(
					CommonConstants.ROLE_ADMIN)) {
				pageToForward = "/admin/home";
				break;
			} else if (authority.getAuthority().equalsIgnoreCase(
					CommonConstants.ROLE_BIDDER)) {
				pageToForward = "/bidder/home";
				break;
			} else if (authority.getAuthority().equalsIgnoreCase(
					CommonConstants.ROLE_OBSERVER)) {
				pageToForward = "/observer/home";
				break;
			} else {
				pageToForward = "/bidder/homelogout";
			}
			// }
		}

		if (pageToForward == null) {
			pageToForward = "logout";
		}

		return "redirect:" + pageToForward;

	}

	@RequestMapping(value = { "/", "/login" }, method = RequestMethod.GET)
	public String start() {
		return "login";
	}

	
	@RequestMapping(value = { "/observer/home", "/observer/index" }, method = RequestMethod.GET)
	public String modelerHome() {
		return "observerhome";
	}

	@RequestMapping(value = "/loginfailed", method = RequestMethod.GET)
	public String loginerror(ModelMap model) {
		model.addAttribute("loginerror", "loginerror");
		return "login";

	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(ModelMap model) {
		return "login";
	}

}
