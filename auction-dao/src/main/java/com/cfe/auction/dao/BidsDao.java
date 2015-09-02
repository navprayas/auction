package com.cfe.auction.dao;

import java.util.List;

import com.cfe.auction.model.auction.persist.AuctionSearchBean;
import com.cfe.auction.model.persist.Bids;

public interface BidsDao extends DAO<Integer, Bids> {
	
	void createBids(Bids bids,AuctionSearchBean auctionSearchBean);

	public List<Bids> getReportSummary(AuctionSearchBean auctionSearchBean);

}
