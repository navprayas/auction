/*     */ package kilim.analysis;
/*     */ 
/*     */ import java.util.Arrays;
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
/*     */ public class Value
/*     */ {
/*  20 */   public static Object NO_VAL = new Object();
/*  21 */   public static Value V_UNDEFINED = new Value(0, "UNDEFINED", NO_VAL);
/*     */   
/*     */ 
/*     */   private String typeDesc;
/*     */   
/*     */   private Object constVal;
/*     */   private int numSites;
/*     */   private int[] sites;
/*     */   
/*  30 */   public int getNumSites() { return this.numSites; }
/*     */   
/*  32 */   public int[] getCreationSites() { return this.sites; }
/*     */   
/*  34 */   public String getTypeDesc() { return this.typeDesc; }
/*     */   
/*  36 */   public Object getConstVal() { return this.constVal; }
/*     */   
/*     */   private Value(int aPos, String aDesc, Object aConst) {
/*  39 */     this.sites = new int[2];
/*  40 */     this.numSites = 1;
/*  41 */     this.sites[0] = aPos;
/*  42 */     this.typeDesc = aDesc;
/*  43 */     this.constVal = aConst;
/*     */   }
/*     */   
/*     */   private Value(int newNumSites, int[] newSites, String newType, Object newConst)
/*     */   {
/*  48 */     Arrays.sort(newSites, 0, newNumSites);
/*  49 */     this.numSites = newNumSites;
/*  50 */     this.sites = newSites;
/*  51 */     this.typeDesc = newType;
/*  52 */     this.constVal = newConst;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Value merge(Value other)
/*     */   {
/*  77 */     int[] newSites = new int[this.numSites + other.numSites];
/*  78 */     for (int i = 0; i < newSites.length; i++) newSites[i] = -1;
/*  79 */     int newNumSites = mergeSites(newSites, other);
/*     */     String newType;
/*     */     try {
/*  82 */       newType = TypeDesc.mergeType(this.typeDesc, other.typeDesc);
/*     */     } catch (IncompatibleTypesException e) {
/*  84 */       newType = "UNDEFINED";
/*     */     }
/*  86 */     Object newConst = this.constVal.equals(other.constVal) ? this.constVal : NO_VAL;
/*  87 */     if ((newNumSites != this.numSites) || (newType != this.typeDesc)) {
/*  88 */       return new Value(newNumSites, newSites, newType, newConst);
/*     */     }
/*  90 */     return this;
/*     */   }
/*     */   
/*     */   private int mergeSites(int[] newSites, Value other)
/*     */   {
/*  95 */     int uniqueNumSites = 0;
/*  96 */     for (int i = 0; i < this.numSites; i++) {
/*  97 */       uniqueNumSites += addTo(newSites, this.sites[i]);
/*     */     }
/*  99 */     for (int i = 0; i < other.numSites; i++) {
/* 100 */       uniqueNumSites += addTo(newSites, other.sites[i]);
/*     */     }
/* 102 */     return uniqueNumSites;
/*     */   }
/*     */   
/*     */   private int addTo(int[] newSites, int site)
/*     */   {
/* 107 */     for (int i = 0; i < newSites.length; i++) {
/* 108 */       int s = newSites[i];
/* 109 */       if (s == -1) {
/* 110 */         newSites[i] = site;
/* 111 */         return 1;
/*     */       }
/* 113 */       if (s == site) return 0;
/*     */     }
/* 115 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean equals(Object obj)
/*     */   {
/* 123 */     if (this == obj) return true;
/* 124 */     Value other = (Value)obj;
/* 125 */     if ((this.typeDesc.equals(other.typeDesc)) && (this.constVal.equals(other.constVal)) && (this.numSites == other.numSites))
/*     */     {
/*     */ 
/*     */ 
/* 129 */       for (int i = 0; i < this.numSites; i++) {
/* 130 */         if (this.sites[i] != other.sites[i]) {
/* 131 */           return false;
/*     */         }
/*     */       }
/* 134 */       return true;
/*     */     }
/* 136 */     return false;
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 141 */     int h = this.typeDesc.hashCode();
/* 142 */     for (int i = 0; i < this.numSites; i++) {
/* 143 */       h ^= this.sites[i];
/*     */     }
/* 145 */     return h;
/*     */   }
/*     */   
/*     */   public static Value make(int pos, String desc) {
/* 149 */     return new Value(pos, desc, NO_VAL);
/*     */   }
/*     */   
/*     */   public static Value make(int pos, String desc, Object aConstVal) {
/* 153 */     return new Value(pos, desc, aConstVal);
/*     */   }
/*     */   
/*     */   public boolean isCategory2() {
/* 157 */     return category() == 2;
/*     */   }
/*     */   
/*     */   public boolean isCategory1() {
/* 161 */     return category() == 1;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 166 */     if ((this.numSites == 0) && (this.typeDesc == "UNDEFINED")) return "undef";
/* 167 */     StringBuffer sb = new StringBuffer(40);
/* 168 */     sb.append(this.typeDesc).append('[');
/* 169 */     for (int i = 0; i < this.numSites; i++) {
/* 170 */       if (i > 0) sb.append(' ');
/* 171 */       sb.append(this.sites[i]);
/*     */     }
/* 173 */     sb.append(']');
/* 174 */     if (this.constVal != NO_VAL) {
/* 175 */       sb.append(" == ").append(this.constVal.toString());
/*     */     }
/* 177 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public boolean isConstant() {
/* 181 */     return (this.constVal != NO_VAL) || (this.typeDesc == "NULL");
/*     */   }
/*     */   
/*     */   public int category() {
/* 185 */     return TypeDesc.isDoubleWord(this.typeDesc) ? 2 : 1;
/*     */   }
/*     */ }


/* Location:              /home/cfeindia/Desktop/kilim.jar!/kilim/analysis/Value.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */