package com.cfe.auction.model.auction.persist;

import java.util.List;

/**
 * 
 * @author Vikas Anand
 *
 */
public class AuctionCacheBean {

	private Integer auctionId;
	private Integer clientId;
	private String schemaName;
	private List<Long> bidItemId;
	
	
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
	public List<Long> getBidItemId() {
		return bidItemId;
	}
	public void setBidItemId(List<Long> bidItemId) {
		this.bidItemId = bidItemId;
	}
	public String getSchemaName() {
		return schemaName;
	}
	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}
}
