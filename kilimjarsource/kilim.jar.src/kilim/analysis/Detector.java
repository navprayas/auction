/*     */ package kilim.analysis;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import kilim.NotPausable;
/*     */ import kilim.Pausable;
/*     */ import kilim.mirrors.ClassMirror;
/*     */ import kilim.mirrors.ClassMirrorNotFoundException;
/*     */ import kilim.mirrors.MethodMirror;
/*     */ import kilim.mirrors.Mirrors;
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
/*     */ public class Detector
/*     */ {
/*     */   public static final int METHOD_NOT_FOUND = 0;
/*     */   public static final int PAUSABLE_METHOD_FOUND = 1;
/*     */   public static final int METHOD_NOT_PAUSABLE = 2;
/*  28 */   static final String[] STANDARD_DONT_CHECK_LIST = { "java.", "javax." };
/*     */   
/*     */ 
/*  31 */   public static final Detector DEFAULT = new Detector(Mirrors.getRuntimeMirrors());
/*     */   private final Mirrors mirrors;
/*     */   ClassMirror NOT_PAUSABLE;
/*     */   
/*     */   public Detector(Mirrors mirrors) {
/*  36 */     this.mirrors = mirrors;
/*     */     
/*  38 */     this.NOT_PAUSABLE = mirrors.mirror(NotPausable.class);
/*  39 */     this.PAUSABLE = mirrors.mirror(Pausable.class);
/*  40 */     this.OBJECT = mirrors.mirror(Object.class);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isPausable(String className, String methodName, String desc)
/*     */   {
/*  48 */     return getPausableStatus(className, methodName, desc) == 1;
/*     */   }
/*     */   
/*     */ 
/*     */   ClassMirror PAUSABLE;
/*     */   
/*     */   ClassMirror OBJECT;
/*     */   
/*     */   public int getPausableStatus(String className, String methodName, String desc)
/*     */   {
/*  58 */     int ret = 0;
/*  59 */     if (methodName.endsWith("init>")) {
/*  60 */       return 2;
/*     */     }
/*  62 */     className = className.replace('/', '.');
/*     */     try {
/*  64 */       ClassMirror cl = this.mirrors.classForName(className);
/*  65 */       MethodMirror m = findMethod(cl, methodName, desc);
/*  66 */       if (m != null) {
/*  67 */         for (ClassMirror c : m.getExceptionTypes()) {
/*  68 */           if (this.NOT_PAUSABLE.isAssignableFrom(c)) {
/*  69 */             return 2;
/*     */           }
/*  71 */           if (this.PAUSABLE.isAssignableFrom(c)) {
/*  72 */             return 1;
/*     */           }
/*     */         }
/*  75 */         return 2;
/*     */       }
/*     */     }
/*     */     catch (ClassMirrorNotFoundException ignore) {}catch (VerifyError ve)
/*     */     {
/*  80 */       return AsmDetector.getPausableStatus(className, methodName, desc, this);
/*     */     }
/*  82 */     return ret;
/*     */   }
/*     */   
/*     */   private MethodMirror findMethod(ClassMirror cl, String methodName, String desc) {
/*  86 */     if (cl == null) return null;
/*  87 */     MethodMirror m = findMethodInHierarchy(cl, methodName, desc);
/*  88 */     if (m == null) {
/*  89 */       cl = this.mirrors.mirror(Object.class);
/*  90 */       for (MethodMirror om : cl.getDeclaredMethods()) {
/*  91 */         if ((om.getName().equals(methodName)) && (om.getMethodDescriptor().equals(desc))) {
/*  92 */           return om;
/*     */         }
/*     */       }
/*     */     }
/*  96 */     return m;
/*     */   }
/*     */   
/*     */   private MethodMirror findMethodInHierarchy(ClassMirror cl, String methodName, String desc)
/*     */   {
/* 101 */     if (cl == null) { return null;
/*     */     }
/* 103 */     for (MethodMirror om : cl.getDeclaredMethods()) {
/* 104 */       if ((om.getName().equals(methodName)) && (om.getMethodDescriptor().equals(desc)) && 
/* 105 */         (!om.isBridge())) {
/* 106 */         return om;
/*     */       }
/*     */     }
/*     */     
/* 110 */     if (this.OBJECT.equals(cl)) {
/* 111 */       return null;
/*     */     }
/* 113 */     MethodMirror m = findMethodInHierarchy(cl.getSuperclass(), methodName, desc);
/* 114 */     if (m != null)
/* 115 */       return m;
/* 116 */     for (ClassMirror ifcl : cl.getInterfaces()) {
/* 117 */       m = findMethodInHierarchy(ifcl, methodName, desc);
/* 118 */       if (m != null)
/* 119 */         return m;
/*     */     }
/* 121 */     return null;
/*     */   }
/*     */   
/* 124 */   public static String D_FIBER_ = "Lkilim/Fiber;)";
/*     */   
/*     */   private static String statusToStr(int st)
/*     */   {
/* 128 */     switch (st) {
/* 129 */     case 0:  return "not found";
/* 130 */     case 1:  return "pausable";
/* 131 */     case 2:  return "not pausable"; }
/* 132 */     throw new AssertionError("Unknown status");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/* 137 */   private static final ThreadLocal<Detector> DETECTOR = new ThreadLocal();
/*     */   
/*     */   static Detector getDetector() {
/* 140 */     Detector d = (Detector)DETECTOR.get();
/* 141 */     if (d == null) return DEFAULT;
/* 142 */     return d;
/*     */   }
/*     */   
/*     */   static Detector setDetector(Detector d) {
/* 146 */     Detector res = (Detector)DETECTOR.get();
/* 147 */     DETECTOR.set(d);
/* 148 */     return res;
/*     */   }
/*     */   
/*     */   public String commonSuperType(String oa, String ob) throws ClassMirrorNotFoundException {
/* 152 */     String a = toClassName(oa);
/* 153 */     String b = toClassName(ob);
/*     */     try
/*     */     {
/* 156 */       ClassMirror ca = this.mirrors.classForName(a);
/* 157 */       ClassMirror cb = this.mirrors.classForName(b);
/* 158 */       if (ca.isAssignableFrom(cb)) return oa;
/* 159 */       if (cb.isAssignableFrom(ca)) return ob;
/* 160 */       if ((ca.isInterface()) && (cb.isInterface())) {
/* 161 */         return "Ljava/lang/Object;";
/*     */       }
/*     */     }
/*     */     catch (ClassMirrorNotFoundException e) {}
/*     */     
/*     */ 
/* 167 */     ArrayList<String> sca = getSuperClasses(a);
/* 168 */     ArrayList<String> scb = getSuperClasses(b);
/* 169 */     int lasta = sca.size() - 1;
/* 170 */     int lastb = scb.size() - 1;
/*     */     do {
/* 172 */       if (!((String)sca.get(lasta)).equals(scb.get(lastb))) break;
/* 173 */       lasta--;
/* 174 */       lastb--;
/*     */ 
/*     */ 
/*     */     }
/* 178 */     while ((lasta >= 0) && (lastb >= 0));
/* 179 */     return toDesc((String)sca.get(lasta + 1));
/*     */   }
/*     */   
/*     */   public ArrayList<String> getSuperClasses(String cc) throws ClassMirrorNotFoundException
/*     */   {
/* 184 */     ClassMirror c = this.mirrors.classForName(cc);
/* 185 */     ArrayList<String> ret = new ArrayList(3);
/* 186 */     while (c != null) {
/* 187 */       ret.add(c.getName());
/* 188 */       c = c.getSuperclass();
/*     */     }
/* 190 */     return ret;
/*     */   }
/*     */   
/*     */   private static String toDesc(String name)
/*     */   {
/* 195 */     return "L" + name.replace('.', '/') + ';';
/*     */   }
/*     */   
/*     */   private static String toClassName(String s)
/*     */   {
/* 200 */     return s.replace('/', '.').substring(1, s.length() - 1);
/*     */   }
/*     */   
/* 203 */   static String JAVA_LANG_OBJECT = "java.lang.Object";
/*     */ }


/* Location:              /home/cfeindia/Desktop/kilim.jar!/kilim/analysis/Detector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */