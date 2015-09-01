/*    */ package kilim.http;
/*    */ 
/*    */ import java.lang.reflect.Array;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Utils
/*    */ {
/*    */   public static Object[] growArray(Object[] input, int extraRoom)
/*    */   {
/* 13 */     int size = input.length + extraRoom;
/* 14 */     Object[] ret = (Object[])Array.newInstance(input.getClass().getComponentType(), size);
/* 15 */     System.arraycopy(input, 0, ret, 0, input.length);
/* 16 */     return ret;
/*    */   }
/*    */   
/*    */   public static int[] growArray(int[] input, int extraRoom) {
/* 20 */     int size = input.length + extraRoom;
/* 21 */     int[] ret = (int[])Array.newInstance(input.getClass().getComponentType(), size);
/* 22 */     System.arraycopy(input, 0, ret, 0, input.length);
/* 23 */     return ret;
/*    */   }
/*    */ }


/* Location:              /home/cfeindia/Desktop/kilim.jar!/kilim/http/Utils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */