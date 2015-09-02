package com.cfe.auction.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cfe.auction.model.auction.persist.AuctionSearchBean;
import com.cfe.auction.model.persist.BidItem;
import com.cfe.auction.model.persist.BidderCategory;
import com.cfe.auction.service.BidderCategoryService;
import com.cfe.auction.service.BiditemUtilService;
import com.cfe.auction.service.IBidItemFilterService;
import com.cfe.auction.service.cache.manager.AuctionCacheManager;
import com.cfe.auction.web.constants.CommonConstants;

@Service
@Transactional
public class BidItemUtilityServiceImpl implements BiditemUtilService {

	private static final Logger LOG = LoggerFactory
			.getLogger(BidItemUtilityServiceImpl.class);

	@Autowired
	private BidderCategoryService bidderCategoryService;
	@Autowired
	private IBidItemFilterService bidItemFilterService;

	public List<BidItem> getBidItemsMarketList(
			AuctionSearchBean auctionSearchBean) {

		List<BidItem> bidItems = new ArrayList<BidItem>();
		List<BidderCategory> bidderCategoryList = new ArrayList<BidderCategory>();

		if (auctionSearchBean.getRole().equalsIgnoreCase(
				CommonConstants.ROLE_OBSERVER)) {
			if (AuctionCacheManager.getActiveAuctionId(auctionSearchBean) != null) {
				bidderCategoryList = bidderCategoryService
						.getAllCategory(AuctionCacheManager
								.getActiveAuctionId(auctionSearchBean));
				LOG.debug("Category Id" + bidderCategoryList);

				bidItems = AuctionCacheManager.getBidItems(auctionSearchBean);
				if (bidItems != null) {
					List<Integer> categoryIds = getCategoryIdList(bidderCategoryList);
					bidItemFilterService.getBidItemListForMarketList(bidItems,
							categoryIds, AuctionCacheManager
									.getActiveBidSequenceId(auctionSearchBean));
				}
			}
		} else {

			bidderCategoryList = bidderCategoryService.getBidderCategory(
					auctionSearchBean.getUserId(), auctionSearchBean);
			bidItems = AuctionCacheManager.getBidItems(auctionSearchBean);
			Date currDate = new Date();
			long refreshTime = auctionSearchBean.getAuctionStartTime()
					.getTime() - currDate.getTime();
			refreshTime = (long) (refreshTime / 1000);
			LOG.debug("*****refreshTime::" + refreshTime);
			if (refreshTime <= 0) {
				refreshTime = 0;
				BidItem bidItem = AuctionCacheManager
						.getActiveBidItem(auctionSearchBean);
				if (bidItem != null) {
					refreshTime = bidItem.getTimeLeft();
				}
			}
			if (bidItems != null) {
				System.out.println("BidItems" + bidItems);
				List<Integer> categoryIds = getCategoryIdList(bidderCategoryList);
				List<BidItem> bidItemsFinal = bidItemFilterService
						.getBidItemListForMarketList(
								bidItems,
								categoryIds,
								AuctionCacheManager
										.getActiveBidSequenceId(auctionSearchBean));
				if (bidItemsFinal != null) {
					for (BidItem bidItem : bidItemsFinal) {
						LOG.debug("bidItem : " + bidItem.getBidItemId() + " "
								+ bidItem.getBidSpan() + " " + refreshTime);
						refreshTime += bidItem.getBidSpan();
						bidItem.setTimeLeft(refreshTime);
						bidItem.setTimeCounter(refreshTime);
						if (bidItem.getCurrentMarketPrice() == null) {
							bidItem.setCurrentMarketPrice(bidItem
									.getMinBidPrice());
						}
					}
				}

			}

		}

		return bidItems;
	}

	private List<Integer> getCategoryIdList(
			List<BidderCategory> bidderCategoryList) {
		List<Integer> categoryIds = new ArrayList<Integer>();
		if (bidderCategoryList != null && !bidderCategoryList.isEmpty()) {
			for (BidderCategory category : bidderCategoryList) {
				categoryIds.add(category.getCategoryId());
			}
		}
		return categoryIds;
	}

	@Override
	public List<BidItem> getBidItemsActiveMarketList(
			AuctionSearchBean auctionSearchBean) {
		List<BidItem> bidItems = new ArrayList<BidItem>();
		BidItem bidItem = null;
		List<BidderCategory> bidderCategoryList = new ArrayList<BidderCategory>();
		if (auctionSearchBean.getRole().equalsIgnoreCase(
				CommonConstants.ROLE_OBSERVER)) {
			bidderCategoryList = bidderCategoryService
					.getAllCategory(AuctionCacheManager
							.getActiveAuctionId(auctionSearchBean));
		} else {
			bidderCategoryList = bidderCategoryService.getBidderCategory(
					auctionSearchBean.getUserId(), auctionSearchBean);
		}
		bidItem = AuctionCacheManager.getActiveBidItem(auctionSearchBean);
		bidItems.add(bidItem);
		if (bidItem != null) {
			List<Integer> categoryIds = getCategoryIdList(bidderCategoryList);
			bidItems = bidItemFilterService.getBidItemListForActiveMarketList(
					bidItems, categoryIds, bidItem.getSeqId());

		}

		return bidItems;
	}

	@Override
	public List<BidItem> getBidItemsClosedMarketList(
			AuctionSearchBean auctionSearchBean) {
		List<BidItem> bidItems = new ArrayList<BidItem>();
		List<BidderCategory> bidderCategoryList = new ArrayList<BidderCategory>();
		if (auctionSearchBean.getRole().equalsIgnoreCase(
				CommonConstants.ROLE_OBSERVER)) {
			bidderCategoryList = bidderCategoryService
					.getAllCategory(AuctionCacheManager
							.getActiveAuctionId(auctionSearchBean));
		} else {

			bidderCategoryList = bidderCategoryService.getBidderCategory(
					auctionSearchBean.getUserId(), auctionSearchBean);
		}

		bidItems = AuctionCacheManager.getBidItems(auctionSearchBean);
		List<Integer> categoryIds = getCategoryIdList(bidderCategoryList);
		bidItems = bidItemFilterService.getBidItemListForClosedMarketList(
				bidItems, categoryIds,
				AuctionCacheManager.getActiveBidSequenceId(auctionSearchBean));

		return bidItems;
	}

}
