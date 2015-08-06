package com.cfe.auction.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.cfe.auction.service.BidItemService;

@Controller
public class CommonController {

	@Autowired
	private BidItemService bidItemService;
	
	/*public void getBidItemCache()
	{
		bidItemService.getBidItems();
	}*/

}
