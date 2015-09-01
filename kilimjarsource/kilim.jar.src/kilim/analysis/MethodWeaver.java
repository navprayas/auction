/*     */ package kilim.analysis;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import org.objectweb.asm.AnnotationVisitor;
/*     */ import org.objectweb.asm.Attribute;
/*     */ import org.objectweb.asm.ClassVisitor;
/*     */ import org.objectweb.asm.Label;
/*     */ import org.objectweb.asm.MethodVisitor;
/*     */ import org.objectweb.asm.tree.AbstractInsnNode;
/*     */ import org.objectweb.asm.tree.AnnotationNode;
/*     */ import org.objectweb.asm.tree.LineNumberNode;
/*     */ import org.objectweb.asm.tree.LocalVariableNode;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MethodWeaver
/*     */ {
/*     */   private ClassWeaver classWeaver;
/*     */   private MethodFlow methodFlow;
/*     */   private boolean isPausable;
/*     */   private int maxVars;
/*     */   private int maxStack;
/*     */   private int fiberVar;
/*     */   private int numWordsInSig;
/*  64 */   private ArrayList<CallWeaver> callWeavers = new ArrayList(5);
/*     */   
/*     */   MethodWeaver(ClassWeaver cw, MethodFlow mf) {
/*  67 */     this.classWeaver = cw;
/*  68 */     this.methodFlow = mf;
/*  69 */     this.isPausable = mf.isPausable();
/*  70 */     this.fiberVar = this.methodFlow.maxLocals;
/*  71 */     this.maxVars = (this.fiberVar + 1);
/*  72 */     this.maxStack = (this.methodFlow.maxStack + 1);
/*  73 */     if (!mf.isAbstract()) {
/*  74 */       createCallWeavers();
/*     */     }
/*     */   }
/*     */   
/*     */   public void accept(ClassVisitor cv) {
/*  79 */     MethodFlow mf = this.methodFlow;
/*  80 */     String[] exceptions = ClassWeaver.toStringArray(mf.exceptions);
/*  81 */     String desc = mf.desc;
/*  82 */     String sig = mf.signature;
/*  83 */     if (mf.isPausable()) {
/*  84 */       desc = desc.replace(")", "Lkilim/Fiber;)");
/*  85 */       if (sig != null)
/*  86 */         sig = sig.replace(")", "Lkilim/Fiber;)");
/*     */     }
/*  88 */     MethodVisitor mv = cv.visitMethod(mf.access, mf.name, desc, sig, exceptions);
/*  89 */     if (!mf.isAbstract()) {
/*  90 */       if (mf.isPausable()) {
/*  91 */         accept(mv);
/*     */       } else {
/*  93 */         mf.accept(mv);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   void accept(MethodVisitor mv) {
/*  99 */     visitAttrs(mv);
/* 100 */     visitCode(mv);
/* 101 */     mv.visitEnd();
/*     */   }
/*     */   
/*     */   private void visitAttrs(MethodVisitor mv) {
/* 105 */     MethodFlow mf = this.methodFlow;
/*     */     
/*     */ 
/* 108 */     if (mf.annotationDefault != null) {
/* 109 */       AnnotationVisitor av = mv.visitAnnotationDefault();
/* 110 */       MethodFlow.acceptAnnotation(av, null, mf.annotationDefault);
/* 111 */       av.visitEnd();
/*     */     }
/* 113 */     int n = mf.visibleAnnotations == null ? 0 : mf.visibleAnnotations.size();
/* 114 */     for (int i = 0; i < n; i++) {
/* 115 */       AnnotationNode an = (AnnotationNode)mf.visibleAnnotations.get(i);
/* 116 */       an.accept(mv.visitAnnotation(an.desc, true));
/*     */     }
/* 118 */     n = mf.invisibleAnnotations == null ? 0 : mf.invisibleAnnotations.size();
/*     */     
/* 120 */     for (i = 0; i < n; i++) {
/* 121 */       AnnotationNode an = (AnnotationNode)mf.invisibleAnnotations.get(i);
/* 122 */       an.accept(mv.visitAnnotation(an.desc, false));
/*     */     }
/* 124 */     n = mf.visibleParameterAnnotations == null ? 0 : mf.visibleParameterAnnotations.length;
/*     */     
/* 126 */     for (i = 0; i < n; i++) {
/* 127 */       List<?> l = mf.visibleParameterAnnotations[i];
/* 128 */       if (l != null)
/*     */       {
/*     */ 
/* 131 */         for (int j = 0; j < l.size(); j++) {
/* 132 */           AnnotationNode an = (AnnotationNode)l.get(j);
/* 133 */           an.accept(mv.visitParameterAnnotation(i, an.desc, true));
/*     */         } }
/*     */     }
/* 136 */     n = mf.invisibleParameterAnnotations == null ? 0 : mf.invisibleParameterAnnotations.length;
/*     */     
/* 138 */     for (i = 0; i < n; i++) {
/* 139 */       List<?> l = mf.invisibleParameterAnnotations[i];
/* 140 */       if (l != null)
/*     */       {
/*     */ 
/* 143 */         for (int j = 0; j < l.size(); j++) {
/* 144 */           AnnotationNode an = (AnnotationNode)l.get(j);
/* 145 */           an.accept(mv.visitParameterAnnotation(i, an.desc, false));
/*     */         } }
/*     */     }
/* 148 */     n = mf.attrs == null ? 0 : mf.attrs.size();
/* 149 */     for (i = 0; i < n; i++) {
/* 150 */       mv.visitAttribute((Attribute)mf.attrs.get(i));
/*     */     }
/*     */   }
/*     */   
/*     */   private void visitCode(MethodVisitor mv) {
/* 155 */     mv.visitCode();
/* 156 */     visitTryCatchBlocks(mv);
/* 157 */     visitInstructions(mv);
/* 158 */     visitLineNumbers(mv);
/* 159 */     visitLocals(mv);
/* 160 */     mv.visitMaxs(this.maxStack, this.maxVars);
/*     */   }
/*     */   
/*     */   private void visitLocals(MethodVisitor mv)
/*     */   {
/* 165 */     for (Object l : this.methodFlow.localVariables) {
/* 166 */       ((LocalVariableNode)l).accept(mv);
/*     */     }
/*     */   }
/*     */   
/*     */   private void visitLineNumbers(MethodVisitor mv) {
/* 171 */     for (Object l : this.methodFlow.lineNumbers) {
/* 172 */       ((LineNumberNode)l).accept(mv);
/*     */     }
/*     */   }
/*     */   
/*     */   private void visitInstructions(MethodVisitor mv)
/*     */   {
/* 178 */     genPrelude(mv);
/* 179 */     MethodFlow mf = this.methodFlow;
/* 180 */     BasicBlock lastBB = null;
/* 181 */     for (BasicBlock bb : mf.getBasicBlocks()) {
/* 182 */       int from = bb.startPos;
/*     */       
/* 184 */       if ((bb.isPausable()) && (bb.startFrame != null)) {
/* 185 */         genPausableMethod(mv, bb);
/* 186 */         from = bb.startPos + 1;
/* 187 */       } else if (bb.isCatchHandler()) {
/* 188 */         List<CallWeaver> cwList = getCallsUnderCatchBlock(bb);
/* 189 */         if (cwList != null) {
/* 190 */           genException(mv, bb, cwList);
/* 191 */           from = bb.startPos + 1;
/*     */         }
/*     */       }
/* 194 */       int to = bb.endPos;
/* 195 */       for (int i = from; i <= to; i++) {
/* 196 */         Label l = mf.getLabelAt(i);
/* 197 */         if (l != null) {
/* 198 */           mv.visitLabel(l);
/*     */         }
/* 200 */         bb.getInstruction(i).accept(mv);
/*     */       }
/* 202 */       lastBB = bb;
/*     */     }
/* 204 */     if (lastBB != null) {
/* 205 */       Label l = this.methodFlow.getLabelAt(lastBB.endPos + 1);
/* 206 */       if (l != null) {
/* 207 */         mv.visitLabel(l);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private List<CallWeaver> getCallsUnderCatchBlock(BasicBlock catchBB) {
/* 213 */     List<CallWeaver> cwList = null;
/* 214 */     for (Iterator i$ = this.callWeavers.iterator(); i$.hasNext();) { cw = (CallWeaver)i$.next();
/* 215 */       for (Handler h : cw.bb.handlers) {
/* 216 */         if (h.catchBB == catchBB) {
/* 217 */           if (cwList == null) {
/* 218 */             cwList = new ArrayList(this.callWeavers.size());
/*     */           }
/* 220 */           if (!cwList.contains(cw))
/* 221 */             cwList.add(cw);
/*     */         }
/*     */       }
/*     */     }
/*     */     CallWeaver cw;
/* 226 */     return cwList;
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
/*     */ 
/*     */   private void genPausableMethod(MethodVisitor mv, BasicBlock bb)
/*     */   {
/* 252 */     CallWeaver caw = null;
/* 253 */     if (bb.isGetCurrentTask()) {
/* 254 */       genGetCurrentTask(mv, bb);
/* 255 */       return;
/*     */     }
/* 257 */     for (CallWeaver cw : this.callWeavers) {
/* 258 */       if (cw.getBasicBlock() == bb) {
/* 259 */         caw = cw;
/* 260 */         break;
/*     */       }
/*     */     }
/* 263 */     caw.genCall(mv);
/* 264 */     caw.genPostCall(mv);
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
/*     */   void genGetCurrentTask(MethodVisitor mv, BasicBlock bb)
/*     */   {
/* 278 */     mv.visitLabel(bb.startLabel);
/* 279 */     VMType.loadVar(mv, 0, getFiberVar());
/* 280 */     mv.visitFieldInsn(180, "kilim/Fiber", "task", "Lkilim/Task;");
/*     */   }
/*     */   
/*     */   private void createCallWeavers() {
/* 284 */     MethodFlow mf = this.methodFlow;
/* 285 */     for (BasicBlock bb : mf.getBasicBlocks()) {
/* 286 */       if ((bb.isPausable()) && (bb.startFrame != null) && 
/*     */       
/* 288 */         (!bb.isGetCurrentTask())) {
/* 289 */         CallWeaver cw = new CallWeaver(this, bb);
/* 290 */         this.callWeavers.add(cw);
/*     */       }
/*     */     }
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
/*     */   private void genPrelude(MethodVisitor mv)
/*     */   {
/* 313 */     assert (this.isPausable) : "MethodWeaver.genPrelude called for nonPausable method";
/* 314 */     MethodFlow mf = this.methodFlow;
/*     */     
/* 316 */     int lastVar = getFiberArgVar();
/*     */     
/* 318 */     mv.visitVarInsn(25, lastVar);
/* 319 */     if (lastVar < this.fiberVar) {
/* 320 */       if (this.callWeavers.size() > 0) {
/* 321 */         mv.visitInsn(89);
/*     */       }
/* 323 */       mv.visitVarInsn(58, getFiberVar());
/*     */     }
/*     */     
/* 326 */     if (this.callWeavers.size() == 0)
/*     */     {
/*     */ 
/* 329 */       return;
/*     */     }
/*     */     
/* 332 */     mv.visitFieldInsn(180, "kilim/Fiber", "pc", "I");
/*     */     
/*     */ 
/* 335 */     ensureMaxStack(2);
/*     */     
/*     */ 
/* 338 */     Label startLabel = mf.getOrCreateLabelAtPos(0);
/* 339 */     Label errLabel = new Label();
/*     */     
/* 341 */     Label[] labels = new Label[this.callWeavers.size() + 1];
/* 342 */     labels[0] = startLabel;
/* 343 */     for (int i = 0; i < this.callWeavers.size(); i++) {
/* 344 */       labels[(i + 1)] = new Label();
/*     */     }
/*     */     
/* 347 */     mv.visitTableSwitchInsn(0, this.callWeavers.size(), errLabel, labels);
/*     */     
/* 349 */     mv.visitLabel(errLabel);
/* 350 */     mv.visitMethodInsn(184, "kilim/Fiber", "wrongPC", "()V");
/*     */     
/*     */ 
/* 353 */     int last = this.callWeavers.size() - 1;
/* 354 */     for (int i = 0; i <= last; i++) {
/* 355 */       CallWeaver cw = (CallWeaver)this.callWeavers.get(i);
/* 356 */       mv.visitLabel(labels[(i + 1)]);
/* 357 */       cw.genRewind(mv);
/*     */     }
/* 359 */     mv.visitLabel(startLabel);
/*     */   }
/*     */   
/*     */   boolean isStatic() {
/* 363 */     return this.methodFlow.isStatic();
/*     */   }
/*     */   
/*     */   int getFiberArgVar() {
/* 367 */     int lastVar = getNumWordsInSig();
/* 368 */     if (!isStatic()) {
/* 369 */       lastVar++;
/*     */     }
/* 371 */     return lastVar;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   int getNumWordsInSig()
/*     */   {
/* 379 */     if (this.numWordsInSig != -1) {
/* 380 */       String[] args = TypeDesc.getArgumentTypes(this.methodFlow.desc);
/* 381 */       int size = 0;
/* 382 */       for (int i = 0; i < args.length; i++) {
/* 383 */         size += (TypeDesc.isDoubleWord(args[i]) ? 2 : 1);
/*     */       }
/* 385 */       this.numWordsInSig = size;
/*     */     }
/* 387 */     return this.numWordsInSig;
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
/*     */ 
/*     */   private void genException(MethodVisitor mv, BasicBlock bb, List<CallWeaver> cwList)
/*     */   {
/* 413 */     mv.visitLabel(bb.startLabel);
/* 414 */     Label resumeLabel = new Label();
/* 415 */     VMType.loadVar(mv, 0, getFiberVar());
/* 416 */     mv.visitMethodInsn(182, "kilim/Fiber", "upEx", "()I");
/*     */     
/* 418 */     Label[] labels = new Label[cwList.size() + 1];
/* 419 */     labels[0] = resumeLabel;
/* 420 */     for (int i = 0; i < cwList.size(); i++) {
/* 421 */       labels[(i + 1)] = new Label();
/*     */     }
/* 423 */     mv.visitTableSwitchInsn(0, cwList.size(), resumeLabel, labels);
/* 424 */     int i = 1;
/* 425 */     for (CallWeaver cw : cwList) {
/* 426 */       if (i > 1)
/*     */       {
/*     */ 
/* 429 */         mv.visitJumpInsn(167, resumeLabel);
/*     */       }
/* 431 */       mv.visitLabel(labels[i]);
/* 432 */       cw.genRestoreEx(mv, labels[i]);
/* 433 */       i++;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 439 */     mv.visitLabel(resumeLabel);
/* 440 */     bb.getInstruction(bb.startPos).accept(mv);
/*     */   }
/*     */   
/*     */   int getFiberVar() {
/* 444 */     return this.fiberVar;
/*     */   }
/*     */   
/*     */   void visitTryCatchBlocks(MethodVisitor mv) {
/* 448 */     MethodFlow mf = this.methodFlow;
/* 449 */     ArrayList<BasicBlock> bbs = mf.getBasicBlocks();
/* 450 */     ArrayList<Handler> allHandlers = new ArrayList(bbs.size() * 2);
/* 451 */     for (BasicBlock bb : bbs) {
/* 452 */       allHandlers.addAll(bb.handlers);
/*     */     }
/* 454 */     allHandlers = Handler.consolidate(allHandlers);
/* 455 */     for (Handler h : allHandlers) {
/* 456 */       mv.visitTryCatchBlock(mf.getLabelAt(h.from), mf.getOrCreateLabelAtPos(h.to + 1), h.catchBB.startLabel, h.type);
/*     */     }
/*     */   }
/*     */   
/*     */   void ensureMaxVars(int numVars) {
/* 461 */     if (numVars > this.maxVars) {
/* 462 */       this.maxVars = numVars;
/*     */     }
/*     */   }
/*     */   
/*     */   void ensureMaxStack(int numStack) {
/* 467 */     if (numStack > this.maxStack) {
/* 468 */       this.maxStack = numStack;
/*     */     }
/*     */   }
/*     */   
/*     */   int getPC(CallWeaver weaver) {
/* 473 */     for (int i = 0; i < this.callWeavers.size(); i++) {
/* 474 */       if (this.callWeavers.get(i) == weaver)
/* 475 */         return i + 1;
/*     */     }
/* 477 */     if (!$assertionsDisabled) throw new AssertionError(" No weaver found");
/* 478 */     return 0;
/*     */   }
/*     */   
/*     */   public String createStateClass(ValInfoList valInfoList) {
/* 482 */     return this.classWeaver.createStateClass(valInfoList);
/*     */   }
/*     */   
/*     */   void makeNotWovenMethod(ClassVisitor cv, MethodFlow mf) {
/* 486 */     if (this.classWeaver.isInterface()) { return;
/*     */     }
/* 488 */     int access = mf.access;
/* 489 */     access &= 0xFBFF;
/* 490 */     MethodVisitor mv = cv.visitMethod(access, mf.name, mf.desc, mf.signature, ClassWeaver.toStringArray(mf.exceptions));
/*     */     
/* 492 */     mv.visitCode();
/* 493 */     visitAttrs(mv);
/* 494 */     mv.visitMethodInsn(184, "kilim/Task", "errNotWoven", "()V");
/*     */     
/* 496 */     String rdesc = TypeDesc.getReturnTypeDesc(mf.desc);
/*     */     
/*     */ 
/*     */ 
/* 500 */     int stacksize = 0;
/* 501 */     if (rdesc != "V")
/*     */     {
/* 503 */       stacksize = TypeDesc.isDoubleWord(rdesc) ? 2 : 1;
/* 504 */       int vmt = VMType.toVmType(rdesc);
/* 505 */       mv.visitInsn(VMType.constInsn[vmt]);
/* 506 */       mv.visitInsn(VMType.retInsn[vmt]);
/*     */     } else {
/* 508 */       mv.visitInsn(177);
/*     */     }
/*     */     
/*     */     int numlocals;
/* 512 */     if ((mf.access & 0x400) != 0)
/*     */     {
/*     */ 
/* 515 */       int numlocals = getNumWordsInSig() + 1;
/* 516 */       if (!mf.isStatic()) numlocals++;
/*     */     } else {
/* 518 */       numlocals = mf.maxLocals + 1;
/*     */     }
/* 520 */     mv.visitMaxs(stacksize, numlocals);
/* 521 */     mv.visitEnd();
/*     */   }
/*     */ }


/* Location:              /home/cfeindia/Desktop/kilim.jar!/kilim/analysis/MethodWeaver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */