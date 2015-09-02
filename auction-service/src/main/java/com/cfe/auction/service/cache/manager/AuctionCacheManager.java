package com.cfe.auction.service.cache.manager;

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

	private static Map<Integer, AuctionCacheBean> activeAuctionMap = new HashMap<Integer, AuctionCacheBean>();
	
	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println("Properties Loaded");
		//setActiveAuction();
		System.out.println("active Auction id::");
		//bidItemsCacheService.initCache();
	}

	public void refreshAuctionCache(AuctionCacheBean auctionCacheBean) {
		setActiveAuction(auctionCacheBean);
	}

	public static void flushCache() {
	/*	activeAuctionId = null;
		bidItems = null;
		bidItemsMap = new HashMap<Long, BidItem>();
		auction = null;*/
	}

	public static void setActiveAuctionId(AuctionCacheBean auctionCacheBean) {
		activeAuctionMap.put(auctionCacheBean.getClientId(), auctionCacheBean);
	}

	public static Integer getActiveAuctionId(AuctionSearchBean auctionSearchBean) {
		if (activeAuctionMap.get(auctionSearchBean.getClientId()) != null) {
			return activeAuctionMap.get(auctionSearchBean.getClientId()).getAuctionId();
		}
		return  null;
	}
	
	private void setActiveAuction(AuctionCacheBean auctionCacheBean) {
		if (auctionCacheBean != null) {
			AuctionSearchBean auctionSearchBean = new AuctionSearchBean(auctionCacheBean.getSchemaName());
			auctionSearchBean.setAuctionId(auctionCacheBean.getAuctionId());
			Auction auction = auctionService.getActiveAuction(auctionSearchBean);
			if (auction != null) {
				auctionSearchBean.setGenericId(auction.getBidItemGroupId());
				List<BidItem> bidItems = auctionService.getActiveAuctionBidItem(auctionSearchBean);
				Map<Long, BidItem> bidItemsMap = new HashMap<Long, BidItem>();
				if (bidItems != null) {
					for (BidItem bidItem : bidItems) {
						if (bidItem != null && bidItem.getBidItemId() != null) {
							bidItemsMap.put(bidItem.getBidItemId(), bidItem);
						}
					}
				}
				AuctionCacheBean auctionCacheBean2 = activeAuctionMap.get(auctionCacheBean.getClientId());
				auctionCacheBean2.setBidItems(bidItems);
				auctionCacheBean2.setBidItemsMap(bidItemsMap);
			}
		} 
	}

	public static BidItem getBidItem(AuctionSearchBean auctionSearchBean, Long bidItemId) {
		return activeAuctionMap.get(auctionSearchBean.getClientId()).getBidItemsMap().get(bidItemId);
	}
	
	public static BidItem getActiveBidItem(AuctionSearchBean auctionSearchBean) {
		if (activeAuctionMap.get(auctionSearchBean.getClientId()) != null){
			Long bidItemId = activeAuctionMap.get(auctionSearchBean.getClientId()).getActiveBidItemId();
			if(bidItemId != null){
				return activeAuctionMap.get(auctionSearchBean.getClientId()).getBidItemsMap().get(bidItemId);
			}
		}
		return null;
	}

	public static void setActiveBidItemId(AuctionSearchBean auctionSearchBean, Long activeBidItemId) {
		activeAuctionMap.get(auctionSearchBean.getClientId()).setActiveBidItemId(activeBidItemId);
	}

	public static void setAuctionClosed(AuctionSearchBean auctionSearchBean) {
		activeAuctionMap.get(auctionSearchBean.getClientId()).setAuctionClosed(true);
	}
	
	public static boolean isAuctionClosed(AuctionSearchBean auctionSearchBean) {
		if(activeAuctionMap.get(auctionSearchBean.getClientId()) != null) {
			return activeAuctionMap.get(auctionSearchBean.getClientId()).isAuctionClosed();
		}
		return false;
	}
	
	public static Long getActiveBidItemId(AuctionSearchBean auctionSearchBean) {
		return activeAuctionMap.get(auctionSearchBean.getClientId()).getActiveBidItemId();
	}

	public static AuctionCacheBean getActiveAuctionCacheBean(AuctionSearchBean auctionSearchBean) {
		return activeAuctionMap.get(auctionSearchBean.getClientId());
	}
	public static Long getActiveBidSequenceId(AuctionSearchBean auctionSearchBean) {
		BidItem bidItem = getActiveBidItem(auctionSearchBean);
		return bidItem != null ? bidItem.getSeqId() : null;
	
	}
	public static List<BidItem> getBidItems(AuctionSearchBean auctionSearchBean) {
		if (activeAuctionMap.get(auctionSearchBean.getClientId()) != null) {
			return activeAuctionMap.get(auctionSearchBean.getClientId()).getBidItems();
		}
		return null;
	}

}
