package com.cfe.auction.service;

import java.util.List;

import com.cfe.auction.common.BidItemUi;
import com.cfe.auction.model.auction.persist.AuctionSearchBean;
import com.cfe.auction.model.persist.BidItem;

public interface BidItemService extends CRUDService<Integer, BidItem> {

	List<BidItem> getBidItemsbyAuctionId(AuctionSearchBean auctionSearchBean);
	
	List<BidItemUi> getWonList(String username,AuctionSearchBean auctionSearchBean);

	void updateBidItem(AuctionSearchBean auctionSearchBean, BidItem bidItem);
}
