package com.cfe.auction.dao.impl;

import org.springframework.stereotype.Service;

import com.cfe.auction.dao.BidsDao;
import com.cfe.auction.model.auction.persist.AuctionSearchBean;
import com.cfe.auction.model.persist.Bids;

@Service
public class BidsDaoImpl extends DAOImpl<Integer, Bids> implements BidsDao {

	@Override
	public void createBids(Bids bids, AuctionSearchBean auctionSearchBean) {
		getEntityManager(auctionSearchBean.getSchemaName()).persist(bids);

	}

}
