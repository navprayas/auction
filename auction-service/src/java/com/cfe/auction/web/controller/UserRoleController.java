package com.cfe.auction.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.cfe.auction.service.RoleService;
import com.cfe.auction.service.UserRoleService;
import com.cfe.auction.service.UserService;

@Controller
@RequestMapping("/admin/**")
public class UserRoleController {
	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private UserRoleService userRoleService;
	@RequestMapping(value="/userrole", method=RequestMethod.GET)
	public String getUserRole(ModelMap model)
	{
		model.addAttribute("users", userService.getUsers());
		model.addAttribute("roles", roleService.getRoles());
		return "userrole";
	}
	@RequestMapping(value="/adduserrole", method=RequestMethod.POST)
	public String addUserRole(@RequestParam Integer userid,@RequestParam Integer roleid,ModelMap model)
	
	{
		try{
			userRoleService.addUserRole(userid,roleid);	
			model.addAttribute("success",
					"You have successfully added user role.");
		}catch(Exception e){
			e.printStackTrace(); 
			model.addAttribute("error", "error"); 
		}
		
		model.addAttribute("users", userService.getUsers());
		model.addAttribute("roles", roleService.getRoles());
		return "userrole";
	}
	

}
