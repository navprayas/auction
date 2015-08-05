package com.cfe.auction.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.springframework.stereotype.Service;

import com.cfe.auction.dao.BidItemDao;
import com.cfe.auction.model.persist.BidItem;

@Service
public class BIdItemDaoImpl extends DAOImpl<Integer, BidItem> implements
		BidItemDao {

	@Override
	public List<BidItem> getBidItems() {
		Criteria criteria = getSessionFactory().getCurrentSession()
				.createCriteria(BidItem.class);

		return (List<BidItem>) criteria.list();
	}
}
