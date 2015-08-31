/*    */ package com.navprayas.bidding.engine.common;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.Observable;
/*    */ 
/*    */ public class BidPublisher extends Observable
/*    */ {
/*    */   private static BidPublisher _instance;
/* 10 */   private static Map<Long, Bid> bids = new HashMap();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static synchronized BidPublisher getInstance()
/*    */   {
/* 17 */     if (_instance == null) {
/* 18 */       _instance = new BidPublisher();
/*    */     }
/* 20 */     return _instance;
/*    */   }
/*    */   
/*    */   public Map<Long, Bid> getBids() {
/* 24 */     return bids;
/*    */   }
/*    */   
/*    */   public void notifyBid(Bid bid) {
/* 28 */     bids.put(Long.valueOf(bid.bidItemId), bid);
/* 29 */     setChanged();
/* 30 */     notifyObservers();
/*    */   }
/*    */ }


/* Location:              /home/cfeindia/Desktop/bidding-engine.jar!/com/navprayas/bidding/engine/common/BidPublisher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */