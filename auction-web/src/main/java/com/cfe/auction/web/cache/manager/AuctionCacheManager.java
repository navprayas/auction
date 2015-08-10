package com.cfe.auction.web.cache.manager;

import java.util.List;

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

	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println("Properties Loaded");
		setActiveAuctionId();
		System.out.println("active Auction id::" + activeAuctionId);
		setActiveBidItemsList();
	}

	public void refreshAuctionCache() {
		setActiveAuctionId();
		setActiveBidItemsList();
	}

	private void setActiveAuctionId() {
		Auction auction = auctionService.getActiveAuction();
		if (auction != null) {
			activeAuctionId = auction.getAuctionId();
		}
	}

	private void setActiveBidItemsList() {
		bidItems = auctionService.getActiveAuctionBidItem(activeAuctionId);
	}

	public static Integer getActiveAuctionId() {
		return activeAuctionId;
	}

	public static List<BidItem> getBidItems() {
		return bidItems;
	}
}
