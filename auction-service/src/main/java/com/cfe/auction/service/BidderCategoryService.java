package com.cfe.auction.service;

import java.util.List;

import com.cfe.auction.model.persist.BidderCategory;

public interface BidderCategoryService {
	public List<BidderCategory> getBidderCategory(Integer userId,
			Integer auctionId);

}
