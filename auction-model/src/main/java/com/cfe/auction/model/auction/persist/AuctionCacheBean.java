package com.cfe.auction.model.auction.persist;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.cfe.auction.model.persist.BidItem;

/**
 * 
 * @author Vikas Anand
 *
 */
public class AuctionCacheBean {

	private Integer auctionId;
	private Integer clientId;
	private String schemaName;
	private Date auctionStartTime;
	private Long activeBidItemId;
	private Long bidItemGroupId;
	private boolean isAuctionClosed = false;
	
	
	private List<BidItem> bidItems;
	
	private Map<Long, BidItem> bidItemsMap;
	
	public Integer getAuctionId() {
		return auctionId;
	}
	public void setAuctionId(Integer auctionId) {
		this.auctionId = auctionId;
	}
	public Integer getClientId() {
		return clientId;
	}
	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}
	public String getSchemaName() {
		return schemaName;
	}
	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}
	public List<BidItem> getBidItems() {
		return bidItems;
	}
	public void setBidItems(List<BidItem> bidItems) {
		this.bidItems = bidItems;
	}
	public Map<Long, BidItem> getBidItemsMap() {
		return bidItemsMap;
	}
	public void setBidItemsMap(Map<Long, BidItem> bidItemsMap) {
		this.bidItemsMap = bidItemsMap;
	}
	public Date getAuctionStartTime() {
		return auctionStartTime;
	}
	public void setAuctionStartTime(Date auctionStartTime) {
		this.auctionStartTime = auctionStartTime;
	}
	public Long getActiveBidItemId() {
		return activeBidItemId;
	}
	public void setActiveBidItemId(Long activeBidItemId) {
		this.activeBidItemId = activeBidItemId;
	}
	public Long getBidItemGroupId() {
		return bidItemGroupId;
	}
	public void setBidItemGroupId(Long bidItemGroupId) {
		this.bidItemGroupId = bidItemGroupId;
	}
	public boolean isAuctionClosed() {
		return isAuctionClosed;
	}
	public void setAuctionClosed(boolean isAuctionClosed) {
		this.isAuctionClosed = isAuctionClosed;
	}
}
