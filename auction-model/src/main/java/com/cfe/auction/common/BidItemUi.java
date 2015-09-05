package com.cfe.auction.common;

import java.util.Date;
import java.util.List;

import com.cfe.auction.model.persist.ItemLot;

public class BidItemUi {

	private Long bidItemId;

	private String serialNo;

	private String name;

	private String location;

	private String city;

	private String zone;

	private Double minBidPrice;

	private Double minBidIncrement;

	private Date bidStartTime;

	private Date bidEndTime;

	private Integer initialStartTime;

	private Integer timeExtAfterBid;

	private String statusCode;

	private Date lastUpDateTime;

	private Date createdTime;

	private Double currentMarketPrice;

	private String currency;

	private String isProcessed;
	private Long bidItemGroupId;

	private Long timeleft;

	private List<ItemLot> itemLots;
	private String categoryName;
	
	
	private String unit;

	
	private boolean autoBidFlag;

	
	private Double amountAutoBid;

	
	private Integer totalQuantity;

	public Long getBidItemId() {
		return bidItemId;
	}

	public void setBidItemId(Long bidItemId) {
		this.bidItemId = bidItemId;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public Double getMinBidPrice() {
		return minBidPrice;
	}

	public void setMinBidPrice(Double minBidPrice) {
		this.minBidPrice = minBidPrice;
	}

	public Double getMinBidIncrement() {
		return minBidIncrement;
	}

	public void setMinBidIncrement(Double minBidIncrement) {
		this.minBidIncrement = minBidIncrement;
	}

	public Date getBidStartTime() {
		return bidStartTime;
	}

	public void setBidStartTime(Date bidStartTime) {
		this.bidStartTime = bidStartTime;
	}

	public Date getBidEndTime() {
		return bidEndTime;
	}

	public void setBidEndTime(Date bidEndTime) {
		this.bidEndTime = bidEndTime;
	}

	public Integer getInitialStartTime() {
		return initialStartTime;
	}

	public void setInitialStartTime(Integer initialStartTime) {
		this.initialStartTime = initialStartTime;
	}

	public Integer getTimeExtAfterBid() {
		return timeExtAfterBid;
	}

	public void setTimeExtAfterBid(Integer timeExtAfterBid) {
		this.timeExtAfterBid = timeExtAfterBid;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public Date getLastUpDateTime() {
		return lastUpDateTime;
	}

	public void setLastUpDateTime(Date lastUpDateTime) {
		this.lastUpDateTime = lastUpDateTime;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Double getCurrentMarketPrice() {
		return currentMarketPrice;
	}

	public void setCurrentMarketPrice(Double currentMarketPrice) {
		this.currentMarketPrice = currentMarketPrice;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getIsProcessed() {
		return isProcessed;
	}

	public void setIsProcessed(String isProcessed) {
		this.isProcessed = isProcessed;
	}

	public Long getBidItemGroupId() {
		return bidItemGroupId;
	}

	public void setBidItemGroupId(Long bidItemGroupId) {
		this.bidItemGroupId = bidItemGroupId;
	}

	public Long getTimeleft() {
		return timeleft;
	}

	public void setTimeleft(Long timeleft) {
		this.timeleft = timeleft;
	}

	public List<ItemLot> getItemLots() {
		return itemLots;
	}

	public void setItemLots(List<ItemLot> itemLots) {
		this.itemLots = itemLots;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public Integer getTotalQuantity() {
		return totalQuantity;
	}

	public void setTotalQuantity(Integer totalQuantity) {
		this.totalQuantity = totalQuantity;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public boolean isAutoBidFlag() {
		return autoBidFlag;
	}

	public void setAutoBidFlag(boolean autoBidFlag) {
		this.autoBidFlag = autoBidFlag;
	}

	public Double getAmountAutoBid() {
		return amountAutoBid;
	}

	public void setAmountAutoBid(Double amountAutoBid) {
		this.amountAutoBid = amountAutoBid;
	}

}
