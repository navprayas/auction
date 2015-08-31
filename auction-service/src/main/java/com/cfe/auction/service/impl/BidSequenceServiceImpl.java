package com.cfe.auction.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cfe.auction.dao.IBidSequenceDao;
import com.cfe.auction.model.auction.persist.AuctionSearchBean;
import com.cfe.auction.model.persist.BidSequence;
import com.cfe.auction.service.IBidSequenceService;

/**
 * 
 * @author Vikas Anand
 *
 */
@Service
public class BidSequenceServiceImpl implements IBidSequenceService {

	@Autowired
	IBidSequenceDao bidSequenceDao;
	
	@Transactional
	@Override
	public List<BidSequence> getBidSequenceList(AuctionSearchBean auctionSearchBean) {
		return bidSequenceDao.getBidSequenceList(auctionSearchBean);
	}
	
}
