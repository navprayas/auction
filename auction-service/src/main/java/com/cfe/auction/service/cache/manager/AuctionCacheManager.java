package com.cfe.auction.service.cache.manager;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cfe.auction.model.auction.persist.AuctionCacheBean;
import com.cfe.auction.model.auction.persist.AuctionSearchBean;
import com.cfe.auction.model.persist.Auction;
import com.cfe.auction.model.persist.BidItem;
import com.cfe.auction.service.IAuctionService;
import com.cfe.auction.service.cache.IBidItemsCacheService;

@Component
public class AuctionCacheManager implements InitializingBean {

	@Autowired
	IAuctionService auctionService;

	@Autowired
	IBidItemsCacheService bidItemsCacheService;

	private static Integer activeAuctionId;


	private static Map<Integer, AuctionCacheBean> activeAuctionMap;
	
	private static List<BidItem> bidItems;

	private static Map<Long, BidItem> bidItemsMap = new HashMap<Long, BidItem>();

	private static Auction auction;

	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println("Properties Loaded");
		//setActiveAuction();
		System.out.println("active Auction id::" + activeAuctionId);
		//setActiveBidItemsList();
		setBidItemsMap();
		bidItemsCacheService.initCache();
	}

	public void refreshAuctionCache(AuctionCacheBean auctionCacheDTO) {
		setActiveAuction(auctionCacheDTO);
		//setActiveBidItemsList();
		setBidItemsMap();
	}

	public static void flushCache() {
		activeAuctionId = null;
		bidItems = null;
		bidItemsMap = new HashMap<Long, BidItem>();
		auction = null;
	}

	public static void setActiveAuctionId(Integer auctionId) {
		activeAuctionId = auctionId;
	}

	public static void setActiveAuctionId(AuctionCacheBean auctionCacheDTO) {
		activeAuctionMap.put(auctionCacheDTO.getClientId(), auctionCacheDTO);
	}
	
	private void setActiveAuction(AuctionCacheBean auctionCacheBean) {
		if (auctionCacheBean != null) {
			AuctionSearchBean auctionSearchBean = new AuctionSearchBean(auctionCacheBean.getSchemaName());
			auctionSearchBean.setAuctionId(auctionCacheBean.getAuctionId());
			Auction auction = auctionService.getActiveAuction(auctionSearchBean);
			if (auction != null) {
				auctionSearchBean.setGenericId(auction.getBidItemGroupId());
				bidItems = auctionService.getActiveAuctionBidItem(auctionSearchBean);
			}
		} 
	}

	private static void setBidItemsMap() {
		if (bidItems != null && !bidItems.isEmpty()) {
			for (BidItem bidItem : bidItems) {
				if (bidItem != null && bidItem.getBidItemId() != null) {
					bidItemsMap.put(bidItem.getBidItemId(), bidItem);
				}
			}
		}
	}

	/*private void setActiveBidItemsList() {
		if (activeAuctionId != null) {
			bidItems = auctionService.getActiveAuctionBidItem(activeAuctionId);
		}
	}*/

	public static Integer getActiveAuctionId() {
		return activeAuctionId;
	}

	public static List<BidItem> getBidItems() {
		return bidItems;
	}

	public static Date getAuctionStartTime() {
		if (auction != null) {
			return auction.getAuctionStartTime();
		}
		return null;
	}

	public static BidItem getBidItem(Long bidItemId) {
		return bidItemsMap.get(bidItemId);
	}

}
