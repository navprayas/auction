/*    */ package kilim.nio;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import kilim.Scheduler;
/*    */ import kilim.Task;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SessionTask
/*    */   extends Task
/*    */ {
/*    */   public EndPoint endpoint;
/*    */   public Scheduler preferredScheduler;
/*    */   
/*    */   public EndPoint getEndPoint()
/*    */   {
/* 19 */     return this.endpoint;
/*    */   }
/*    */   
/*    */   public void setEndPoint(EndPoint ep) throws IOException {
/* 23 */     this.endpoint = ep;
/*    */   }
/*    */   
/*    */   public void close() {
/* 27 */     if (this.endpoint != null) {
/* 28 */       this.endpoint.close();
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/cfeindia/Desktop/kilim.jar!/kilim/nio/SessionTask.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */