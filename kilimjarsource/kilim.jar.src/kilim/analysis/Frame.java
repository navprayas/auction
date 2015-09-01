/*     */ package kilim.analysis;
/*     */ 
/*     */ import org.objectweb.asm.tree.MethodNode;
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
/*     */ public class Frame
/*     */ {
/*     */   Value[] locals;
/*     */   Value[] stack;
/*  31 */   int numMonitorsActive = 0;
/*  32 */   int stacklen = 0;
/*     */   
/*     */   private Frame(int nLocals, int nStack, boolean init) {
/*  35 */     this.locals = new Value[nLocals];
/*  36 */     if (init) {
/*  37 */       for (int i = 0; i < nLocals; i++) {
/*  38 */         this.locals[i] = Value.V_UNDEFINED;
/*     */       }
/*     */     }
/*  41 */     this.stack = new Value[nStack];
/*     */   }
/*     */   
/*     */   public Frame(int nLocals, int nStack) {
/*  45 */     this(nLocals, nStack, true);
/*     */   }
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
/*     */   public Frame merge(Frame inframe, boolean localsOnly, Usage usage)
/*     */   {
/*  61 */     int slen = this.stacklen;
/*     */     
/*  63 */     Value[] nst = null;
/*     */     
/*  65 */     if (!localsOnly) {
/*  66 */       Value[] st = this.stack;
/*  67 */       Value[] ist = inframe.stack;
/*  68 */       for (int i = 0; i < slen; i++) {
/*  69 */         Value va = ist[i];
/*  70 */         Value vb = st[i];
/*  71 */         if ((va != vb) && (!va.equals(vb))) {
/*  72 */           Value newval = va.merge(vb);
/*  73 */           if (newval != va) {
/*  74 */             if (nst == null) nst = dupArray(st);
/*  75 */             nst[i] = newval;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*  80 */     Value[] lo = this.locals;
/*  81 */     Value[] ilo = inframe.locals;
/*  82 */     Value[] nlo = null;
/*  83 */     for (int i = 0; i < lo.length; i++)
/*  84 */       if (usage.isLiveIn(i)) {
/*  85 */         Value va = lo[i];
/*  86 */         Value vb = ilo[i];
/*  87 */         if ((va != vb) && (!va.equals(vb))) {
/*  88 */           Value newval = va.merge(vb);
/*  89 */           if (newval != va) {
/*  90 */             if (nlo == null) nlo = dupArray(lo);
/*  91 */             nlo[i] = newval;
/*     */           }
/*     */         } }
/*  94 */     if ((nst == null) && (nlo == null)) {
/*  95 */       return this;
/*     */     }
/*     */     
/*  98 */     if (nst == null) nst = dupArray(this.stack);
/*  99 */     if (nlo == null) nlo = dupArray(this.locals);
/* 100 */     return new Frame(nlo, nst, slen, this.numMonitorsActive);
/*     */   }
/*     */   
/*     */   public static Value[] dupArray(Value[] a)
/*     */   {
/* 105 */     Value[] ret = new Value[a.length];
/* 106 */     System.arraycopy(a, 0, ret, 0, a.length);
/* 107 */     return ret;
/*     */   }
/*     */   
/*     */   private Frame(Value[] alocals, Value[] astack, int astacklen, int aNumMonitorsActive) {
/* 111 */     this.locals = alocals;
/* 112 */     this.stack = astack;
/* 113 */     this.stacklen = astacklen;
/* 114 */     this.numMonitorsActive = aNumMonitorsActive;
/*     */   }
/*     */   
/*     */   public Frame dup() {
/* 118 */     return new Frame(dupArray(this.locals), dupArray(this.stack), this.stacklen, this.numMonitorsActive);
/*     */   }
/*     */   
/*     */   public Frame(String classDesc, MethodNode method) {
/* 122 */     this(method.maxLocals, method.maxStack, false);
/* 123 */     String[] argTypeDescs = TypeDesc.getArgumentTypes(method.desc);
/* 124 */     for (int i = 0; i < method.maxLocals; i++) {
/* 125 */       setLocal(i, Value.V_UNDEFINED);
/*     */     }
/* 127 */     int local = 0;
/* 128 */     int paramPos = 100000;
/* 129 */     if ((method.access & 0x8) == 0)
/*     */     {
/* 131 */       setLocal(local++, Value.make(paramPos++, classDesc));
/*     */     }
/* 133 */     for (int i = 0; i < argTypeDescs.length; i++) {
/* 134 */       local += setLocal(local, Value.make(paramPos++, argTypeDescs[i]));
/*     */     }
/* 136 */     if ((method.access & 0x20) != 0) {
/* 137 */       this.numMonitorsActive = 1;
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean checkType(String desc) {
/* 142 */     if ((desc.equals("Ljava/lang/Object;")) && (desc != "Ljava/lang/Object;")) return false;
/* 143 */     switch (desc.charAt(0)) {
/*     */     case 'A': case 'B': case 'C': case 'D': case 'F': 
/*     */     case 'I': case 'J': case 'L': case 'N': 
/*     */     case 'S': case 'U': case 'Z': case '[': 
/* 147 */       return true;
/*     */     }
/* 149 */     return false;
/*     */   }
/*     */   
/*     */   public int setLocal(int local, Value v)
/*     */   {
/* 154 */     assert (checkType(v.getTypeDesc())) : ("Invalid type: " + v.getTypeDesc());
/* 155 */     this.locals[local] = v;
/* 156 */     if (v.isCategory2()) {
/* 157 */       this.locals[(local + 1)] = v;
/* 158 */       return 2;
/*     */     }
/* 160 */     return 1;
/*     */   }
/*     */   
/*     */   public Value getLocal(int local, int opcode) {
/* 164 */     Value v = this.locals[local];
/* 165 */     String desc = v.getTypeDesc();
/* 166 */     String expected = null;
/* 167 */     switch (opcode) {
/*     */     case 21: 
/* 169 */       if (TypeDesc.isIntType(desc)) {
/* 170 */         return v;
/*     */       }
/* 172 */       expected = "int";
/*     */       
/* 174 */       break;
/*     */     
/*     */     case 22: 
/* 177 */       if (desc == "J") {
/* 178 */         return v;
/*     */       }
/* 180 */       expected = "long";
/*     */       
/* 182 */       break;
/*     */     
/*     */     case 24: 
/* 185 */       if (desc == "D") {
/* 186 */         return v;
/*     */       }
/* 188 */       expected = "double";
/*     */       
/* 190 */       break;
/*     */     
/*     */     case 23: 
/* 193 */       if (desc == "F") {
/* 194 */         return v;
/*     */       }
/* 196 */       expected = "float";
/*     */       
/* 198 */       break;
/*     */     
/*     */     case 25: 
/* 201 */       if (TypeDesc.isRefType(desc)) {
/* 202 */         return v;
/*     */       }
/* 204 */       expected = "ref";
/*     */     }
/*     */     
/*     */     
/* 208 */     throw new AssertionError("Expected " + expected + " in local# " + local + ", got " + desc);
/*     */   }
/*     */   
/*     */   public Value getLocal(int local) {
/* 212 */     return this.locals[local];
/*     */   }
/*     */   
/*     */   public Value getStack(int pos)
/*     */   {
/* 217 */     return this.stack[pos];
/*     */   }
/*     */   
/*     */   public Value push(Value v) {
/* 221 */     assert (v != Value.V_UNDEFINED) : "UNDEFINED type pushed";
/* 222 */     assert (checkType(v.getTypeDesc())) : ("Invalid type: " + v.getTypeDesc());
/* 223 */     this.stack[(this.stacklen++)] = v;
/* 224 */     return v;
/*     */   }
/*     */   
/*     */   public Value pop() {
/*     */     try {
/* 229 */       return this.stack[(--this.stacklen)];
/*     */     } catch (ArrayIndexOutOfBoundsException e) {
/* 231 */       throw new RuntimeException("Verify error. Expected word in stack, but stack is empty");
/*     */     }
/*     */   }
/*     */   
/*     */   public Value popWord() {
/* 236 */     Value v = pop();
/* 237 */     assert (v.isCategory1()) : "double word present where single expected";
/* 238 */     return v;
/*     */   }
/*     */   
/*     */   public void popn(int n) {
/* 242 */     this.stacklen -= n;
/*     */   }
/*     */   
/*     */   void clearStack() {
/* 246 */     this.stacklen = 0;
/*     */   }
/*     */   
/*     */   public boolean equals(Object other)
/*     */   {
/* 251 */     Frame that = (Frame)other;
/* 252 */     for (int i = 0; i < this.locals.length; i++) {
/* 253 */       if (!this.locals[i].equals(that.locals[i])) return false;
/*     */     }
/* 255 */     for (int i = 0; i < this.stacklen; i++) {
/* 256 */       if (!this.stack[i].equals(that.stack[i])) return false;
/*     */     }
/* 258 */     return true;
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 263 */     int hash = 0;
/* 264 */     for (int i = 0; i < this.locals.length; i++) hash ^= this.locals[i].hashCode();
/* 265 */     for (int i = 0; i < this.stacklen; i++) hash ^= this.locals[i].hashCode();
/* 266 */     return hash;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 271 */     StringBuffer sb = new StringBuffer(100);
/* 272 */     int numDefined = 0;
/* 273 */     sb.append("): ");
/* 274 */     for (int i = 0; i < this.locals.length; i++) {
/* 275 */       Value v = this.locals[i];
/* 276 */       if (v != Value.V_UNDEFINED) {
/* 277 */         numDefined++;
/* 278 */         sb.append(i).append(':').append(this.locals[i]).append(" ");
/*     */       }
/*     */     }
/* 281 */     sb.insert(0, numDefined);
/* 282 */     sb.insert(0, "Locals(");
/* 283 */     sb.append("\n").append("Stack(").append(this.stacklen).append("): ");
/* 284 */     for (int i = 0; i < this.stacklen; i++) {
/* 285 */       sb.append(this.stack[i]).append(" ");
/*     */     }
/* 287 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public int getMaxLocals() {
/* 291 */     return this.locals.length;
/*     */   }
/*     */   
/*     */   public int getStackLen() {
/* 295 */     return this.stacklen;
/*     */   }
/*     */ }


/* Location:              /home/cfeindia/Desktop/kilim.jar!/kilim/analysis/Frame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */