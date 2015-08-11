package com.cfe.auction.dao;

import java.util.List;

import com.cfe.auction.model.persist.AutoBids;

public interface AutoBidDao extends DAO<Integer, AutoBids> {
	
	public List<AutoBids> getAutoBids();

}
