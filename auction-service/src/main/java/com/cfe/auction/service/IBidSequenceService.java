package com.cfe.auction.service;

import java.util.List;

import com.cfe.auction.model.persist.BidSequence;

/**
 * 
 * @author Vikas Anand
 *
 */
public interface IBidSequenceService {

	List<BidSequence> getBidSequenceList();

}
