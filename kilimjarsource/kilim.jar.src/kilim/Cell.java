/*     */ package kilim;
/*     */ 
/*     */ import java.util.LinkedList;
/*     */ import java.util.Timer;
/*     */ import java.util.TimerTask;
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
/*     */ public class Cell<T>
/*     */   implements PauseReason, EventPublisher
/*     */ {
/*     */   T msg;
/*     */   EventSubscriber sink;
/*     */   public static final int SPACE_AVAILABLE = 1;
/*     */   public static final int MSG_AVAILABLE = 2;
/*     */   public static final int TIMED_OUT = 3;
/*  25 */   public static final Event spaceAvailble = new Event(2);
/*  26 */   public static final Event messageAvailable = new Event(1);
/*  27 */   public static final Event timedOut = new Event(3);
/*     */   
/*  29 */   LinkedList<EventSubscriber> srcs = new LinkedList();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final boolean $isWoven = true;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public T get(EventSubscriber eo)
/*     */   {
/*  47 */     EventSubscriber producer = null;
/*     */     T ret;
/*  49 */     synchronized (this) {
/*  50 */       if (this.msg == null) {
/*  51 */         T ret = null;
/*  52 */         addMsgAvailableListener(eo);
/*     */       } else {
/*  54 */         ret = this.msg;
/*  55 */         this.msg = null;
/*  56 */         if (this.srcs.size() > 0) {
/*  57 */           producer = (EventSubscriber)this.srcs.poll();
/*     */         }
/*     */       }
/*     */     }
/*  61 */     if (producer != null) {
/*  62 */       producer.onEvent(this, spaceAvailble);
/*     */     }
/*  64 */     return ret;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean put(T amsg, EventSubscriber eo)
/*     */   {
/*  74 */     boolean ret = true;
/*     */     EventSubscriber subscriber;
/*  76 */     synchronized (this) {
/*  77 */       if (amsg == null) {
/*  78 */         throw new NullPointerException("Null message supplied to put");
/*     */       }
/*  80 */       if (this.msg == null) {
/*  81 */         this.msg = amsg;
/*  82 */         EventSubscriber subscriber = this.sink;
/*  83 */         this.sink = null;
/*     */       } else {
/*  85 */         ret = false;
/*     */         
/*  87 */         subscriber = null;
/*  88 */         if (eo != null) {
/*  89 */           this.srcs.add(eo);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  94 */     if (subscriber != null) {
/*  95 */       subscriber.onEvent(this, messageAvailable);
/*     */     }
/*  97 */     return ret;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public T getnb()
/*     */   {
/* 106 */     return (T)get(null);
/*     */   }
/*     */   
/*     */   public T get(Fiber ???)
/*     */     throws Pausable
/*     */   {
/*     */     Object localObject2;
/*     */     Task t;
/* 114 */     switch ((localObject2 = ???).pc) {default:  Fiber.wrongPC(); case 1:  ??? = null;Object localObject1 = null; break; case 0:  t = ((Fiber)localObject2).task;
/* 115 */       msg = get(t);
/*     */     }
/* 117 */     Task.pause(this, ((Fiber)localObject2).down()); S_O localS_O; switch (((Fiber)localObject2).up()) {case 2:  localS_O = new S_O();localS_O.self = this;localS_O.pc = 1;localS_O.f0 = t;((Fiber)localObject2).setState(localS_O);return null; case 3:  return null; case 1:  localS_O = (S_O)((Fiber)localObject2).curState;t = (Task)localS_O.f0; }
/* 118 */     Object msg = get(t);
/*     */     
/* 120 */     return (T)msg;
/*     */   }
/*     */   
/*     */   public T get() throws Pausable { Task.errNotWoven();
/*     */     return null; }
/*     */   
/*     */   public T get(long arg1, Fiber ???) throws Pausable { Object localObject3;
/*     */     final Task t;
/*     */     Object msg;
/*     */     long begin;
/* 129 */     switch ((localObject3 = ???).pc) {default:  Fiber.wrongPC(); case 1:  ??? = null;Object localObject1 = null;long l1 = 0L;Object localObject2 = null; break; case 0:  t = ((Fiber)localObject3).task;
/* 130 */       msg = get(t);
/* 131 */       begin = System.currentTimeMillis(); }
/* 132 */     while (msg == null) {
/* 133 */       Object tt = new TimerTask() {
/*     */         public void run() {
/* 135 */           Cell.this.removeMsgAvailableListener(t);
/* 136 */           t.onEvent(Cell.this, Cell.timedOut);
/*     */         }
/* 138 */       };
/* 139 */       Task.timer.schedule((TimerTask)tt, timeoutMillis);
/* 140 */       Task.pause(this, ((Fiber)localObject3).down()); S_O3L2 localS_O3L2; switch (((Fiber)localObject3).up()) {case 2:  localS_O3L2 = new S_O3L2();localS_O3L2.self = this;localS_O3L2.pc = 1;localS_O3L2.f0 = t;localS_O3L2.f1 = msg;localS_O3L2.f2 = tt;localS_O3L2.f3 = timeoutMillis;localS_O3L2.f4 = begin;((Fiber)localObject3).setState(localS_O3L2);return null; case 3:  return null; case 1:  localS_O3L2 = (S_O3L2)((Fiber)localObject3).curState;timeoutMillis = localS_O3L2.f3;t = (Task)localS_O3L2.f0;msg = localS_O3L2.f1;begin = localS_O3L2.f4;tt = (1)localS_O3L2.f2; }
/* 141 */       ((TimerTask)tt).cancel();
/*     */       
/*     */ 
/*     */ 
/* 145 */       msg = System.currentTimeMillis() - begin > timeoutMillis ? null : get(t);
/*     */     }
/* 147 */     return (T)msg; }
/*     */   
/*     */   public T get(long paramLong) throws Pausable { Task.errNotWoven();
/*     */     return null; }
/*     */   
/* 151 */   public synchronized void addSpaceAvailableListener(EventSubscriber spcSub) { this.srcs.add(spcSub); }
/*     */   
/*     */   public synchronized void removeSpaceAvailableListener(EventSubscriber spcSub)
/*     */   {
/* 155 */     this.srcs.remove(spcSub);
/*     */   }
/*     */   
/*     */   public synchronized void addMsgAvailableListener(EventSubscriber msgSub)
/*     */   {
/* 160 */     if (this.sink != null) {
/* 161 */       throw new AssertionError("Error: A mailbox can not be shared by two consumers.  New = " + msgSub + ", Old = " + this.sink);
/*     */     }
/*     */     
/*     */ 
/* 165 */     this.sink = msgSub;
/*     */   }
/*     */   
/*     */   public synchronized void removeMsgAvailableListener(EventSubscriber msgSub) {
/* 169 */     if (this.sink == msgSub) {
/* 170 */       this.sink = null;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/* 175 */   public boolean putnb(T msg) { return put(msg, null); }
/*     */   
/*     */   public void put(T arg1, Fiber ???) throws Pausable { Object localObject;
/*     */     Task t;
/* 179 */     switch ((localObject = ???).pc) {default:  Fiber.wrongPC(); case 1:  ??? = null; break; case 0:  t = ((Fiber)localObject).task;
/*     */     }
/* 181 */     Task.pause(this, ((Fiber)localObject).down()); S_O2 localS_O2; switch (((Fiber)localObject).up()) {case 2:  localS_O2 = new S_O2();localS_O2.self = this;localS_O2.pc = 1;localS_O2.f0 = msg;localS_O2.f1 = t;((Fiber)localObject).setState(localS_O2);return; case 3:   case 1:  localS_O2 = (S_O2)((Fiber)localObject).curState;msg = localS_O2.f0;t = (Task)localS_O2.f1; } }
/*     */   
/*     */   public void put(T paramT) throws Pausable
/*     */   {}
/*     */   
/* 186 */   public boolean put(T arg1, int arg2, Fiber ???) throws Pausable { Object localObject2; final Task t; long begin; switch ((localObject2 = ???).pc) {default:  Fiber.wrongPC(); case 1:  ??? = null;long l1 = 0L;Object localObject1 = null; break; case 0:  t = ((Fiber)localObject2).task;
/* 187 */       begin = System.currentTimeMillis();
/*     */       
/* 189 */       Object tt = new TimerTask() {
/*     */         public void run() {
/* 191 */           Cell.this.removeSpaceAvailableListener(t);
/* 192 */           t.onEvent(Cell.this, Cell.timedOut);
/*     */         }
/* 194 */       };
/* 195 */       Task.timer.schedule((TimerTask)tt, timeoutMillis); }
/* 196 */     Task.pause(this, ((Fiber)localObject2).down()); S_O2IL localS_O2IL; switch (((Fiber)localObject2).up()) {case 2:  localS_O2IL = new S_O2IL();localS_O2IL.self = this;localS_O2IL.pc = 1;localS_O2IL.f0 = msg;localS_O2IL.f1 = t;localS_O2IL.f2 = timeoutMillis;localS_O2IL.f3 = begin;((Fiber)localObject2).setState(localS_O2IL);return false; case 3:  return false; case 1:  localS_O2IL = (S_O2IL)((Fiber)localObject2).curState;msg = localS_O2IL.f0;timeoutMillis = localS_O2IL.f2;t = (Task)localS_O2IL.f1;begin = localS_O2IL.f3; }
/* 197 */     if (System.currentTimeMillis() - begin >= timeoutMillis) {
/* 198 */       return false;
/*     */     }
/*     */     
/* 201 */     return true; }
/*     */   
/*     */   public boolean put(T paramT, int paramInt) throws Pausable { Task.errNotWoven();
/*     */     return false; }
/*     */   
/* 205 */   public void putb(T msg) { putb(msg, 0L); }
/*     */   
/*     */   public class BlockingSubscriber implements EventSubscriber { public BlockingSubscriber() {}
/*     */     
/* 209 */     public volatile boolean eventRcvd = false;
/*     */     
/* 211 */     public void onEvent(EventPublisher ep, Event e) { synchronized (Cell.this) {
/* 212 */         this.eventRcvd = true;
/* 213 */         Cell.this.notify();
/*     */       }
/*     */     }
/*     */     
/* 217 */     public void blockingWait(long timeoutMillis) { long start = System.currentTimeMillis();
/* 218 */       long remaining = timeoutMillis;
/* 219 */       boolean infiniteWait = timeoutMillis == 0L;
/* 220 */       synchronized (Cell.this) {
/* 221 */         while ((!this.eventRcvd) && ((infiniteWait) || (remaining > 0L))) {
/*     */           try {
/* 223 */             Cell.this.wait(infiniteWait ? 0L : remaining);
/*     */           } catch (InterruptedException ie) {}
/* 225 */           long elapsed = System.currentTimeMillis() - start;
/* 226 */           remaining -= elapsed;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void putb(T msg, long timeoutMillis) {
/* 233 */     Cell<T>.BlockingSubscriber evs = new BlockingSubscriber();
/* 234 */     if (!put(msg, evs)) {
/* 235 */       evs.blockingWait(timeoutMillis);
/*     */     }
/* 237 */     if (!evs.eventRcvd) {
/* 238 */       removeSpaceAvailableListener(evs);
/*     */     }
/*     */   }
/*     */   
/*     */   public synchronized boolean hasMessage() {
/* 243 */     return this.msg != null;
/*     */   }
/*     */   
/*     */   public synchronized boolean hasSpace() {
/* 247 */     return this.msg == null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public T getb()
/*     */   {
/* 257 */     return (T)getb(0L);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public T getb(long timeoutMillis)
/*     */   {
/* 268 */     Cell<T>.BlockingSubscriber evs = new BlockingSubscriber();
/*     */     
/*     */     T msg;
/* 271 */     if ((msg = get(evs)) == null) {
/* 272 */       evs.blockingWait(timeoutMillis);
/* 273 */       if (evs.eventRcvd) {
/* 274 */         msg = get(null);
/* 275 */         assert (msg != null) : "Received event, but message is null";
/*     */       }
/*     */     }
/* 278 */     if (msg == null) {
/* 279 */       removeMsgAvailableListener(evs);
/*     */     }
/* 281 */     return msg;
/*     */   }
/*     */   
/*     */   public synchronized String toString() {
/* 285 */     return "id:" + System.identityHashCode(this) + " " + this.msg;
/*     */   }
/*     */   
/*     */   public boolean isValid(Task t)
/*     */   {
/* 290 */     synchronized (this) {
/* 291 */       return (t == this.sink) || (this.srcs.contains(t));
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/cfeindia/Desktop/kilim.jar!/kilim/Cell.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */