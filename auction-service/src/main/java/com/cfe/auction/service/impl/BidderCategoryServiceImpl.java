package com.cfe.auction.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cfe.auction.dao.BidderCategoryDao;
import com.cfe.auction.model.persist.BidderCategory;
import com.cfe.auction.service.BidderCategoryService;

@Service
@Transactional
public class BidderCategoryServiceImpl implements BidderCategoryService {
	@Autowired
	private BidderCategoryDao bidderCategoryDao;

	@Override
	public List<BidderCategory> getBidderCategory(Integer userId, Integer auctionId) {
		// TODO Auto-generated method stub
		return bidderCategoryDao.getBidderCategory(userId, auctionId);
	}

}
