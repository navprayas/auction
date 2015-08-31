/*    */ package com.navprayas.bidding.engine.common;
/*    */ 
/*    */ import java.util.Date;
/*    */ 
/*    */ public class BidItemUpdate
/*    */ {
/*    */   public long Id;
/*    */   private long bidItemId;
/*    */   private String statusCode;
/*    */   private Date bidStartTime;
/*    */   private Date bidEndTime;
/*    */   private Date lastUpdateTime;
/*    */   
/*    */   public BidItemUpdate(long bidItemId) {
/* 15 */     this.bidItemId = bidItemId;
/*    */   }
/*    */   
/*    */   public void setBidItemId(long bidItemId) {
/* 19 */     this.bidItemId = bidItemId;
/*    */   }
/*    */   
/* 22 */   public long getBidItemId() { return this.bidItemId; }
/*    */   
/*    */   public void setStatusCode(String statusCode)
/*    */   {
/* 26 */     this.statusCode = statusCode;
/*    */   }
/*    */   
/* 29 */   public String getStatusCode() { return this.statusCode; }
/*    */   
/*    */   public void setBidStartTime(Date bidStartTime)
/*    */   {
/* 33 */     this.bidStartTime = bidStartTime;
/*    */   }
/*    */   
/* 36 */   public Date getBidStartTime() { return this.bidStartTime; }
/*    */   
/*    */   public void setBidEndTime(Date bidEndTime)
/*    */   {
/* 40 */     this.bidEndTime = bidEndTime;
/*    */   }
/*    */   
/* 43 */   public Date getBidEndTime() { return this.bidEndTime; }
/*    */   
/*    */   public void setLastUpdateTime(Date lastUpdateTime)
/*    */   {
/* 47 */     this.lastUpdateTime = lastUpdateTime;
/*    */   }
/*    */   
/*    */   public Date getLastUpdateTime() {
/* 51 */     return this.lastUpdateTime;
/*    */   }
/*    */   
/*    */   public long getId() {
/* 55 */     return this.Id;
/*    */   }
/*    */   
/*    */   public void setId(long id) {
/* 59 */     this.Id = id;
/*    */   }
/*    */ }


/* Location:              /home/cfeindia/Desktop/bidding-engine.jar!/com/navprayas/bidding/engine/common/BidItemUpdate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */