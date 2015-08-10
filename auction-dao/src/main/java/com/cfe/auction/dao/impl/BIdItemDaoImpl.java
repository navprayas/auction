package com.cfe.auction.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.cfe.auction.dao.BidItemDao;
import com.cfe.auction.model.persist.BidItem;

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
		return (List<BidItem>) criteria.list();
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
}
