package com.cfe.auction.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cfe.auction.dao.IAuctionDao;
import com.cfe.auction.model.persist.Auction;
import com.cfe.auction.model.persist.BidItem;
import com.cfe.auction.service.BidItemService;
import com.cfe.auction.service.IAuctionService;
import com.cfe.auction.service.cache.manager.AuctionCacheManager;
/**
 * 
 * @author Vikas Anand
 *
 */
@Service("auctionServiceImpl")
@Transactional
public class AuctionServiceImpl extends
		CRUDServiceImpl<Integer, Auction, IAuctionDao> implements
		IAuctionService {
	
	@Autowired
	IAuctionDao auctionDao;
	
	@Autowired
	BidItemService bidItemService;

	@Autowired
	public AuctionServiceImpl(IAuctionDao dao) {
		super(dao);
	}
	@Transactional
	@Override
	public List<BidItem> getActiveAuctionBidItem(Integer auctionId) { 
	  if (AuctionCacheManager.getActiveAuctionId() != null) {
		  List<BidItem> bidItems = bidItemService.getBidItemsbyAuctionId(AuctionCacheManager.getActiveAuctionId());
		  return bidItems;
	  }
	  return null;
	}
	@Transactional
	@Override
	public Auction getActiveAuction(Integer auctionId) {
		return auctionDao.getActiveAuction(auctionId);
	}
	
	@Transactional
	@Override
	public Auction getActiveAuction() {
		return auctionDao.getActiveAuction();
	}
	@Transactional
	@Override
	public List<Auction> getAuctionList() {
		return auctionDao.getAuctionList();
	}
	
	@Transactional
	@Override
	public void closeAuction(Integer auctionId) {
		auctionDao.closeAuction(auctionId);
	}

	@Transactional
	@Override
	public boolean isValidAuction(Integer auctionId) {
		return auctionDao.isValidAuction(auctionId);
	}
	@Override
	public void setActualAuctionStartTime(Integer auctionId,
			Date actualAuctionStartTime) {
		auctionDao.setActualAuctionStartTime(auctionId, actualAuctionStartTime);
	}
	
}
