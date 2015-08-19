package com.cfe.auction.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.cfe.auction.dao.BidItemDao;
import com.cfe.auction.model.persist.BidItem;
import com.cfe.auction.model.persist.BidSequence;

@Repository
public class BIdItemDaoImpl extends DAOImpl<Integer, BidItem> implements
		BidItemDao {
	/*
	 * private Logger logger = org.slf4j.LoggerFactory
	 * .getLogger(BIdItemDaoImpl.class);
	 */

	@SuppressWarnings("unchecked")
	@Override
	public List<BidItem> getBidItems(Long bidItemGroupId) {
		Query query = getEntityManager().createQuery(" from BidItem as bidItem where bidItemGroupId = :bidItemGroupId");
		query.setParameter("bidItemGroupId", bidItemGroupId);
		return  (List<BidItem>)query.getResultList();
	}
	public List<BidItem> getBidItems(Integer auctionId) {

		Query query = getEntityManager().createQuery(" from BidItem as bidItem , BidSequence bidSeq "
				+ "where bidSeq.auction.auctionId = :auctionId and bidSeq.bidItem.bidItemId = bidItem.bidItemId");
		query.setParameter("auctionId", auctionId);	
		List<Object[]> objectsList = query.getResultList();
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

	@Override
	public BidItem getBidItem(Long id) {

		Query query = getEntityManager().createQuery(
				"from BidItem where bidItemId = :bidItemId");
		query.setParameter("bidItemId", id);
		return (BidItem) query.getSingleResult();
	}
}
