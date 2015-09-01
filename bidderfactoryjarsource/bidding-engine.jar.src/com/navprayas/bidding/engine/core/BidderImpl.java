/*    */ package com.navprayas.bidding.engine.core;
/*    */ 
/*    */ import com.navprayas.bidding.engine.common.Bid;
/*    */ import kilim.Fiber;
/*    */ import kilim.Mailbox;
/*    */ import kilim.State;
/*    */ import kilim.Task;
/*    */ 
/*    */ public class BidderImpl extends Task implements com.navprayas.bidding.engine.intf.Bidder
/*    */ {
/*    */   private Mailbox<Bid> _mailbox;
/*    */   private Bid bid;
/*    */   public static final boolean $isWoven = true;
/*    */   
/*    */   public BidderImpl(Mailbox<Bid> _mailbox)
/*    */   {
/* 17 */     this._mailbox = _mailbox;
/*    */   }
/*    */   
/*    */   public void execute(Fiber paramFiber) throws kilim.Pausable, Exception
/*    */   {
/* 22 */     switch (paramFiber.pc) {default:  Fiber.wrongPC();
/*    */     case 1:  break; case 2:  break; case 0:  this._mailbox.putb(this.bid);
/*    */     }
/*    */     
/* 26 */     this._mailbox.put(this.bid, paramFiber.down()); State localState; switch (paramFiber.up()) {case 2:  localState = new State();localState.self = this;localState.pc = 1;paramFiber.setState(localState);return; case 3:  return;
/*    */     }
/* 28 */     Task.exit(Integer.valueOf(0), paramFiber.down()); switch (paramFiber.up()) {case 2:  localState = new State();localState.self = this;localState.pc = 2;paramFiber.setState(localState);return; case 3:   }
/*    */   }
/*    */   
/*    */   public void execute() throws kilim.Pausable, Exception
/*    */   {}
/*    */   
/* 34 */   public void call(Bid bid) throws com.navprayas.bidding.engine.except.BiddingException { 
	      try { this.bid = bid;
/* 35 */       start();
/*    */     }
/*    */     catch (Exception e) {
/* 38 */       throw new com.navprayas.bidding.engine.except.BiddingException("Failed to Bid!!!");
/*    */     }
/*    */   }
/*    */ }
/* Location:              /home/cfeindia/Desktop/bidding-engine.jar!/com/navprayas/bidding/engine/core/BidderImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */