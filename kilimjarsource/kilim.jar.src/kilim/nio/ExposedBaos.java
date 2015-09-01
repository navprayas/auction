/*    */ package kilim.nio;
/*    */ 
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.nio.ByteBuffer;
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
/*    */ public class ExposedBaos
/*    */   extends ByteArrayOutputStream
/*    */ {
/*    */   public ExposedBaos() {}
/*    */   
/*    */   public ExposedBaos(int size)
/*    */   {
/* 23 */     super(size);
/*    */   }
/*    */   
/*    */   public byte[] toByteArray()
/*    */   {
/* 28 */     return this.buf;
/*    */   }
/*    */   
/*    */   public ByteBuffer toByteBuffer() {
/* 32 */     return ByteBuffer.wrap(this.buf, 0, this.count);
/*    */   }
/*    */   
/*    */   public void setCount(int n) {
/* 36 */     this.count = n;
/*    */   }
/*    */ }


/* Location:              /home/cfeindia/Desktop/kilim.jar!/kilim/nio/ExposedBaos.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */