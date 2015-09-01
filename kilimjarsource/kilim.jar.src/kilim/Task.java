/*     */ package kilim;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Timer;
/*     */ import java.util.TimerTask;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
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
/*     */ public abstract class Task
/*     */   implements EventSubscriber
/*     */ {
/*  23 */   public volatile Thread currentThread = null;
/*     */   
/*  25 */   static PauseReason yieldReason = new YieldReason();
/*     */   
/*     */ 
/*     */   public final int id;
/*     */   
/*  30 */   static final AtomicInteger idSource = new AtomicInteger();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Fiber fiber;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected PauseReason pauseReason;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  52 */   protected boolean running = false;
/*  53 */   protected boolean done = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   volatile WorkerThread preferredResumeThread;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   int numActivePins;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private LinkedList<Mailbox<ExitMsg>> exitMBs;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Scheduler scheduler;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  84 */   public Object exitResult = "OK";
/*     */   
/*     */ 
/*  87 */   public static final Timer timer = new Timer(true);
/*     */   public static final boolean $isWoven = true;
/*     */   
/*  90 */   public Task() { this.id = idSource.incrementAndGet();
/*  91 */     this.fiber = new Fiber(this);
/*     */   }
/*     */   
/*     */   public int id() {
/*  95 */     return this.id;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public synchronized Task setScheduler(Scheduler s)
/*     */   {
/* 102 */     this.scheduler = s;
/* 103 */     return this;
/*     */   }
/*     */   
/*     */ 
/* 107 */   public synchronized Scheduler getScheduler() { return this.scheduler; }
/*     */   
/*     */   public void resumeOnScheduler(Scheduler paramScheduler, Fiber paramFiber) throws Pausable {
/*     */     ;
/* 111 */     switch (paramFiber.pc) {default:  Fiber.wrongPC(); case 1:  break; case 0:  if (this.scheduler == s) return;
/* 112 */       this.scheduler = s; }
/* 113 */     yield(paramFiber.down()); switch (paramFiber.up()) {case 2:  State localState = new State();localState.self = this;localState.pc = 1;paramFiber.setState(localState);return;
/*     */     case 3: 
/*     */       
/*     */     }
/*     */   }
/*     */   
/*     */   public void resumeOnScheduler(Scheduler paramScheduler) throws Pausable
/*     */   {}
/*     */   
/*     */   public Task start() {
/* 123 */     if (this.scheduler == null) {
/* 124 */       setScheduler(Scheduler.getDefaultScheduler());
/*     */     }
/* 126 */     resume();
/* 127 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getStackDepth()
/*     */   {
/* 139 */     StackTraceElement[] stes = new Exception().getStackTrace();
/* 140 */     int len = stes.length;
/* 141 */     for (int i = 0; i < len; i++) {
/* 142 */       StackTraceElement ste = stes[i];
/* 143 */       if (ste.getMethodName().equals("_runExecute"))
/*     */       {
/* 145 */         return i - 1;
/*     */       }
/*     */     }
/* 148 */     throw new AssertionError("Expected task to be run by WorkerThread");
/*     */   }
/*     */   
/*     */   public void onEvent(EventPublisher ep, Event e) {
/* 152 */     resume();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean resume()
/*     */   {
/* 159 */     if (this.scheduler == null) { return false;
/*     */     }
/* 161 */     boolean doSchedule = false;
/*     */     
/*     */ 
/*     */ 
/* 165 */     synchronized (this) {
/* 166 */       if ((this.done) || (this.running)) return false;
/* 167 */       this.running = (doSchedule = 1);
/*     */     }
/* 169 */     if (doSchedule) {
/* 170 */       this.scheduler.schedule(this);
/*     */     }
/* 172 */     return doSchedule;
/*     */   }
/*     */   
/*     */   public void informOnExit(Mailbox<ExitMsg> exit) {
/* 176 */     if (isDone()) {
/* 177 */       exit.putnb(new ExitMsg(this, this.exitResult));
/* 178 */       return;
/*     */     }
/* 180 */     synchronized (this) {
/* 181 */       if (this.exitMBs == null) this.exitMBs = new LinkedList();
/* 182 */       this.exitMBs.add(exit);
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
/* 194 */   public static Task getCurrentTask(Fiber paramFiber)
/* 194 */     throws Pausable { return null; }
/*     */   
/*     */   public static Task getCurrentTask() throws Pausable {
/*     */     errNotWoven();
/*     */     return null;
/*     */   }
/*     */   
/*     */   public static void exit(Object aExitValue) throws Pausable
/*     */   {}
/*     */   
/* 203 */   public static void exit(Object aExitValue, Fiber f) { assert (f.pc == 0) : "f.pc != 0";
/* 204 */     f.task.setPauseReason(new TaskDoneReason(aExitValue));
/* 205 */     f.togglePause();
/*     */   }
/*     */   
/*     */ 
/*     */   public static void errorExit(Throwable ex)
/*     */     throws Pausable
/*     */   {}
/*     */   
/*     */   public static void errorExit(Throwable ex, Fiber f)
/*     */   {
/* 215 */     assert (f.pc == 0) : "fc.pc != 0";
/* 216 */     f.task.setPauseReason(new TaskDoneReason(ex));
/* 217 */     f.togglePause();
/*     */   }
/*     */   
/*     */   public static void errNotWoven() {
/* 221 */     System.err.println("############################################################");
/* 222 */     System.err.println("Task has either not been woven or the classpath is incorrect");
/* 223 */     System.err.println("############################################################");
/* 224 */     Thread.dumpStack();
/* 225 */     System.exit(0);
/*     */   }
/*     */   
/*     */   public static void errNotWoven(Task t) {
/* 229 */     System.err.println("############################################################");
/* 230 */     System.err.println("Task " + t.getClass() + " has either not been woven or the classpath is incorrect");
/* 231 */     System.err.println("############################################################");
/* 232 */     Thread.dumpStack();
/* 233 */     System.exit(0);
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
/*     */   public static Object invoke(Method arg0, Object arg1, Object[] arg2, Fiber ???)
/*     */     throws Pausable, IllegalAccessException, IllegalArgumentException, InvocationTargetException
/*     */   {
/* 258 */     Object localObject1 = ???;Fiber f = ((Fiber)localObject1).task.fiber;
/*     */     Object[] fargs;
/* 260 */     if (f.pc == 0) {
/* 261 */       mthd = getWovenMethod(mthd);
/*     */       Object[] fargs;
/* 263 */       Object[] fargs; if (args == null) {
/* 264 */         fargs = new Object[1];
/*     */       } else {
/* 266 */         fargs = new Object[args.length + 1];
/* 267 */         System.arraycopy(args, 0, fargs, 0, args.length);
/*     */       }
/* 269 */       fargs[(fargs.length - 1)] = f;
/*     */     }
/*     */     else {
/* 272 */       ArgState as = (ArgState)f.getState();
/* 273 */       mthd = (Method)as.mthd;
/* 274 */       target = as.obj;
/* 275 */       fargs = as.fargs;
/*     */     }
/* 277 */     f.down();
/* 278 */     Object ret = mthd.invoke(target, fargs);
/* 279 */     switch (f.up()) {
/*     */     case 0: 
/*     */     case 1: 
/* 282 */       return ret;
/*     */     case 2: 
/* 284 */       ArgState as = new ArgState();
/* 285 */       as.fargs = fargs;
/* 286 */       as.pc = 1;
/* 287 */       as.mthd = mthd;
/* 288 */       f.setState(as);
/* 289 */       return null;
/*     */     case 3: 
/* 291 */       return null;
/*     */     }
/* 293 */     throw new IllegalAccessException("Internal Error");
/*     */   }
/*     */   
/*     */   public static Object invoke(Method paramMethod, Object paramObject, Object... paramVarArgs) throws Pausable, IllegalAccessException, IllegalArgumentException, InvocationTargetException { errNotWoven();
/*     */     return null; }
/*     */   
/* 298 */   private static Method getWovenMethod(Method m) { Class<?>[] ptypes = m.getParameterTypes();
/* 299 */     if ((ptypes.length <= 0) || (!ptypes[(ptypes.length - 1)].getName().equals("kilim.Fiber")))
/*     */     {
/*     */ 
/* 302 */       boolean found = false;
/*     */       label158:
/* 304 */       for (Method wm : m.getDeclaringClass().getDeclaredMethods())
/* 305 */         if ((wm != m) && (wm.getName().equals(m.getName())))
/*     */         {
/* 307 */           Class<?>[] wptypes = wm.getParameterTypes();
/* 308 */           if ((wptypes.length == ptypes.length + 1) && (wptypes[(wptypes.length - 1)].getName().equals("kilim.Fiber")))
/*     */           {
/* 310 */             for (int i = 0; i < ptypes.length; i++)
/* 311 */               if (ptypes[i] != wptypes[i])
/*     */                 break label158;
/* 313 */             m = wm;
/* 314 */             found = true;
/* 315 */             break;
/*     */           }
/*     */         }
/* 318 */       if (!found) {
/* 319 */         throw new IllegalArgumentException("Found no pausable method corresponding to supplied method: " + m);
/*     */       }
/*     */     }
/* 322 */     return m;
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public static void sleep(long arg0, Fiber arg2)
/*     */     throws Pausable
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_2
/*     */     //   1: dup
/*     */     //   2: astore_3
/*     */     //   3: getfield 88	kilim/Fiber:pc	I
/*     */     //   6: tableswitch	default:+22->28, 0:+37->43, 1:+25->31
/*     */     //   28: invokestatic 91	kilim/Fiber:wrongPC	()V
/*     */     //   31: aconst_null
/*     */     //   32: astore_2
/*     */     //   33: aload_3
/*     */     //   34: invokevirtual 363	kilim/Fiber:getCallee	()Ljava/lang/Object;
/*     */     //   37: checkcast 187	kilim/Mailbox
/*     */     //   40: goto +28 -> 68
/*     */     //   43: new 187	kilim/Mailbox
/*     */     //   46: dup
/*     */     //   47: iconst_1
/*     */     //   48: invokespecial 365	kilim/Mailbox:<init>	(I)V
/*     */     //   51: astore_2
/*     */     //   52: getstatic 367	kilim/Task:timer	Ljava/util/Timer;
/*     */     //   55: new 12	kilim/Task$1
/*     */     //   58: dup
/*     */     //   59: aload_2
/*     */     //   60: invokespecial 369	kilim/Task$1:<init>	(Lkilim/Mailbox;)V
/*     */     //   63: lload_0
/*     */     //   64: invokevirtual 374	java/util/Timer:schedule	(Ljava/util/TimerTask;J)V
/*     */     //   67: aload_2
/*     */     //   68: aload_3
/*     */     //   69: invokevirtual 95	kilim/Fiber:down	()Lkilim/Fiber;
/*     */     //   72: invokevirtual 378	kilim/Mailbox:get	(Lkilim/Fiber;)Ljava/lang/Object;
/*     */     //   75: aload_3
/*     */     //   76: invokevirtual 102	kilim/Fiber:up	()I
/*     */     //   79: tableswitch	default:+54->133, 0:+54->133, 1:+54->133, 2:+29->108, 3:+52->131
/*     */     //   108: pop
/*     */     //   109: new 104	kilim/State
/*     */     //   112: dup
/*     */     //   113: invokespecial 105	kilim/State:<init>	()V
/*     */     //   116: astore 4
/*     */     //   118: aload 4
/*     */     //   120: iconst_1
/*     */     //   121: putfield 109	kilim/State:pc	I
/*     */     //   124: aload_3
/*     */     //   125: aload 4
/*     */     //   127: invokevirtual 113	kilim/Fiber:setState	(Lkilim/State;)V
/*     */     //   130: return
/*     */     //   131: pop
/*     */     //   132: return
/*     */     //   133: pop
/*     */     //   134: return
/*     */     // Line number table:
/*     */     //   Java source line #331	-> byte code offset #43
/*     */     //   Java source line #332	-> byte code offset #52
/*     */     //   Java source line #337	-> byte code offset #67
/*     */     //   Java source line #338	-> byte code offset #134
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   43	92	0	millis	long
/*     */     //   52	83	2	sleepmb	Mailbox<Integer>
/*     */   }
/*     */   
/*     */   public static void sleep(long paramLong)
/*     */     throws Pausable
/*     */   {}
/*     */   
/*     */   public static void yield()
/*     */     throws Pausable
/*     */   {}
/*     */   
/*     */   public static void yield(Fiber f)
/*     */   {
/* 345 */     if (f.pc == 0) {
/* 346 */       f.task.setPauseReason(yieldReason);
/*     */     } else {
/* 348 */       f.task.setPauseReason(null);
/*     */     }
/* 350 */     f.togglePause();
/*     */   }
/*     */   
/*     */ 
/*     */   public static void pause(PauseReason pauseReason)
/*     */     throws Pausable
/*     */   {}
/*     */   
/*     */ 
/*     */   public static void pause(PauseReason pauseReason, Fiber f)
/*     */   {
/* 361 */     if (f.pc == 0) {
/* 362 */       f.task.setPauseReason(pauseReason);
/*     */     } else {
/* 364 */       f.task.setPauseReason(null);
/*     */     }
/* 366 */     f.togglePause();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void execute()
/*     */     throws Pausable, Exception
/*     */   {
/* 378 */     errNotWoven(this);
/*     */   }
/*     */   
/*     */   public void execute(Fiber f) throws Exception {
/* 382 */     errNotWoven(this);
/*     */   }
/*     */   
/*     */   public String toString() {
/* 386 */     return "" + this.id + "(running=" + this.running + ",pr=" + this.pauseReason + ")";
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public String dump()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: dup
/*     */     //   2: astore_1
/*     */     //   3: monitorenter
/*     */     //   4: new 256	java/lang/StringBuilder
/*     */     //   7: dup
/*     */     //   8: invokespecial 257	java/lang/StringBuilder:<init>	()V
/*     */     //   11: ldc_w 392
/*     */     //   14: invokevirtual 263	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   17: aload_0
/*     */     //   18: getfield 65	kilim/Task:id	I
/*     */     //   21: invokevirtual 395	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
/*     */     //   24: ldc_w 397
/*     */     //   27: invokevirtual 263	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   30: aload_0
/*     */     //   31: getfield 49	kilim/Task:running	Z
/*     */     //   34: invokevirtual 400	java/lang/StringBuilder:append	(Z)Ljava/lang/StringBuilder;
/*     */     //   37: ldc_w 409
/*     */     //   40: invokevirtual 263	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   43: aload_0
/*     */     //   44: getfield 404	kilim/Task:pauseReason	Lkilim/PauseReason;
/*     */     //   47: invokevirtual 270	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*     */     //   50: ldc_w 406
/*     */     //   53: invokevirtual 263	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   56: invokevirtual 275	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   59: aload_1
/*     */     //   60: monitorexit
/*     */     //   61: areturn
/*     */     //   62: astore_2
/*     */     //   63: aload_1
/*     */     //   64: monitorexit
/*     */     //   65: aload_2
/*     */     //   66: athrow
/*     */     // Line number table:
/*     */     //   Java source line #390	-> byte code offset #0
/*     */     //   Java source line #391	-> byte code offset #4
/*     */     //   Java source line #395	-> byte code offset #62
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	67	0	this	Task
/*     */     //   2	62	1	Ljava/lang/Object;	Object
/*     */     //   62	4	2	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   4	61	62	finally
/*     */     //   62	65	62	finally
/*     */   }
/*     */   
/*     */   public void pinToThread()
/*     */   {
/* 399 */     this.numActivePins += 1;
/*     */   }
/*     */   
/*     */   public void unpinFromThread() {
/* 403 */     this.numActivePins -= 1;
/*     */   }
/*     */   
/*     */   protected final void setPauseReason(PauseReason pr)
/*     */   {
/* 408 */     this.pauseReason = pr;
/*     */   }
/*     */   
/*     */   public final PauseReason getPauseReason() {
/* 412 */     return this.pauseReason;
/*     */   }
/*     */   
/*     */   public synchronized boolean isDone()
/*     */   {
/* 417 */     return this.done;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void _runExecute(WorkerThread thread)
/*     */     throws NotPausable
/*     */   {
/* 426 */     Fiber f = this.fiber;
/* 427 */     boolean isDone = false;
/*     */     try {
/* 429 */       this.currentThread = Thread.currentThread();
/* 430 */       assert ((this.preferredResumeThread == null) || (this.preferredResumeThread == thread)) : ("Resumed " + this.id + " in incorrect thread. ");
/*     */       
/* 432 */       execute(f.begin());
/*     */       
/*     */ 
/* 435 */       isDone = (f.end()) || ((this.pauseReason instanceof TaskDoneReason));
/* 436 */       if ((!$assertionsDisabled) && ((this.pauseReason != null) || (!isDone)) && ((this.pauseReason == null) || (isDone))) throw new AssertionError("pauseReason:" + this.pauseReason + ",isDone =" + isDone);
/*     */     } catch (Throwable th) {
/* 438 */       th.printStackTrace();
/*     */       
/* 440 */       setPauseReason(new TaskDoneReason(th));
/* 441 */       isDone = true;
/*     */     }
/*     */     
/* 444 */     if (isDone) {
/* 445 */       this.done = true;
/*     */       
/* 447 */       if (this.numActivePins > 0)
/* 448 */         throw new AssertionError("Task ended but has active locks");
/*     */       ExitMsg msg;
/* 450 */       if (this.exitMBs != null) {
/* 451 */         if ((this.pauseReason instanceof TaskDoneReason)) {
/* 452 */           this.exitResult = ((TaskDoneReason)this.pauseReason).exitObj;
/*     */         }
/* 454 */         msg = new ExitMsg(this, this.exitResult);
/* 455 */         for (Mailbox<ExitMsg> exitMB : this.exitMBs) {
/* 456 */           exitMB.putnb(msg);
/*     */         }
/*     */       }
/* 459 */       this.preferredResumeThread = null;
/*     */     } else {
/* 461 */       if (thread != null) {
/* 462 */         if (this.numActivePins > 0) {
/* 463 */           this.preferredResumeThread = thread;
/*     */         } else {
/* 465 */           assert (this.numActivePins == 0) : ("numActivePins == " + this.numActivePins);
/* 466 */           this.preferredResumeThread = null;
/*     */         }
/*     */       }
/*     */       
/* 470 */       PauseReason pr = this.pauseReason;
/* 471 */       synchronized (this) {
/* 472 */         this.running = false;
/* 473 */         this.currentThread = null;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 480 */       if (!pr.isValid(this)) {
/* 481 */         resume();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public ExitMsg joinb() {
/* 487 */     Mailbox<ExitMsg> mb = new Mailbox();
/* 488 */     informOnExit(mb);
/* 489 */     return (ExitMsg)mb.getb();
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public ExitMsg join(Fiber arg1)
/*     */     throws Pausable
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_1
/*     */     //   1: dup
/*     */     //   2: astore_2
/*     */     //   3: getfield 88	kilim/Fiber:pc	I
/*     */     //   6: tableswitch	default:+22->28, 0:+37->43, 1:+25->31
/*     */     //   28: invokestatic 91	kilim/Fiber:wrongPC	()V
/*     */     //   31: aconst_null
/*     */     //   32: astore_1
/*     */     //   33: aload_2
/*     */     //   34: invokevirtual 363	kilim/Fiber:getCallee	()Ljava/lang/Object;
/*     */     //   37: checkcast 187	kilim/Mailbox
/*     */     //   40: goto +17 -> 57
/*     */     //   43: new 187	kilim/Mailbox
/*     */     //   46: dup
/*     */     //   47: invokespecial 479	kilim/Mailbox:<init>	()V
/*     */     //   50: astore_1
/*     */     //   51: aload_0
/*     */     //   52: aload_1
/*     */     //   53: invokevirtual 481	kilim/Task:informOnExit	(Lkilim/Mailbox;)V
/*     */     //   56: aload_1
/*     */     //   57: aload_2
/*     */     //   58: invokevirtual 95	kilim/Fiber:down	()Lkilim/Fiber;
/*     */     //   61: invokevirtual 378	kilim/Mailbox:get	(Lkilim/Fiber;)Ljava/lang/Object;
/*     */     //   64: aload_2
/*     */     //   65: invokevirtual 102	kilim/Fiber:up	()I
/*     */     //   68: tableswitch	default:+61->129, 0:+61->129, 1:+61->129, 2:+32->100, 3:+58->126
/*     */     //   100: pop
/*     */     //   101: new 104	kilim/State
/*     */     //   104: dup
/*     */     //   105: invokespecial 105	kilim/State:<init>	()V
/*     */     //   108: astore_3
/*     */     //   109: aload_3
/*     */     //   110: aload_0
/*     */     //   111: putfield 108	kilim/State:self	Ljava/lang/Object;
/*     */     //   114: aload_3
/*     */     //   115: iconst_1
/*     */     //   116: putfield 109	kilim/State:pc	I
/*     */     //   119: aload_2
/*     */     //   120: aload_3
/*     */     //   121: invokevirtual 113	kilim/Fiber:setState	(Lkilim/State;)V
/*     */     //   124: aconst_null
/*     */     //   125: areturn
/*     */     //   126: pop
/*     */     //   127: aconst_null
/*     */     //   128: areturn
/*     */     //   129: checkcast 182	kilim/ExitMsg
/*     */     //   132: areturn
/*     */     // Line number table:
/*     */     //   Java source line #493	-> byte code offset #43
/*     */     //   Java source line #494	-> byte code offset #51
/*     */     //   Java source line #495	-> byte code offset #56
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   43	90	0	this	Task
/*     */     //   51	82	1	mb	Mailbox<ExitMsg>
/*     */   }
/*     */   
/*     */   public ExitMsg join()
/*     */     throws Pausable
/*     */   {
/*     */     errNotWoven();
/*     */     return null;
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj)
/*     */   {
/* 500 */     return obj == this;
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 505 */     return this.id;
/*     */   }
/*     */   
/*     */   static class ArgState
/*     */     extends State
/*     */   {
/*     */     Object mthd;
/*     */     Object obj;
/*     */     Object[] fargs;
/*     */   }
/*     */ }


/* Location:              /home/cfeindia/Desktop/kilim.jar!/kilim/Task.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */