package com.cfe.auction.model.auction.persist;
/**
 * 
 * @author Vikas Anand
 *
 */
public class AuctionSearchBean {

	public AuctionSearchBean(final String schemaName) {
		this.schemaName = schemaName;
	}
	
	public AuctionSearchBean () {
		
	}
	private String schemaName;
	private Integer auctionId;
	private Long genericId;
	
	public String getSchemaName() {
		return schemaName;
	}

	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}

	public Integer getAuctionId() {
		return auctionId;
	}

	public void setAuctionId(Integer auctionId) {
		this.auctionId = auctionId;
	}

	public Long getGenericId() {
		return genericId;
	}

	public void setGenericId(Long genericId) {
		this.genericId = genericId;
	}
}
