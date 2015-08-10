package com.cfe.auction.service.impl;

import static ch.lambdaj.Lambda.filter;
import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hamcrest.Matchers;
import org.springframework.stereotype.Component;

import com.cfe.auction.common.BidItemUi;
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
				having(on(BidItem.class).getCategory().getId(),
						Matchers.equalTo(categoryId)), bidItems);

		return bidItemFinal;
	}

	@Override
	public List<BidItem> getBidItemListForActiveMarket(List<BidItem> bidItems,
			final List<Integer> categoryIds) {
		if (categoryIds != null && !categoryIds.isEmpty()) {
			List<BidItem> bidItemFinal = new ArrayList<BidItem>();
			for (Integer categoryId : categoryIds) {
				bidItemFinal.addAll(filter(
						having(on(BidItem.class).getCategory().getId(),
								Matchers.equalTo(categoryId)), bidItems));
			}
			return bidItemFinal;
		}
		return null;
	}

	@Override
	public List<BidItem> getBidItemListForActiveMarketBySequenceId(List<BidItem> bidItems,
			final Long seqId) {
		if (seqId != null) {
			List<BidItem> bidItemFinal = new ArrayList<BidItem>();
			bidItemFinal.addAll(filter(
					having(on(BidItem.class).getSeqId(),
							Matchers.greaterThan(seqId)), bidItems));
			
			return bidItemFinal;
		}
		return null;
	}
	
	@Override
	public List<BidItem> getBidItemListForClosedMarket(List<BidItem> bidItems,
			final String categoryId) {
		List<BidItem> bidItemFinal = filter(
				having(on(BidItem.class).getCategory().getId(),
						Matchers.equalTo(categoryId)), bidItems);
		return bidItemFinal;
	}

	@Override
	public List<BidItem> getBidItemListForActiveMarket(List<BidItem> bidItems,
			Integer categoryId) {
		return filter(
				having(on(BidItem.class).getCategory().getId(),
						Matchers.equalTo(categoryId)), bidItems);
	}

	@Override
	public List<BidItemUi> getBidItemListForcategoryIdAjax(
			List<BidItem> bidItems, Integer categoryId) {
		List<BidItemUi> bidItemList = new ArrayList<BidItemUi>();
		List<BidItem> bidItemsResult = filter(
				having(on(BidItem.class).getCategory().getId(),
						Matchers.equalTo(categoryId)), bidItems);
		bidItemToBidItemUiConverter(bidItemList, bidItemsResult);

		return bidItemList;
	}

	@Override
	public List<BidItemUi> getBidItemListForActiveMarketAjax(
			List<BidItem> bidItems, Integer categoryId) {
		List<BidItemUi> bidItemList = new ArrayList<BidItemUi>();
		List<BidItem> bidItemsResult = filter(
				having(on(BidItem.class).getCategory().getId(),
						Matchers.equalTo(categoryId)), bidItems);
		bidItemToBidItemUiConverter(bidItemList, bidItemsResult);

		return bidItemList;
	}

	@Override
	public List<BidItemUi> getBidItemListForClosedMarketAjax(
			List<BidItem> bidItems, Integer categoryId) {
		List<BidItemUi> bidItemList = new ArrayList<BidItemUi>();
		List<BidItem> bidItemsResult = filter(
				having(on(BidItem.class).getCategory().getId(),
						Matchers.equalTo(categoryId)), bidItems);
		bidItemToBidItemUiConverter(bidItemList, bidItemsResult);

		return bidItemList;
	}

	private void bidItemToBidItemUiConverter(List<BidItemUi> bidItemList,
			List<BidItem> bidItemsResult) {
		BidItemUi bidItemUi = null;
		for (BidItem bidItem : bidItemsResult) {
			bidItemUi = new BidItemUi();

			bidItemUi.setBidItemId(bidItem.getBidItemId());
			bidItemUi.setBidItemGroupId(bidItem.getBidItemGroupId());
			bidItemUi.setBidEndTime(bidItem.getBidEndTime());

			bidItemUi.setBidStartTime(bidItem.getBidStartTime());
			bidItemUi.setCity(bidItem.getCity());
			bidItemUi.setLocation(bidItem.getLocation());
			bidItemUi.setMinBidIncrement(bidItem.getMinBidIncrement());
			bidItemUi.setMinBidPrice(bidItem.getMinBidPrice());
			bidItemUi.setZone(bidItem.getZone());
			bidItemUi.setName(bidItem.getName());
			bidItemUi.setCurrency(bidItem.getCurrency());
			bidItemUi.setCurrentMarketPrice(bidItem.getCurrentMarketPrice());
			bidItemUi.setTimeleft(bidItem.getTimeLeft());
			bidItemUi.setCreatedTime(bidItem.getCreatedTime());
			bidItemList.add(bidItemUi);

		}
	}
}
