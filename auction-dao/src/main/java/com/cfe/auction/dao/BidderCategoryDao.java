package com.cfe.auction.dao;

import java.util.List;

import com.cfe.auction.model.persist.BidderCategory;

public interface BidderCategoryDao {
	public List<BidderCategory> getBidderCategory(Integer userId,
			Integer auctionId);

}
