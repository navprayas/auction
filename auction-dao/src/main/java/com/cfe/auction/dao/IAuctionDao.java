package com.cfe.auction.dao;

import java.util.Date;
import java.util.List;

import com.cfe.auction.model.persist.Auction;

public interface IAuctionDao extends DAO<Integer, Auction> {

	Auction getActiveAuction(Integer auctionId);
	void closeAuction(Integer auctionId);
	boolean isValidAuction(Integer auctionId);
	Auction getActiveAuction();
	void setActualAuctionStartTime(Integer auctionId,
			Date actualAuctionStartTime);
	List<Auction> getAuctionList(String schemaKey);

}
