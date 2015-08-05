package com.cfe.auction.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.cfe.auction.model.persist.User;
import com.cfe.auction.service.UserService;

@Controller
@RequestMapping("/admin/**")
public class UserController {

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String register(
			@RequestParam(value = "id", required = false) Integer userId,
			@RequestParam(value = "operation", required = false) String operation,
			ModelMap model) {
		if (userId != null) {

			model.addAttribute("command", userService.read(userId));
			model.addAttribute("operation", operation);
		} else {
			model.addAttribute("command", new User());
			model.addAttribute("operation", "CREATE");
		}
		return "register";

	}

	@RequestMapping(value = "/edituser", method = RequestMethod.POST)
	public String editSystem(@ModelAttribute User user, ModelMap model) {
		try {

			user.setPassword(userService.eccodePassword(user.getPassword()));
			user.setRetryCount((byte) 5);
			userService.update(user);
			model.addAttribute("success", "You have successfully updated user.");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("command", user);
			model.addAttribute("error", "error");
			model.addAttribute("operation", "EDIT");
			return "register";
		}

		model.addAttribute("users", userService.getUsers());
		return "users";
	}

	@RequestMapping(value = "/adduser", method = RequestMethod.POST)
	public String register(@ModelAttribute User user, ModelMap model) {
		try {
			userService.addUser(user);
			model.addAttribute("success", "You have successfully added user.");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("error", "error");
			model.addAttribute("command", new User());
			model.addAttribute("operation", "CREATE");
			return "register";
		}

		model.addAttribute("users", userService.getUsers());
		return "users";

	}

	@RequestMapping(value = "/userlist", method = RequestMethod.GET)
	public String getUsers(ModelMap model) {
		model.addAttribute("users", userService.getUsers());
		return "users";

	}

}
