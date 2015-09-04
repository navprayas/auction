package com.cfe.auction.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cfe.auction.common.Bid;
import com.cfe.auction.dao.BidItemDao;
import com.cfe.auction.dao.BidderCategoryDao;
import com.cfe.auction.dao.BidsDao;
import com.cfe.auction.dao.IAuctionDao;
import com.cfe.auction.model.auction.persist.AuctionSearchBean;
import com.cfe.auction.model.persist.BidItem;
import com.cfe.auction.model.persist.Bids;
import com.cfe.auction.service.BidsService;

@Service("bidsServiceImpl")
@Transactional
public class BidsServiceImpl extends CRUDServiceImpl<Integer, Bids, BidsDao>
		implements BidsService {
	@Autowired
	private BidsDao bidsDao;
	@Autowired
	private BidItemDao bidItemDao;
	@Autowired
	private IAuctionDao iAuctionDao;
	@Autowired
	private BidderCategoryDao bidderCategoryDao;

	@Autowired
	public BidsServiceImpl(BidsDao dao) {
		super(dao);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createBidService(Bid bid, AuctionSearchBean auctionSearchBean) {
		Bids bids = new Bids();
		bids.setAuctionId(bid.getAuctionId());
		// bids.setBidItem(bidItem);
		bids.setBidItemId(bid.getBidItemId());
		bids.setBidAmount(bid.getBidAmount());
		bids.setCurrency("INR");
		// bids.setBidStatus(bid.getBidStatus());
		bids.setBidStatus("A");
		bids.setBidTime(new Date());
		bids.setBidType(bid.getBidType());
		bids.setComments(bid.getComments());
		bids.setBidderName(bid.getBidderName());
		bidsDao.createBids(bids, auctionSearchBean);
	}

	@Override
	public List<Bid> getReportSummary(AuctionSearchBean auctionSearchBean) {
		List<Bids> bidsList = bidsDao.getReportSummary(auctionSearchBean);

		List<Bid> bidList = new ArrayList<Bid>();
		converterBidsToBidEntity(bidList, bidsList, auctionSearchBean);
		System.out.println(bidList);
		return bidList;
	}

	private void converterBidsToBidEntity(List<Bid> bidList,
			List<Bids> bidsList, AuctionSearchBean auctionSearchBean) {
		Bid bid = null;
		for (Bids bids : bidsList) {
			bid = new Bid();
			bid.setBidItemId(bids.getBidItemId());
			BidItem bidItem = bidItemDao.getBidItem(bids.getBidItemId(),
					auctionSearchBean);

			bid.setBidItemName(bidItem.getName());
			bid.setCategoryName(bidItem.getCategory().getCategoryName());
			bid.setZone(bidItem.getZone());
			bid.setLocation(bidItem.getLocation());
			bid.setCity(bidItem.getCity());
			bid.setUnit(bidItem.getUnit());
			bid.setTotalQuantity(bidItem.getTotalQuantity());
			bid.setAuctionId(bids.getAuctionId());
			bid.setAuctionName(iAuctionDao.get(
					Integer.parseInt(bids.getAuctionId().toString())).getName());
			bid.setBidAmount(bids.getBidAmount());
			bid.setBidderName(bids.getBidderName());
			bid.setBidStatus(bids.getBidStatus());
			bid.setBidTime(bids.getBidTime());
			bid.setBidType(bids.getBidType());
			bid.setComments(bids.getComments());
			bid.setStatus(bids.getBidStatus());
			bid.setCurrency(bids.getCurrency());
			bid.setSalesPrice(bids.getBidAmount());
			bidList.add(bid);

		}

	}

}
