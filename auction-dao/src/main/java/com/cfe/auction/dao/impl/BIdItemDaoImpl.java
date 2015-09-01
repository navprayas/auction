package com.cfe.auction.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.cfe.auction.dao.BidItemDao;
import com.cfe.auction.model.auction.persist.AuctionSearchBean;
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
	public List<BidItem> getBidItems(AuctionSearchBean auctionSearchBean) {

		Query query = getEntityManager().createQuery(" from BidItem as bidItem , BidSequence bidSeq "
				+ "where bidSeq.auction.auctionId = :auctionId and bidSeq.bidItemId = bidItem.bidItemId");
		query.setParameter("auctionId", auctionSearchBean.getAuctionId());	
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
	public BidItem getBidItem(Long id, AuctionSearchBean auctionSearchBean) {

		Query query = getEntityManager(auctionSearchBean.getSchemaName()).createQuery(
				"from BidItem where bidItemId = :bidItemId");
		query.setParameter("bidItemId", id);
		return (BidItem) query.getSingleResult();
	}
	
	@Override
	public void updateBidItem(AuctionSearchBean auctionSearchBean, BidItem bidItem) {

		EntityManager entityManager = getEntityManager(auctionSearchBean.getSchemaName());
		BidItem dbBidItem = entityManager.find(BidItem.class, bidItem.getBidItemId());
		if(dbBidItem != null) {
			dbBidItem.setBidStartTime(bidItem.getBidStartTime());
			dbBidItem.setBidEndTime(bidItem.getBidEndTime());
			dbBidItem.setLastUpDateTime(bidItem.getLastUpDateTime());
			dbBidItem.setCurrentMarketPrice(bidItem.getCurrentMarketPrice());
			dbBidItem.setStatusCode(bidItem.getStatus());
			entityManager.merge(dbBidItem);
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<BidItem> getWonList(String username, AuctionSearchBean auctionSearchBean) {
		Query query = getEntityManager(auctionSearchBean.getSchemaName())
				.createQuery("From CloseBids closeBids where closeBids.bidderName= :bidderName ");
		query.setParameter("bidderName", username);
		List<CloseBids> closebidList = query.getResultList();
		
		List<BidItem> listBidItems = new ArrayList<BidItem>();
		for (CloseBids closebid : closebidList) {
			BidItem bidItem = closebid.getBidItem();
			listBidItems.add(bidItem);
		}
		return listBidItems ;
	}

	@Override
	public BidItem createOrUpdate(BidItem bidItem,
			AuctionSearchBean auctionSearchBean) {
		getEntityManager(auctionSearchBean.getSchemaName()).merge(bidItem);
		return bidItem;
		
	}
}
