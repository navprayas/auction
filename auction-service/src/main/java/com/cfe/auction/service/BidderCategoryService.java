package com.cfe.auction.service;

import java.util.List;

import com.cfe.auction.model.auction.persist.AuctionSearchBean;
import com.cfe.auction.model.persist.BidderCategory;

public interface BidderCategoryService {
	
	public List<BidderCategory> getAllCategory(Integer auctionId);

	List<BidderCategory> getBidderCategory(Integer userId,
			AuctionSearchBean auctionSearchBean);

}
