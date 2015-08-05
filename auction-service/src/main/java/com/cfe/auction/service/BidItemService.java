package com.cfe.auction.service;

import java.util.List;

import com.cfe.auction.model.persist.BidItem;

public interface BidItemService extends CRUDService<Integer, BidItem> {
	public List<BidItem> getBidItems();
	

}
