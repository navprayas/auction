/*    */ package kilim;
/*    */ 
/*    */ 
/*    */ public class TaskDoneReason
/*    */   implements PauseReason
/*    */ {
/*    */   Object exitObj;
/*    */   
/*    */   TaskDoneReason(Object o)
/*    */   {
/* 11 */     this.exitObj = o;
/*    */   }
/*    */   
/*    */   public boolean isValid(Task t) {
/* 15 */     return true;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 19 */     return "Done. Exit msg = " + this.exitObj;
/*    */   }
/*    */ }


/* Location:              /home/cfeindia/Desktop/kilim.jar!/kilim/TaskDoneReason.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */