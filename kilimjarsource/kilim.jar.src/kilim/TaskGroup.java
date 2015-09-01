/*    */ package kilim;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashSet;
/*    */ import java.util.List;
/*    */ 
/*    */ public class TaskGroup extends Task
/*    */ {
/*  9 */   private Mailbox<Task> addedTasksMB = new Mailbox();
/* 10 */   private Mailbox<ExitMsg> exitmb = new Mailbox();
/* 11 */   private HashSet<Task> tasks = new HashSet();
/*    */   
/* 13 */   public List<ExitMsg> results = java.util.Collections.synchronizedList(new ArrayList());
/*    */   public static final boolean $isWoven = true;
/*    */   
/* 16 */   public void execute(Fiber ???) throws Pausable { Object localObject; switch ((localObject = ???).pc) {default:  Fiber.wrongPC(); case 1:  break; case 2: case 0:  for (goto 252; (!this.tasks.isEmpty()) || (this.addedTasksMB.hasMessage());) {
/* 17 */         switch (((Fiber)localObject).up()) {case 2:  Mailbox.select(new Mailbox[] { this.addedTasksMB, this.exitmb }, ((Fiber)localObject).down());localState = new State();localState.self = this;localState.pc = 1;((Fiber)localObject).setState(localState);return; case 3:  null; return;
/*    */         }
/*    */         Task t;
/* 16 */         switch (null)
/*    */         {
/*    */         case 0: 
/* 19 */           t = (Task)this.addedTasksMB.getnb();
/* 20 */           t.informOnExit(this.exitmb);
/* 21 */           this.tasks.add(t);
/* 22 */           break;
/*    */         case 1: 
/* 24 */           ExitMsg em = (ExitMsg)this.exitmb.getnb();
/* 25 */           this.results.add(em);
/* 26 */           this.tasks.remove(em.task);
/*    */         }
/*    */       } }
/*    */     State localState;
/* 30 */     exit(this.results, ((Fiber)localObject).down()); switch (((Fiber)localObject).up()) {case 2:  localState = new State();localState.self = this;localState.pc = 2;((Fiber)localObject).setState(localState);return; case 3:   } }
/*    */   
/*    */   public void execute() throws Pausable
/*    */   {}
/*    */   
/* 35 */   public ExitMsg joinb() { start();
/* 36 */     return super.joinb();
/*    */   }
/*    */   
/*    */   /* Error */
/*    */   public ExitMsg join(Fiber arg1)
/*    */     throws Pausable
/*    */   {
/*    */     // Byte code:
/*    */     //   0: aload_1
/*    */     //   1: getfield 58	kilim/Fiber:pc	I
/*    */     //   4: tableswitch	default:+24->28, 0:+31->35, 1:+27->31
/*    */     //   28: invokestatic 61	kilim/Fiber:wrongPC	()V
/*    */     //   31: aload_0
/*    */     //   32: goto +9 -> 41
/*    */     //   35: aload_0
/*    */     //   36: invokevirtual 132	kilim/TaskGroup:start	()Lkilim/Task;
/*    */     //   39: pop
/*    */     //   40: aload_0
/*    */     //   41: aload_1
/*    */     //   42: invokevirtual 72	kilim/Fiber:down	()Lkilim/Fiber;
/*    */     //   45: invokespecial 138	kilim/Task:join	(Lkilim/Fiber;)Lkilim/ExitMsg;
/*    */     //   48: aload_1
/*    */     //   49: invokevirtual 80	kilim/Fiber:up	()I
/*    */     //   52: tableswitch	default:+61->113, 0:+61->113, 1:+61->113, 2:+32->84, 3:+58->110
/*    */     //   84: pop
/*    */     //   85: new 82	kilim/State
/*    */     //   88: dup
/*    */     //   89: invokespecial 83	kilim/State:<init>	()V
/*    */     //   92: astore_2
/*    */     //   93: aload_2
/*    */     //   94: aload_0
/*    */     //   95: putfield 87	kilim/State:self	Ljava/lang/Object;
/*    */     //   98: aload_2
/*    */     //   99: iconst_1
/*    */     //   100: putfield 88	kilim/State:pc	I
/*    */     //   103: aload_1
/*    */     //   104: aload_2
/*    */     //   105: invokevirtual 92	kilim/Fiber:setState	(Lkilim/State;)V
/*    */     //   108: aconst_null
/*    */     //   109: areturn
/*    */     //   110: pop
/*    */     //   111: aconst_null
/*    */     //   112: areturn
/*    */     //   113: areturn
/*    */     // Line number table:
/*    */     //   Java source line #41	-> byte code offset #35
/*    */     //   Java source line #42	-> byte code offset #40
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	signature
/*    */     //   35	79	0	this	TaskGroup
/*    */   }
/*    */   
/*    */   public ExitMsg join()
/*    */     throws Pausable
/*    */   {
/*    */     Task.errNotWoven();
/*    */     return null;
/*    */   }
/*    */   
/*    */   public void add(Task t)
/*    */   {
/* 46 */     t.informOnExit(this.exitmb);
/* 47 */     this.addedTasksMB.putnb(t);
/*    */   }
/*    */ }


/* Location:              /home/cfeindia/Desktop/kilim.jar!/kilim/TaskGroup.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */