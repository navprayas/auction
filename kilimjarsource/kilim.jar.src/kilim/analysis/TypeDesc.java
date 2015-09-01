/*     */ package kilim.analysis;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import java.util.HashMap;
/*     */ import kilim.Constants;
/*     */ import kilim.mirrors.ClassMirrorNotFoundException;
/*     */ import org.objectweb.asm.Type;
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
/*     */ public class TypeDesc
/*     */ {
/*     */   static final HashMap<String, String> knownTypes;
/*     */   
/*     */   static
/*     */   {
/*  35 */     knownTypes = new HashMap(30);
/*     */     
/*     */ 
/*  38 */     Field[] fields = Constants.class.getFields();
/*     */     try {
/*  40 */       for (int i = 0; i < fields.length; i++) {
/*  41 */         Field f = fields[i];
/*  42 */         if (f.getName().startsWith("D_")) {
/*  43 */           String val = (String)f.get(null);
/*  44 */           knownTypes.put(val, val);
/*     */         }
/*     */       }
/*     */     } catch (IllegalAccessException iae) {
/*  48 */       iae.printStackTrace();
/*     */     }
/*  50 */     knownTypes.put("java/lang/Object", "Ljava/lang/Object;");
/*  51 */     knownTypes.put("java/lang/String", "Ljava/lang/String;");
/*     */   }
/*     */   
/*     */   static boolean isDoubleWord(String desc) {
/*  55 */     return (desc == "D") || (desc == "J");
/*     */   }
/*     */   
/*     */   public static String getInterned(String desc) {
/*  59 */     String ret = (String)knownTypes.get(desc);
/*  60 */     if (ret == null) {
/*  61 */       switch (desc.charAt(0)) {
/*     */       case 'L': case '[': 
/*  63 */         return desc; }
/*  64 */       return "L" + desc + ';';
/*     */     }
/*     */     
/*  67 */     return ret;
/*     */   }
/*     */   
/*     */   public static String getReturnTypeDesc(String desc)
/*     */   {
/*  72 */     return getInterned(desc.substring(desc.indexOf(")") + 1));
/*     */   }
/*     */   
/*     */   static boolean isSingleWord(String desc) {
/*  76 */     return !isDoubleWord(desc);
/*     */   }
/*     */   
/*     */   public static String getComponentType(String t) {
/*  80 */     if (t.charAt(0) != '[') {
/*  81 */       throw new InternalError("Can't get component type of " + t);
/*     */     }
/*  83 */     return getInterned(t.substring(1));
/*     */   }
/*     */   
/*  86 */   public static String getTypeDesc(Object object) { if ((object instanceof Integer)) return "I";
/*  87 */     if ((object instanceof Long)) return "J";
/*  88 */     if ((object instanceof Float)) return "F";
/*  89 */     if ((object instanceof Double)) return "D";
/*  90 */     if ((object instanceof String)) return "Ljava/lang/String;";
/*  91 */     if ((object instanceof Boolean)) return "Z";
/*  92 */     if ((object instanceof Type))
/*  93 */       return getInterned(((Type)object).getDescriptor());
/*  94 */     throw new InternalError("Unrecognized ldc constant: " + object);
/*     */   }
/*     */   
/*     */   private static int typelen(char[] buf, int off) {
/*  98 */     int start = off;
/*  99 */     switch (buf[off]) {
/*     */     case 'L': 
/* 101 */       while (buf[(off++)] != ';') {}
/*     */       
/* 103 */       return off - start;
/*     */     case 'B': case 'C': case 'D': case 'F': case 'I': 
/*     */     case 'J': case 'S': case 'V': case 'Z': 
/* 106 */       return 1;
/*     */     case '[': 
/* 108 */       return typelen(buf, off + 1) + 1;
/*     */     }
/* 110 */     throw new InternalError("Unknown descriptor type");
/*     */   }
/*     */   
/*     */   public static String[] getArgumentTypes(String methodDescriptor)
/*     */   {
/* 115 */     char[] buf = methodDescriptor.toCharArray();
/* 116 */     int size = getNumArgumentTypes(buf);
/* 117 */     String[] args = new String[size];
/* 118 */     size = 0;
/* 119 */     int off = 1;
/* 120 */     while (buf[off] != ')') {
/* 121 */       int len = typelen(buf, off);
/* 122 */       args[size] = getInterned(new String(buf, off, len));
/* 123 */       off += len;
/* 124 */       size++;
/*     */     }
/* 126 */     return args;
/*     */   }
/*     */   
/*     */   public static int getNumArgumentTypes(String desc) {
/* 130 */     return getNumArgumentTypes(desc.toCharArray());
/*     */   }
/*     */   
/*     */   public static int getNumArgumentTypes(char[] buf) {
/* 134 */     int off = 1;
/* 135 */     int size = 0;
/*     */     
/* 137 */     while (buf[off] != ')')
/*     */     {
/*     */ 
/* 140 */       off += typelen(buf, off);
/* 141 */       size++;
/*     */     }
/* 143 */     return size;
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
/*     */   public static String mergeType(String a, String b)
/*     */     throws IncompatibleTypesException
/*     */   {
/* 159 */     if (a == "UNDEFINED") return b;
/* 160 */     if (b == "UNDEFINED") return a;
/* 161 */     char ac = a.charAt(0);
/* 162 */     char bc = b.charAt(0);
/* 163 */     if (a == "NULL") {
/* 164 */       assert ((b == "NULL") || (bc == 'L') || (bc == '[')) : ("merging NULL type with non ref type: " + b);
/* 165 */       return b;
/*     */     }
/* 167 */     if (b == "NULL") {
/* 168 */       assert ((b == "NULL") || (bc == 'L') || (bc == '[')) : ("merging NULL type with non ref type: " + a);
/* 169 */       return a;
/*     */     }
/* 171 */     if ((a == b) || (a.equals(b))) return a;
/* 172 */     switch (ac) {
/*     */     case 'N': 
/* 174 */       if (bc == 'L') return b;
/*     */       break;
/*     */     case 'L': 
/* 177 */       if (bc == 'L')
/* 178 */         return commonSuperType(a, b);
/* 179 */       if (bc == 'N')
/* 180 */         return a;
/* 181 */       if (bc == '[') {
/* 182 */         return "Ljava/lang/Object;";
/*     */       }
/*     */       break;
/*     */     case '[': 
/* 186 */       if (bc == '[')
/*     */         try {
/* 188 */           return "[" + mergeType(getComponentType(a), getComponentType(b));
/*     */         }
/*     */         catch (IncompatibleTypesException ite)
/*     */         {
/* 192 */           return "Ljava/lang/Object;";
/*     */         }
/* 194 */       if (bc == 'L') {
/* 195 */         return "Ljava/lang/Object;";
/*     */       }
/*     */       break;
/*     */     case 'B': case 'C': case 'I': 
/*     */     case 'S': case 'Z': 
/* 200 */       switch (bc) {
/* 201 */       case 'B': case 'C': case 'I': case 'S': case 'Z':  return "I";
/*     */       }
/*     */       break;
/*     */     }
/* 205 */     throw new IncompatibleTypesException("" + a + "," + b);
/*     */   }
/*     */   
/* 208 */   static String JAVA_LANG_OBJECT = "java.lang.Object";
/*     */   
/*     */   public static String commonSuperType(String oa, String ob)
/*     */   {
/*     */     try {
/* 213 */       if ((oa == "Ljava/lang/Object;") || (ob == "Ljava/lang/Object;")) return "Ljava/lang/Object;";
/* 214 */       if (oa.equals(ob)) { return oa;
/*     */       }
/* 216 */       return Detector.getDetector().commonSuperType(oa, ob);
/*     */ 
/*     */     }
/*     */     catch (ClassMirrorNotFoundException cnfe)
/*     */     {
/* 221 */       throw new InternalError(cnfe.getMessage());
/*     */     }
/*     */   }
/*     */   
/*     */   public static boolean isIntType(String typeDesc) {
/* 226 */     return (typeDesc == "I") || (typeDesc == "C") || (typeDesc == "S") || (typeDesc == "B") || (typeDesc == "Z");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isRefType(String typeDesc)
/*     */   {
/* 234 */     char c = typeDesc.charAt(0);
/* 235 */     return (typeDesc == "NULL") || (c == '[') || (c == 'L');
/*     */   }
/*     */   
/*     */   public static String getInternalName(String desc) {
/* 239 */     if (desc.charAt(0) == 'L') {
/* 240 */       return desc.substring(1, desc.length() - 1);
/*     */     }
/* 242 */     assert (desc.charAt(0) == '[') : ("Unexpected internal name " + desc);
/* 243 */     return desc;
/*     */   }
/*     */ }


/* Location:              /home/cfeindia/Desktop/kilim.jar!/kilim/analysis/TypeDesc.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */