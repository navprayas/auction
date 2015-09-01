/*     */ package kilim.analysis;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.BitSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Usage
/*     */ {
/*     */   private int nLocals;
/*     */   private BitSet in;
/*     */   private BitSet use;
/*     */   private BitSet def;
/*     */   
/*     */   public Usage(int numLocals)
/*     */   {
/*  49 */     this.nLocals = numLocals;
/*  50 */     this.in = new BitSet(numLocals);
/*  51 */     this.use = new BitSet(numLocals);
/*  52 */     this.def = new BitSet(numLocals);
/*     */   }
/*     */   
/*     */   public void read(int var) {
/*  56 */     assert (var < this.nLocals) : ("local var num=" + var + " exceeds nLocals = " + this.nLocals);
/*  57 */     if (!this.def.get(var))
/*     */     {
/*  59 */       this.use.set(var);
/*     */     }
/*     */   }
/*     */   
/*     */   public void write(int var) {
/*  64 */     assert (var < this.nLocals) : ("local var num=" + var + " exceeds nLocals = " + this.nLocals);
/*  65 */     this.def.set(var);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isLiveIn(int var)
/*     */   {
/*  72 */     return this.in.get(var);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean evalLiveIn(ArrayList<Usage> succUsage)
/*     */   {
/*  81 */     BitSet out = new BitSet(this.nLocals);
/*  82 */     BitSet old_in = (BitSet)this.in.clone();
/*  83 */     if (succUsage.size() == 0) {
/*  84 */       this.in = this.use;
/*     */     }
/*     */     else {
/*  87 */       out = (BitSet)((Usage)succUsage.get(0)).in.clone();
/*  88 */       for (int i = 1; i < succUsage.size(); i++) {
/*  89 */         out.or(((Usage)succUsage.get(i)).in);
/*     */       }
/*     */       
/*  92 */       BitSet def1 = (BitSet)this.def.clone();
/*  93 */       def1.flip(0, this.nLocals);
/*  94 */       out.and(def1);
/*  95 */       out.or(this.use);
/*  96 */       this.in = out;
/*     */     }
/*  98 */     return !this.in.equals(old_in);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   void absorb(Usage succ)
/*     */   {
/* 107 */     BitSet b = (BitSet)this.def.clone();
/* 108 */     b.flip(0, this.nLocals);
/* 109 */     b.and(succ.use);
/* 110 */     this.use.or(b);
/* 111 */     this.def.or(succ.def);
/*     */   }
/*     */   
/*     */   public String toString() {
/* 115 */     StringBuffer sb = new StringBuffer();
/* 116 */     sb.append("use");
/* 117 */     printBits(sb, this.use);
/* 118 */     sb.append("def");
/* 119 */     printBits(sb, this.def);
/* 120 */     sb.append("in");
/* 121 */     printBits(sb, this.in);
/* 122 */     return sb.toString();
/*     */   }
/*     */   
/*     */   private void printBits(StringBuffer sb, BitSet b) {
/* 126 */     int numDefined = 0;
/* 127 */     for (int i = 0; i < this.nLocals; i++) {
/* 128 */       if (b.get(i)) numDefined++;
/*     */     }
/* 130 */     sb.append('(').append(numDefined).append("): ");
/* 131 */     for (int i = 0; i < this.nLocals; i++) {
/* 132 */       if (b.get(i))
/* 133 */         sb.append(i).append(' ');
/*     */     }
/* 135 */     sb.append('\n');
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setLiveIn(int var)
/*     */   {
/* 143 */     this.in.set(var);
/*     */   }
/*     */   
/*     */   Usage copy() {
/* 147 */     Usage ret = new Usage(this.nLocals);
/* 148 */     ret.use = this.use;
/* 149 */     ret.def = this.def;
/* 150 */     return ret;
/*     */   }
/*     */ }


/* Location:              /home/cfeindia/Desktop/kilim.jar!/kilim/analysis/Usage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */