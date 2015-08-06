package com.cfe.auction.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.cfe.auction.dao.IAuctionDao;
import com.cfe.auction.model.persist.Auction;

@Repository
public class AuctionDaoImpl extends DAOImpl<Integer, Auction> implements
		IAuctionDao {

	@SuppressWarnings("unchecked")
	@Override
	public Auction getActiveAuction() {
		Criteria criteria = getSessionFactory().getCurrentSession()
				.createCriteria(Auction.class);
		criteria = criteria.add(Restrictions.eq("status", "Running"));
		List<Auction> list = (List<Auction>) criteria.list();
		if( list != null && list.size() >= 1) {
			return list.get(0);
		}
		return null;
	}
}
