package com.cfe.auction.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cfe.auction.dao.IBidSequenceDao;
import com.cfe.auction.model.persist.BidSequence;

/**
 * 
 * @author Vikas Anand
 *
 */
@Repository
public class BidSequenceDaoImpl implements IBidSequenceDao {

	@Autowired
	SessionFactory sessionFactory;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<BidSequence> getBidSequenceList(Integer auctionId) {
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(BidSequence.class);
		criteria = criteria.add(Restrictions.eq("auction.auctionId", auctionId));
		List<BidSequence> list = (List<BidSequence>) criteria.list();
		return list;
	}
	
}
