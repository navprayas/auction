package com.cfe.auction.service.cache.manager;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cfe.auction.model.persist.Auction;
import com.cfe.auction.model.persist.BidItem;
import com.cfe.auction.service.IAuctionService;

@Component
public class AuctionCacheManager implements InitializingBean {
	
	@Autowired
	IAuctionService auctionService;
	
	private static Integer activeAuctionId;
	
	private static List<BidItem> bidItems;
	
	private static Map<Long, BidItem> bidItemsMap = new HashMap<Long, BidItem>();
	
	private static Auction auction;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println("Properties Loaded");
		setActiveAuction();
		System.out.println("active Auction id::" + activeAuctionId);
		setActiveBidItemsList();
		setBidItemsMap();
		
	}
	public void refreshAuctionCache() {
		setActiveAuction();
		setActiveBidItemsList();
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
	private void setActiveAuction() {
		if (activeAuctionId == null) {
			auction = auctionService.getActiveAuction();
			if (auction != null) {
				activeAuctionId = auction.getId();
			}
		} else {
			auction = auctionService.getActiveAuction(activeAuctionId);
		}
	}
	
	private static void setBidItemsMap() {
		if(bidItems != null && !bidItems.isEmpty()) {
			for(BidItem bidItem : bidItems) {
				if(bidItem != null && bidItem.getId() != null) {
					bidItemsMap.put(bidItem.getBidItemId(), bidItem);
				}
			}
		}
	}
	
	private void setActiveBidItemsList() {
		if (activeAuctionId != null) {
			bidItems = auctionService.getActiveAuctionBidItem(activeAuctionId);
		}
	}
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
		return  null;
	}
	public static BidItem getBidItem(Long bidItemId) {
		return bidItemsMap.get(bidItemId);
	}

}
