/*    */ package kilim.http;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import kilim.Scheduler;
/*    */ import kilim.nio.NioSelectorScheduler;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class HttpServer
/*    */ {
/*    */   public NioSelectorScheduler nio;
/*    */   
/*    */   public HttpServer() {}
/*    */   
/*    */   public HttpServer(int port, Class<? extends HttpSession> httpSessionClass)
/*    */     throws IOException
/*    */   {
/* 32 */     this.nio = new NioSelectorScheduler();
/* 33 */     listen(port, httpSessionClass, Scheduler.getDefaultScheduler());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void listen(int port, Class<? extends HttpSession> httpSessionClass, Scheduler httpSessionScheduler)
/*    */     throws IOException
/*    */   {
/* 47 */     this.nio.listen(port, httpSessionClass, httpSessionScheduler);
/*    */   }
/*    */ }


/* Location:              /home/cfeindia/Desktop/kilim.jar!/kilim/http/HttpServer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */