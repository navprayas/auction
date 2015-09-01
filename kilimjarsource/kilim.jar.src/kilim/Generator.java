/*    */ package kilim;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import java.util.NoSuchElementException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Generator<T>
/*    */   extends Task
/*    */   implements Iterator<T>, Iterable<T>
/*    */ {
/*    */   T nextVal;
/*    */   public static final boolean $isWoven = true;
/*    */   
/*    */   public boolean hasNext()
/*    */   {
/* 43 */     if (this.nextVal == null) {
/* 44 */       if (isDone())
/* 45 */         return false;
/* 46 */       _runExecute(null);
/* 47 */       return this.nextVal != null;
/*    */     }
/* 49 */     return true;
/*    */   }
/*    */   
/*    */ 
/*    */   public T next()
/*    */   {
/* 55 */     if (this.nextVal != null) {
/* 56 */       T ret = this.nextVal;
/* 57 */       this.nextVal = null;
/* 58 */       return ret;
/*    */     }
/* 60 */     if (isDone()) {
/* 61 */       throw new NoSuchElementException();
/*    */     }
/* 63 */     _runExecute(null);
/* 64 */     T ret = this.nextVal;
/* 65 */     this.nextVal = null;
/* 66 */     return ret;
/*    */   }
/*    */   
/*    */   public void remove() {
/* 70 */     throw new AssertionError("Not Supported");
/*    */   }
/*    */   
/*    */ 
/* 74 */   public Iterator<T> iterator() { return this; }
/*    */   
/*    */   public void yield(T paramT, Fiber paramFiber) throws Pausable {
/*    */     ;
/* 78 */     switch (paramFiber.pc) {default:  Fiber.wrongPC(); case 1:  break; case 0:  this.nextVal = val; }
/* 79 */     Task.yield(paramFiber.down()); switch (paramFiber.up()) {case 2:  State localState = new State();localState.self = this;localState.pc = 1;paramFiber.setState(localState);return;
/*    */     case 3: 
/*    */       
/*    */     }
/*    */   }
/*    */   
/*    */   public void yield(T paramT)
/*    */     throws Pausable
/*    */   {}
/*    */ }


/* Location:              /home/cfeindia/Desktop/kilim.jar!/kilim/Generator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */