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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Mailbox<T>
/*     */   implements PauseReason, EventPublisher
/*     */ {
/*     */   T[] msgs;
/*  27 */   private int iprod = 0;
/*  28 */   private int icons = 0;
/*  29 */   private int numMsgs = 0;
/*  30 */   private int maxMsgs = 300;
/*     */   
/*     */   EventSubscriber sink;
/*     */   
/*     */   public static final int SPACE_AVAILABLE = 1;
/*     */   
/*     */   public static final int MSG_AVAILABLE = 2;
/*     */   
/*     */   public static final int TIMED_OUT = 3;
/*  39 */   public static final Event spaceAvailble = new Event(2);
/*  40 */   public static final Event messageAvailable = new Event(1);
/*  41 */   public static final Event timedOut = new Event(3);
/*     */   
/*  43 */   LinkedList<EventSubscriber> srcs = new LinkedList();
/*     */   
/*     */ 
/*     */   public static final boolean $isWoven = true;
/*     */   
/*     */ 
/*     */ 
/*     */   public Mailbox()
/*     */   {
/*  52 */     this(10);
/*     */   }
/*     */   
/*     */   public Mailbox(int initialSize) {
/*  56 */     this(initialSize, Integer.MAX_VALUE);
/*     */   }
/*     */   
/*     */   public Mailbox(int initialSize, int maxSize)
/*     */   {
/*  61 */     if (initialSize > maxSize) {
/*  62 */       throw new IllegalArgumentException("initialSize: " + initialSize + " cannot exceed maxSize: " + maxSize);
/*     */     }
/*  64 */     this.msgs = ((Object[])new Object[initialSize]);
/*  65 */     this.maxMsgs = maxSize;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public T get(EventSubscriber eo)
/*     */   {
/*  76 */     EventSubscriber producer = null;
/*  77 */     T msg; synchronized (this) {
/*  78 */       int n = this.numMsgs;
/*  79 */       if (n > 0) {
/*  80 */         int ic = this.icons;
/*  81 */         T msg = this.msgs[ic];this.msgs[ic] = null;
/*  82 */         this.icons = ((ic + 1) % this.msgs.length);
/*  83 */         this.numMsgs = (n - 1);
/*     */         
/*  85 */         if (this.srcs.size() > 0) {
/*  86 */           producer = (EventSubscriber)this.srcs.poll();
/*     */         }
/*     */       } else {
/*  89 */         msg = null;
/*  90 */         addMsgAvailableListener(eo);
/*     */       }
/*     */     }
/*  93 */     if (producer != null) {
/*  94 */       producer.onEvent(this, spaceAvailble);
/*     */     }
/*  96 */     return msg;
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
/*     */   public boolean put(T msg, EventSubscriber eo)
/*     */   {
/* 109 */     boolean ret = true;
/*     */     EventSubscriber subscriber;
/* 111 */     synchronized (this) {
/* 112 */       if (msg == null) {
/* 113 */         throw new NullPointerException("Null message supplied to put");
/*     */       }
/* 115 */       int ip = this.iprod;
/* 116 */       int ic = this.icons;
/* 117 */       int n = this.numMsgs;
/* 118 */       if (n == this.msgs.length) {
/* 119 */         assert (ic == ip) : "numElements == msgs.length && ic != ip";
/* 120 */         if (n < this.maxMsgs) {
/* 121 */           T[] newmsgs = (Object[])new Object[Math.min(n * 2, this.maxMsgs)];
/* 122 */           System.arraycopy(this.msgs, ic, newmsgs, 0, n - ic);
/* 123 */           if (ic > 0) {
/* 124 */             System.arraycopy(this.msgs, 0, newmsgs, n - ic, ic);
/*     */           }
/* 126 */           this.msgs = newmsgs;
/* 127 */           ip = n;
/* 128 */           ic = 0;
/*     */         } else {
/* 130 */           ret = false;
/*     */         }
/*     */       }
/* 133 */       if (ret) {
/* 134 */         this.numMsgs = (n + 1);
/* 135 */         this.msgs[ip] = msg;
/* 136 */         this.iprod = ((ip + 1) % this.msgs.length);
/* 137 */         this.icons = ic;
/* 138 */         EventSubscriber subscriber = this.sink;
/* 139 */         this.sink = null;
/*     */       } else {
/* 141 */         subscriber = null;
/*     */         
/* 143 */         if (eo != null) {
/* 144 */           this.srcs.add(eo);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 149 */     if (subscriber != null) {
/* 150 */       subscriber.onEvent(this, messageAvailable);
/*     */     }
/* 152 */     return ret;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public T getnb()
/*     */   {
/* 161 */     return (T)get(null);
/*     */   }
/*     */   
/*     */   public T get(Fiber ???)
/*     */     throws Pausable
/*     */   {
/*     */     Object localObject2;
/*     */     Task t;
/* 169 */     switch ((localObject2 = ???).pc) {default:  Fiber.wrongPC(); case 1:  ??? = null;Object localObject1 = null; break; case 0:  t = ((Fiber)localObject2).task;
/* 170 */       msg = get(t);
/*     */     }
/* 172 */     Task.pause(this, ((Fiber)localObject2).down()); S_O localS_O; switch (((Fiber)localObject2).up()) {case 2:  localS_O = new S_O();localS_O.self = this;localS_O.pc = 1;localS_O.f0 = t;((Fiber)localObject2).setState(localS_O);return null; case 3:  return null; case 1:  localS_O = (S_O)((Fiber)localObject2).curState;t = (Task)localS_O.f0; }
/* 173 */     Object msg = get(t);
/*     */     
/* 175 */     return (T)msg;
/*     */   }
/*     */   
/*     */   public T get() throws Pausable { Task.errNotWoven();
/*     */     return null; }
/*     */   
/*     */   public T get(long arg1, Fiber ???) throws Pausable { Object localObject3;
/*     */     final Task t;
/*     */     Object msg;
/*     */     long end;
/* 184 */     switch ((localObject3 = ???).pc) {default:  Fiber.wrongPC(); case 1:  ??? = null;Object localObject1 = null;long l1 = 0L;Object localObject2 = null; break; case 0:  t = ((Fiber)localObject3).task;
/* 185 */       msg = get(t);
/* 186 */       end = System.currentTimeMillis() + timeoutMillis; }
/* 187 */     while (msg == null) {
/* 188 */       Object tt = new TimerTask() {
/*     */         public void run() {
/* 190 */           Mailbox.this.removeMsgAvailableListener(t);
/* 191 */           t.onEvent(Mailbox.this, Mailbox.timedOut);
/*     */         }
/* 193 */       };
/* 194 */       Task.timer.schedule((TimerTask)tt, timeoutMillis);
/* 195 */       Task.pause(this, ((Fiber)localObject3).down()); S_O2L localS_O2L; switch (((Fiber)localObject3).up()) {case 2:  localS_O2L = new S_O2L();localS_O2L.self = this;localS_O2L.pc = 1;localS_O2L.f0 = t;localS_O2L.f1 = tt;localS_O2L.f2 = end;((Fiber)localObject3).setState(localS_O2L);return null; case 3:  return null; case 1:  localS_O2L = (S_O2L)((Fiber)localObject3).curState;t = (Task)localS_O2L.f0;end = localS_O2L.f2;tt = (1)localS_O2L.f1; }
/* 196 */       ((TimerTask)tt).cancel();
/*     */       
/* 198 */       msg = get(t);
/*     */       
/* 200 */       timeoutMillis = end - System.currentTimeMillis();
/*     */       
/* 202 */       removeMsgAvailableListener(t);
/*     */     }
/*     */     
/*     */ 
/* 206 */     return (T)msg;
/*     */   }
/*     */   
/*     */   public T get(long paramLong)
/*     */     throws Pausable
/*     */   {
/*     */     Task.errNotWoven();
/*     */     return null;
/*     */   }
/*     */   
/*     */   public static int select(Mailbox[] arg0, Fiber ???)
/*     */     throws Pausable
/*     */   {
/*     */     Object localObject2;
/* 219 */     switch ((localObject2 = ???).pc) {default:  Fiber.wrongPC(); case 1:  ??? = null;Object localObject1 = null;int i = 0; break; } for (;;) { int i = 0;
/* 220 */       if (mboxes[i].hasMessage()) {
/* 221 */         return i;
/*     */       }
/* 219 */       i++;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 224 */       Task t = ((Fiber)localObject2).task;
/* 225 */       EmptySet_MsgAvListener pauseReason = new EmptySet_MsgAvListener(t, mboxes);
/*     */       
/* 227 */       for (int i = 0; i < mboxes.length; i++) {
/* 228 */         mboxes[i].addMsgAvailableListener(pauseReason);
/*     */       }
/* 230 */       Task.pause(pauseReason, ((Fiber)localObject2).down()); S_O2 localS_O2; switch (((Fiber)localObject2).up()) {case 2:  localS_O2 = new S_O2();localS_O2.pc = 1;localS_O2.f0 = mboxes;localS_O2.f1 = pauseReason;((Fiber)localObject2).setState(localS_O2);return 0; case 3:  return 0; case 1:  localS_O2 = (S_O2)((Fiber)localObject2).curState;mboxes = (Mailbox[])localS_O2.f0;pauseReason = (EmptySet_MsgAvListener)localS_O2.f1; }
/* 231 */       for (int i = 0; i < mboxes.length; i++)
/* 232 */         mboxes[i].removeMsgAvailableListener(pauseReason);
/*     */     }
/*     */   }
/*     */   
/*     */   public static int select(Mailbox... paramVarArgs) throws Pausable { Task.errNotWoven();
/*     */     return 0; }
/*     */   
/* 238 */   public synchronized void addSpaceAvailableListener(EventSubscriber spcSub) { this.srcs.add(spcSub); }
/*     */   
/*     */   public synchronized void removeSpaceAvailableListener(EventSubscriber spcSub)
/*     */   {
/* 242 */     this.srcs.remove(spcSub);
/*     */   }
/*     */   
/*     */   public synchronized void addMsgAvailableListener(EventSubscriber msgSub)
/*     */   {
/* 247 */     if ((this.sink != null) && (this.sink != msgSub)) {
/* 248 */       throw new AssertionError("Error: A mailbox can not be shared by two consumers.  New = " + msgSub + ", Old = " + this.sink);
/*     */     }
/*     */     
/*     */ 
/* 252 */     this.sink = msgSub;
/*     */   }
/*     */   
/*     */   public synchronized void removeMsgAvailableListener(EventSubscriber msgSub) {
/* 256 */     if (this.sink == msgSub) {
/* 257 */       this.sink = null;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean putnb(T msg)
/*     */   {
/* 266 */     return put(msg, null);
/*     */   }
/*     */   
/*     */ 
/*     */   public void put(T arg1, Fiber ???)
/*     */     throws Pausable
/*     */   {
/*     */     Object localObject;
/*     */     Task t;
/* 275 */     switch ((localObject = ???).pc) {default:  Fiber.wrongPC(); case 1:  ??? = null; break; case 0:  t = ((Fiber)localObject).task;
/*     */     }
/* 277 */     Task.pause(this, ((Fiber)localObject).down()); S_O2 localS_O2; switch (((Fiber)localObject).up()) {case 2:  localS_O2 = new S_O2();localS_O2.self = this;localS_O2.pc = 1;localS_O2.f0 = msg;localS_O2.f1 = t;((Fiber)localObject).setState(localS_O2);return; case 3:   case 1:  localS_O2 = (S_O2)((Fiber)localObject).curState;msg = localS_O2.f0;t = (Task)localS_O2.f1; }
/*     */   }
/*     */   
/*     */   public void put(T paramT) throws Pausable
/*     */   {}
/*     */   
/*     */   public boolean put(T arg1, int arg2, Fiber ???) throws Pausable {
/*     */     Object localObject2;
/*     */     final Task t;
/*     */     long begin;
/* 287 */     switch ((localObject2 = ???).pc) {default:  Fiber.wrongPC(); case 1:  ??? = null;long l1 = 0L;Object localObject1 = null; break; case 0:  t = ((Fiber)localObject2).task;
/* 288 */       begin = System.currentTimeMillis();
/*     */       
/* 290 */       Object tt = new TimerTask() {
/*     */         public void run() {
/* 292 */           Mailbox.this.removeSpaceAvailableListener(t);
/* 293 */           t.onEvent(Mailbox.this, Mailbox.timedOut);
/*     */         }
/* 295 */       };
/* 296 */       Task.timer.schedule((TimerTask)tt, timeoutMillis); }
/* 297 */     Task.pause(this, ((Fiber)localObject2).down()); S_O2IL localS_O2IL; switch (((Fiber)localObject2).up()) {case 2:  localS_O2IL = new S_O2IL();localS_O2IL.self = this;localS_O2IL.pc = 1;localS_O2IL.f0 = msg;localS_O2IL.f1 = t;localS_O2IL.f2 = timeoutMillis;localS_O2IL.f3 = begin;((Fiber)localObject2).setState(localS_O2IL);return false; case 3:  return false; case 1:  localS_O2IL = (S_O2IL)((Fiber)localObject2).curState;msg = localS_O2IL.f0;timeoutMillis = localS_O2IL.f2;t = (Task)localS_O2IL.f1;begin = localS_O2IL.f3; }
/* 298 */     if (System.currentTimeMillis() - begin >= timeoutMillis) {
/* 299 */       return false;
/*     */     }
/*     */     
/* 302 */     return true; }
/*     */   
/*     */   public boolean put(T paramT, int paramInt) throws Pausable { Task.errNotWoven();
/*     */     return false; }
/*     */   
/* 306 */   public void putb(T msg) { putb(msg, 0L); }
/*     */   
/*     */   public class BlockingSubscriber implements EventSubscriber { public BlockingSubscriber() {}
/*     */     
/* 310 */     public volatile boolean eventRcvd = false;
/*     */     
/* 312 */     public void onEvent(EventPublisher ep, Event e) { synchronized (Mailbox.this) {
/* 313 */         this.eventRcvd = true;
/* 314 */         Mailbox.this.notify();
/*     */       }
/*     */     }
/*     */     
/* 318 */     public void blockingWait(long timeoutMillis) { long start = System.currentTimeMillis();
/* 319 */       long remaining = timeoutMillis;
/* 320 */       boolean infiniteWait = timeoutMillis == 0L;
/* 321 */       synchronized (Mailbox.this) {
/* 322 */         while ((!this.eventRcvd) && ((infiniteWait) || (remaining > 0L))) {
/*     */           try {
/* 324 */             Mailbox.this.wait(infiniteWait ? 0L : remaining);
/*     */           } catch (InterruptedException ie) {}
/* 326 */           long elapsed = System.currentTimeMillis() - start;
/* 327 */           remaining -= elapsed;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void putb(T msg, long timeoutMillis)
/*     */   {
/* 339 */     Mailbox<T>.BlockingSubscriber evs = new BlockingSubscriber();
/* 340 */     if (!put(msg, evs)) {
/* 341 */       evs.blockingWait(timeoutMillis);
/*     */     }
/* 343 */     if (!evs.eventRcvd) {
/* 344 */       removeSpaceAvailableListener(evs);
/*     */     }
/*     */   }
/*     */   
/*     */   public synchronized int size() {
/* 349 */     return this.numMsgs;
/*     */   }
/*     */   
/*     */   public synchronized boolean hasMessage() {
/* 353 */     return this.numMsgs > 0;
/*     */   }
/*     */   
/*     */   public synchronized boolean hasSpace() {
/* 357 */     return this.maxMsgs - this.numMsgs > 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public T getb()
/*     */   {
/* 367 */     return (T)getb(0L);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public T getb(long timeoutMillis)
/*     */   {
/* 377 */     Mailbox<T>.BlockingSubscriber evs = new BlockingSubscriber();
/*     */     
/*     */     T msg;
/* 380 */     if ((msg = get(evs)) == null) {
/* 381 */       evs.blockingWait(timeoutMillis);
/* 382 */       if (evs.eventRcvd) {
/* 383 */         msg = get(null);
/* 384 */         assert (msg != null) : "Received event, but message is null";
/*     */       }
/*     */     }
/* 387 */     if (msg == null) {
/* 388 */       removeMsgAvailableListener(evs);
/*     */     }
/* 390 */     return msg;
/*     */   }
/*     */   
/*     */   public synchronized String toString() {
/* 394 */     return "id:" + System.identityHashCode(this) + " " + "numMsgs:" + this.numMsgs;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isValid(Task t)
/*     */   {
/* 404 */     synchronized (this) {
/* 405 */       return (t == this.sink) || (this.srcs.contains(t));
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/cfeindia/Desktop/kilim.jar!/kilim/Mailbox.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */