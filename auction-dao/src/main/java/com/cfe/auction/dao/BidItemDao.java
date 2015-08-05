package com.cfe.auction.dao;

import java.util.List;

import com.cfe.auction.model.persist.BidItem;

public interface BidItemDao extends DAO<Integer, BidItem> {

	public List<BidItem> getBidItems();

}
