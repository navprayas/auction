package com.cfe.auction.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cfe.auction.dao.BidderCategoryDao;
import com.cfe.auction.model.persist.BidderCategory;

@Service
public class BidderCategoryDaoImpl implements BidderCategoryDao {
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public List<BidderCategory> getBidderCategory(Integer userId,
			Integer auctionId) {
		Query query = sessionFactory
				.getCurrentSession()
				.createQuery(
						"from  BidderCategory bc where bc.auction.auctionId=:auctionId and bc.bidderCategoryId.user.id=:userId ");
		query.setInteger("auctionId", auctionId);
		query.setInteger("userId", userId);
		return (List<BidderCategory>) query.list();

	}

	@Override
	public Integer saveBIdderCategory(BidderCategory bidderCategory) {
		return (Integer) sessionFactory.getCurrentSession()
				.save(bidderCategory);

	}

	@Override
	public List<BidderCategory> getAllCategory(Integer auctionId) {
		Query query = sessionFactory
				.getCurrentSession()
				.createQuery(
						"from  BidderCategory bc where bc.auction.auctionId=:auctionId");
		query.setInteger("auctionId", auctionId);
		return (List<BidderCategory>) query.list();
	}
}
