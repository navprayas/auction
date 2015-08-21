package com.cfe.auction.service;

import java.util.Date;
import java.util.List;

import com.cfe.auction.model.auction.persist.AuctionSearchBean;
import com.cfe.auction.model.persist.Auction;
import com.cfe.auction.model.persist.BidItem;

/**
 * 
 * @author Vikas Anand
 * 
 */
public interface IAuctionService extends CRUDService<Integer, Auction> {

	void closeAuction(Integer auctionId);

	List<BidItem> getActiveAuctionBidItem(AuctionSearchBean auctionSearchBean);

	Auction getActiveAuction(AuctionSearchBean auctionSearchBean);

	Auction getActiveAuction();

	boolean isValidAuction(Integer auctionId);
	
	void setActualAuctionStartTime(Integer auctionId,
			Date actualAuctionStartTime);

	List<Auction> getAuctionList(String schemaKey);
}
