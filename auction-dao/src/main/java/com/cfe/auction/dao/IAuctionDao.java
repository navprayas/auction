package com.cfe.auction.dao;

import com.cfe.auction.model.persist.Auction;

public interface IAuctionDao extends DAO<Integer, Auction> {

	Auction getActiveAuction();

}
