package com.cfe.auction.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cfe.auction.common.Bid;
import com.cfe.auction.dao.BidsDao;
import com.cfe.auction.model.auction.persist.AuctionSearchBean;
import com.cfe.auction.model.persist.Bids;
import com.cfe.auction.service.BidsService;

@Service("bidsServiceImpl")
@Transactional
public class BidsServiceImpl extends CRUDServiceImpl<Integer, Bids, BidsDao>
		implements BidsService {
	@Autowired
	private BidsDao bidsDao;

	@Autowired
	public BidsServiceImpl(BidsDao dao) {
		super(dao);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createBidService(Bid bid, AuctionSearchBean auctionSearchBean) {
		Bids bids = new Bids();
		bids.setAuctionId(Long.parseLong(bid.getAuctionId().toString()));
		bids.setBidId(bid.getBidItemId());
		bids.setBidAmount(bid.getBidAmount());
		bids.setCurrency("INR");
		bids.setBidStatus(bid.getBidStatus());
		bids.setBidType(bid.getBidType());
		bids.setComments(bid.getComments());
		bidsDao.createBids(bids, auctionSearchBean);
		//create(bids);

	}

}
