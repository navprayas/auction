/*     */ package kilim;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.lang.reflect.Field;
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
/*     */ public final class Fiber
/*     */ {
/*     */   public State curState;
/*     */   public int pc;
/*  44 */   private State[] stateStack = new State[10];
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  49 */   private int iStack = -1;
/*     */   
/*     */   boolean isPausing;
/*     */   
/*     */   boolean isDone;
/*     */   public Task task;
/*     */   private static final State PAUSE_STATE;
/*     */   public static final int NOT_PAUSING__NO_STATE = 0;
/*     */   public static final int NOT_PAUSING__HAS_STATE = 1;
/*     */   public static final int PAUSING__NO_STATE = 2;
/*     */   public static final int PAUSING__HAS_STATE = 3;
/*     */   
/*     */   static
/*     */   {
/*  63 */     PAUSE_STATE = new State();
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
/*     */ 
/*     */ 
/*     */ 
/*  89 */     PAUSE_STATE.pc = 1;
/*     */   }
/*     */   
/*     */   public Fiber(Task t) {
/*  93 */     this.task = t;
/*     */   }
/*     */   
/*     */   public Task task() {
/*  97 */     return this.task;
/*     */   }
/*     */   
/*     */   public boolean isDone() {
/* 101 */     return this.isDone;
/*     */   }
/*     */   
/*     */   public static void pause() throws Pausable {
/* 105 */     throw new IllegalStateException("pause() called without weaving");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void pause(Fiber f)
/*     */   {
/* 116 */     f.togglePause();
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
/*     */   public int up()
/*     */   {
/* 135 */     int d = this.iStack;
/* 136 */     this.iStack = (--d);
/* 137 */     if (this.isPausing)
/*     */     {
/*     */ 
/* 140 */       return this.stateStack[d] == null ? 2 : 3;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 146 */     State[] stack = this.stateStack;
/* 147 */     State cs = this.curState = stack[d];
/* 148 */     if (cs == null) {
/* 149 */       this.pc = 0;
/*     */       
/*     */ 
/* 152 */       return 0;
/*     */     }
/* 154 */     stack[d] = null;
/* 155 */     this.pc = cs.pc;
/*     */     
/*     */ 
/* 158 */     return 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final Fiber begin()
/*     */   {
/* 165 */     return down();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final boolean end()
/*     */   {
/* 172 */     assert (this.iStack == 0) : ("Reset: Expected iStack == 0, not " + this.iStack + "\n" + this);
/* 173 */     boolean isDone = !this.isPausing;
/*     */     
/* 175 */     if (isDone)
/*     */     {
/* 177 */       this.stateStack[0] = null;
/*     */     }
/*     */     
/* 180 */     this.isPausing = false;
/* 181 */     this.iStack = -1;
/*     */     
/*     */ 
/* 184 */     return isDone;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Fiber down()
/*     */   {
/* 195 */     int d = ++this.iStack;
/* 196 */     if (d >= this.stateStack.length)
/*     */     {
/* 198 */       ensureSize(d * 2);
/* 199 */       this.pc = 0;
/* 200 */       this.curState = null;
/*     */     } else {
/* 202 */       State s = this.stateStack[d];
/* 203 */       this.curState = s;
/* 204 */       this.pc = (s == null ? 0 : s.pc);
/*     */     }
/*     */     
/*     */ 
/* 208 */     return this;
/*     */   }
/*     */   
/*     */   static void ds() {
/* 212 */     for (StackTraceElement ste : new Exception().getStackTrace()) {
/* 213 */       String cl = ste.getClassName();
/* 214 */       String meth = ste.getMethodName();
/* 215 */       if ((!cl.startsWith("kilim.Worker")) && (!meth.equals("go")) && (!meth.equals("ds"))) {
/* 216 */         String line = ":" + ste.getLineNumber();
/* 217 */         System.out.println('\t' + cl + '.' + ste.getMethodName() + '(' + ste.getFileName() + line + ')');
/*     */       }
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
/*     */   public int upEx()
/*     */   {
/* 247 */     int is = this.task.getStackDepth() - 2;
/* 248 */     State cs = this.stateStack[is];
/*     */     
/* 250 */     for (int i = this.iStack; i >= is; i--) {
/* 251 */       this.stateStack[i] = null;
/*     */     }
/*     */     
/* 254 */     this.iStack = is;
/* 255 */     this.curState = cs;
/* 256 */     return cs == null ? 0 : cs.pc;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object getCallee()
/*     */   {
/* 265 */     assert (this.stateStack[this.iStack] != PAUSE_STATE) : "No callee: this state is the pause state";
/* 266 */     assert (this.stateStack[this.iStack] != null) : "Callee is null";
/* 267 */     return this.stateStack[(this.iStack + 1)].self;
/*     */   }
/*     */   
/*     */   private State[] ensureSize(int newsize)
/*     */   {
/* 272 */     State[] newStack = new State[newsize];
/* 273 */     System.arraycopy(this.stateStack, 0, newStack, 0, this.stateStack.length);
/* 274 */     this.stateStack = newStack;
/* 275 */     return newStack;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setState(State state)
/*     */   {
/* 285 */     this.stateStack[this.iStack] = state;
/* 286 */     this.isPausing = true;
/*     */   }
/*     */   
/*     */   public State getState()
/*     */   {
/* 291 */     return this.stateStack[this.iStack];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   void togglePause()
/*     */   {
/* 299 */     if (this.curState == null) {
/* 300 */       setState(PAUSE_STATE);
/*     */     } else {
/* 302 */       assert (this.curState == PAUSE_STATE) : ("togglePause: Expected PAUSE_STATE, instead got: iStack == " + this.iStack + ", state = " + this.curState);
/* 303 */       this.stateStack[this.iStack] = null;
/* 304 */       this.isPausing = false;
/*     */     }
/*     */   }
/*     */   
/*     */   public String toString() {
/* 309 */     StringBuilder sb = new StringBuilder(40);
/* 310 */     sb.append("iStack = ").append(this.iStack).append(", pc = ").append(this.pc);
/* 311 */     if (this.isPausing) {
/* 312 */       sb.append(" pausing");
/*     */     }
/* 314 */     sb.append('\n');
/* 315 */     for (int i = 0; i < this.stateStack.length; i++) {
/* 316 */       State st = this.stateStack[i];
/* 317 */       if (st != null) {
/* 318 */         sb.append(st.getClass().getName()).append('[').append(i).append("]: ");
/* 319 */         stateToString(sb, this.stateStack[i]);
/*     */       }
/*     */     }
/* 322 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public void wrongPC() {
/* 326 */     throw new IllegalStateException("Wrong pc: " + this.pc);
/*     */   }
/*     */   
/*     */   private static void stateToString(StringBuilder sb, State s) {
/* 330 */     if (s == PAUSE_STATE) {
/* 331 */       sb.append("PAUSE\n");
/* 332 */       return;
/*     */     }
/* 334 */     Field[] fs = s.getClass().getFields();
/* 335 */     for (int i = 0; i < fs.length; i++) {
/* 336 */       Field f = fs[i];
/* 337 */       sb.append(f.getName()).append(" = ");
/*     */       Object v;
/*     */       try {
/* 340 */         v = f.get(s);
/*     */       } catch (IllegalAccessException iae) {
/* 342 */         v = "?";
/*     */       }
/* 344 */       sb.append(' ').append(v).append(' ');
/*     */     }
/* 346 */     sb.append('\n');
/*     */   }
/*     */   
/*     */   void clearPausing() {
/* 350 */     this.isPausing = false;
/*     */   }
/*     */ }


/* Location:              /home/cfeindia/Desktop/kilim.jar!/kilim/Fiber.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */