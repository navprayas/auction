/*    */ package com.navprayas.bidding.engine.except;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BiddingException
/*    */   extends Exception
/*    */ {
/*    */   public static final long serialVersionUID = 653235685664321L;
/*    */   
/*    */   private int code;
/*    */   
/*    */ 
/*    */   public BiddingException(String message)
/*    */   {
/* 15 */     super(message);
/*    */   }
/*    */   
/*    */ 
/*    */   public BiddingException(String message, int code)
/*    */   {
/* 21 */     super(message);
/* 22 */     this.code = code;
/*    */   }
/*    */ }


/* Location:              /home/cfeindia/Desktop/bidding-engine.jar!/com/navprayas/bidding/engine/except/BiddingException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */