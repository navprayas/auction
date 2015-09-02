package com.cfe.auction.service;

import java.util.List;

import com.cfe.auction.model.auction.persist.AuctionSearchBean;
import com.cfe.auction.model.persist.BidItem;

public interface BiditemUtilService {
	public List<BidItem> getBidItemsMarketList(
			AuctionSearchBean auctionSearchBean);
	public List<BidItem> getBidItemsActiveMarketList(
			AuctionSearchBean auctionSearchBean);
	
	public List<BidItem> getBidItemsClosedMarketList(
			AuctionSearchBean auctionSearchBean);
}
