package com.cfe.auction.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.hibernate.Query;
import org.springframework.stereotype.Service;

import com.cfe.auction.common.CommonBIddItems;
import com.cfe.auction.dao.BidItemDao;
import com.cfe.auction.dao.model.persist.AutoBids;
import com.cfe.auction.dao.model.persist.BidItem;
import com.cfe.auction.dao.model.persist.BidSequence;
import com.cfe.auction.dao.model.persist.BidderCategory;

@Service
public class BIdItemDaoImpl extends DAOImpl<Integer, BidItem> implements
		BidItemDao {

	@Override
	public List<BidItem> getBidItemsForCategoryForMarketListForBidder(
			long categoryId, CommonBIddItems commonVO) {
		String queryString = " from BidItem as bidItem left outer join bidItem.autoBids as autoBidsList, BidderCategory as bidderCateg, BidSequence bidSeq  "
				+ " where bidItem.category.categoryId = :categoryId  and bidderCateg.auction.auctionId = :auctionId "
				+ " and bidItem.category.categoryId = bidderCateg.bidderCategoryId.category.categoryId and bidderCateg.bidderCategoryId.user.username = :userName "
				+ " and bidSeq.auction.auctionId = :auctionId and bidSeq.bidItem.bidItemId = bidItem.bidItemId ";
		Long sequenceId = commonVO.getSequenceId();
		String userName = commonVO.getUserName();
		if (sequenceId != 0)
			queryString += " and bidSeq.sequenceId > :sequenceId order by bidSeq.sequenceId ";
		else
			queryString += " order by bidSeq.sequenceId";

		Query query = getSessionFactory().getCurrentSession()
				.createQuery(queryString)
				.setFirstResult(commonVO.getFirstResult())
				.setMaxResults(commonVO.getMaxResult());
		query.setParameter("categoryId", categoryId);
		query.setParameter("auctionId", commonVO.getAuctionId());
		if (sequenceId != 0L)
			query.setParameter("sequenceId", sequenceId);
		query.setParameter("userName", userName);

		List<Object[]> objectsList = query.list();
		List<BidItem> bidItemsList = new ArrayList<BidItem>();
		// Logger.debug("bidItemList.size()::::" + objectsList.size());

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
					bidItem.setCurrentAutoBidId(Long.parseLong(autoBid.getId()
							.toString()));
					break;
				}

			}
		/*	logger.debug("BidItem::" + bidItem);
			logger.debug("autoBids::" + autoBids);
			logger.debug("BidderCategory::" + bidderCategory);*/
			bidItem.setSeqId(bidSequence.getSequenceId());
			bidItem.setBidSpan(bidSequence.getBidspan());
			if (!bidItemsList.contains(bidItem))
				bidItemsList.add(bidItem);
		}
		return bidItemsList;
	}

}
