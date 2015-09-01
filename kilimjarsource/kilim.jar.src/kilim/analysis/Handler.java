/*    */ package kilim.analysis;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
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
/*    */ public class Handler
/*    */   implements Comparable<Handler>
/*    */ {
/*    */   public int from;
/*    */   public int to;
/*    */   public String type;
/*    */   public BasicBlock catchBB;
/*    */   
/*    */   public Handler(int aFrom, int aTo, String aType, BasicBlock aCatchBB)
/*    */   {
/* 38 */     this.from = aFrom;
/* 39 */     this.to = aTo;
/* 40 */     if (aType == null)
/*    */     {
/*    */ 
/* 43 */       aType = "java/lang/Throwable";
/*    */     }
/* 45 */     this.type = aType;
/* 46 */     this.catchBB = aCatchBB;
/*    */   }
/*    */   
/*    */   public int compareTo(Handler h) {
/* 50 */     int c = this.type.compareTo(h.type);
/* 51 */     if (c != 0) { return c;
/*    */     }
/* 53 */     c = this.catchBB.compareTo(h.catchBB);
/* 54 */     if (c != 0) { return c;
/*    */     }
/* 56 */     return this.from == h.from ? 0 : this.from < h.from ? -1 : 1;
/*    */   }
/*    */   
/*    */   public static ArrayList<Handler> consolidate(ArrayList<Handler> list) {
/* 60 */     Collections.sort(list);
/* 61 */     ArrayList<Handler> newList = new ArrayList(list.size());
/* 62 */     Handler cur = null;
/* 63 */     for (Handler h : list) {
/* 64 */       if (cur == null) {
/* 65 */         cur = h;
/* 66 */         newList.add(cur);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       }
/* 72 */       else if ((cur.type.equals(h.type)) && (cur.catchBB == h.catchBB) && (h.from == cur.to + 1)) {
/* 73 */         cur.to = h.to;
/*    */       } else {
/* 75 */         cur = h;
/* 76 */         newList.add(cur);
/*    */       }
/*    */     }
/* 79 */     return newList;
/*    */   }
/*    */ }


/* Location:              /home/cfeindia/Desktop/kilim.jar!/kilim/analysis/Handler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */