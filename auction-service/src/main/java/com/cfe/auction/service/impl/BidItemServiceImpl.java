package com.cfe.auction.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cfe.auction.dao.BidItemDao;
import com.cfe.auction.dao.model.persist.BidItem;

@Service
@Transactional
public class BidItemServiceImpl extends
		CRUDServiceImpl<Integer, BidItem, BidItemDao> {
	@Autowired
	public BidItemServiceImpl(BidItemDao dao) {
		super(dao);
		// TODO Auto-generated constructor stub
	}

}
