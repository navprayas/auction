package com.cfe.auction.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cfe.auction.dao.IAuctionDao;
import com.cfe.auction.model.auction.persist.AuctionSearchBean;
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
	public List<BidItem> getActiveAuctionBidItem(
			AuctionSearchBean auctionSearchBean) {
		List<BidItem> bidItems = bidItemService
				.getBidItemsbyAuctionId(auctionSearchBean);
		return bidItems;
	}

	@Transactional
	@Override
	public Auction getActiveAuction(AuctionSearchBean auctionSearchBean) {
		return auctionDao.getActiveAuction(auctionSearchBean);
	}

	@Transactional
	@Override
	public Auction getActiveAuction() {
		return auctionDao.getActiveAuction();
	}

	@Transactional
	@Override
	public List<Auction> getAuctionList(String schemaKey) {
		return auctionDao.getAuctionList(schemaKey);
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

	@Override
	public void closeAuction(AuctionSearchBean auctionSearchBean) {
		auctionDao.closeAuction(auctionSearchBean);

	}

	@Override
	public boolean isValidAuction(AuctionSearchBean auctionSearchBean) {
		// TODO Auto-generated method stub
		return auctionDao.isValidAuction(auctionSearchBean);
	}

}
