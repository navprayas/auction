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
						"from  BidderCategory bc where bc.auction.id=:auctionId and bc.bidderCategoryId.user.id=:userId ");
		query.setInteger("auctionId", auctionId);
		query.setInteger("userId", userId);
		return (List<BidderCategory>) query.list();

	}

}
