package com.cfe.auction.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.cfe.auction.dao.model.persist.Role;
import com.cfe.auction.service.RoleService;
import com.cfe.auction.service.UserService;

@RequestMapping("/admin/**")
@Controller
public class RoleController {
	@Autowired
	private RoleService roleService;
	@Autowired
	private UserService userService;

	@RequestMapping(value = "/roles", method = RequestMethod.GET)
	public String getRoles(ModelMap model) {
		model.addAttribute("roles", roleService.getRoles());

		return "role";
	}

	@RequestMapping(value = "/addrole", method = RequestMethod.GET)
	public String Roles(
			@RequestParam(value = "id", required = false) Integer roleId,
			@RequestParam(value = "operation", required = false) String operation,
			ModelMap model) {
		if (roleId != null) {

			model.addAttribute("command", roleService.read(roleId));
			model.addAttribute("operation", operation);
		} else {
			model.addAttribute("command", new Role());
			model.addAttribute("operation", "CREATE");
		}
		return "addrole";
	}

	@RequestMapping(value = "/editrole", method = RequestMethod.POST)
	public String editSystem(
			@ModelAttribute Role role,
			ModelMap model,
			@RequestParam(value = "id", required = false) Integer roleId) {
		try {
			roleService.update(role);
			model.addAttribute("success", "You have successfully updated role.");
		} catch (Exception e) {
			e.printStackTrace();
			if (roleId != null) {
				model.addAttribute("command", roleService.read(roleId));
				model.addAttribute("operation", "EDIT");
			}
			model.addAttribute("error", "error");
			return "addrole";
		}
		model.addAttribute("roles", roleService.getRoles());
		return "role";
	}

	@RequestMapping(value = "/addrole", method = RequestMethod.POST)
	public String addRoles(@ModelAttribute Role role, ModelMap model) {
		try {
			roleService.create(role);
			model.addAttribute("success",
					"You have successfully created a role.");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("command", new Role());
			model.addAttribute("operation", "CREATE");
			model.addAttribute("error", "error");
			return "addrole";
		}

		model.addAttribute("roles", roleService.getRoles());
		return "role";
	}

}
