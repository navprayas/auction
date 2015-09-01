/*     */ package kilim.analysis;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import kilim.KilimException;
/*     */ import org.objectweb.asm.ClassReader;
/*     */ import org.objectweb.asm.MethodVisitor;
/*     */ import org.objectweb.asm.tree.ClassNode;
/*     */ import org.objectweb.asm.tree.FieldNode;
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
/*     */ public class ClassFlow
/*     */   extends ClassNode
/*     */ {
/*     */   ArrayList<MethodFlow> methodFlows;
/*     */   ClassReader cr;
/*     */   String classDesc;
/*     */   private boolean isPausable;
/*  37 */   public boolean isWoven = false;
/*     */   private Detector detector;
/*     */   
/*     */   public ClassFlow(InputStream is, Detector detector) throws IOException {
/*  41 */     this.cr = new ClassReader(is);
/*  42 */     this.detector = detector;
/*     */   }
/*     */   
/*     */   public ClassFlow(String aClassName, Detector detector) throws IOException {
/*  46 */     this.cr = new ClassReader(aClassName);
/*  47 */     this.detector = detector;
/*     */   }
/*     */   
/*     */   public ClassFlow(byte[] data, Detector detector) {
/*  51 */     this.cr = new ClassReader(data);
/*  52 */     this.detector = detector;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions)
/*     */   {
/*  64 */     MethodFlow mn = new MethodFlow(this, access, name, desc, signature, exceptions, this.detector);
/*     */     
/*  66 */     this.methods.add(mn);
/*  67 */     return mn;
/*     */   }
/*     */   
/*     */   public ArrayList<MethodFlow> getMethodFlows() {
/*  71 */     assert (this.methodFlows != null) : "ClassFlow.analyze not called";
/*  72 */     return this.methodFlows;
/*     */   }
/*     */   
/*     */   public ArrayList<MethodFlow> analyze(boolean forceAnalysis)
/*     */     throws KilimException
/*     */   {
/*  78 */     Detector save = Detector.setDetector(this.detector);
/*     */     try
/*     */     {
/*  81 */       this.cr.accept(this, false);
/*  82 */       for (Object o : this.fields) {
/*  83 */         FieldNode fn = (FieldNode)o;
/*  84 */         if (fn.name.equals("$isWoven")) {
/*  85 */           this.isWoven = true;
/*  86 */           break;
/*     */         }
/*     */       }
/*  89 */       if ((this.isWoven) && (!forceAnalysis)) {
/*  90 */         return new ArrayList();
/*     */       }
/*     */       
/*  93 */       this.cr = null;
/*  94 */       this.classDesc = TypeDesc.getInterned("L" + this.name + ';');
/*  95 */       ArrayList<MethodFlow> flows = new ArrayList(this.methods.size());
/*  96 */       String msg = "";
/*  97 */       for (Object o : this.methods) {
/*     */         try {
/*  99 */           mf = (MethodFlow)o;
/* 100 */           if (mf.isBridge()) {
/* 101 */             MethodFlow mmf = getOrigWithSameSig(mf);
/* 102 */             if (mmf != null)
/* 103 */               mf.setPausable(mmf.isPausable());
/*     */           }
/* 105 */           mf.verifyPausables();
/* 106 */           if (mf.isPausable())
/* 107 */             this.isPausable = true;
/* 108 */           if (((mf.isPausable()) || (forceAnalysis)) && (!mf.isAbstract())) {
/* 109 */             mf.analyze();
/*     */           }
/*     */         } catch (KilimException ke) {
/*     */           MethodFlow mf;
/* 113 */           msg = msg + ke.getMessage() + "\n-------------------------------------------------\n";
/*     */         }
/*     */       }
/* 116 */       if (msg.length() > 0) {
/* 117 */         throw new KilimException(msg);
/*     */       }
/* 119 */       this.methodFlows = flows;
/* 120 */       return flows;
/*     */     }
/*     */     finally {
/* 123 */       Detector.setDetector(save);
/*     */     }
/*     */   }
/*     */   
/*     */   private MethodFlow getOrigWithSameSig(MethodFlow bridgeMethod) {
/* 128 */     for (Object o : this.methods) {
/* 129 */       MethodFlow mf = (MethodFlow)o;
/* 130 */       if (mf != bridgeMethod)
/*     */       {
/* 132 */         if (mf.name.equals(bridgeMethod.name)) {
/* 133 */           String mfArgs = mf.desc.substring(0, mf.desc.indexOf(')'));
/* 134 */           String bmArgs = bridgeMethod.desc.substring(0, bridgeMethod.desc.indexOf(')'));
/* 135 */           if (mfArgs.equals(bmArgs))
/* 136 */             return mf;
/*     */         } }
/*     */     }
/* 139 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public String getClassDescriptor()
/*     */   {
/* 145 */     return this.classDesc;
/*     */   }
/*     */   
/*     */   public String getClassName() {
/* 149 */     return this.name.replace('/', '.');
/*     */   }
/*     */   
/*     */   public boolean isPausable() {
/* 153 */     getMethodFlows();
/* 154 */     return this.isPausable;
/*     */   }
/*     */   
/*     */   boolean isInterface() {
/* 158 */     return (this.access & 0x200) != 0;
/*     */   }
/*     */   
/*     */   public Detector detector() {
/* 162 */     return this.detector;
/*     */   }
/*     */ }


/* Location:              /home/cfeindia/Desktop/kilim.jar!/kilim/analysis/ClassFlow.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */