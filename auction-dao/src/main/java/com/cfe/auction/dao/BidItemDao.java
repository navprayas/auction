package com.cfe.auction.dao;

import java.util.List;

import com.cfe.auction.model.persist.BidItem;

public interface BidItemDao extends DAO<Integer, BidItem> {

	List<BidItem> getBidItems(Integer auctionId);

	List<BidItem> getBIdItemActiveMarket();

	List<BidItem> getBIdItemClosedMarket();
	public BidItem getBidItem(Long id); 
	public List<BidItem> getBidItems(Long bidItemGroupId);

	List<BidItem> getWonList(String username);
}
