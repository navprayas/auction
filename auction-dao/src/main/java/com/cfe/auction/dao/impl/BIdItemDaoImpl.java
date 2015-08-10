package com.cfe.auction.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.cfe.auction.dao.BidItemDao;
import com.cfe.auction.model.persist.BidItem;
import com.cfe.auction.model.persist.BidSequence;

@Repository
public class BIdItemDaoImpl extends DAOImpl<Integer, BidItem> implements
		BidItemDao {
	/*private Logger logger = org.slf4j.LoggerFactory
			.getLogger(BIdItemDaoImpl.class);*/

	@SuppressWarnings("unchecked")
	@Override
	public List<BidItem> getBidItems(Integer auctionId) {

		Query query = getSessionFactory().getCurrentSession().createQuery(" from BidItem as bidItem , BidSequence bidSeq "
				+ "where bidSeq.auction.id = :auctionId and bidSeq.bidItem.bidItemId = bidItem.bidItemId");
		query.setInteger("auctionId", auctionId);	
		List<Object[]> objectsList = query.list();
		List<BidItem> bidItemsList = new ArrayList<BidItem>();

		for (Object[] objects : objectsList) {
			BidItem bidItem = (BidItem)objects[0];
			BidSequence bidSequence = (BidSequence)objects[1];

			bidItem.setSeqId(bidSequence.getSequenceId());
			bidItem.setBidSpan(bidSequence.getBidspan());

			if(!bidItemsList.contains(bidItem)) {
				bidItemsList.add(bidItem);
			}
		}
		return bidItemsList;
	}

	@Override
	public List<BidItem> getBIdItemActiveMarket() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BidItem> getBIdItemClosedMarket() {
		// TODO Auto-generated method stub
		return null;
	}
}
