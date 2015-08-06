package com.cfe.auction.service.impl;

import static ch.lambdaj.Lambda.filter;
import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;

import java.util.List;

import org.hamcrest.Matchers;
import org.springframework.stereotype.Component;

import com.cfe.auction.model.persist.BidItem;
import com.cfe.auction.service.IBidItemFilterService;

/**
 * 
 * @author Vikas Anand
 * 
 */
@Component
public class BidItemFilterServiceImpl implements IBidItemFilterService {

	@Override
	public List<BidItem> getBidItemListForcategoryId(List<BidItem> bidItems,
			final String categoryId) {

		List<BidItem> bidItemFinal = filter(
				having(on(BidItem.class).getCategory(),
						Matchers.equalTo(categoryId)), bidItems);
		return bidItemFinal;
	}

	@Override
	public List<BidItem> getBidItemListForActiveMarket(List<BidItem> bidItems,
			String categoryId) {
		List<BidItem> bidItemFinal = filter(
				having(on(BidItem.class).getCategory(),
						Matchers.equalTo(categoryId)), bidItems);
		return bidItemFinal;
	}

	@Override
	public List<BidItem> getBidItemListForClosedMarket(List<BidItem> bidItems,
			String categoryId) {
		List<BidItem> bidItemFinal = filter(
				having(on(BidItem.class).getCategory(),
						Matchers.equalTo(categoryId)), bidItems);
		return bidItemFinal;
	}
}
