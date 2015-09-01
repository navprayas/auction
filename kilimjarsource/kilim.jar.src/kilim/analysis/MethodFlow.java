/*     */ package kilim.analysis;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.PriorityQueue;
/*     */ import kilim.KilimException;
/*     */ import org.objectweb.asm.AnnotationVisitor;
/*     */ import org.objectweb.asm.Attribute;
/*     */ import org.objectweb.asm.Label;
/*     */ import org.objectweb.asm.MethodVisitor;
/*     */ import org.objectweb.asm.tree.AbstractInsnNode;
/*     */ import org.objectweb.asm.tree.AnnotationNode;
/*     */ import org.objectweb.asm.tree.LineNumberNode;
/*     */ import org.objectweb.asm.tree.LocalVariableNode;
/*     */ import org.objectweb.asm.tree.MethodInsnNode;
/*     */ import org.objectweb.asm.tree.MethodNode;
/*     */ import org.objectweb.asm.tree.TryCatchBlockNode;
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
/*     */ public class MethodFlow
/*     */   extends MethodNode
/*     */ {
/*     */   ClassFlow classFlow;
/*     */   private ArrayList<Label> posToLabelMap;
/*     */   private HashMap<Label, Integer> labelToPosMap;
/*     */   private HashMap<Label, BasicBlock> labelToBBMap;
/*     */   private BBList basicBlocks;
/*     */   private PriorityQueue<BasicBlock> workset;
/*     */   private boolean hasPausableAnnotation;
/*     */   private boolean suppressPausableCheck;
/*  84 */   private List<MethodInsnNode> pausableMethods = new LinkedList();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final Detector detector;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public MethodFlow(ClassFlow classFlow, int access, String name, String desc, String signature, String[] exceptions, Detector detector)
/*     */   {
/*  96 */     super(access, name, desc, signature, exceptions);
/*  97 */     this.classFlow = classFlow;
/*  98 */     this.detector = detector;
/*  99 */     int numInstructions = this.instructions.size();
/* 100 */     this.posToLabelMap = new ArrayList(numInstructions);
/* 101 */     for (int i = numInstructions - 1; i >= 0; i--) {
/* 102 */       this.posToLabelMap.add(null);
/*     */     }
/* 104 */     this.labelToPosMap = new HashMap(numInstructions * 2);
/* 105 */     this.labelToBBMap = new HashMap(numInstructions);
/* 106 */     if ((exceptions != null) && (exceptions.length > 0)) {
/* 107 */       for (String e : exceptions) {
/* 108 */         if (e.equals("kilim/Pausable")) {
/* 109 */           this.hasPausableAnnotation = true;
/* 110 */           break; }
/* 111 */         if (e.equals("kilim/NotPausable")) {
/* 112 */           this.suppressPausableCheck = true;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void analyze() throws KilimException {
/* 119 */     buildBasicBlocks();
/* 120 */     if (this.basicBlocks.size() == 0) return;
/* 121 */     consolidateBasicBlocks();
/* 122 */     assignCatchHandlers();
/* 123 */     inlineSubroutines();
/* 124 */     doLiveVarAnalysis();
/* 125 */     dataFlow();
/* 126 */     this.labelToBBMap = null;
/*     */   }
/*     */   
/*     */   public void verifyPausables()
/*     */     throws KilimException
/*     */   {
/* 132 */     if ((this.classFlow.isWoven) || (this.suppressPausableCheck)) { return;
/*     */     }
/* 134 */     if ((!this.hasPausableAnnotation) && (!this.pausableMethods.isEmpty()))
/*     */     {
/* 136 */       String name = toString(this.classFlow.getClassName(), this.name, this.desc);
/* 137 */       String msg; String msg; if (this.name.endsWith("init>")) {
/* 138 */         msg = "Constructor " + name + " calls pausable methods:\n";
/*     */       } else {
/* 140 */         msg = name + " should be marked pausable. It calls pausable methods\n";
/*     */       }
/* 142 */       for (MethodInsnNode min : this.pausableMethods) {
/* 143 */         msg = msg + toString(min.owner, min.name, min.desc) + '\n';
/*     */       }
/* 145 */       throw new KilimException(msg);
/*     */     }
/* 147 */     if (this.classFlow.superName != null) {
/* 148 */       checkStatus(this.classFlow.superName, this.name, this.desc);
/*     */     }
/* 150 */     if (this.classFlow.interfaces != null) {
/* 151 */       for (Object ifc : this.classFlow.interfaces) {
/* 152 */         checkStatus((String)ifc, this.name, this.desc);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void checkStatus(String superClassName, String methodName, String desc) throws KilimException {
/* 158 */     int status = this.detector.getPausableStatus(superClassName, methodName, desc);
/* 159 */     if ((status == 1) && (!this.hasPausableAnnotation)) {
/* 160 */       throw new KilimException("Base class method is pausable, derived class is not: \nBase class = " + superClassName + "\nDerived class = " + this.classFlow.name + "\nMethod = " + methodName + desc);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 165 */     if ((status == 2) && (this.hasPausableAnnotation)) {
/* 166 */       throw new KilimException("Base class method is not pausable, but derived class is: \nBase class = " + superClassName + "\nDerived class = " + this.classFlow.name + "\nMethod = " + methodName + desc);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private String toString(String className, String methName, String desc)
/*     */   {
/* 174 */     return className.replace('/', '.') + '.' + methName + desc;
/*     */   }
/*     */   
/*     */ 
/*     */   public void visitLabel(Label label)
/*     */   {
/* 180 */     setLabel(this.instructions.size(), label);
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
/*     */   public void visitMethodInsn(int opcode, String owner, String name, String desc)
/*     */   {
/* 197 */     super.visitMethodInsn(opcode, owner, name, desc);
/*     */     
/*     */ 
/*     */ 
/* 201 */     if (!this.classFlow.isWoven) {
/* 202 */       int methodStatus = this.detector.getPausableStatus(owner, name, desc);
/* 203 */       if (methodStatus == 0)
/* 204 */         throw new KilimException("Check classpath. Method " + owner + "." + name + desc + " could not be located");
/* 205 */       if (methodStatus == 1) {
/* 206 */         MethodInsnNode min = (MethodInsnNode)this.instructions.get(this.instructions.size() - 1);
/* 207 */         this.pausableMethods.add(min);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void inlineSubroutines() throws KilimException {
/* 213 */     markPausableJSRs();
/*     */     int id;
/* 215 */     for (;;) { ArrayList<BasicBlock> newBBs = null;
/* 216 */       for (BasicBlock bb : this.basicBlocks) {
/* 217 */         if (!bb.hasFlag(128)) {
/* 218 */           bb.setFlag(128);
/* 219 */           if (bb.lastInstruction() == 168) {
/* 220 */             newBBs = bb.inline();
/* 221 */             if (newBBs != null)
/*     */               break;
/*     */           }
/*     */         }
/*     */       }
/* 226 */       if (newBBs == null) {
/*     */         break;
/*     */       }
/* 229 */       id = this.basicBlocks.size();
/* 230 */       for (BasicBlock bb : newBBs) {
/* 231 */         bb.setId(id++);
/* 232 */         this.basicBlocks.add(bb);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 237 */     for (BasicBlock bb : this.basicBlocks) {
/* 238 */       bb.changeJSR_RET_toGOTOs();
/*     */     }
/*     */   }
/*     */   
/*     */   private void markPausableJSRs() throws KilimException
/*     */   {
/* 244 */     for (BasicBlock bb : this.basicBlocks) {
/* 245 */       bb.checkPausableJSR();
/*     */     }
/*     */   }
/*     */   
/*     */   boolean isPausableMethodInsn(MethodInsnNode min)
/*     */   {
/* 251 */     return this.pausableMethods.contains(min);
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 256 */     ArrayList<BasicBlock> ret = getBasicBlocks();
/* 257 */     Collections.sort(ret);
/* 258 */     return ret.toString();
/*     */   }
/*     */   
/*     */   public BBList getBasicBlocks() {
/* 262 */     return this.basicBlocks;
/*     */   }
/*     */   
/*     */   private void assignCatchHandlers()
/*     */   {
/* 267 */     ArrayList<TryCatchBlockNode> tcbs = (ArrayList)this.tryCatchBlocks;
/*     */     
/*     */ 
/* 270 */     if (tcbs.size() == 0) return;
/* 271 */     ArrayList<Handler> handlers = new ArrayList(tcbs.size());
/*     */     
/* 273 */     for (int i = 0; i < tcbs.size(); i++) {
/* 274 */       TryCatchBlockNode tcb = (TryCatchBlockNode)tcbs.get(i);
/* 275 */       handlers.add(new Handler(getLabelPosition(tcb.start), getLabelPosition(tcb.end) - 1, tcb.type, getOrCreateBasicBlock(tcb.handler)));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 281 */     for (BasicBlock bb : this.basicBlocks) {
/* 282 */       bb.chooseCatchHandlers(handlers);
/*     */     }
/*     */   }
/*     */   
/*     */   void buildBasicBlocks()
/*     */   {
/* 288 */     int numInstructions = this.instructions.size();
/* 289 */     this.basicBlocks = new BBList();
/*     */     
/* 291 */     for (int i = 0; i < numInstructions; i++) {
/* 292 */       Label l = getOrCreateLabelAtPos(i);
/* 293 */       BasicBlock bb = getOrCreateBasicBlock(l);
/* 294 */       i = bb.initialize(i);
/* 295 */       this.basicBlocks.add(bb);
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
/*     */   private void doLiveVarAnalysis()
/*     */   {
/* 310 */     ArrayList<BasicBlock> bbs = getBasicBlocks();
/* 311 */     Collections.sort(bbs);
/*     */     boolean changed;
/*     */     do
/*     */     {
/* 315 */       changed = false;
/* 316 */       for (int i = bbs.size() - 1; i >= 0; i--) {
/* 317 */         changed = (((BasicBlock)bbs.get(i)).flowVarUsage()) || (changed);
/*     */       }
/* 319 */     } while (changed);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void consolidateBasicBlocks()
/*     */   {
/* 330 */     BBList newBBs = new BBList(this.basicBlocks.size());
/* 331 */     int pos = 0;
/* 332 */     for (BasicBlock bb : this.basicBlocks) {
/* 333 */       if (!bb.hasFlag(4)) {
/* 334 */         bb.coalesceTrivialFollowers();
/*     */         
/* 336 */         bb.setId(pos++);
/* 337 */         newBBs.add(bb);
/*     */       }
/*     */     }
/* 340 */     this.basicBlocks = newBBs;
/* 341 */     assert (checkNoBasicBlockLeftBehind());
/*     */   }
/*     */   
/*     */   private boolean checkNoBasicBlockLeftBehind() {
/* 345 */     ArrayList<BasicBlock> bbs = this.basicBlocks;
/* 346 */     HashSet<BasicBlock> hs = new HashSet(bbs.size() * 2);
/* 347 */     hs.addAll(bbs);
/* 348 */     int prevBBend = -1;
/* 349 */     for (BasicBlock bb : bbs) {
/* 350 */       assert (bb.isInitialized()) : ("BB not inited: " + bb);
/* 351 */       assert (bb.startPos == prevBBend + 1);
/* 352 */       for (BasicBlock succ : bb.successors) {
/* 353 */         assert (succ.isInitialized()) : ("Basic block not inited. Succ of " + bb);
/*     */         
/* 355 */         assert (hs.contains(succ)) : ("BB not found:\n" + succ);
/*     */       }
/* 357 */       prevBBend = bb.endPos;
/*     */     }
/* 359 */     assert (((BasicBlock)bbs.get(bbs.size() - 1)).endPos == this.instructions.size() - 1);
/* 360 */     return true;
/*     */   }
/*     */   
/*     */   private void dataFlow() {
/* 364 */     this.workset = new PriorityQueue(this.instructions.size(), new BBComparator());
/*     */     
/* 366 */     BasicBlock startBB = (BasicBlock)getBasicBlocks().get(0);
/* 367 */     assert (startBB != null) : "Null starting block in flowTypes()";
/* 368 */     startBB.startFrame = new Frame(this.classFlow.getClassDescriptor(), this);
/* 369 */     enqueue(startBB);
/*     */     
/* 371 */     while (!this.workset.isEmpty()) {
/* 372 */       BasicBlock bb = dequeue();
/* 373 */       bb.interpret();
/*     */     }
/*     */   }
/*     */   
/*     */   void setLabel(int pos, Label l) {
/* 378 */     for (int i = pos - this.posToLabelMap.size() + 1; i >= 0; i--)
/*     */     {
/* 380 */       this.posToLabelMap.add(null);
/*     */     }
/* 382 */     assert (this.posToLabelMap.get(pos) == null);
/* 383 */     this.posToLabelMap.set(pos, l);
/* 384 */     this.labelToPosMap.put(l, Integer.valueOf(pos));
/*     */   }
/*     */   
/*     */   Label getOrCreateLabelAtPos(int pos) {
/* 388 */     Label ret = null;
/* 389 */     if (pos < this.posToLabelMap.size()) {
/* 390 */       ret = (Label)this.posToLabelMap.get(pos);
/*     */     }
/* 392 */     if (ret == null) {
/* 393 */       ret = new Label();
/* 394 */       setLabel(pos, ret);
/*     */     }
/* 396 */     return ret;
/*     */   }
/*     */   
/*     */   int getLabelPosition(Label l) {
/* 400 */     return ((Integer)this.labelToPosMap.get(l)).intValue();
/*     */   }
/*     */   
/*     */   BasicBlock getOrCreateBasicBlock(Label l) {
/* 404 */     BasicBlock ret = (BasicBlock)this.labelToBBMap.get(l);
/* 405 */     if (ret == null) {
/* 406 */       ret = new BasicBlock(this, l);
/* 407 */       Object oldVal = this.labelToBBMap.put(l, ret);
/* 408 */       assert (oldVal == null) : "Duplicate BB created at label";
/*     */     }
/* 410 */     return ret;
/*     */   }
/*     */   
/*     */   BasicBlock getBasicBlock(Label l) {
/* 414 */     return (BasicBlock)this.labelToBBMap.get(l);
/*     */   }
/*     */   
/*     */   private BasicBlock dequeue() {
/* 418 */     BasicBlock bb = (BasicBlock)this.workset.poll();
/* 419 */     bb.unsetFlag(1);
/* 420 */     return bb;
/*     */   }
/*     */   
/*     */   void enqueue(BasicBlock bb) {
/* 424 */     assert (bb.startFrame != null) : "Enqueued null start frame";
/* 425 */     if (!bb.hasFlag(1)) {
/* 426 */       this.workset.add(bb);
/* 427 */       bb.setFlag(1);
/*     */     }
/*     */   }
/*     */   
/*     */   public Label getLabelAt(int pos) {
/* 432 */     return pos < this.posToLabelMap.size() ? (Label)this.posToLabelMap.get(pos) : null;
/*     */   }
/*     */   
/*     */   void addInlinedBlock(BasicBlock bb) {
/* 436 */     bb.setId(this.basicBlocks.size());
/* 437 */     this.basicBlocks.add(bb);
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
/*     */   public void accept(MethodVisitor mv)
/*     */   {
/* 451 */     if (this.annotationDefault != null) {
/* 452 */       AnnotationVisitor av = mv.visitAnnotationDefault();
/* 453 */       acceptAnnotation(av, null, this.annotationDefault);
/* 454 */       av.visitEnd();
/*     */     }
/* 456 */     int n = this.visibleAnnotations == null ? 0 : this.visibleAnnotations.size();
/* 457 */     for (int i = 0; i < n; i++) {
/* 458 */       AnnotationNode an = (AnnotationNode)this.visibleAnnotations.get(i);
/* 459 */       an.accept(mv.visitAnnotation(an.desc, true));
/*     */     }
/* 461 */     n = this.invisibleAnnotations == null ? 0 : this.invisibleAnnotations.size();
/* 462 */     for (i = 0; i < n; i++) {
/* 463 */       AnnotationNode an = (AnnotationNode)this.invisibleAnnotations.get(i);
/* 464 */       an.accept(mv.visitAnnotation(an.desc, false));
/*     */     }
/* 466 */     n = this.visibleParameterAnnotations == null ? 0 : this.visibleParameterAnnotations.length;
/*     */     
/*     */ 
/* 469 */     for (i = 0; i < n; i++) {
/* 470 */       List<?> l = this.visibleParameterAnnotations[i];
/* 471 */       if (l != null)
/*     */       {
/*     */ 
/* 474 */         for (int j = 0; j < l.size(); j++) {
/* 475 */           AnnotationNode an = (AnnotationNode)l.get(j);
/* 476 */           an.accept(mv.visitParameterAnnotation(i, an.desc, true));
/*     */         } }
/*     */     }
/* 479 */     n = this.invisibleParameterAnnotations == null ? 0 : this.invisibleParameterAnnotations.length;
/*     */     
/*     */ 
/* 482 */     for (i = 0; i < n; i++) {
/* 483 */       List<?> l = this.invisibleParameterAnnotations[i];
/* 484 */       if (l != null)
/*     */       {
/*     */ 
/* 487 */         for (int j = 0; j < l.size(); j++) {
/* 488 */           AnnotationNode an = (AnnotationNode)l.get(j);
/* 489 */           an.accept(mv.visitParameterAnnotation(i, an.desc, false));
/*     */         } }
/*     */     }
/* 492 */     n = this.attrs == null ? 0 : this.attrs.size();
/* 493 */     for (i = 0; i < n; i++) {
/* 494 */       mv.visitAttribute((Attribute)this.attrs.get(i));
/*     */     }
/*     */     
/* 497 */     if (this.instructions.size() > 0) {
/* 498 */       mv.visitCode();
/*     */       
/* 500 */       for (i = 0; i < this.tryCatchBlocks.size(); i++) {
/* 501 */         ((TryCatchBlockNode)this.tryCatchBlocks.get(i)).accept(mv);
/*     */       }
/*     */       
/* 504 */       for (i = 0; i < this.instructions.size(); i++) {
/* 505 */         Label l = getLabelAt(i);
/* 506 */         if (l != null) {
/* 507 */           mv.visitLabel(l);
/*     */         }
/* 509 */         ((AbstractInsnNode)this.instructions.get(i)).accept(mv);
/*     */       }
/* 511 */       Label l = getLabelAt(this.instructions.size());
/* 512 */       if (l != null) {
/* 513 */         mv.visitLabel(l);
/*     */       }
/*     */       
/* 516 */       n = this.localVariables == null ? 0 : this.localVariables.size();
/* 517 */       for (i = 0; i < n; i++) {
/* 518 */         ((LocalVariableNode)this.localVariables.get(i)).accept(mv);
/*     */       }
/*     */       
/* 521 */       n = this.lineNumbers == null ? 0 : this.lineNumbers.size();
/* 522 */       for (i = 0; i < n; i++) {
/* 523 */         ((LineNumberNode)this.lineNumbers.get(i)).accept(mv);
/*     */       }
/*     */       
/* 526 */       mv.visitMaxs(this.maxStack, this.maxLocals);
/*     */     }
/* 528 */     mv.visitEnd();
/*     */   }
/*     */   
/*     */   public int getNumArgs() {
/* 532 */     int ret = TypeDesc.getNumArgumentTypes(this.desc);
/* 533 */     if (!isStatic()) ret++;
/* 534 */     return ret;
/*     */   }
/*     */   
/*     */   public boolean isPausable() {
/* 538 */     return this.hasPausableAnnotation;
/*     */   }
/*     */   
/*     */   public void setPausable(boolean isPausable) {
/* 542 */     this.hasPausableAnnotation = isPausable;
/*     */   }
/*     */   
/*     */   public static void acceptAnnotation(AnnotationVisitor av, String name, Object value)
/*     */   {
/* 547 */     if ((value instanceof String[])) {
/* 548 */       String[] typeconst = (String[])value;
/* 549 */       av.visitEnum(name, typeconst[0], typeconst[1]);
/* 550 */     } else if ((value instanceof AnnotationNode)) {
/* 551 */       AnnotationNode an = (AnnotationNode)value;
/* 552 */       an.accept(av.visitAnnotation(name, an.desc));
/* 553 */     } else if ((value instanceof List)) {
/* 554 */       AnnotationVisitor v = av.visitArray(name);
/* 555 */       List<?> array = (List)value;
/* 556 */       for (int j = 0; j < array.size(); j++) {
/* 557 */         acceptAnnotation(v, null, array.get(j));
/*     */       }
/* 559 */       v.visitEnd();
/*     */     } else {
/* 561 */       av.visit(name, value);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isAbstract() {
/* 566 */     return (this.access & 0x400) != 0;
/*     */   }
/*     */   
/* 569 */   public boolean isStatic() { return (this.access & 0x8) != 0; }
/*     */   
/*     */   public boolean isBridge()
/*     */   {
/* 573 */     return (this.access & 0x40) != 0;
/*     */   }
/*     */   
/*     */   public Detector detector() {
/* 577 */     return this.classFlow.detector();
/*     */   }
/*     */ }


/* Location:              /home/cfeindia/Desktop/kilim.jar!/kilim/analysis/MethodFlow.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */