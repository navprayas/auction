package com.cfe.auction.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cfe.auction.dao.BidItemDao;
import com.cfe.auction.model.persist.BidItem;
import com.cfe.auction.service.BidItemService;

;

@Service
@Transactional
public class BidItemServiceImpl extends
		CRUDServiceImpl<Integer, BidItem, BidItemDao> implements BidItemService {
	@Autowired
	private BidItemDao bidItemDao;

	@Autowired
	public BidItemServiceImpl(BidItemDao dao) {
		super(dao);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<BidItem> getBidItems() {
		// TODO Auto-generated method stub
		return bidItemDao.getBidItems();
	}

}
