package com.cfe.auction.service.cache;

import com.cfe.auction.model.auction.persist.AuctionCacheBean;

/**
 * 
 * @author Vikas Anand
 *
 */
public interface IBidItemsCacheService {

	long setNextActiveBidItem(AuctionCacheBean auctionCacheBean);

	void initCache(AuctionCacheBean auctionCacheBean);

}
