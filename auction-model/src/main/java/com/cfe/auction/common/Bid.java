package com.cfe.auction.common;

import java.util.Date;

public class Bid {
	private Long id;
	private Integer version;
	private Long bidId;
	private Long bidItemId;
	private Long auctionId;
	private Double bidAmount;
	private String currency;
	private Date bidTime;
	private String bidStatus;
	private String bidderName;
	private String comments;
	private int bidType;
	private String status;
	private Long bidItemGroupId;
	private String bidItemName;
	private String auctionName;
	private String categoryName;
	private String unit;
	private int totalQuantity;
	private String zone;
	private String city;
	private String location;
	private Double salesPrice;
	private Double totalSalesPrice;

	public int getBidType() {
		return bidType;
	}

	public void setBidType(int bidType) {
		this.bidType = bidType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Integer getVersion() {
		return version;
	}

	public void setBidId(Long bidId) {
		this.bidId = bidId;
	}

	public Long getBidId() {
		return bidId;
	}

	public void setBidItemId(Long bidItemId) {
		this.bidItemId = bidItemId;
	}

	public Long getBidItemId() {
		return bidItemId;
	}

	public void setAuctionId(Long auctionId) {
		this.auctionId = auctionId;
	}

	public Long getAuctionId() {
		return auctionId;
	}

	public void setBidAmount(Double bidAmount) {
		this.bidAmount = bidAmount;
	}

	public Double getBidAmount() {
		return bidAmount;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getCurrency() {
		return currency;
	}

	public void setBidTime(Date bidTime) {
		this.bidTime = bidTime;
	}

	public Date getBidTime() {
		return bidTime;
	}

	public void setBidStatus(String bidStatus) {
		this.bidStatus = bidStatus;
	}

	public String getBidStatus() {
		return bidStatus;
	}

	public void setBidderName(String bidderName) {
		this.bidderName = bidderName;
	}

	public String getBidderName() {
		return bidderName;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getComments() {
		return comments;
	}

	public Long getBidItemGroupId() {
		return bidItemGroupId;
	}

	public void setBidItemGroupId(Long bidItemGroupId) {
		this.bidItemGroupId = bidItemGroupId;
	}

	public String getBidItemName() {
		return bidItemName;
	}

	public void setBidItemName(String bidItemName) {
		this.bidItemName = bidItemName;
	}

	public String getAuctionName() {
		return auctionName;
	}

	public void setAuctionName(String auctionName) {
		this.auctionName = auctionName;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public int getTotalQuantity() {
		return totalQuantity;
	}

	public void setTotalQuantity(int totalQuantity) {
		this.totalQuantity = totalQuantity;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Double getSalesPrice() {
		return salesPrice;
	}

	public void setSalesPrice(Double salesPrice) {
		this.salesPrice = salesPrice;
	}

	public Double getTotalSalesPrice() {
		return totalSalesPrice;
	}

	public void setTotalSalesPrice(Double totalSalesPrice) {
		this.totalSalesPrice = totalSalesPrice;
	}

}
