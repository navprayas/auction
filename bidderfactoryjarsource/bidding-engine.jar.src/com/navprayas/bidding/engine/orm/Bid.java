/*    */ package com.navprayas.bidding.engine.orm;
/*    */ 
/*    */ import java.util.Date;
/*    */ 
/*    */ public class Bid {
/*    */   private Long id;
/*    */   private Integer version;
/*    */   private Long bidId;
/*    */   private Long bidItemId;
/*    */   private Long auctionId;
/*    */   private Double bidAmount;
/*    */   private String currency;
/*    */   private Date bidTime;
/*    */   private String bidStatus;
/*    */   private String bidderName;
/*    */   private String comments;
/*    */   
/*    */   public void setId(Long id) {
/* 19 */     this.id = id;
/*    */   }
/*    */   
/* 22 */   public Long getId() { return this.id; }
/*    */   
/*    */   public void setVersion(Integer version) {
/* 25 */     this.version = version;
/*    */   }
/*    */   
/* 28 */   public Integer getVersion() { return this.version; }
/*    */   
/*    */   public void setBidId(Long bidId) {
/* 31 */     this.bidId = bidId;
/*    */   }
/*    */   
/* 34 */   public Long getBidId() { return this.bidId; }
/*    */   
/*    */   public void setBidItemId(Long bidItemId) {
/* 37 */     this.bidItemId = bidItemId;
/*    */   }
/*    */   
/* 40 */   public Long getBidItemId() { return this.bidItemId; }
/*    */   
/*    */   public void setAuctionId(Long auctionId) {
/* 43 */     this.auctionId = auctionId;
/*    */   }
/*    */   
/* 46 */   public Long getAuctionId() { return this.auctionId; }
/*    */   
/*    */   public void setBidAmount(Double bidAmount) {
/* 49 */     this.bidAmount = bidAmount;
/*    */   }
/*    */   
/* 52 */   public Double getBidAmount() { return this.bidAmount; }
/*    */   
/*    */   public void setCurrency(String currency) {
/* 55 */     this.currency = currency;
/*    */   }
/*    */   
/* 58 */   public String getCurrency() { return this.currency; }
/*    */   
/*    */   public void setBidTime(Date bidTime) {
/* 61 */     this.bidTime = bidTime;
/*    */   }
/*    */   
/* 64 */   public Date getBidTime() { return this.bidTime; }
/*    */   
/*    */   public void setBidStatus(String bidStatus) {
/* 67 */     this.bidStatus = bidStatus;
/*    */   }
/*    */   
/* 70 */   public String getBidStatus() { return this.bidStatus; }
/*    */   
/*    */   public void setBidderName(String bidderName) {
/* 73 */     this.bidderName = bidderName;
/*    */   }
/*    */   
/* 76 */   public String getBidderName() { return this.bidderName; }
/*    */   
/*    */   public void setComments(String comments) {
/* 79 */     this.comments = comments;
/*    */   }
/*    */   
/* 82 */   public String getComments() { return this.comments; }
/*    */ }


/* Location:              /home/cfeindia/Desktop/bidding-engine.jar!/com/navprayas/bidding/engine/orm/Bid.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */