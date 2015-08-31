package com.cfe.auction.dao;

import java.util.List;

import com.cfe.auction.model.auction.persist.AuctionSearchBean;
import com.cfe.auction.model.persist.BidItem;

public interface BidItemDao extends DAO<Integer, BidItem> {

	List<BidItem> getBidItems(AuctionSearchBean auctionSearchBean);

	BidItem getBidItem(Long id, AuctionSearchBean auctionSearchBean);

	List<BidItem> getWonList(String username,
			AuctionSearchBean auctionSearchBean);

	void updateBidItem(AuctionSearchBean auctionSearchBean, BidItem bidItem);

	BidItem createOrUpdate(BidItem bidItem, AuctionSearchBean auctionSearchBean);
}
