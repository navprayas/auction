package com.cfe.auction.dao.impl;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Service;

import com.cfe.auction.dao.BidderCategoryDao;
import com.cfe.auction.model.auction.persist.AuctionSearchBean;
import com.cfe.auction.model.persist.BidderCategory;

@Service
public class BidderCategoryDaoImpl extends DAOImpl<Integer, BidderCategory>
		implements BidderCategoryDao {

	@Override
	public List<BidderCategory> getBidderCategory(Integer userId,
			AuctionSearchBean auctionSearchBean) {
		System.out.println("userId " + userId + "   " + auctionSearchBean.getAuctionId());
		Query query = getEntityManager(auctionSearchBean.getSchemaName())
				.createQuery(
						"from  BidderCategory bc where bc.auction.auctionId=:auctionId and bc.userId=:userId ");
		query.setParameter("auctionId", auctionSearchBean.getAuctionId());
		query.setParameter("userId", userId);
		return (List<BidderCategory>) query.getResultList();

	}

	@Override
	public Integer saveBIdderCategory(BidderCategory bidderCategory) {
		getEntityManager().persist(bidderCategory);
		return bidderCategory.getId();
	}

	@Override
	public List<BidderCategory> getAllCategory(Integer auctionId) {
		Query query = getEntityManager()
				.createQuery(
						"from  BidderCategory bc where bc.auction.auctionId=:auctionId");
		query.setParameter("auctionId", auctionId);
		return (List<BidderCategory>) query.getResultList();
	}
}
