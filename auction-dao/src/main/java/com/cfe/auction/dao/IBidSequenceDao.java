package com.cfe.auction.dao;

import java.util.List;

import com.cfe.auction.model.persist.BidSequence;

/**
 * 
 * @author Vikas Anand
 *
 */
public interface IBidSequenceDao extends DAO<Integer, BidSequence> {

	List<BidSequence> getBidSequenceList(Integer auctionId);

}
