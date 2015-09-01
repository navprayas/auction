/*    */ package kilim.analysis;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Range
/*    */ {
/*    */   int from;
/*    */   
/*    */ 
/*    */ 
/*    */   int to;
/*    */   
/*    */ 
/*    */ 
/*    */   public Range(int aFrom, int aTo)
/*    */   {
/* 18 */     this.from = aFrom;
/* 19 */     this.to = aTo;
/*    */   }
/*    */   
/*    */ 
/*    */   static Range intersect(int a1, int e1, int a2, int e2)
/*    */   {
/* 25 */     assert ((a1 <= e1) && (a2 <= e2));
/*    */     int a;
/* 27 */     if ((a1 <= a2) && (a2 <= e1)) {
/* 28 */       a = a2; } else { int a;
/* 29 */       if ((a2 <= a1) && (a1 <= e2)) {
/* 30 */         a = a1;
/*    */       } else
/* 32 */         return null; }
/*    */     int a;
/* 34 */     return new Range(a, e1 < e2 ? e1 : e2);
/*    */   }
/*    */ }


/* Location:              /home/cfeindia/Desktop/kilim.jar!/kilim/analysis/Range.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */