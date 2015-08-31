/*     */ package com.navprayas.bidding.engine.common;
/*     */ 
/*     */ import java.util.Date;
/*     */ 
/*     */ public class BidItem
/*     */ {
/*     */   private long bidItemId;
/*     */   private String statusCode;
/*     */   private Date bidStartTime;
/*     */   private Date bidEndTime;
/*     */   private long auctionId;
/*     */   private double minBidPrice;
/*     */   private double minBidIncrement;
/*     */   private int timeExtAfterBid;
/*     */   private int marketType;
/*     */   private String currency;
/*     */   private Bid bid;
/*     */   private boolean autoBidFlag;
/*     */   private double autoBidAmount;
/*     */   private long autoBidderId;
/*     */   private String autoBidderName;
/*     */   private Date lastUpdateTime;
/*     */   
/*     */   public BidItem(long bidItemId) {
/*  25 */     this.bidItemId = bidItemId;
/*     */   }
/*     */   
/*     */   public long getBidItemId() {
/*  29 */     return this.bidItemId;
/*     */   }
/*     */   
/*     */   public void setBid(Bid bid) {
/*  33 */     this.bid = bid;
/*     */   }
/*     */   
/*     */   public Bid getBid() {
/*  37 */     return this.bid;
/*     */   }
/*     */   
/*     */   public void setStatusCode(String statusCode) {
/*  41 */     this.statusCode = statusCode;
/*     */   }
/*     */   
/*     */   public String getStatusCode() {
/*  45 */     return this.statusCode;
/*     */   }
/*     */   
/*     */   public void setBidStartTime(Date bidStartTime) {
/*  49 */     this.bidStartTime = bidStartTime;
/*     */   }
/*     */   
/*     */   public Date getBidStartTime() {
/*  53 */     return this.bidStartTime;
/*     */   }
/*     */   
/*     */   public void setBidEndTime(Date bidEndTime) {
/*  57 */     this.bidEndTime = bidEndTime;
/*     */   }
/*     */   
/*     */   public Date getBidEndTime() {
/*  61 */     return this.bidEndTime;
/*     */   }
/*     */   
/*     */   public void setAuctionId(long auctionId) {
/*  65 */     this.auctionId = auctionId;
/*     */   }
/*     */   
/*     */   public long getAuctionId() {
/*  69 */     return this.auctionId;
/*     */   }
/*     */   
/*     */   public void setMinBidPrice(double minBidPrice) {
/*  73 */     this.minBidPrice = minBidPrice;
/*     */   }
/*     */   
/*     */   public double getMinBidPrice() {
/*  77 */     return this.minBidPrice;
/*     */   }
/*     */   
/*     */   public void setMinBidIncrement(double minBidIncrement) {
/*  81 */     this.minBidIncrement = minBidIncrement;
/*     */   }
/*     */   
/*     */   public double getMinBidIncrement() {
/*  85 */     return this.minBidIncrement;
/*     */   }
/*     */   
/*     */   public void setTimeExtAfterBid(int timeExtAfterBid) {
/*  89 */     this.timeExtAfterBid = timeExtAfterBid;
/*     */   }
/*     */   
/*     */   public int getTimeExtAfterBid() {
/*  93 */     return this.timeExtAfterBid;
/*     */   }
/*     */   
/*     */   public void setMarketType(int marketType) {
/*  97 */     this.marketType = marketType;
/*     */   }
/*     */   
/*     */   public int getMarketType() {
/* 101 */     return this.marketType;
/*     */   }
/*     */   
/*     */   public void setCurrency(String currency) {
/* 105 */     this.currency = currency;
/*     */   }
/*     */   
/*     */   public String getCurrency() {
/* 109 */     return this.currency;
/*     */   }
/*     */   
/*     */   public void setAutoBidFlag(boolean autoBidFlag) {
/* 113 */     this.autoBidFlag = autoBidFlag;
/*     */   }
/*     */   
/*     */   public boolean isAutoBidFlag() {
/* 117 */     return this.autoBidFlag;
/*     */   }
/*     */   
/*     */   public void setAutoBidderId(long autoBidderId) {
/* 121 */     this.autoBidderId = autoBidderId;
/*     */   }
/*     */   
/*     */   public long getAutoBidderId() {
/* 125 */     return this.autoBidderId;
/*     */   }
/*     */   
/*     */   public void setAutoBidderName(String autoBidderName) {
/* 129 */     this.autoBidderName = autoBidderName;
/*     */   }
/*     */   
/*     */   public String getAutoBidderName() {
/* 133 */     return this.autoBidderName;
/*     */   }
/*     */   
/*     */   public void setAutoBidAmount(double autoBidAmount) {
/* 137 */     this.autoBidAmount = autoBidAmount;
/*     */   }
/*     */   
/*     */   public double getAutoBidAmount() {
/* 141 */     return this.autoBidAmount;
/*     */   }
/*     */   
/*     */   public void setLastUpdateTime(Date lastUpdateTime) {
/* 145 */     this.lastUpdateTime = lastUpdateTime;
/*     */   }
/*     */   
/*     */   public Date getLastUpdateTime() {
/* 149 */     return this.lastUpdateTime;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 154 */     return 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 162 */       "BidItem [bidItemId=" + this.bidItemId + ", statusCode=" + this.statusCode + ", bidStartTime=" + this.bidStartTime + ", bidEndTime=" + this.bidEndTime + ", auctionId=" + this.auctionId + ", minBidPrice=" + this.minBidPrice + ", minBidIncrement=" + this.minBidIncrement + ", timeExtAfterBid=" + this.timeExtAfterBid + ", marketType=" + this.marketType + ", currency=" + this.currency + ", bid=" + this.bid + ", autoBidFlag=" + this.autoBidFlag + ", autoBidAmount=" + this.autoBidAmount + ", autoBidderId=" + this.autoBidderId + ", autoBidderName=" + this.autoBidderName + "]";
/*     */   }
/*     */ }


/* Location:              /home/cfeindia/Desktop/bidding-engine.jar!/com/navprayas/bidding/engine/common/BidItem.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */