package com.cfe.auction.dao;

import java.util.List;

import com.cfe.auction.model.auction.persist.AuctionSearchBean;
import com.cfe.auction.model.persist.AutoBids;

public interface AutoBidDao extends DAO<Integer, AutoBids> {
	
	List<AutoBids> getAutoBids(AuctionSearchBean auctionSearchBean);

	AutoBids create(AutoBids autoBid, AuctionSearchBean auctionSearchBean);

}
