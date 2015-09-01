/*     */ package kilim.analysis;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import kilim.KilimException;
/*     */ import org.objectweb.asm.Attribute;
/*     */ import org.objectweb.asm.ClassVisitor;
/*     */ import org.objectweb.asm.ClassWriter;
/*     */ import org.objectweb.asm.MethodVisitor;
/*     */ import org.objectweb.asm.tree.AnnotationNode;
/*     */ import org.objectweb.asm.tree.FieldNode;
/*     */ import org.objectweb.asm.tree.InnerClassNode;
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
/*     */ public class ClassWeaver
/*     */ {
/*     */   ClassFlow classFlow;
/*  31 */   List<ClassInfo> classInfoList = new LinkedList();
/*  32 */   static HashSet<String> stateClasses = new HashSet();
/*     */   
/*     */   public ClassWeaver(byte[] data, Detector detector) {
/*  35 */     this.classFlow = new ClassFlow(data, detector);
/*  36 */     weave();
/*     */   }
/*     */   
/*     */   public ClassWeaver(InputStream is, Detector detector) throws IOException {
/*  40 */     this.classFlow = new ClassFlow(is, detector);
/*  41 */     weave();
/*     */   }
/*     */   
/*     */   public ClassWeaver(String className, Detector detector) throws IOException {
/*  45 */     this.classFlow = new ClassFlow(className, detector);
/*  46 */     weave();
/*     */   }
/*     */   
/*     */   private void weave() throws KilimException {
/*  50 */     this.classFlow.analyze(false);
/*  51 */     if ((needsWeaving()) && (this.classFlow.isPausable())) {
/*  52 */       ClassWriter cw = new ClassWriter(false);
/*  53 */       accept(cw);
/*  54 */       addClassInfo(new ClassInfo(this.classFlow.getClassName(), cw.toByteArray()));
/*     */     }
/*     */   }
/*     */   
/*     */   private void accept(ClassVisitor cv) {
/*  59 */     ClassFlow cf = this.classFlow;
/*     */     
/*  61 */     String[] interfaces = toStringArray(cf.interfaces);
/*  62 */     cv.visit(cf.version, cf.access, cf.name, cf.signature, cf.superName, interfaces);
/*     */     
/*  64 */     if ((cf.sourceFile != null) || (cf.sourceDebug != null)) {
/*  65 */       cv.visitSource(cf.sourceFile, cf.sourceDebug);
/*     */     }
/*     */     
/*  68 */     if (cf.outerClass != null) {
/*  69 */       cv.visitOuterClass(cf.outerClass, cf.outerMethod, cf.outerMethodDesc);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  74 */     int n = cf.visibleAnnotations == null ? 0 : cf.visibleAnnotations.size();
/*  75 */     for (int i = 0; i < n; i++) {
/*  76 */       AnnotationNode an = (AnnotationNode)cf.visibleAnnotations.get(i);
/*  77 */       an.accept(cv.visitAnnotation(an.desc, true));
/*     */     }
/*  79 */     n = cf.invisibleAnnotations == null ? 0 : cf.invisibleAnnotations.size();
/*     */     
/*  81 */     for (i = 0; i < n; i++) {
/*  82 */       AnnotationNode an = (AnnotationNode)cf.invisibleAnnotations.get(i);
/*  83 */       an.accept(cv.visitAnnotation(an.desc, false));
/*     */     }
/*     */     
/*  86 */     n = cf.attrs == null ? 0 : cf.attrs.size();
/*  87 */     for (i = 0; i < n; i++) {
/*  88 */       cv.visitAttribute((Attribute)cf.attrs.get(i));
/*     */     }
/*     */     
/*  91 */     for (i = 0; i < cf.innerClasses.size(); i++) {
/*  92 */       ((InnerClassNode)cf.innerClasses.get(i)).accept(cv);
/*     */     }
/*     */     
/*  95 */     for (i = 0; i < cf.fields.size(); i++) {
/*  96 */       ((FieldNode)cf.fields.get(i)).accept(cv);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 102 */     cv.visitField(25, "$isWoven", "Z", "Z", Boolean.TRUE);
/*     */     
/* 104 */     for (i = 0; i < cf.methods.size(); i++) {
/* 105 */       MethodFlow m = (MethodFlow)cf.methods.get(i);
/* 106 */       if (needsWeaving(m)) {
/* 107 */         MethodWeaver mw = new MethodWeaver(this, m);
/* 108 */         mw.accept(cv);
/* 109 */         mw.makeNotWovenMethod(cv, m);
/*     */       } else {
/* 111 */         m.accept(cv);
/*     */       }
/*     */     }
/*     */     
/* 115 */     cv.visitEnd();
/*     */   }
/*     */   
/*     */   static String[] toStringArray(List list)
/*     */   {
/* 120 */     String[] array = new String[list.size()];
/* 121 */     list.toArray(array);
/* 122 */     return array;
/*     */   }
/*     */   
/*     */   void addClassInfo(ClassInfo ci) {
/* 126 */     this.classInfoList.add(ci);
/*     */   }
/*     */   
/*     */   public List<ClassInfo> getClassInfos() {
/* 130 */     return this.classInfoList;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 140 */   static String FIBER_SUFFIX = "Lkilim/Fiber;)";
/*     */   
/* 142 */   boolean needsWeaving(MethodFlow mf) { if ((!mf.isPausable()) || (mf.desc.endsWith(FIBER_SUFFIX)))
/* 143 */       return false;
/* 144 */     String fdesc = mf.desc.replace(")", FIBER_SUFFIX);
/* 145 */     for (MethodFlow omf : this.classFlow.getMethodFlows()) {
/* 146 */       if (omf != mf) {
/* 147 */         if ((mf.name.equals(omf.name)) && (fdesc.equals(omf.desc)))
/* 148 */           return false;
/*     */       }
/*     */     }
/* 151 */     return true;
/*     */   }
/*     */   
/*     */   boolean needsWeaving() {
/* 155 */     if (this.classFlow.isWoven) return false;
/* 156 */     for (MethodFlow mf : this.classFlow.getMethodFlows()) {
/* 157 */       if (needsWeaving(mf)) return true;
/*     */     }
/* 159 */     return false;
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
/*     */   String createStateClass(ValInfoList valInfoList)
/*     */   {
/* 181 */     int[] numByType = { 0, 0, 0, 0, 0 };
/* 182 */     for (ValInfo vi : valInfoList) {
/* 183 */       numByType[vi.vmt] += 1;
/*     */     }
/* 185 */     String className = makeClassName(numByType);
/* 186 */     if (stateClasses.contains(className)) {
/* 187 */       return className;
/*     */     }
/* 189 */     stateClasses.add(className);
/* 190 */     ClassWriter cw = new ClassWriter(false);
/* 191 */     cw.visit(196653, 17, className, null, "kilim/State", null);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 197 */     MethodVisitor mw = cw.visitMethod(1, "<init>", "()V", null, null);
/* 198 */     mw.visitInsn(42);
/* 199 */     mw.visitMethodInsn(183, "kilim/State", "<init>", "()V");
/* 200 */     mw.visitInsn(177);
/*     */     
/* 202 */     mw.visitMaxs(1, 1);
/* 203 */     mw.visitEnd();
/*     */     
/* 205 */     for (ValInfo vi : valInfoList) {
/* 206 */       cw.visitField(1, vi.fieldName, vi.fieldDesc(), null, null);
/*     */     }
/* 208 */     addClassInfo(new ClassInfo(className, cw.toByteArray()));
/* 209 */     return className;
/*     */   }
/*     */   
/*     */   private String makeClassName(int[] numByType) {
/* 213 */     StringBuilder sb = new StringBuilder(30);
/* 214 */     sb.append("kilim/S_");
/* 215 */     for (int t = 0; t < 5; t++) {
/* 216 */       int c = numByType[t];
/* 217 */       if (c != 0)
/*     */       {
/* 219 */         sb.append(VMType.abbrev[t]);
/* 220 */         if (c > 1)
/* 221 */           sb.append(c);
/*     */       }
/*     */     }
/* 224 */     return sb.toString();
/*     */   }
/*     */   
/*     */   boolean isInterface() {
/* 228 */     return this.classFlow.isInterface();
/*     */   }
/*     */ }


/* Location:              /home/cfeindia/Desktop/kilim.jar!/kilim/analysis/ClassWeaver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */