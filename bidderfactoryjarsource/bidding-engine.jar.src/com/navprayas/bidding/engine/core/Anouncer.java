/*    */ package com.navprayas.bidding.engine.core;
/*    */ 
/*    */ import com.navprayas.bidding.engine.common.Bid;
/*    */ import kilim.Mailbox;
/*    */ import kilim.Pausable;
/*    */ import kilim.Task;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Anouncer
/*    */   extends Task
/*    */ {
/* 13 */   public static Mailbox<Bid> _anouncebox = new Mailbox(2000);
/*    */   private static Anouncer _instance;
/*    */   public static final boolean $isWoven = true;
/*    */   
/* 17 */   public Mailbox<Bid> getMailBox() { return _anouncebox; }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static synchronized Anouncer getInstance()
/*    */   {
/* 25 */     if (_instance == null) {
/* 26 */       _instance = new Anouncer();
/*    */     }
/* 28 */     return _instance;
/*    */   }
/*    */   
/*    */   /* Error */
/*    */   public void execute(kilim.Fiber arg1)
/*    */     throws Pausable
/*    */   {
/*    */     // Byte code:
/*    */     //   0: aload_1
/*    */     //   1: dup
/*    */     //   2: astore_3
/*    */     //   3: getfield 45	kilim/Fiber:pc	I
/*    */     //   6: tableswitch	default:+22->28, 0:+35->41, 1:+25->31
/*    */     //   28: invokestatic 48	kilim/Fiber:wrongPC	()V
/*    */     //   31: aload_3
/*    */     //   32: invokevirtual 52	kilim/Fiber:getCallee	()Ljava/lang/Object;
/*    */     //   35: checkcast 17	kilim/Mailbox
/*    */     //   38: goto +6 -> 44
/*    */     //   41: getstatic 23	com/navprayas/bidding/engine/core/Anouncer:_anouncebox	Lkilim/Mailbox;
/*    */     //   44: aload_3
/*    */     //   45: invokevirtual 56	kilim/Fiber:down	()Lkilim/Fiber;
/*    */     //   48: invokevirtual 60	kilim/Mailbox:get	(Lkilim/Fiber;)Ljava/lang/Object;
/*    */     //   51: aload_3
/*    */     //   52: invokevirtual 64	kilim/Fiber:up	()I
/*    */     //   55: tableswitch	default:+60->115, 0:+60->115, 1:+60->115, 2:+29->84, 3:+58->113
/*    */     //   84: pop
/*    */     //   85: new 66	kilim/State
/*    */     //   88: dup
/*    */     //   89: invokespecial 67	kilim/State:<init>	()V
/*    */     //   92: astore 4
/*    */     //   94: aload 4
/*    */     //   96: aload_0
/*    */     //   97: putfield 71	kilim/State:self	Ljava/lang/Object;
/*    */     //   100: aload 4
/*    */     //   102: iconst_1
/*    */     //   103: putfield 72	kilim/State:pc	I
/*    */     //   106: aload_3
/*    */     //   107: aload 4
/*    */     //   109: invokevirtual 76	kilim/Fiber:setState	(Lkilim/State;)V
/*    */     //   112: return
/*    */     //   113: pop
/*    */     //   114: return
/*    */     //   115: checkcast 78	com/navprayas/bidding/engine/common/Bid
/*    */     //   118: astore_1
/*    */     //   119: getstatic 84	java/lang/System:out	Ljava/io/PrintStream;
/*    */     //   122: ldc 86
/*    */     //   124: invokevirtual 92	java/io/PrintStream:println	(Ljava/lang/String;)V
/*    */     //   127: aload_0
/*    */     //   128: aload_1
/*    */     //   129: invokevirtual 96	com/navprayas/bidding/engine/core/Anouncer:anounce	(Lcom/navprayas/bidding/engine/common/Bid;)V
/*    */     //   132: goto -91 -> 41
/*    */     //   135: aload_3
/*    */     //   136: invokevirtual 99	kilim/Fiber:upEx	()I
/*    */     //   139: tableswitch	default:+21->160, 0:+21->160, 1:+21->160
/*    */     //   160: astore_2
/*    */     //   161: aload_2
/*    */     //   162: invokevirtual 102	java/lang/Exception:printStackTrace	()V
/*    */     //   165: getstatic 84	java/lang/System:out	Ljava/io/PrintStream;
/*    */     //   168: new 104	java/lang/StringBuilder
/*    */     //   171: dup
/*    */     //   172: ldc 106
/*    */     //   174: invokespecial 108	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*    */     //   177: aload_1
/*    */     //   178: invokevirtual 112	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*    */     //   181: invokevirtual 116	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*    */     //   184: invokevirtual 92	java/io/PrintStream:println	(Ljava/lang/String;)V
/*    */     //   187: goto -146 -> 41
/*    */     // Line number table:
/*    */     //   Java source line #35	-> byte code offset #41
/*    */     //   Java source line #38	-> byte code offset #119
/*    */     //   Java source line #39	-> byte code offset #127
/*    */     //   Java source line #41	-> byte code offset #135
/*    */     //   Java source line #42	-> byte code offset #161
/*    */     //   Java source line #43	-> byte code offset #165
/*    */     //   Java source line #34	-> byte code offset #187
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	signature
/*    */     //   41	149	0	this	Anouncer
/*    */     //   119	68	1	bid	Bid
/*    */     //   161	26	2	e	Exception
/*    */     // Exception table:
/*    */     //   from	to	target	type
/*    */     //   119	132	135	java/lang/Exception
/*    */   }
/*    */   
/*    */   public void execute()
/*    */     throws Pausable
/*    */   {}
/*    */   
/*    */   public void anounce(Bid bid)
/*    */   {
/* 50 */     BidPublisher.getInstance().notifyBid(bid);
/*    */   }
/*    */ }


/* Location:              /home/cfeindia/Desktop/bidding-engine.jar!/com/navprayas/bidding/engine/core/Anouncer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */