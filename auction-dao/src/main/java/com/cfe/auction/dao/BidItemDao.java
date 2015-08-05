package com.cfe.auction.dao;

import java.util.List;

import com.cfe.auction.common.CommonBIddItems;
import com.cfe.auction.dao.model.persist.BidItem;

public interface BidItemDao extends DAO<Integer, BidItem> {

	public List<BidItem> getBidItemsForCategoryForMarketListForBidder(long categoryId,CommonBIddItems commonVO);

}
