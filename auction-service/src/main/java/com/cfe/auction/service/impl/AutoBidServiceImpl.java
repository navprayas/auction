package com.cfe.auction.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cfe.auction.dao.AutoBidDao;
import com.cfe.auction.dao.BidItemDao;
import com.cfe.auction.dao.IAuctionDao;
import com.cfe.auction.model.auction.persist.AuctionSearchBean;
import com.cfe.auction.model.persist.AutoBids;
import com.cfe.auction.model.persist.BidItem;
import com.cfe.auction.service.AutoBidService;

@Service
@Transactional
public class AutoBidServiceImpl extends
		CRUDServiceImpl<Integer, AutoBids, AutoBidDao> implements
		AutoBidService {
	@Autowired
	private BidItemDao bidItemDao;
	@Autowired
	private IAuctionDao iAuctionDao;
	@Autowired
	private AutoBidDao autoBidDao;

	@Autowired
	public AutoBidServiceImpl(AutoBidDao dao) {
		super(dao);
	}

	@Override
	public void saveAutoBid(String userName, Long categoryId, Long bidItemId,
			Double bidAmount, String comment,
			AuctionSearchBean auctionSearchBean) {

		BidItem bidItem = bidItemDao.getBidItem(bidItemId, auctionSearchBean);
		System.out.println(bidItem.getCurrency());

		AutoBids autoBid = new AutoBids();
		autoBid.setBidItemId(bidItemId);
		autoBid.setBidTime(new Date());
		autoBid.setCurrency("INR");
		autoBid.setBidAmount(bidAmount);
		autoBid.setBidderName(userName);
		autoBid.setBidStatus("A");
		autoBid.setComments(comment);
		autoBid.setAuctionId(auctionSearchBean.getAuctionId());
		System.out.println(autoBid);

		List<AutoBids> autoBids = autoBidDao.getAutoBids(auctionSearchBean);
		if (autoBids != null)
			for (AutoBids autoBidItem : autoBids) {
				if (autoBidItem.getBidderName().equalsIgnoreCase(userName)) {
					autoBidItem.setBidStatus("I");
					autoBidDao.createOrUpdate(autoBidItem);
				}
			}

		autoBid = autoBidDao.create(autoBid, auctionSearchBean);

		bidItem.setCurrentAutoBidId(autoBid.getAutoBidId());

		bidItemDao.createOrUpdate(bidItem, auctionSearchBean);

	}
}
