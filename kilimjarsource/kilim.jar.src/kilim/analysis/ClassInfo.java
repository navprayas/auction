/*    */ package kilim.analysis;
/*    */ 
/*    */ 
/*    */ public class ClassInfo
/*    */ {
/*    */   public String className;
/*    */   
/*    */   public byte[] bytes;
/*    */   
/*    */ 
/*    */   public ClassInfo(String aClassName, byte[] aBytes)
/*    */   {
/* 13 */     this.className = aClassName.replace('.', '/');
/* 14 */     this.bytes = aBytes;
/*    */   }
/*    */ }


/* Location:              /home/cfeindia/Desktop/kilim.jar!/kilim/analysis/ClassInfo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */