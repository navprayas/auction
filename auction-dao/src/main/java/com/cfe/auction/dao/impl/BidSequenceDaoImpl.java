package com.cfe.auction.dao.impl;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.cfe.auction.dao.IBidSequenceDao;
import com.cfe.auction.model.persist.BidSequence;

/**
 * 
 * @author Vikas Anand
 *
 */
@Repository
public class BidSequenceDaoImpl extends DAOImpl<Integer, BidSequence> implements IBidSequenceDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<BidSequence> getBidSequenceList(Integer auctionId) {
		Query query = getEntityManager().createQuery(" from BidSequence  where auction.auctionId = :auctionId");
		query.setParameter("auctionId", auctionId);
		
		List<BidSequence> list = (List<BidSequence>) query.getResultList();
		return list;
	}
	
}
