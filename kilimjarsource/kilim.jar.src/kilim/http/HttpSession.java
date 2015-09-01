/*    */ package kilim.http;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ import kilim.Fiber;
/*    */ import kilim.Pausable;
/*    */ import kilim.S_O;
/*    */ import kilim.State;
/*    */ import kilim.Task;
/*    */ import kilim.nio.SessionTask;
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
/*    */ public class HttpSession
/*    */   extends SessionTask
/*    */ {
/*    */   public HttpRequest readRequest(HttpRequest paramHttpRequest, Fiber paramFiber)
/*    */     throws IOException, Pausable
/*    */   {
/*    */     ;
/* 30 */     switch (paramFiber.pc) {default:  Fiber.wrongPC(); case 1:  break; case 0:  req.reuse(); }
/* 31 */     req.readFrom(this.endpoint, paramFiber.down()); S_O localS_O; switch (paramFiber.up()) {case 2:  localS_O = new S_O();localS_O.self = this;localS_O.pc = 1;localS_O.f0 = req;paramFiber.setState(localS_O);return null; case 3:  return null; case 1:  localS_O = (S_O)paramFiber.curState;req = (HttpRequest)localS_O.f0; }
/* 32 */     return req;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public HttpRequest readRequest(HttpRequest paramHttpRequest)
/*    */     throws IOException, Pausable
/*    */   {
/*    */     Task.errNotWoven();
/*    */     return null;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void sendResponse(HttpResponse paramHttpResponse, Fiber paramFiber)
/*    */     throws IOException, Pausable
/*    */   {
/*    */     ;
/*    */     
/* 50 */     switch (paramFiber.pc) {default:  Fiber.wrongPC(); case 1:  break; } resp.writeTo(this.endpoint, paramFiber.down()); S_O localS_O; switch (paramFiber.up()) {case 2:  localS_O = new S_O();localS_O.self = this;localS_O.pc = 1;localS_O.f0 = resp;paramFiber.setState(localS_O);return; case 3:  return; case 1:  localS_O = (S_O)paramFiber.curState;resp = (HttpResponse)localS_O.f0; }
/* 51 */     resp.reuse();
/*    */   }
/*    */   
/* 54 */   static byte[] pre = "<html><body><p>".getBytes();
/* 55 */   static byte[] post = "</body></html>".getBytes();
/*    */   public static final boolean $isWoven = true;
/*    */   
/*    */   public void sendResponse(HttpResponse paramHttpResponse)
/*    */     throws IOException, Pausable
/*    */   {}
/*    */   
/*    */   public void problem(HttpResponse arg1, byte[] arg2, String arg3, Fiber localObject1) throws IOException, Pausable
/*    */   {
/*    */     ;
/*    */     Object localObject2;
/*    */     OutputStream os;
/* 67 */     switch ((localObject2 = localObject1).pc) {default:  Fiber.wrongPC(); case 1:  localObject1 = null; break; case 0:  resp.status = statusCode;
/* 68 */       resp.setContentType("text/html");
/* 69 */       os = resp.getOutputStream();
/* 70 */       os.write(pre);
/* 71 */       os.write(htmlMsg.getBytes());
/* 72 */       os.write(post); }
/* 73 */     sendResponse(resp, ((Fiber)localObject2).down()); switch (((Fiber)localObject2).up()) {case 2:  State localState = new State();localState.self = this;localState.pc = 1;((Fiber)localObject2).setState(localState);return;
/*    */     case 3: 
/*    */       
/*    */     }
/*    */   }
/*    */   
/*    */   public void problem(HttpResponse paramHttpResponse, byte[] paramArrayOfByte, String paramString)
/*    */     throws IOException, Pausable
/*    */   {}
/*    */ }


/* Location:              /home/cfeindia/Desktop/kilim.jar!/kilim/http/HttpSession.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */