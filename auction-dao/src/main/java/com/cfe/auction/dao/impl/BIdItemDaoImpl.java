package com.cfe.auction.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.cfe.auction.dao.BidItemDao;
import com.cfe.auction.model.persist.AutoBids;
import com.cfe.auction.model.persist.BidItem;
import com.cfe.auction.model.persist.BidSequence;
import com.cfe.auction.model.persist.BidderCategory;

@Repository
public class BIdItemDaoImpl extends DAOImpl<Integer, BidItem> implements
		BidItemDao {
	private Logger logger = org.slf4j.LoggerFactory
			.getLogger(BIdItemDaoImpl.class);

	@SuppressWarnings("unchecked")
	@Override
	public List<BidItem> getBidItems(Long bidItemGroupId) {
		Criteria criteria = getSessionFactory().getCurrentSession()
				.createCriteria(BidItem.class);
		criteria = criteria.add(Restrictions.eq("bidItemGroupId", bidItemGroupId));
		return (List<BidItem>) criteria.list();
	}
}
