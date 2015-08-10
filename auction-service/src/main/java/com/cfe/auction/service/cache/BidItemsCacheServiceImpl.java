package com.cfe.auction.service.cache;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cfe.auction.model.persist.BidItem;
import com.cfe.auction.model.persist.BidSequence;
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
	
	private BidItemScheduler scheduler = null;

	boolean startFlag = true;
	
	public Date auctionStartTime;
	
	@Override
	public long setNextActiveBidItem() {
		if(startFlag) {
			startFlag = false;
			logger.debug("In Start of Sequence");
			AuctionCacheService.setActiveBidItemId();
			BidItem bidItem = getBidItem();
			logger.debug("BidItem from Redis : " + bidItem);
			return bidItem.getBidSpan();
		} 
		else {
			logger.debug("In Next of Sequence");
			BidItem bidItem = getBidItem();
			logger.debug("BidItem from Auction cache service : " + bidItem);
			if(bidItem == null) {
				return 0;
			}
			if(bidItem.getTimeLeft() > 0) {
				logger.debug("Extended Time: " + bidItem.getTimeLeft());
				return bidItem.getTimeLeft();		
			}
			else {
				AuctionCacheService.setActiveBidItemId();
				//cleanBidItem(activeBidItemId);
				bidItem = getBidItem();
				if(bidItem == null) {
					return 0;
				}
				return bidItem.getBidSpan();
			}
		}
	}

	@Transactional
	@Override
	public void initCache()
	{
		logger.debug("Initializing cache " + new Date());
		
		startFlag = true;
		Date startTime = null;
		if(auctionStartTime != null) {
			startTime = auctionStartTime;
		}else {
			startTime = AuctionCacheManager.getAuctionStartTime();
		} 
		List<BidSequence> bidSequenceList = bidSequenceService.getBidSequenceList();
		AuctionCacheService.setBidSequenceQueue(bidSequenceList);
		logger.debug("bidSequenceList " + bidSequenceList);
		try
		{
			if(bidSequenceList != null && bidSequenceList.size() > 0)
			{
				//RedisCacheService.setBidIdKey(commonDao.getMaxBidId());
				//RedisCacheService.setAutoBidIdKey(commonDao.getMaxAutoBidId());
				scheduler = new BidItemScheduler();
				scheduler.setCacheService(this);
				scheduler.start(startTime);
			}
		}
		catch(Exception e)
		{
			logger.error("Cache service failed " + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void setAuctionStartTime(Date auctionStartTime) {
		this.auctionStartTime = auctionStartTime;
	}
	
	private void initializeBidItem(BidItem bidItem) {
		Calendar cal = Calendar.getInstance() ;
		bidItem.setLastUpDateTime(cal.getTime());
		bidItem.setBidStartTime(cal.getTime());
		//long bidSpan = RedisCacheService.getBidItemSpan(bidItem.getBidItemId());
		BidSequence bidSequence = AuctionCacheService.getBidSequenceDetails(bidItem.getBidItemId());
		long bidSpan = bidSequence.getBidspan();
		long seqId = bidSequence.getSequenceId();
		cal.add(Calendar.SECOND, (int)bidSpan);
		bidItem.setBidEndTime(cal.getTime());
		bidItem.setStatusCode("ACTIVE");
		bidItem.setBidSpan(bidSpan);
		bidItem.setSeqId(seqId);
	}
	
	@Transactional
	private BidItem getBidItem()
	{
		Long activeBidItemId = AuctionCacheService.getActiveBidItemId();
		BidItem bidItem = AuctionCacheService.getActiveBidItem(activeBidItemId);
		if(bidItem == null && activeBidItemId != 0) {
			bidItem = AuctionCacheManager.getBidItem(activeBidItemId);
			initializeBidItem(bidItem);
			AuctionCacheService.setActiveBidItem(activeBidItemId, bidItem); 
		}
		return bidItem;
	}
	
	/*private void cleanBidItem(long bidItemId) {
		RedisCacheService.setExpiredBidItem(bidItemId);
		commonDao.updateBidItem(RedisCacheService.getBidItem(RedisConstants.BIDITEM + bidItemId, bidItemId + ""));
	}*/
}
