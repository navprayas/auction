package com.cfe.auction.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.cfe.auction.dao.BidItemDao;
import com.cfe.auction.model.persist.BidItem;
import com.cfe.auction.model.persist.BidSequence;
import com.cfe.auction.model.persist.CloseBids;

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
		Criteria criteria = getSessionFactory().getCurrentSession()
				.createCriteria(BidItem.class);
		criteria = criteria.add(Restrictions.eq("bidItemGroupId",
				bidItemGroupId));
		return  (List<BidItem>)criteria.list();
	}
	public List<BidItem> getBidItems(Integer auctionId) {

		Query query = getSessionFactory().getCurrentSession().createQuery(" from BidItem as bidItem , BidSequence bidSeq "
				+ "where bidSeq.auction.auctionId = :auctionId and bidSeq.bidItem.bidItemId = bidItem.bidItemId");
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

	@Override
	public BidItem getBidItem(Long id) {

		Query query = getSessionFactory().getCurrentSession().createQuery(
				"from BidItem where bidItemId=:bidItemId");
		query.setLong("bidItemId", id);
		return (BidItem) query.uniqueResult();
	}
	@Override
	public List<BidItem> getWonList(String username) {
		Query query = getSessionFactory().getCurrentSession().createQuery("From CloseBids closeBids where closeBids.bidderName= :bidderName ");
		query.setParameter("bidderName", username);
		List<CloseBids> closebidList = query.list();
		
		List<BidItem> listBidItems = new ArrayList<BidItem>();
		for (CloseBids closebid : closebidList) {
			BidItem bidItem = closebid.getBidItem();
			listBidItems.add(bidItem);
		}
		return listBidItems ;
	}
}
