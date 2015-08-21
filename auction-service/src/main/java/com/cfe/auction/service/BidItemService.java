package com.cfe.auction.service;

import java.util.List;

import com.cfe.auction.model.auction.persist.AuctionSearchBean;
import com.cfe.auction.model.persist.BidItem;

public interface BidItemService extends CRUDService<Integer, BidItem> {

	//List<BidItem> getWonList(String username);

	List<BidItem> getBidItemsbyAuctionId(AuctionSearchBean auctionSearchBean);
	
	List<BidItem> getWonList(String username,AuctionSearchBean auctionSearchBean);
}
