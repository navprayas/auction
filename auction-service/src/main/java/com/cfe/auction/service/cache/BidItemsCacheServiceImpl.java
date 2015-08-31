package com.cfe.auction.service.cache;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cfe.auction.model.auction.persist.AuctionCacheBean;
import com.cfe.auction.model.auction.persist.AuctionSearchBean;
import com.cfe.auction.model.persist.BidItem;
import com.cfe.auction.model.persist.BidSequence;
import com.cfe.auction.service.BidItemService;
import com.cfe.auction.service.IBidSequenceService;
import com.cfe.auction.service.cache.manager.AuctionCacheManager;
import com.cfe.auction.service.cache.manager.AuctionCacheService;
import com.cfe.auction.service.cache.scheduler.BidItemScheduler;

/**
 * 
 * @author Vikas Anand
 *
 */
@Service
public class BidItemsCacheServiceImpl implements IBidItemsCacheService {
	
	private final static Logger logger = LoggerFactory.getLogger(BidItemsCacheServiceImpl.class);
	
	@Autowired
	IBidSequenceService bidSequenceService;
	
	@Autowired
	BidItemService bidItemService;
	
	private BidItemScheduler scheduler = null;

	boolean startFlag = true;
	
	@Override
	public long setNextActiveBidItem(AuctionCacheBean auctionCacheBean) {
		AuctionSearchBean auctionSearchBean = new AuctionSearchBean();
		auctionSearchBean.setClientId(auctionCacheBean.getClientId());
		if(startFlag) {
			startFlag = false;
			logger.debug("In Start of Sequence");
			Long activeBidItemId = AuctionCacheService.pollActiveBidItemId(auctionCacheBean);
			AuctionCacheManager.setActiveBidItemId(auctionSearchBean, activeBidItemId);
			BidItem bidItem = getBidItem(activeBidItemId, auctionSearchBean);
			logger.debug("BidItem from Redis : " + bidItem);
			return bidItem != null ? bidItem.getBidSpan() : 0;
		} 
		else {
			logger.debug("In Next of Sequence");
			BidItem bidItem = AuctionCacheManager.getActiveBidItem(auctionSearchBean);
			logger.debug("BidItem from Auction cache service : " + bidItem);
			if(bidItem == null) {
				return 0;
			}
			if(bidItem.getTimeLeft() > 0) {
				logger.debug("Extended Time: " + bidItem.getTimeLeft());
				return bidItem.getTimeLeft();		
			}
			else {
				cleanBidItem(AuctionCacheManager.getActiveBidItemId(auctionSearchBean), auctionSearchBean);
				Long activeBidItemId = AuctionCacheService.pollActiveBidItemId(auctionCacheBean);
				AuctionCacheManager.setActiveBidItemId(auctionSearchBean, activeBidItemId);
				bidItem = getBidItem(activeBidItemId, auctionSearchBean);
				if(bidItem == null) {
					return 0;
				}
				return bidItem.getBidSpan();
			}
		}
	}

	@Override
	public void initCache(AuctionCacheBean auctionCacheBean)
	{
		logger.debug("Initializing cache " + new Date());
		
		startFlag = true;
		
		AuctionSearchBean auctionSearchBean = new AuctionSearchBean(auctionCacheBean.getSchemaName());
		auctionSearchBean.setAuctionId(auctionCacheBean.getAuctionId());
		
		List<BidSequence> bidSequenceList = bidSequenceService.getBidSequenceList(auctionSearchBean);
		AuctionCacheService.setBidSequenceQueue(auctionCacheBean, bidSequenceList);
		logger.debug("bidSequenceList " + bidSequenceList);
		try
		{
			if(bidSequenceList != null && bidSequenceList.size() > 0)
			{
				//RedisCacheService.setBidIdKey(commonDao.getMaxBidId());
				//RedisCacheService.setAutoBidIdKey(commonDao.getMaxAutoBidId());
				scheduler = new BidItemScheduler();
				scheduler.setCacheService(this);
				scheduler.start(auctionCacheBean);
			}
		}
		catch(Exception e)
		{
			logger.error("Cache service failed " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	private void initializeBidItem(BidItem bidItem) {
		Calendar cal = Calendar.getInstance() ;
		bidItem.setLastUpDateTime(cal.getTime());
		bidItem.setBidStartTime(cal.getTime());
		long bidSpan = bidItem.getBidSpan();

		cal.add(Calendar.SECOND, (int)bidSpan);
		bidItem.setBidEndTime(cal.getTime());
		bidItem.setStatusCode("ACTIVE");
		bidItem.setBidSpan(bidSpan);
	}
	
	@Transactional
	private BidItem getBidItem(Long activeBidItemId, AuctionSearchBean auctionSearchBean)
	{
		if (activeBidItemId != 0) {
		BidItem bidItem = AuctionCacheManager.getBidItem(auctionSearchBean, activeBidItemId);
			if (bidItem != null) {
				initializeBidItem(bidItem);
				return bidItem;
			}
		}
		return null;
	}
	
	private void cleanBidItem(long bidItemId, AuctionSearchBean auctionSearchBean) {
		BidItem bidItem = setExpiredBidItem(bidItemId, auctionSearchBean);
		bidItemService.updateBidItem(auctionSearchBean, bidItem);
	}
	
	private BidItem setExpiredBidItem(long bidItemId, AuctionSearchBean auctionSearchBean) {
		BidItem bidItem = AuctionCacheManager.getBidItem(auctionSearchBean, bidItemId);
		bidItem.setBidEndTime(new Date());
		bidItem.setLastUpDateTime(new Date());
		//bidItem.setCurrentMarketPrice(bidItem.getCurrentMarketPrice());
		bidItem.setStatusCode("END");
		return bidItem;
	}
}


