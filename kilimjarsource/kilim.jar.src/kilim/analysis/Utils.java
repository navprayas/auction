/*    */ package kilim.analysis;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Utils
/*    */ {
/* 14 */   public static String indentStr = "";
/* 15 */   public static String spaces = "                                        ";
/*    */   
/*    */   public static void indentWith(String s) {
/* 18 */     indentStr += s;
/*    */   }
/*    */   
/*    */   public static void indent(int numSpaces) {
/* 22 */     indentWith(spaces.substring(0, numSpaces));
/*    */   }
/*    */   
/*    */   public static void dedent(int numSpaces) {
/* 26 */     indentStr = indentStr.substring(0, indentStr.length() - numSpaces);
/*    */   }
/*    */   
/*    */   public static String format(String s) {
/* 30 */     if (indentStr.length() == 0)
/* 31 */       return s;
/* 32 */     int i = s.indexOf('\n');
/* 33 */     if (i >= 0) {
/* 34 */       StringBuffer sb = new StringBuffer(100);
/* 35 */       sb.append(indentStr);
/* 36 */       int prev = 0;
/*    */       do
/*    */       {
/* 39 */         sb.append(s, prev, i + 1);
/*    */         
/* 41 */         sb.append(indentStr);
/* 42 */         prev = i + 1;
/* 43 */         if (prev >= s.length())
/*    */           break;
/* 45 */         i = s.indexOf('\n', prev);
/* 46 */       } while (i != -1);
/*    */       
/* 48 */       sb.append(s, prev, s.length());
/* 49 */       return sb.toString();
/*    */     }
/* 51 */     return indentStr + s;
/*    */   }
/*    */   
/*    */   public static void resetIndentation()
/*    */   {
/* 56 */     indentStr = "";
/*    */   }
/*    */   
/*    */   public static void p(String s) {
/* 60 */     System.out.print(format(s));
/*    */   }
/*    */   
/*    */   public static void pn(String s) {
/* 64 */     System.out.println(format(s));
/*    */   }
/*    */   
/*    */   public static void pn(int i) {
/* 68 */     System.out.println(format("" + i));
/*    */   }
/*    */   
/*    */   public static void pn() {
/* 72 */     System.out.println();
/*    */   }
/*    */   
/*    */   public static void pn(Object o) {
/* 76 */     pn(o == null ? "null" : o.toString());
/*    */   }
/*    */ }


/* Location:              /home/cfeindia/Desktop/kilim.jar!/kilim/analysis/Utils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */