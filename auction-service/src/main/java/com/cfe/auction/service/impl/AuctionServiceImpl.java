package com.cfe.auction.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cfe.auction.dao.IAuctionDao;
import com.cfe.auction.model.persist.Auction;
import com.cfe.auction.model.persist.BidItem;
import com.cfe.auction.service.BidItemService;
import com.cfe.auction.service.IAuctionService;
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
	public List<BidItem> getActiveAuctionBidItem() { 
	  Auction auction = auctionDao.getActiveAuction();
	  if (auction != null) {
		  List<BidItem> bidItems = bidItemService.getBidItemsbyGroupId(auction.getBidItemGroupId());
		  return bidItems;
	  }
	  return null;
	}
	@Transactional
	@Override
	public Auction getActiveAuction() {
		return auctionDao.getActiveAuction();
	}
	@Override
	public List<Auction> getAuctionList() {
		return auctionDao.getAuctionList();
	}

}
