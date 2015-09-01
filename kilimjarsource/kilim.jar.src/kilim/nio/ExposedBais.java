/*    */ package kilim.nio;
/*    */ 
/*    */ import java.io.ByteArrayInputStream;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ExposedBais
/*    */   extends ByteArrayInputStream
/*    */ {
/*    */   public ExposedBais(int size)
/*    */   {
/* 17 */     super(new byte[size]);
/*    */   }
/*    */   
/*    */   public ExposedBais(byte[] buf, int offset, int length) {
/* 21 */     super(buf, offset, length);
/*    */   }
/*    */   
/*    */   public ExposedBais(byte[] buf) {
/* 25 */     super(buf);
/*    */   }
/*    */   
/*    */   public byte[] toByteArray() {
/* 29 */     return this.buf;
/*    */   }
/*    */   
/*    */   public void setCount(int n) {
/* 33 */     this.count = n;
/*    */   }
/*    */ }


/* Location:              /home/cfeindia/Desktop/kilim.jar!/kilim/nio/ExposedBais.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */