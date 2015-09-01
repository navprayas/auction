/*    */ package kilim;
/*    */ 
/*    */ import java.util.concurrent.TimeUnit;
/*    */ 
/*    */ public class ReentrantLock extends java.util.concurrent.locks.ReentrantLock {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*  8 */   public ReentrantLock() { super(false); }
/*    */   
/*    */   public ReentrantLock(boolean fair) {
/* 11 */     super(fair);
/*    */   }
/*    */   
/*    */   public Thread getOwner() {
/* 15 */     return super.getOwner();
/*    */   }
/*    */   
/* 18 */   Thread locker = null;
/*    */   
/*    */   public void lock() {
/* 21 */     super.lock();
/* 22 */     Thread t = Thread.currentThread();
/* 23 */     this.locker = t;
/* 24 */     if ((t instanceof WorkerThread)) {
/* 25 */       Task tsk = ((WorkerThread)t).getCurrentTask();
/* 26 */       if (t != null) { tsk.pinToThread();
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   public boolean tryLock()
/*    */   {
/* 33 */     boolean ret = super.tryLock();
/* 34 */     Thread t = Thread.currentThread();
/* 35 */     if ((ret) && ((t instanceof WorkerThread))) {
/* 36 */       this.locker = t;
/* 37 */       Task tsk = ((WorkerThread)t).getCurrentTask();
/* 38 */       if (t != null) tsk.pinToThread();
/*    */     }
/* 40 */     return ret;
/*    */   }
/*    */   
/*    */   public boolean tryLock(long timeout, TimeUnit unit)
/*    */     throws InterruptedException
/*    */   {
/* 46 */     boolean ret = super.tryLock(timeout, unit);
/* 47 */     Thread t = Thread.currentThread();
/* 48 */     if ((ret) && ((t instanceof WorkerThread))) {
/* 49 */       this.locker = t;
/* 50 */       Task tsk = ((WorkerThread)t).getCurrentTask();
/* 51 */       if (t != null) tsk.pinToThread();
/*    */     }
/* 53 */     return ret;
/*    */   }
/*    */   
/*    */   public void unlock()
/*    */   {
/*    */     try {
/* 59 */       super.unlock();
/*    */     } catch (IllegalMonitorStateException ims) {
/* 61 */       System.err.println("Locking thread: " + this.locker + ", unlocking thread: " + Thread.currentThread());
/* 62 */       ims.printStackTrace();
/* 63 */       System.exit(1);
/*    */     }
/* 65 */     Thread t = Thread.currentThread();
/* 66 */     if ((t instanceof WorkerThread)) {
/* 67 */       Task tsk = ((WorkerThread)t).getCurrentTask();
/* 68 */       if (t != null) tsk.unpinFromThread();
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/cfeindia/Desktop/kilim.jar!/kilim/ReentrantLock.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */