/*   */ package com.navprayas.bidding.engine.core;
/*   */ 
/*   */ import com.navprayas.bidding.engine.intf.Bidder;
/*   */ 
/*   */ public class BidderFactory
/*   */ {
/*   */   public static Bidder create() {
/* 8 */     return new BidderImpl(AuctioneerFactory.create().getMailBox());
/*   */   }
/*   */ }


/* Location:              /home/cfeindia/Desktop/bidding-engine.jar!/com/navprayas/bidding/engine/core/BidderFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */