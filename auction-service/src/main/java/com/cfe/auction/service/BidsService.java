package com.cfe.auction.service;

import java.util.List;

import com.cfe.auction.common.Bid;
import com.cfe.auction.model.auction.persist.AuctionSearchBean;
import com.cfe.auction.model.persist.Bids;

public interface BidsService extends CRUDService<Integer, Bids> {
	public void createBidService(Bid bids, AuctionSearchBean auctionSearchBean);
	public List<Bid>  getReportSummary(AuctionSearchBean auctionSearchBean);

}
