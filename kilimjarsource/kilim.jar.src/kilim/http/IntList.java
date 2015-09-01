/*    */ package kilim.http;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class IntList
/*    */ {
/*    */   public int[] array;
/*    */   
/*    */ 
/*    */   public int numElements;
/*    */   
/*    */ 
/* 13 */   public IntList(int initialSize) { this.array = new int[initialSize]; }
/*    */   
/* 15 */   public IntList() { this(10); }
/*    */   
/*    */   public void add(int element) {
/* 18 */     if (this.numElements == this.array.length) {
/* 19 */       this.array = ((int[])Utils.growArray(this.array, this.array.length * 3 / 2));
/*    */     }
/* 21 */     this.array[(this.numElements++)] = element;
/*    */   }
/*    */   
/*    */   public int get(int index) {
/* 25 */     return this.array[index];
/*    */   }
/*    */ }


/* Location:              /home/cfeindia/Desktop/kilim.jar!/kilim/http/IntList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */