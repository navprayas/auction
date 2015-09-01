/*    */ package kilim.nio;
/*    */ 
/*    */ import java.nio.channels.spi.AbstractSelectableChannel;
/*    */ import kilim.Mailbox;
/*    */ 
/*    */ 
/*    */ public class SockEvent
/*    */ {
/*    */   public int interestOps;
/*    */   public AbstractSelectableChannel ch;
/*    */   public Mailbox<SockEvent> replyTo;
/*    */   
/*    */   public SockEvent(Mailbox<SockEvent> aReplyTo, AbstractSelectableChannel ach, int ops)
/*    */   {
/* 15 */     this.ch = ach;
/* 16 */     this.interestOps = ops;
/* 17 */     this.replyTo = aReplyTo;
/*    */   }
/*    */ }


/* Location:              /home/cfeindia/Desktop/kilim.jar!/kilim/nio/SockEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */