/*    */ package kilim;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class YieldReason
/*    */   implements PauseReason
/*    */ {
/*    */   public boolean isValid(Task t)
/*    */   {
/* 12 */     return false;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 16 */     return "yield";
/*    */   }
/*    */ }


/* Location:              /home/cfeindia/Desktop/kilim.jar!/kilim/YieldReason.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */