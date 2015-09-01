/*    */ package kilim;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ import java.util.concurrent.atomic.AtomicInteger;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class WorkerThread
/*    */   extends Thread
/*    */ {
/*    */   volatile Task runningTask;
/* 17 */   RingQueue<Task> tasks = new RingQueue(10);
/*    */   Scheduler scheduler;
/* 19 */   static AtomicInteger gid = new AtomicInteger();
/* 20 */   public int numResumes = 0;
/*    */   
/*    */   WorkerThread(Scheduler ascheduler) {
/* 23 */     super("KilimWorker-" + gid.incrementAndGet());
/* 24 */     this.scheduler = ascheduler;
/*    */   }
/*    */   
/*    */   public void run() {
/*    */     try {
/*    */       for (;;) {
/* 30 */         Task t = getNextTask(this);
/* 31 */         this.runningTask = t;
/* 32 */         t._runExecute(this);
/* 33 */         this.runningTask = null;
/*    */       }
/*    */     }
/*    */     catch (ShutdownException se) {}catch (OutOfMemoryError ex)
/*    */     {
/* 38 */       System.err.println("Out of memory");
/* 39 */       System.exit(1);
/*    */     } catch (Throwable ex) {
/* 41 */       ex.printStackTrace();
/* 42 */       System.err.println(this.runningTask);
/*    */     }
/* 44 */     this.runningTask = null;
/*    */   }
/*    */   
/*    */   protected Task getNextTask(WorkerThread workerThread) throws ShutdownException {
/* 48 */     Task t = null;
/*    */     for (;;) {
/* 50 */       if (this.scheduler.isShutdown()) {
/* 51 */         throw new ShutdownException();
/*    */       }
/* 53 */       t = getNextTask();
/* 54 */       if (t != null) {
/*    */         break;
/*    */       }
/*    */       
/* 58 */       this.scheduler.loadNextTask(this);
/* 59 */       synchronized (this)
/*    */       {
/* 61 */         t = (Task)this.tasks.get();
/* 62 */         if (t != null) {
/*    */           break;
/*    */         }
/* 65 */         this.scheduler.addWaitingThread(this);
/*    */         try {
/* 67 */           wait();
/*    */         }
/*    */         catch (InterruptedException ignore) {}
/*    */       }
/*    */     }
/* 72 */     assert (t != null) : "Returning null task";
/* 73 */     return t;
/*    */   }
/*    */   
/*    */   public Task getCurrentTask() {
/* 77 */     return this.runningTask;
/*    */   }
/*    */   
/*    */   public synchronized void addRunnableTask(Task t) {
/* 81 */     assert ((t.preferredResumeThread == null) || (t.preferredResumeThread == this)) : "Task given to wrong thread";
/* 82 */     this.tasks.put(t);
/* 83 */     notify();
/*    */   }
/*    */   
/*    */   public synchronized boolean hasTasks() {
/* 87 */     return this.tasks.size() > 0;
/*    */   }
/*    */   
/*    */   public synchronized Task getNextTask() {
/* 91 */     return (Task)this.tasks.get();
/*    */   }
/*    */   
/*    */   public synchronized void waitForMsgOrSignal() {
/*    */     try {
/* 96 */       if (this.tasks.size() == 0) {
/* 97 */         wait();
/*    */       }
/*    */     }
/*    */     catch (InterruptedException ignore) {}
/*    */   }
/*    */ }


/* Location:              /home/cfeindia/Desktop/kilim.jar!/kilim/WorkerThread.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */