package com.cfe.auction.dao;

import java.util.Date;
import java.util.List;

import com.cfe.auction.model.auction.persist.AuctionSearchBean;
import com.cfe.auction.model.persist.Auction;
import com.cfe.auction.model.persist.BidItem;
import com.cfe.auction.model.persist.BidSequence;
import com.cfe.auction.model.persist.ItemLot;

public interface IAuctionDao extends DAO<Integer, Auction> {

	Auction getActiveAuction(AuctionSearchBean auctionSearchBean);

	Auction getActiveAuction();

	void setActualAuctionStartTime(Integer auctionId,
			Date actualAuctionStartTime);

	List<Auction> getAuctionList(String schemaKey);

	void closeAuction(AuctionSearchBean auctionSearchBean);

	boolean isValidAuction(AuctionSearchBean auctionSearchBean);

	void saveAuctionDataDao(ItemLot itemLotEntity,
			BidItem bidItemEntity, BidSequence bidSequence,
			AuctionSearchBean auctionSearchBean);

}
