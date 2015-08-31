/*    */ package com.navprayas.bidding.engine.core;
/*    */ 
/*    */ import com.navprayas.bidding.engine.common.Bid;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.Observable;
/*    */ 
/*    */ public class BidPublisher
/*    */   extends Observable
/*    */ {
/*    */   private static BidPublisher _instance;
/* 12 */   private static Map<Long, Bid> bids = new HashMap();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static synchronized BidPublisher getInstance()
/*    */   {
/* 19 */     if (_instance == null) {
/* 20 */       _instance = new BidPublisher();
/*    */     }
/* 22 */     return _instance;
/*    */   }
/*    */   
/*    */   public Map<Long, Bid> getBids() {
/* 26 */     return bids;
/*    */   }
/*    */   
/*    */   public void notifyBid(Bid bid) {
/* 30 */     bids.put(Long.valueOf(bid.bidItemId), bid);
/* 31 */     setChanged();
/* 32 */     notifyObservers(Long.valueOf(bid.bidItemId));
/*    */   }
/*    */ }


/* Location:              /home/cfeindia/Desktop/bidding-engine.jar!/com/navprayas/bidding/engine/core/BidPublisher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */