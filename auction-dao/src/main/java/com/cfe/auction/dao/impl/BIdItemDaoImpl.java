package com.cfe.auction.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.cfe.auction.dao.BidItemDao;
import com.cfe.auction.model.persist.BidItem;

@Repository
public class BIdItemDaoImpl extends DAOImpl<Integer, BidItem> implements
		BidItemDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<BidItem> getBidItems(Long bidItemGroupId) {
		Criteria criteria = getSessionFactory().getCurrentSession()
				.createCriteria(BidItem.class);
		criteria = criteria.add(Restrictions.eq("bidItemGroupId", bidItemGroupId));
		return (List<BidItem>) criteria.list();
	}
}
