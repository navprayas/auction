/*     */ package kilim;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.LinkedList;
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
/*     */ public class Scheduler
/*     */ {
/*     */   public static volatile Scheduler defaultScheduler;
/*     */   public static int defaultNumberThreads;
/*  24 */   public LinkedList<WorkerThread> allThreads = new LinkedList();
/*  25 */   public RingQueue<WorkerThread> waitingThreads = new RingQueue(10);
/*  26 */   protected volatile boolean shutdown = false;
/*  27 */   public RingQueue<Task> runnableTasks = new RingQueue(100);
/*     */   
/*     */   static
/*     */   {
/*  21 */     defaultScheduler = null;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  30 */     String s = System.getProperty("kilim.Scheduler.numThreads");
/*  31 */     if (s != null) {
/*     */       try {
/*  33 */         defaultNumberThreads = Integer.parseInt(s);
/*     */       } catch (Exception e) {}
/*     */     }
/*  36 */     if (defaultNumberThreads == 0) {
/*  37 */       defaultNumberThreads = Runtime.getRuntime().availableProcessors();
/*     */     }
/*     */   }
/*     */   
/*     */   public Scheduler(int numThreads)
/*     */   {
/*  43 */     for (int i = 0; i < numThreads; i++) {
/*  44 */       WorkerThread wt = new WorkerThread(this);
/*  45 */       this.allThreads.add(wt);
/*  46 */       addWaitingThread(wt);
/*  47 */       wt.start();
/*     */     }
/*     */   }
/*     */   
/*     */   void addWaitingThread(WorkerThread wt) {
/*  52 */     synchronized (this.waitingThreads) {
/*  53 */       this.waitingThreads.put(wt);
/*     */     }
/*     */   }
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
/*     */   public void schedule(Task t)
/*     */   {
/*  68 */     WorkerThread wt = null;
/*     */     
/*  70 */     synchronized (this) {
/*  71 */       assert (t.running == true) : ("Task " + t + " scheduled even though running is false");
/*  72 */       this.runnableTasks.put(t);
/*     */     }
/*  74 */     wt = getWaitingThread();
/*  75 */     if (wt != null) {
/*  76 */       synchronized (wt) {
/*  77 */         wt.notify();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void shutdown() {
/*  83 */     this.shutdown = true;
/*  84 */     if (defaultScheduler == this) {
/*  85 */       defaultScheduler = null;
/*     */     }
/*  87 */     for (WorkerThread wt : this.allThreads) {
/*  88 */       synchronized (wt) {
/*  89 */         wt.notify();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isShutdown() {
/*  95 */     return this.shutdown;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   void loadNextTask(WorkerThread wt)
/*     */     throws ShutdownException
/*     */   {
/*     */     for (;;)
/*     */     {
/* 107 */       Task t = null;
/* 108 */       WorkerThread prefThread = null;
/*     */       
/* 110 */       synchronized (this) {
/* 111 */         if (this.shutdown) { throw new ShutdownException();
/*     */         }
/* 113 */         t = (Task)this.runnableTasks.get();
/* 114 */         if (t == null) {
/*     */           break;
/*     */         }
/*     */         
/* 118 */         prefThread = t.preferredResumeThread;
/* 119 */         if ((prefThread == null) || (prefThread == wt)) {
/* 120 */           wt.addRunnableTask(t);
/* 121 */           break;
/*     */         }
/*     */         
/*     */ 
/* 125 */         prefThread.addRunnableTask(t);
/* 126 */         synchronized (prefThread) {
/* 127 */           prefThread.notify();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static synchronized Scheduler getDefaultScheduler()
/*     */   {
/* 137 */     if (defaultScheduler == null) {
/* 138 */       defaultScheduler = new Scheduler(defaultNumberThreads);
/*     */     }
/* 140 */     return defaultScheduler;
/*     */   }
/*     */   
/*     */   public static void setDefaultScheduler(Scheduler s) {
/* 144 */     defaultScheduler = s;
/*     */   }
/*     */   
/*     */   public void dump() {
/* 148 */     System.out.println(this.runnableTasks);
/*     */   }
/*     */   
/*     */   protected Scheduler() {}
/*     */   
/*     */   /* Error */
/*     */   WorkerThread getWaitingThread()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 7	kilim/Scheduler:waitingThreads	Lkilim/RingQueue;
/*     */     //   4: dup
/*     */     //   5: astore_1
/*     */     //   6: monitorenter
/*     */     //   7: aload_0
/*     */     //   8: getfield 7	kilim/Scheduler:waitingThreads	Lkilim/RingQueue;
/*     */     //   11: invokevirtual 16	kilim/RingQueue:get	()Ljava/lang/Object;
/*     */     //   14: checkcast 10	kilim/WorkerThread
/*     */     //   17: aload_1
/*     */     //   18: monitorexit
/*     */     //   19: areturn
/*     */     //   20: astore_2
/*     */     //   21: aload_1
/*     */     //   22: monitorexit
/*     */     //   23: aload_2
/*     */     //   24: athrow
/*     */     // Line number table:
/*     */     //   Java source line #58	-> byte code offset #0
/*     */     //   Java source line #59	-> byte code offset #7
/*     */     //   Java source line #60	-> byte code offset #20
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	25	0	this	Scheduler
/*     */     //   5	17	1	Ljava/lang/Object;	Object
/*     */     //   20	4	2	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   7	19	20	finally
/*     */     //   20	23	20	finally
/*     */   }
/*     */ }


/* Location:              /home/cfeindia/Desktop/kilim.jar!/kilim/Scheduler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */