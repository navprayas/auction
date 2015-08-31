/*    */ package com.navprayas.bidding.engine.common;
/*    */ 
/*    */ public class User
/*    */ {
/*    */   private String firstName;
/*    */   private String middleName;
/*    */   private String lastName;
/*    */   private long userId;
/*    */   
/*    */   public User(long userId, String firstName, String lastName)
/*    */   {
/* 12 */     this.userId = userId;
/* 13 */     this.firstName = firstName;
/* 14 */     this.lastName = lastName;
/*    */   }
/*    */   
/*    */   public String getFirstName() {
/* 18 */     return this.firstName;
/*    */   }
/*    */   
/*    */   public String getLastName() {
/* 22 */     return this.lastName;
/*    */   }
/*    */   
/*    */   public long getUserId() {
/* 26 */     return this.userId;
/*    */   }
/*    */ }


/* Location:              /home/cfeindia/Desktop/bidding-engine.jar!/com/navprayas/bidding/engine/common/User.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */