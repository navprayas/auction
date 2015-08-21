package com.cfe.auction.dao;

import java.util.List;

import com.cfe.auction.model.auction.persist.AuctionSearchBean;
import com.cfe.auction.model.persist.BidItem;

public interface BidItemDao extends DAO<Integer, BidItem> {

	List<BidItem> getBidItems(AuctionSearchBean auctionSearchBean);

	List<BidItem> getBIdItemActiveMarket();

	List<BidItem> getBIdItemClosedMarket();
	
	BidItem getBidItem(Long id); 

	List<BidItem> getWonList(String username);
}
