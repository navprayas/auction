package com.cfe.auction.service;

import java.util.List;

import com.cfe.auction.model.persist.Auction;
import com.cfe.auction.model.persist.BidItem;

/**
 * 
 * @author Vikas Anand
 * 
 */
public interface IAuctionService extends CRUDService<Integer, Auction> {

	List<BidItem> getActiveAuctionBidItem();

	List<Auction> getAuctionList();

	Auction getActiveAuction();

}
