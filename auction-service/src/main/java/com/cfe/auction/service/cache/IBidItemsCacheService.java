package com.cfe.auction.service.cache;

import java.util.Date;

/**
 * 
 * @author Vikas Anand
 *
 */
public interface IBidItemsCacheService {

	long setNextActiveBidItem();

	void setAuctionStartTime(Date auctionStartTime);

	void initCache();

}
