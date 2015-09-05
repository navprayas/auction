package com.cfe.auction.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cfe.auction.common.Bid;
import com.cfe.auction.dao.BidItemDao;
import com.cfe.auction.dao.CategoryDao;
import com.cfe.auction.dao.IAuctionDao;
import com.cfe.auction.model.auction.persist.AuctionSearchBean;
import com.cfe.auction.model.persist.BidItem;
import com.cfe.auction.model.persist.Bids;
import com.cfe.auction.model.persist.Category;
import com.cfe.auction.service.BidItemService;

;

@Service("bidItemServiceImpl")
@Transactional
public class BidItemServiceImpl extends
		CRUDServiceImpl<Integer, BidItem, BidItemDao> implements BidItemService {
	@Autowired
	private BidItemDao bidItemDao;
	@Autowired
	private IAuctionDao iAuctionDao;
	@Autowired
	private CategoryDao categoryDao;

	@Autowired
	public BidItemServiceImpl(BidItemDao dao) {
		super(dao);
	}

	@Transactional
	@Override
	public List<BidItem> getBidItemsbyAuctionId(
			AuctionSearchBean auctionSearchBean) {
		return bidItemDao.getBidItems(auctionSearchBean);
	}

	@Transactional
	@Override
	public void updateBidItem(AuctionSearchBean auctionSearchBean,
			BidItem bidItem) {
		bidItemDao.updateBidItem(auctionSearchBean, bidItem);
	}

	@Transactional
	@Override
	public List<BidItem> getWonList(String username,
			AuctionSearchBean auctionSearchBean) {
		List<Bid> bidList = new ArrayList<Bid>();

		converterBidsToBidEntity(bidList,
				bidItemDao.getWonList(username, auctionSearchBean),
				auctionSearchBean);
		return bidItemDao.getWonList(username, auctionSearchBean);
	}

	private void converterBidsToBidEntity(List<Bid> bidList,
			List<BidItem> bidItems, AuctionSearchBean auctionSearchBean) {
		Bid bid = null;
		for (BidItem bidItem : bidItems) {
			bid = new Bid();
			bid.setBidItemName(bidItem.getName());
			Category category = categoryDao.get(bidItem.getCategoryId());
			bid.setCategoryName(category.getCategoryName());
			bid.setZone(bidItem.getZone());
			bid.setLocation(bidItem.getLocation());
			bid.setCity(bidItem.getCity());
			bid.setUnit(bidItem.getUnit());
			bid.setTotalQuantity(bidItem.getTotalQuantity());
			/*bid.setAuctionId(bids.getAuctionId());
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
			bid.setSalesPrice(bids.getBidAmount());*/
			bidList.add(bid);

		}

	}

}
