package com.cfe.auction.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.cfe.auction.model.auction.persist.AuctionSearchBean;
import com.cfe.auction.model.persist.Auction;
import com.cfe.auction.model.persist.BidItem;

/**
 * 
 * @author Vikas Anand
 * 
 */
public interface IAuctionService extends CRUDService<Integer, Auction> {

	List<BidItem> getActiveAuctionBidItem(AuctionSearchBean auctionSearchBean);

	Auction getActiveAuction(AuctionSearchBean auctionSearchBean);

	Auction getActiveAuction();

	void setActualAuctionStartTime(Integer auctionId,
			Date actualAuctionStartTime);

	List<Auction> getAuctionList(String schemaKey);

	void closeAuction(AuctionSearchBean auctionSearchBean);

	boolean isValidAuction(AuctionSearchBean auctionSearchBean);

	void saveAuctionData(MultipartFile file,AuctionSearchBean auctionSearchBean) throws IOException;

}
