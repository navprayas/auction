package com.cfe.auction.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import com.cfe.auction.dao.BidItemDao;
import com.cfe.auction.model.persist.AutoBids;
import com.cfe.auction.model.persist.BidItem;
import com.cfe.auction.model.persist.BidSequence;
import com.cfe.auction.model.persist.BidderCategory;

@Service
public class BIdItemDaoImpl extends DAOImpl<Integer, BidItem> implements
		BidItemDao {
	private Logger logger = org.slf4j.LoggerFactory
			.getLogger(BIdItemDaoImpl.class);

	@Override
	public List<BidItem> getBidItems() {
		String queryString = " from BidItem as bidItem left outer join bidItem.autoBids as autoBidsList, BidderCategory as bidderCateg, BidSequence bidSeq  "
				+ " where bidItem.category.categoryId = :categoryId  and bidderCateg.auction.auctionId = :auctionId "
				+ " and bidItem.category.categoryId = bidderCateg.bidderCategoryId.category.categoryId and bidderCateg.bidderCategoryId.user.username = :userName "
				+ " and bidSeq.auction.auctionId = :auctionId and bidSeq.bidItem.bidItemId = bidItem.bidItemId ";
		Long sequenceId = 1l;// commonVO.getSequenceId();
		String userName = "bidder";// commonVO.getUserName();
		if (sequenceId != 0)
			queryString += " and bidSeq.sequenceId > :sequenceId order by bidSeq.sequenceId ";
		else
			queryString += " order by bidSeq.sequenceId";

		Query query = getSessionFactory().getCurrentSession().createQuery(
				queryString);
		query.setParameter("categoryId", 1);
		query.setParameter("auctionId", 76);
		if (sequenceId != 0L)
			query.setParameter("sequenceId", sequenceId);
		query.setParameter("userName", userName);

		List<Object[]> objectsList = query.list();
		List<BidItem> bidItemsList = new ArrayList<BidItem>();
		logger.debug("bidItemList.size()::::" + objectsList.size());

		for (Object[] objects : objectsList) {
			BidItem bidItem = (BidItem) objects[0];
			AutoBids autoBids = (AutoBids) objects[1];
			BidderCategory bidderCategory = (BidderCategory) objects[2];
			BidSequence bidSequence = (BidSequence) objects[3];
			List<AutoBids> autoBidsList = bidItem.getAutoBids();
			for (AutoBids autoBid : autoBidsList) {
				if (userName.equals(autoBid.getBidderName())
						&& autoBid.getBidStatus().equalsIgnoreCase("A")) {
					bidItem.setAutoBidFlag(true);
					bidItem.setAmountAutoBid(autoBid.getBidAmount());
					bidItem.setCurrentAutoBidId((long) autoBid.getId());
					break;
				}

			}
			logger.debug("BidItem::" + bidItem);
			logger.debug("autoBids::" + autoBids);
			logger.debug("BidderCategory::" + bidderCategory);
			bidItem.setSeqId(bidSequence.getSequenceId());
			bidItem.setBidSpan(bidSequence.getBidspan());
			if (!bidItemsList.contains(bidItem))
				bidItemsList.add(bidItem);
		}
		return bidItemsList;
	}

	@Override
	public List<BidItem> getBIdItemActiveMarket() {
		Query query = getSessionFactory()
				.getCurrentSession()
				.createQuery(
						" from BidItem as bidItem, BidderCategory as bidderCateg where bidItem.category.categoryId = :categoryId  and bidderCateg.bidderCategoryId.category.categoryId = :categoryId and bidItem.bidStartTime < :currentTime and bidItem.bidEndTime > :currentTime  and bidderCateg.bidderCategoryId.user.username = :userName ");
		query.setParameter("categoryId", 2);
		query.setParameter("currentTime", new Date());
		query.setParameter("userName", "bidder");
		List<Object[]> objectsList = query.list();
		List<BidItem> bidItemsList = new ArrayList<BidItem>();
		logger.debug("bidItemList.size()::::" + objectsList.size());
		for (Object[] objects : objectsList) {
			BidItem bidItem = (BidItem) objects[0];
			BidderCategory bidderCategory = (BidderCategory) objects[1];
			bidItemsList.add(bidItem);
		}
		return bidItemsList;
	}

	@Override
	public List<BidItem> getBIdItemClosedMarket() {
		Long sequenceId = 1l;// commonVO.getSequenceId();
		Long auctionId = 76l;// commonVO.getAuctionId();
		String userName = "bidder"; // commonVO.getUserName();
		String queryString = " from BidItem as bidItem, BidderCategory as bidderCateg, BidSequence bidSeq  "
				+ "where bidItem.category.categoryId = bidderCateg.bidderCategoryId.category.categoryId and bidderCateg.bidderCategoryId.user.username = :userName "
				+ " and bidItem.category.categoryId = :categoryId  and  bidderCateg.auction.auctionId = :auctionId and "
				+ " bidSeq.auction.auctionId = :auctionId and bidSeq.bidItem.bidItemId = bidItem.bidItemId ";
		if (sequenceId != 0L)
			queryString += " and bidSeq.sequenceId < :sequenceId order by bidSeq.sequenceId ";
		else
			queryString += " order by bidSeq.sequenceId";

		Query query = getSessionFactory().getCurrentSession().createQuery(
				queryString);

		// query.setParameter("bidStartTime", new Date());
		query.setParameter("categoryId", 2);
		query.setParameter("auctionId", auctionId);
		if (sequenceId != 0L)
			query.setParameter("sequenceId", sequenceId);
		query.setParameter("userName", userName);

		List<Object[]> objectsList = query.list();
		List<BidItem> bidItemsList = new ArrayList<BidItem>();
		logger.debug("bidItemList.size()::::" + objectsList.size());
		for (Object[] objects : objectsList) {
			BidItem bidItem = (BidItem) objects[0];
			// BidderCategory bidderCategory = (BidderCategory)objects[1];
			BidSequence bidSeq = (BidSequence) objects[2];
			bidItem.setSeqId(bidSeq.getSequenceId());
			String bidderName = bidItem.getBidderName();
			if (bidderName != null && bidderName.equals(userName)) {
				bidItem.setStatus("Won");
			}
			bidItemsList.add(bidItem);

		}
		return bidItemsList;
	}
}
