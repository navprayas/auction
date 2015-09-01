/*     */ package kilim.nio;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.ServerSocket;
/*     */ import java.net.Socket;
/*     */ import java.nio.channels.SelectionKey;
/*     */ import java.nio.channels.Selector;
/*     */ import java.nio.channels.ServerSocketChannel;
/*     */ import java.nio.channels.SocketChannel;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import kilim.Fiber;
/*     */ import kilim.Mailbox;
/*     */ import kilim.Pausable;
/*     */ import kilim.RingQueue;
/*     */ import kilim.S_I;
/*     */ import kilim.Scheduler;
/*     */ import kilim.Task;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NioSelectorScheduler
/*     */   extends Scheduler
/*     */ {
/*  46 */   public static int LISTEN_BACKLOG = 1000;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Selector sel;
/*     */   
/*     */ 
/*     */ 
/*     */   public SelectorThread selectorThread;
/*     */   
/*     */ 
/*     */ 
/*  59 */   public Mailbox<SockEvent> registrationMbx = new Mailbox(1000);
/*     */   
/*     */ 
/*     */ 
/*     */   public NioSelectorScheduler()
/*     */     throws IOException
/*     */   {
/*  66 */     this.sel = Selector.open();
/*  67 */     this.selectorThread = new SelectorThread(this);
/*  68 */     this.selectorThread.start();
/*  69 */     Task t = new RegistrationTask(this.registrationMbx, this.sel);
/*  70 */     t.setScheduler(this);
/*  71 */     t.start();
/*     */   }
/*     */   
/*     */   public void listen(int port, Class<? extends SessionTask> sockTaskClass, Scheduler sockTaskScheduler) throws IOException
/*     */   {
/*  76 */     Task t = new ListenTask(port, this, sockTaskClass);
/*  77 */     t.setScheduler(this);
/*  78 */     t.start();
/*     */   }
/*     */   
/*     */   public void schedule(Task t)
/*     */   {
/*  83 */     addRunnable(t);
/*  84 */     if (Thread.currentThread() != this.selectorThread) {
/*  85 */       this.sel.wakeup();
/*     */     }
/*     */   }
/*     */   
/*     */   public void shutdown()
/*     */   {
/*  91 */     super.shutdown();
/*  92 */     this.sel.wakeup();
/*     */   }
/*     */   
/*     */   synchronized void addRunnable(Task t) {
/*  96 */     this.runnableTasks.put(t);
/*     */   }
/*     */   
/*     */   synchronized RingQueue<Task> swapRunnables(RingQueue<Task> emptyRunnables) {
/* 100 */     RingQueue<Task> ret = this.runnableTasks;
/* 101 */     this.runnableTasks = emptyRunnables;
/* 102 */     return ret;
/*     */   }
/*     */   
/*     */   static class SelectorThread extends Thread {
/*     */     NioSelectorScheduler _scheduler;
/*     */     
/*     */     public SelectorThread(NioSelectorScheduler scheduler) {
/* 109 */       super();
/* 110 */       this._scheduler = scheduler;
/*     */     }
/*     */     
/*     */     public void run()
/*     */     {
/* 115 */       Selector sel = this._scheduler.sel;
/* 116 */       RingQueue<Task> runnables = new RingQueue(100);
/*     */       for (;;) {
/*     */         int n;
/*     */         try {
/* 120 */           if (this._scheduler.isShutdown()) {
/* 121 */             Iterator<SelectionKey> it = sel.keys().iterator();
/* 122 */             if (it.hasNext()) {
/* 123 */               SelectionKey sk = (SelectionKey)it.next();
/* 124 */               sk.cancel();
/* 125 */               Object o = sk.attachment();
/* 126 */               if (((o instanceof SockEvent)) && ((((SockEvent)o).ch instanceof ServerSocketChannel)))
/*     */               {
/*     */                 try
/*     */                 {
/* 130 */                   ((ServerSocketChannel)((SockEvent)o).ch).close();
/*     */                 } catch (IOException ignore) {}
/*     */               }
/* 133 */               continue; }
/* 134 */             break; }
/*     */           int n;
/* 136 */           if (this._scheduler.numRunnables() > 0) {
/* 137 */             n = sel.selectNow();
/*     */           } else {
/* 139 */             n = sel.select();
/*     */           }
/*     */         } catch (IOException ignore) {
/* 142 */           n = 0;
/* 143 */           ignore.printStackTrace();
/*     */         }
/* 145 */         if (n > 0) {
/* 146 */           Iterator<SelectionKey> it = sel.selectedKeys().iterator();
/* 147 */           while (it.hasNext()) {
/* 148 */             SelectionKey sk = (SelectionKey)it.next();
/* 149 */             it.remove();
/* 150 */             Object o = sk.attachment();
/* 151 */             sk.interestOps(0);
/* 152 */             if ((o instanceof SockEvent)) {
/* 153 */               SockEvent ev = (SockEvent)o;
/* 154 */               ev.replyTo.putnb(ev);
/* 155 */             } else if ((o instanceof Task)) {
/* 156 */               Task t = (Task)o;
/* 157 */               t.resume();
/*     */             }
/*     */           }
/*     */         }
/* 161 */         runnables.reset();
/* 162 */         runnables = this._scheduler.swapRunnables(runnables);
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 167 */         while (runnables.size() > 0) {
/* 168 */           Task t = (Task)runnables.get();
/* 169 */           t._runExecute(null);
/*     */           
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 175 */           if ((t instanceof SessionTask)) {
/* 176 */             SessionTask st = (SessionTask)t;
/* 177 */             if (st.isDone()) {
/* 178 */               st.close();
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public synchronized int numRunnables() {
/* 187 */     return this.runnableTasks.size();
/*     */   }
/*     */   
/*     */   public static class ListenTask extends SessionTask {
/*     */     Class<? extends SessionTask> sessionClass;
/*     */     ServerSocketChannel ssc;
/*     */     int port;
/*     */     public static final boolean $isWoven = true;
/*     */     
/*     */     public ListenTask(int port, NioSelectorScheduler selScheduler, Class<? extends SessionTask> sessionClass) throws IOException {
/* 197 */       this.port = port;
/* 198 */       this.sessionClass = sessionClass;
/* 199 */       this.ssc = ServerSocketChannel.open();
/* 200 */       this.ssc.socket().setReuseAddress(true);
/* 201 */       this.ssc.socket().bind(new InetSocketAddress(port), NioSelectorScheduler.LISTEN_BACKLOG);
/* 202 */       this.ssc.configureBlocking(false);
/* 203 */       setEndPoint(new EndPoint(selScheduler.registrationMbx, this.ssc));
/*     */     }
/*     */     
/*     */ 
/* 207 */     public String toString() { return "ListenTask: " + this.port; }
/*     */     
/*     */     public void execute(Fiber ???) throws Pausable, Exception {
/*     */       Object localObject2;
/*     */       int n;
/* 212 */       switch ((localObject2 = ???).pc) {default:  Fiber.wrongPC(); case 1:  int i = 0;Object localObject1 = null; break; case 0:  n = 0; }
/*     */       for (;;) {
/* 214 */         SocketChannel ch = this.ssc.accept();
/*     */         
/* 216 */         this.ssc.close();
/*     */         
/*     */ 
/* 219 */         if (ch == null) {
/* 220 */           this.endpoint.pauseUntilAcceptable(((Fiber)localObject2).down()); S_I localS_I; switch (((Fiber)localObject2).up()) {case 2:  localS_I = new S_I();localS_I.self = this;localS_I.pc = 1;localS_I.f0 = n;((Fiber)localObject2).setState(localS_I);return; case 3:  return; case 1:  localS_I = (S_I)((Fiber)localObject2).curState;n = localS_I.f0; }
/*     */         } else {
/* 222 */           ch.socket().setTcpNoDelay(true);
/* 223 */           ch.configureBlocking(false);
/* 224 */           SessionTask task = (SessionTask)this.sessionClass.newInstance();
/*     */           try {
/* 226 */             EndPoint ep = new EndPoint(this.endpoint.sockEvMbx, ch);
/* 227 */             task.setEndPoint(ep);
/* 228 */             n++;
/*     */             
/* 230 */             task.start();
/*     */           } catch (IOException ioe) {
/* 232 */             ch.close();
/* 233 */             System.err.println("Unable to start session:");
/* 234 */             ioe.printStackTrace();
/*     */           }
/*     */         }
/*     */       } }
/*     */     
/*     */     public void execute() throws Pausable, Exception
/*     */     {} }
/*     */   
/*     */   public static class RegistrationTask extends Task { Mailbox<SockEvent> mbx;
/*     */     Selector selector;
/*     */     public static final boolean $isWoven = true;
/*     */     
/* 246 */     public RegistrationTask(Mailbox<SockEvent> ambx, Selector asel) { this.mbx = ambx;
/* 247 */       this.selector = asel;
/*     */     }
/*     */     
/*     */     /* Error */
/*     */     public void execute(Fiber arg1)
/*     */       throws Pausable, Exception
/*     */     {
/*     */       // Byte code:
/*     */       //   0: aload_1
/*     */       //   1: dup
/*     */       //   2: astore_3
/*     */       //   3: getfield 41	kilim/Fiber:pc	I
/*     */       //   6: tableswitch	default:+22->28, 0:+35->41, 1:+25->31
/*     */       //   28: invokestatic 44	kilim/Fiber:wrongPC	()V
/*     */       //   31: aload_3
/*     */       //   32: invokevirtual 48	kilim/Fiber:getCallee	()Ljava/lang/Object;
/*     */       //   35: checkcast 50	kilim/Mailbox
/*     */       //   38: goto +7 -> 45
/*     */       //   41: aload_0
/*     */       //   42: getfield 23	kilim/nio/NioSelectorScheduler$RegistrationTask:mbx	Lkilim/Mailbox;
/*     */       //   45: aload_3
/*     */       //   46: invokevirtual 54	kilim/Fiber:down	()Lkilim/Fiber;
/*     */       //   49: invokevirtual 58	kilim/Mailbox:get	(Lkilim/Fiber;)Ljava/lang/Object;
/*     */       //   52: aload_3
/*     */       //   53: invokevirtual 62	kilim/Fiber:up	()I
/*     */       //   56: tableswitch	default:+63->119, 0:+63->119, 1:+63->119, 2:+32->88, 3:+61->117
/*     */       //   88: pop
/*     */       //   89: new 64	kilim/State
/*     */       //   92: dup
/*     */       //   93: invokespecial 65	kilim/State:<init>	()V
/*     */       //   96: astore 4
/*     */       //   98: aload 4
/*     */       //   100: aload_0
/*     */       //   101: putfield 69	kilim/State:self	Ljava/lang/Object;
/*     */       //   104: aload 4
/*     */       //   106: iconst_1
/*     */       //   107: putfield 70	kilim/State:pc	I
/*     */       //   110: aload_3
/*     */       //   111: aload 4
/*     */       //   113: invokevirtual 74	kilim/Fiber:setState	(Lkilim/State;)V
/*     */       //   116: return
/*     */       //   117: pop
/*     */       //   118: return
/*     */       //   119: checkcast 76	kilim/nio/SockEvent
/*     */       //   122: astore_1
/*     */       //   123: aload_1
/*     */       //   124: getfield 80	kilim/nio/SockEvent:ch	Ljava/nio/channels/spi/AbstractSelectableChannel;
/*     */       //   127: aload_0
/*     */       //   128: getfield 25	kilim/nio/NioSelectorScheduler$RegistrationTask:selector	Ljava/nio/channels/Selector;
/*     */       //   131: aload_1
/*     */       //   132: getfield 83	kilim/nio/SockEvent:interestOps	I
/*     */       //   135: invokevirtual 89	java/nio/channels/spi/AbstractSelectableChannel:register	(Ljava/nio/channels/Selector;I)Ljava/nio/channels/SelectionKey;
/*     */       //   138: astore_2
/*     */       //   139: aload_2
/*     */       //   140: aload_1
/*     */       //   141: invokevirtual 95	java/nio/channels/SelectionKey:attach	(Ljava/lang/Object;)Ljava/lang/Object;
/*     */       //   144: pop
/*     */       //   145: goto -104 -> 41
/*     */       // Line number table:
/*     */       //   Java source line #253	-> byte code offset #41
/*     */       //   Java source line #254	-> byte code offset #123
/*     */       //   Java source line #255	-> byte code offset #139
/*     */       //   Java source line #256	-> byte code offset #145
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	signature
/*     */       //   41	107	0	this	RegistrationTask
/*     */       //   123	22	1	ev	SockEvent
/*     */       //   139	6	2	sk	SelectionKey
/*     */     }
/*     */     
/*     */     public void execute()
/*     */       throws Pausable, Exception
/*     */     {}
/*     */   }
/*     */ }


/* Location:              /home/cfeindia/Desktop/kilim.jar!/kilim/nio/NioSelectorScheduler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */