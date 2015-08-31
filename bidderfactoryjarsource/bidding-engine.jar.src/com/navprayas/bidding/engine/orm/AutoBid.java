/*    */ package com.navprayas.bidding.engine.orm;
/*    */ 
/*    */ import java.util.Date;
/*    */ 
/*    */ public class AutoBid {
/*    */   private Long id;
/*    */   private Integer version;
/*    */   private Long bidId;
/*    */   private Long auctionId;
/*    */   private Long bidItemId;
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
/*    */   public void setAuctionId(Long auctionId) {
/* 31 */     this.auctionId = auctionId;
/*    */   }
/*    */   
/* 34 */   public Long getAuctionId() { return this.auctionId; }
/*    */   
/*    */   public void setBidAmount(Double bidAmount) {
/* 37 */     this.bidAmount = bidAmount;
/*    */   }
/*    */   
/* 40 */   public Double getBidAmount() { return this.bidAmount; }
/*    */   
/*    */   public void setBidItemId(Long bidItemId) {
/* 43 */     this.bidItemId = bidItemId;
/*    */   }
/*    */   
/* 46 */   public Long getBidItemId() { return this.bidItemId; }
/*    */   
/*    */   public void setCurrency(String currency) {
/* 49 */     this.currency = currency;
/*    */   }
/*    */   
/* 52 */   public String getCurrency() { return this.currency; }
/*    */   
/*    */   public void setBidTime(Date bidTime) {
/* 55 */     this.bidTime = bidTime;
/*    */   }
/*    */   
/* 58 */   public Date getBidTime() { return this.bidTime; }
/*    */   
/*    */   public void setBidStatus(String bidStatus) {
/* 61 */     this.bidStatus = bidStatus;
/*    */   }
/*    */   
/* 64 */   public String getBidStatus() { return this.bidStatus; }
/*    */   
/*    */   public void setBidderName(String bidderName) {
/* 67 */     this.bidderName = bidderName;
/*    */   }
/*    */   
/* 70 */   public String getBidderName() { return this.bidderName; }
/*    */   
/*    */   public void setComments(String comments) {
/* 73 */     this.comments = comments;
/*    */   }
/*    */   
/* 76 */   public String getComments() { return this.comments; }
/*    */   
/*    */   public void setBidId(Long bidId) {
/* 79 */     this.bidId = bidId;
/*    */   }
/*    */   
/* 82 */   public Long getBidId() { return this.bidId; }
/*    */ }


/* Location:              /home/cfeindia/Desktop/bidding-engine.jar!/com/navprayas/bidding/engine/orm/AutoBid.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */