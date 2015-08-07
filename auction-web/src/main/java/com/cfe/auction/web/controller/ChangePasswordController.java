package com.cfe.auction.web.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.cfe.auction.model.persist.User;
import com.cfe.auction.service.UserService;
import com.cfe.auction.web.constants.CommonConstants;

@Controller
public class ChangePasswordController {
	@Autowired
	private UserService userService;

	@RequestMapping(value = { "/admin/changepassword",
			"/observer/changepassword", "/bidder/changepassword" }, method = RequestMethod.GET)
	public String changePassword() {
		return "changepassword";
	}

	@RequestMapping(value = { "/admin/changepassword",
			"/observer/changepassword", "/bidder/changepassword" }, method = RequestMethod.POST)
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
		return "changepassword";
	}
}
