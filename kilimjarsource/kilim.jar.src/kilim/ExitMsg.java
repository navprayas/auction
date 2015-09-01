/*    */ package kilim;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ExitMsg
/*    */ {
/*    */   public final Task task;
/*    */   
/*    */ 
/*    */   public final Object result;
/*    */   
/*    */ 
/*    */ 
/*    */   public ExitMsg(Task t, Object res)
/*    */   {
/* 17 */     this.task = t;
/* 18 */     this.result = res;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 22 */     return "exit(" + this.task.id + "), result = " + this.result;
/*    */   }
/*    */   
/*    */   public int hashCode()
/*    */   {
/* 27 */     return this.task.id;
/*    */   }
/*    */   
/*    */   public boolean equals(Object obj) {
/* 31 */     return this == obj;
/*    */   }
/*    */ }


/* Location:              /home/cfeindia/Desktop/kilim.jar!/kilim/ExitMsg.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */