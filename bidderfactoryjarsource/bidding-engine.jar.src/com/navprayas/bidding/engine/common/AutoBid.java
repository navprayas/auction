/*     */ package com.navprayas.bidding.engine.common;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Date;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AutoBid
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1121152295249282551L;
/*     */   public long Id;
/*     */   public int version;
/*     */   public long bidId;
/*     */   public long bidItemId;
/*     */   public long auctionId;
/*     */   public double bidAmount;
/*     */   public String currency;
/*  19 */   public Date bidTime = new Date();
/*     */   public String status;
/*     */   public long bidderId;
/*     */   public String bidderName;
/*     */   public String comments;
/*     */   public int bidType;
/*     */   
/*     */   public AutoBid() {}
/*     */   
/*     */   public AutoBid(long bidderId) {
/*  29 */     this.bidderId = bidderId;
/*     */   }
/*     */   
/*     */   public AutoBid(String bidderName) {
/*  33 */     this.bidderName = bidderName;
/*     */   }
/*     */   
/*     */   public AutoBid(String bidderName, long auctionId, long bidItemId, double bidAmount, String currency, String status, int bidType, String comments, double autoBidAmount) {
/*  37 */     this.bidderName = bidderName;
/*  38 */     this.auctionId = auctionId;
/*  39 */     this.bidItemId = bidItemId;
/*  40 */     this.bidAmount = bidAmount;
/*  41 */     this.currency = currency;
/*  42 */     this.status = status;
/*  43 */     this.bidType = bidType;
/*  44 */     this.comments = comments;
/*     */   }
/*     */   
/*     */   public AutoBid(long bidderId, long auctionId, long bidItemId, double bidAmount, String currency, String status, int bidType, String comments, double autoBidAmount) {
/*  48 */     this.bidderId = bidderId;
/*  49 */     this.auctionId = auctionId;
/*  50 */     this.bidItemId = bidItemId;
/*  51 */     this.bidAmount = bidAmount;
/*  52 */     this.currency = currency;
/*  53 */     this.status = status;
/*  54 */     this.bidType = bidType;
/*  55 */     this.comments = comments;
/*     */   }
/*     */   
/*     */   public void setVersion(int version) {
/*  59 */     this.version = version;
/*     */   }
/*     */   
/*     */   public int getVersion() {
/*  63 */     return this.version;
/*     */   }
/*     */   
/*     */   public void setBidId(long bidId) {
/*  67 */     this.bidId = bidId;
/*     */   }
/*     */   
/*     */   public long getBidId() {
/*  71 */     return this.bidId;
/*     */   }
/*     */   
/*     */   public void setAuctionId(long auctionId) {
/*  75 */     this.auctionId = auctionId;
/*     */   }
/*     */   
/*     */   public long getAuctionId() {
/*  79 */     return this.auctionId;
/*     */   }
/*     */   
/*     */   public long getBidderId() {
/*  83 */     return this.bidderId;
/*     */   }
/*     */   
/*     */   public void setBidderId(long bidderId) {
/*  87 */     this.bidderId = bidderId;
/*     */   }
/*     */   
/*     */   public long getBidItemId() {
/*  91 */     return this.bidItemId;
/*     */   }
/*     */   
/*     */   public void setBidItemId(long bidItemId) {
/*  95 */     this.bidItemId = bidItemId;
/*     */   }
/*     */   
/*     */   public double getBidAmount() {
/*  99 */     return this.bidAmount;
/*     */   }
/*     */   
/*     */   public void setBidAmount(double bidAmount) {
/* 103 */     this.bidAmount = bidAmount;
/*     */   }
/*     */   
/*     */   public String getCurrency() {
/* 107 */     return this.currency;
/*     */   }
/*     */   
/*     */   public void setCurrency(String currency) {
/* 111 */     this.currency = currency;
/*     */   }
/*     */   
/*     */   public String getBidderName() {
/* 115 */     return this.bidderName;
/*     */   }
/*     */   
/*     */   public void setBidderName(String bidderName) {
/* 119 */     this.bidderName = bidderName;
/*     */   }
/*     */   
/*     */   public String getStatus() {
/* 123 */     return this.status;
/*     */   }
/*     */   
/*     */   public void setStatus(String status) {
/* 127 */     this.status = status;
/*     */   }
/*     */   
/*     */   public Date getBidTime() {
/* 131 */     return this.bidTime;
/*     */   }
/*     */   
/*     */   public void setBidType(int bidType) {
/* 135 */     this.bidType = bidType;
/*     */   }
/*     */   
/*     */   public int getBidType() {
/* 139 */     return this.bidType;
/*     */   }
/*     */   
/*     */   public void setComments(String comments) {
/* 143 */     this.comments = comments;
/*     */   }
/*     */   
/*     */   public String getComments() {
/* 147 */     return this.comments;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 151 */     return 
/*     */     
/* 153 */       "version$" + this.version + ", bidId$" + this.bidId + ", bidItemId:" + this.bidItemId + ", auctionId:" + this.auctionId + ", bidAmount$" + this.bidAmount + ", currency$" + this.currency + ", bidTime$" + this.bidTime + ", status$" + this.status + ", bidderId$" + this.bidderId + ", bidderName$" + this.bidderName + ", bidType$" + this.bidType + ", comments$" + this.comments;
/*     */   }
/*     */   
/*     */   public long getId() {
/* 157 */     return this.Id;
/*     */   }
/*     */   
/*     */   public void setId(long id) {
/* 161 */     this.Id = id;
/*     */   }
/*     */   
/*     */   public void setBidTime(Date bidTime) {
/* 165 */     this.bidTime = bidTime;
/*     */   }
/*     */ }


/* Location:              /home/cfeindia/Desktop/bidding-engine.jar!/com/navprayas/bidding/engine/common/AutoBid.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */