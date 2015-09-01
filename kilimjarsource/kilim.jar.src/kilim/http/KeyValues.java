/*    */ package kilim.http;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class KeyValues
/*    */ {
/*    */   public String[] keys;
/*    */   
/*    */ 
/*    */ 
/*    */   public String[] values;
/*    */   
/*    */ 
/*    */   public int count;
/*    */   
/*    */ 
/* 18 */   public KeyValues() { this(5); }
/*    */   
/* 20 */   public KeyValues(int size) { this.keys = new String[size];
/* 21 */     this.values = new String[size];
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String get(String key)
/*    */   {
/* 29 */     int i = indexOf(key);
/* 30 */     return i == -1 ? "" : this.values[i];
/*    */   }
/*    */   
/*    */   public int indexOf(String key) {
/* 34 */     int len = this.count;
/* 35 */     for (int i = 0; i < len; i++) {
/* 36 */       if (this.keys[i].equals(key)) {
/* 37 */         return i;
/*    */       }
/*    */     }
/* 40 */     return -1;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void put(String key, String value)
/*    */   {
/* 50 */     int i = indexOf(key);
/* 51 */     if (i == -1) {
/* 52 */       if (this.count == this.keys.length) {
/* 53 */         this.keys = ((String[])Utils.growArray(this.keys, this.count * 2));
/* 54 */         this.values = ((String[])Utils.growArray(this.values, this.count * 2));
/*    */       }
/* 56 */       this.keys[this.count] = key;
/* 57 */       this.values[this.count] = value;
/* 58 */       this.count += 1;
/*    */     } else {
/* 60 */       this.values[i] = value;
/*    */     }
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 66 */     StringBuilder sb = new StringBuilder();
/* 67 */     sb.append('[');
/* 68 */     for (int i = 0; i < this.count; i++) {
/* 69 */       if (i != 0) sb.append(", ");
/* 70 */       sb.append(this.keys[i]).append(':').append(this.values[i]);
/*    */     }
/* 72 */     sb.append(']');
/* 73 */     return sb.toString();
/*    */   }
/*    */ }


/* Location:              /home/cfeindia/Desktop/kilim.jar!/kilim/http/KeyValues.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */