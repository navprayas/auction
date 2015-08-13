package com.cfe.auction.service;

import java.util.List;

import com.cfe.auction.common.BidItemUi;
import com.cfe.auction.model.persist.BidItem;

/**
 * 
 * @author Vikas Anand
 * 
 */
public interface IBidItemFilterService {

	List<BidItem> getBidItemListForcategoryId(List<BidItem> bidItems,
			String categoryId);

	public List<BidItem> getBidItemListForClosedMarket(List<BidItem> bidItems,
			final String categoryId);

	List<BidItem> getBidItemListForActiveMarket(List<BidItem> bidItems,
			Integer categoryId);

	List<BidItem> getBidItemListForMarketList(List<BidItem> bidItems,
			List<Integer> categoryIds, Long bidSequenceId);

	List<BidItemUi> getBidItemListForcategoryIdAjax(List<BidItem> bidItems,
			Integer categoryId);

	List<BidItemUi> getBidItemListForActiveMarketAjax(List<BidItem> bidItems,
			Integer categoryId);

	List<BidItemUi> getBidItemListForClosedMarketAjax(List<BidItem> bidItems,
			Integer categoryId);

}
