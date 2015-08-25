package com.cfe.auction.dao;

import java.util.List;

import com.cfe.auction.model.auction.persist.AuctionCacheBean;
import com.cfe.auction.model.auction.persist.AuctionSearchBean;
import com.cfe.auction.model.persist.BidderCategory;

public interface BidderCategoryDao extends DAO<Integer, BidderCategory>{
	public Integer saveBIdderCategory(BidderCategory bidderCategory);

	public List<BidderCategory> getAllCategory(Integer auctionId);

	List<BidderCategory> getBidderCategory(Integer userId,
			AuctionSearchBean auctionSearchBean);

}
