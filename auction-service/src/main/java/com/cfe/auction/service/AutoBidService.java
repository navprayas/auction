package com.cfe.auction.service;

import com.cfe.auction.model.persist.AutoBids;

public interface AutoBidService extends CRUDService<Integer, AutoBids> {

	public void saveAutoBid(String userName, Long categoryId, Long bidItemId,
			Double bidAmount, String string, Integer auctionId);

}
