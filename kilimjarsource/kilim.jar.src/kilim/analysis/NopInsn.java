/*    */ package kilim.analysis;
/*    */ 
/*    */ import org.objectweb.asm.MethodVisitor;
/*    */ import org.objectweb.asm.tree.AbstractInsnNode;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class NopInsn
/*    */   extends AbstractInsnNode
/*    */ {
/*    */   public NopInsn()
/*    */   {
/* 16 */     super(0);
/*    */   }
/*    */   
/*    */   public int getType() {
/* 20 */     return 0;
/*    */   }
/*    */   
/*    */   public void accept(MethodVisitor mv) {}
/*    */ }


/* Location:              /home/cfeindia/Desktop/kilim.jar!/kilim/analysis/NopInsn.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */