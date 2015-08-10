package com.cfe.auction.dao.impl;

import org.springframework.stereotype.Service;

import com.cfe.auction.dao.AutoBidDao;
import com.cfe.auction.model.persist.AutoBids;
@Service
public class AutoBidDaoImpl extends DAOImpl<Integer, AutoBids> implements
		AutoBidDao {

}
