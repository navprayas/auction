/*     */ package kilim.analysis;
/*     */ 
/*     */ import java.util.BitSet;
/*     */ import java.util.Collections;
/*     */ import org.objectweb.asm.Label;
/*     */ import org.objectweb.asm.MethodVisitor;
/*     */ import org.objectweb.asm.tree.MethodInsnNode;
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
/*     */ public class CallWeaver
/*     */ {
/*     */   private MethodWeaver methodWeaver;
/*     */   BasicBlock bb;
/*     */   private Label resumeLabel;
/*     */   Label callLabel;
/*     */   private ValInfoList valInfoList;
/*     */   BitSet varUsage;
/*     */   int numVars;
/*     */   private String stateClassName;
/* 170 */   int numArgs = -1;
/*     */   
/*     */   public CallWeaver(MethodWeaver mw, BasicBlock aBB) {
/* 173 */     this.methodWeaver = mw;
/* 174 */     this.bb = aBB;
/* 175 */     this.callLabel = this.bb.startLabel;
/* 176 */     this.varUsage = new BitSet(2 * this.bb.flow.maxLocals);
/* 177 */     this.resumeLabel = this.bb.flow.getLabelAt(this.bb.startPos + 1);
/* 178 */     if (this.resumeLabel == null)
/* 179 */       this.resumeLabel = new Label();
/* 180 */     assignRegisters();
/* 181 */     this.stateClassName = createStateClass();
/* 182 */     this.methodWeaver.ensureMaxStack(getNumBottom() + 3);
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
/*     */   private void assignRegisters()
/*     */   {
/* 198 */     Frame f = this.bb.startFrame;
/* 199 */     MethodWeaver mw = this.methodWeaver;
/* 200 */     this.varUsage.set(mw.getFiberVar());
/* 201 */     this.numVars = (mw.getFiberVar() + 1);
/*     */     
/* 203 */     mw.ensureMaxVars(this.numVars);
/* 204 */     Usage u = this.bb.usage;
/* 205 */     this.valInfoList = new ValInfoList();
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
/* 221 */     this.varUsage.set(0, f.getMaxLocals());
/*     */     
/* 223 */     for (int i = this.bb.flow.isStatic() ? 0 : 1; 
/* 224 */         i < f.getMaxLocals(); i++) {
/* 225 */       Value v = f.getLocal(i);
/* 226 */       if ((u.isLiveIn(i)) && 
/* 227 */         (!v.isConstant()) && (!this.valInfoList.contains(v))) {
/* 228 */         ValInfo vi = new ValInfo(v);
/* 229 */         vi.var = i;
/* 230 */         this.valInfoList.add(vi);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 240 */     int numBottom = getNumBottom();
/* 241 */     for (i = 0; i < numBottom; i++) {
/* 242 */       Value v = f.getStack(i);
/* 243 */       if ((!v.isConstant()) && (!this.valInfoList.contains(v))) {
/* 244 */         ValInfo vi = new ValInfo(v);
/* 245 */         this.valInfoList.add(vi);
/*     */       }
/*     */     }
/* 248 */     Collections.sort(this.valInfoList);
/* 249 */     int fieldNum = 0;
/* 250 */     for (ValInfo vi : this.valInfoList) {
/* 251 */       vi.fieldName = ("f" + fieldNum++);
/*     */     }
/*     */   }
/*     */   
/*     */   int getStackLen() {
/* 256 */     return this.bb.startFrame.getStackLen();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   int getNumArgs()
/*     */   {
/* 263 */     if (this.numArgs == -1) {
/* 264 */       this.numArgs = (TypeDesc.getNumArgumentTypes(getMethodInsn().desc) + (isStaticCall() ? 0 : 1));
/*     */     }
/*     */     
/* 267 */     return this.numArgs;
/*     */   }
/*     */   
/*     */   final boolean isStaticCall() {
/* 271 */     return getMethodInsn().getOpcode() == 184;
/*     */   }
/*     */   
/*     */   final MethodInsnNode getMethodInsn() {
/* 275 */     return (MethodInsnNode)this.bb.getInstruction(this.bb.startPos);
/*     */   }
/*     */   
/*     */   int getNumBottom() {
/* 279 */     return getStackLen() - getNumArgs();
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
/*     */   void genRewind(MethodVisitor mv)
/*     */   {
/* 299 */     Frame f = this.bb.startFrame;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 307 */     for (int i = this.methodWeaver.getFiberArgVar(); i < f.getMaxLocals();) {
/* 308 */       Value v = f.getLocal(i);
/* 309 */       if (v.getTypeDesc() != "UNDEFINED")
/*     */       {
/* 311 */         int vmt = VMType.toVmType(v.getTypeDesc());
/* 312 */         mv.visitInsn(VMType.constInsn[vmt]);
/* 313 */         VMType.storeVar(mv, vmt, i);
/*     */       }
/* 315 */       i += (v.isCategory2() ? 2 : 1);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 320 */     int numBottom = getNumBottom();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 325 */     for (int spos = 0; spos < numBottom; spos++) {
/* 326 */       Value v = f.getStack(spos);
/* 327 */       if (v.isConstant()) {
/* 328 */         mv.visitInsn(VMType.constInsn[VMType.toVmType(v.getTypeDesc())]);
/*     */       } else {
/* 330 */         ValInfo vi = this.valInfoList.find(v);
/* 331 */         mv.visitInsn(VMType.constInsn[vi.vmt]);
/*     */       }
/*     */     }
/* 334 */     if (!isStaticCall())
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 341 */       Value v = f.getStack(numBottom);
/* 342 */       if ((!this.methodWeaver.isStatic()) && (f.getLocal(0) == v))
/*     */       {
/* 344 */         mv.visitInsn(42);
/*     */       } else {
/* 346 */         VMType.loadVar(mv, 0, this.methodWeaver.getFiberVar());
/* 347 */         mv.visitMethodInsn(182, "kilim/Fiber", "getCallee", "()Ljava/lang/Object;");
/* 348 */         mv.visitTypeInsn(192, getReceiverTypename());
/*     */       }
/* 350 */       spos++;
/*     */     }
/*     */     
/* 353 */     int len = f.getStackLen();
/* 355 */     for (; 
/* 355 */         spos < len; spos++) {
/* 356 */       Value v = f.getStack(spos);
/* 357 */       int vmt = VMType.toVmType(v.getTypeDesc());
/* 358 */       mv.visitInsn(VMType.constInsn[vmt]);
/*     */     }
/*     */     
/* 361 */     mv.visitJumpInsn(167, this.callLabel);
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
/* 381 */   static String fiberArg = "Lkilim/Fiber;)";
/*     */   
/* 383 */   void genCall(MethodVisitor mv) { mv.visitLabel(this.callLabel);
/* 384 */     VMType.loadVar(mv, 0, this.methodWeaver.getFiberVar());
/* 385 */     mv.visitMethodInsn(182, "kilim/Fiber", "down", "()Lkilim/Fiber;");
/* 386 */     MethodInsnNode mi = getMethodInsn();
/* 387 */     if (mi.desc.indexOf(fiberArg) == -1)
/*     */     {
/*     */ 
/*     */ 
/* 391 */       mi.desc = mi.desc.replace(")", fiberArg);
/*     */     }
/* 393 */     mi.accept(mv);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   void genPostCall(MethodVisitor mv)
/*     */   {
/* 427 */     VMType.loadVar(mv, 0, this.methodWeaver.getFiberVar());
/* 428 */     mv.visitMethodInsn(182, "kilim/Fiber", "up", "()I");
/* 429 */     Label restoreLabel = new Label();
/* 430 */     Label saveLabel = new Label();
/* 431 */     Label unwindLabel = new Label();
/* 432 */     Label[] labels = { this.resumeLabel, restoreLabel, saveLabel, unwindLabel };
/*     */     
/* 434 */     mv.visitTableSwitchInsn(0, 3, this.resumeLabel, labels);
/* 435 */     genSave(mv, saveLabel);
/* 436 */     genUnwind(mv, unwindLabel);
/* 437 */     genRestore(mv, restoreLabel);
/* 438 */     mv.visitLabel(this.resumeLabel);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void genUnwind(MethodVisitor mv, Label unwindLabel)
/*     */   {
/* 450 */     mv.visitLabel(unwindLabel);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 455 */     String rdesc = getReturnType();
/* 456 */     if (rdesc != "V") {
/* 457 */       mv.visitInsn(TypeDesc.isDoubleWord(rdesc) ? 88 : 87);
/*     */     }
/*     */     
/* 460 */     int i = getNumBottom() - 1;
/* 461 */     Frame f = this.bb.startFrame;
/* 462 */     for (; i >= 0; i--) {
/* 463 */       mv.visitInsn(f.getStack(i).isCategory1() ? 87 : 88);
/*     */     }
/*     */     
/*     */ 
/* 467 */     rdesc = TypeDesc.getReturnTypeDesc(this.bb.flow.desc);
/* 468 */     if (rdesc != "V")
/*     */     {
/* 470 */       int vmt = VMType.toVmType(rdesc);
/* 471 */       mv.visitInsn(VMType.constInsn[vmt]);
/* 472 */       mv.visitInsn(VMType.retInsn[vmt]);
/*     */     } else {
/* 474 */       mv.visitInsn(177);
/*     */     }
/*     */   }
/*     */   
/*     */   private String getReturnType() {
/* 479 */     return TypeDesc.getReturnTypeDesc(getMethodInsn().desc);
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
/*     */   private void genSave(MethodVisitor mv, Label saveLabel)
/*     */   {
/* 496 */     mv.visitLabel(saveLabel);
/*     */     
/* 498 */     Frame f = this.bb.startFrame;
/*     */     
/* 500 */     String retType = getReturnType();
/* 501 */     if (retType != "V")
/*     */     {
/*     */ 
/* 504 */       mv.visitInsn(TypeDesc.isDoubleWord(retType) ? 88 : 87);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 515 */     mv.visitTypeInsn(187, this.stateClassName);
/* 516 */     mv.visitInsn(89);
/*     */     
/* 518 */     mv.visitMethodInsn(183, this.stateClassName, "<init>", "()V");
/*     */     
/* 520 */     int stateVar = allocVar(1);
/* 521 */     VMType.storeVar(mv, 0, stateVar);
/*     */     
/* 523 */     if (!this.bb.flow.isStatic()) {
/* 524 */       VMType.loadVar(mv, 0, stateVar);
/* 525 */       mv.visitInsn(42);
/* 526 */       mv.visitFieldInsn(181, "kilim/State", "self", "Ljava/lang/Object;");
/*     */     }
/* 528 */     int pc = this.methodWeaver.getPC(this);
/* 529 */     VMType.loadVar(mv, 0, stateVar);
/* 530 */     if (pc < 6) {
/* 531 */       mv.visitInsn(3 + pc);
/*     */     } else {
/* 533 */       mv.visitIntInsn(16, pc);
/*     */     }
/* 535 */     mv.visitFieldInsn(181, "kilim/State", "pc", "I");
/*     */     
/*     */ 
/* 538 */     for (int i = getNumBottom() - 1; 
/* 539 */         i >= 0; i--) {
/* 540 */       Value v = f.getStack(i);
/* 541 */       ValInfo vi = this.valInfoList.find(v);
/* 542 */       if (vi == null)
/*     */       {
/*     */ 
/* 545 */         mv.visitInsn(v.category() == 2 ? 88 : 87);
/*     */ 
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/*     */ 
/*     */ 
/* 553 */         int var = allocVar(vi.val.category());
/* 554 */         VMType.storeVar(mv, vi.vmt, var);
/* 555 */         VMType.loadVar(mv, 0, stateVar);
/* 556 */         VMType.loadVar(mv, vi.vmt, var);
/* 557 */         mv.visitFieldInsn(181, this.stateClassName, vi.fieldName, vi.fieldDesc());
/* 558 */         releaseVar(var, vi.val.category());
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 563 */     int fieldNum = 0;
/* 564 */     for (ValInfo vi : this.valInfoList)
/*     */     {
/* 566 */       if (vi.var != -1)
/*     */       {
/*     */ 
/*     */ 
/* 570 */         VMType.loadVar(mv, 0, stateVar);
/* 571 */         VMType.loadVar(mv, vi.vmt, vi.var);
/* 572 */         mv.visitFieldInsn(181, this.stateClassName, vi.fieldName, vi.fieldDesc());
/* 573 */         fieldNum++;
/*     */       }
/*     */     }
/*     */     
/* 577 */     VMType.loadVar(mv, 0, this.methodWeaver.getFiberVar());
/* 578 */     VMType.loadVar(mv, 0, stateVar);
/* 579 */     mv.visitMethodInsn(182, "kilim/Fiber", "setState", "(Lkilim/State;)V");
/*     */     
/* 581 */     releaseVar(stateVar, 1);
/*     */     
/*     */ 
/* 584 */     retType = TypeDesc.getReturnTypeDesc(this.bb.flow.desc);
/* 585 */     if (retType == "V") {
/* 586 */       mv.visitInsn(177);
/*     */     } else {
/* 588 */       int vmt = VMType.toVmType(retType);
/*     */       
/* 590 */       mv.visitInsn(VMType.constInsn[vmt]);
/*     */       
/* 592 */       mv.visitInsn(VMType.retInsn[vmt]);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void genRestore(MethodVisitor mv, Label restoreLabel)
/*     */   {
/* 623 */     mv.visitLabel(restoreLabel);
/* 624 */     Frame f = this.bb.startFrame;
/* 625 */     int numBottom = getNumBottom();
/* 626 */     int retVar = -1;
/* 627 */     int retctype = -1;
/* 628 */     if (numBottom > 0)
/*     */     {
/*     */ 
/* 631 */       String retType = getReturnType();
/* 632 */       if (retType != "V")
/*     */       {
/* 634 */         retctype = VMType.toVmType(retType);
/* 635 */         retVar = allocVar(VMType.category[retctype]);
/* 636 */         VMType.storeVar(mv, retctype, retVar);
/*     */       }
/*     */       
/* 639 */       for (int i = numBottom - 1; i >= 0; i--) {
/* 640 */         Value v = f.getStack(i);
/* 641 */         int insn = v.isCategory1() ? 87 : 88;
/* 642 */         mv.visitInsn(insn);
/*     */       }
/*     */     }
/*     */     
/* 646 */     int stateVar = -1;
/* 647 */     if (this.valInfoList.size() > 0) {
/* 648 */       stateVar = allocVar(1);
/*     */     }
/* 650 */     genRestoreVars(mv, stateVar);
/*     */     
/*     */ 
/* 653 */     for (int i = 0; i < numBottom; i++) {
/* 654 */       Value v = f.getStack(i);
/* 655 */       if (v.isConstant()) {
/* 656 */         loadConstant(mv, v);
/*     */       } else {
/* 658 */         ValInfo vi = this.valInfoList.find(v);
/* 659 */         if (vi.var == -1) {
/* 660 */           VMType.loadVar(mv, 0, stateVar);
/* 661 */           mv.visitFieldInsn(180, this.stateClassName, vi.fieldName, vi.fieldDesc());
/* 662 */           checkcast(mv, v);
/*     */         }
/*     */         else
/*     */         {
/* 666 */           VMType.loadVar(mv, vi.vmt, vi.var);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 676 */     if ((numBottom > 0) && 
/* 677 */       (retVar != -1)) {
/* 678 */       VMType.loadVar(mv, retctype, retVar);
/*     */     }
/*     */     
/* 681 */     releaseVar(stateVar, 1);
/* 682 */     if (retctype != -1) {
/* 683 */       releaseVar(retVar, VMType.category[retctype]);
/*     */     }
/*     */   }
/*     */   
/*     */   void genRestoreEx(MethodVisitor mv, Label restoreLabel)
/*     */   {
/* 689 */     mv.visitLabel(restoreLabel);
/* 690 */     int stateVar = -1;
/* 691 */     if (this.valInfoList.size() > 0) {
/* 692 */       stateVar = allocVar(1);
/*     */     }
/* 694 */     genRestoreVars(mv, stateVar);
/* 695 */     releaseVar(stateVar, 1);
/*     */   }
/*     */   
/*     */ 
/*     */   private void genRestoreVars(MethodVisitor mv, int stateVar)
/*     */   {
/* 701 */     Frame f = this.bb.startFrame;
/*     */     
/* 703 */     if (this.valInfoList.size() > 0)
/*     */     {
/* 705 */       VMType.loadVar(mv, 0, this.methodWeaver.getFiberVar());
/* 706 */       mv.visitFieldInsn(180, "kilim/Fiber", "curState", "Lkilim/State;");
/* 707 */       if (!this.stateClassName.equals("kilim/State")) {
/* 708 */         mv.visitTypeInsn(192, this.stateClassName);
/*     */       }
/* 710 */       VMType.storeVar(mv, 0, stateVar);
/*     */     }
/*     */     
/*     */ 
/* 714 */     Usage u = this.bb.usage;
/* 715 */     int len = f.getMaxLocals();
/* 716 */     for (int i = this.bb.flow.isStatic() ? 0 : 1; 
/* 717 */         i < len; i++)
/* 718 */       if (u.isLiveIn(i))
/*     */       {
/* 720 */         Value v = f.getLocal(i);
/* 721 */         int vmt = VMType.toVmType(v.getTypeDesc());
/* 722 */         if (v.isConstant()) {
/* 723 */           loadConstant(mv, v);
/*     */         } else {
/* 725 */           ValInfo vi = this.valInfoList.find(v);
/* 726 */           if (vi.var == i)
/*     */           {
/* 728 */             VMType.loadVar(mv, 0, stateVar);
/* 729 */             mv.visitFieldInsn(180, this.stateClassName, vi.fieldName, vi.fieldDesc());
/* 730 */             checkcast(mv, v);
/*     */           }
/*     */           else {
/* 733 */             assert (vi.var < i);
/* 734 */             VMType.loadVar(mv, vi.vmt, vi.var);
/*     */           }
/*     */         }
/*     */         
/*     */ 
/* 739 */         VMType.storeVar(mv, vmt, i);
/*     */       }
/* 741 */     releaseVar(stateVar, 1);
/*     */   }
/*     */   
/*     */   private String getReceiverTypename() {
/* 745 */     MethodInsnNode min = getMethodInsn();
/* 746 */     return min.owner;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void checkcast(MethodVisitor mv, Value v)
/*     */   {
/* 757 */     String valType = v.getTypeDesc();
/* 758 */     int vmt = VMType.toVmType(valType);
/* 759 */     switch (vmt) {
/*     */     case 0: 
/* 761 */       if ((valType == "Ljava/lang/Object;") || (valType == "NULL")) {
/* 762 */         return;
/*     */       }
/* 764 */       mv.visitTypeInsn(192, TypeDesc.getInternalName(valType));
/* 765 */       break;
/*     */     case 1: 
/* 767 */       if (valType == "I")
/* 768 */         return;
/* 769 */       int insn = 0;
/* 770 */       if (valType == "S") {
/* 771 */         insn = 147;
/* 772 */       } else if (valType == "B") {
/* 773 */         insn = 145;
/* 774 */       } else if (valType == "C") {
/* 775 */         insn = 146;
/*     */       } else
/* 777 */         assert (valType == "Z");
/* 778 */       mv.visitInsn(insn);
/* 779 */       break;
/*     */     }
/*     */     
/*     */   }
/*     */   
/*     */   private void loadConstant(MethodVisitor mv, Value v)
/*     */   {
/* 786 */     if (v.getTypeDesc() == "NULL") {
/* 787 */       mv.visitInsn(1);
/* 788 */       return;
/*     */     }
/* 790 */     Object c = v.getConstVal();
/* 791 */     if ((c instanceof Integer)) {
/* 792 */       int i = ((Integer)c).intValue();
/* 793 */       if ((i > -1) && (i <= 5)) {
/* 794 */         mv.visitInsn(i + 1 + 2);
/* 795 */         return; }
/* 796 */       if ((i >= -128) && (i <= 127)) {
/* 797 */         mv.visitIntInsn(16, i);
/* 798 */         return; }
/* 799 */       if ((i >= 32768) && (i <= 32767)) {
/* 800 */         mv.visitIntInsn(17, i);
/* 801 */         return;
/*     */       }
/* 803 */     } else if ((c instanceof Float)) {
/* 804 */       Float f = Float.valueOf(((Float)c).floatValue());
/* 805 */       int insn = 0;
/* 806 */       if (f.floatValue() == 0.0D) {
/* 807 */         insn = 11;
/* 808 */       } else if (f.floatValue() == 1.0D) {
/* 809 */         insn = 12;
/* 810 */       } else if (f.floatValue() == 2.0D)
/* 811 */         insn = 13;
/* 812 */       if (insn != 0) {
/* 813 */         mv.visitInsn(insn);
/* 814 */         return;
/*     */       }
/* 816 */     } else if ((c instanceof Long)) {
/* 817 */       Long l = Long.valueOf(((Long)c).longValue());
/* 818 */       int insn = 0;
/* 819 */       if (l.longValue() == 0L) {
/* 820 */         insn = 9;
/* 821 */       } else if (l.longValue() == 1L)
/* 822 */         insn = 10;
/* 823 */       if (insn != 0) {
/* 824 */         mv.visitInsn(insn);
/* 825 */         return;
/*     */       }
/* 827 */     } else if ((c instanceof Double)) {
/* 828 */       Double d = Double.valueOf(((Double)c).doubleValue());
/* 829 */       int insn = 0;
/* 830 */       if (d.doubleValue() == 0.0D) {
/* 831 */         insn = 14;
/* 832 */       } else if (d.doubleValue() == 1.0D)
/* 833 */         insn = 15;
/* 834 */       if (insn != 0) {
/* 835 */         mv.visitInsn(insn);
/* 836 */         return;
/*     */       }
/*     */     }
/*     */     
/* 840 */     mv.visitLdcInsn(c);
/*     */   }
/*     */   
/*     */   private String createStateClass() {
/* 844 */     return this.valInfoList.size() == 0 ? "kilim/State" : this.methodWeaver.createStateClass(this.valInfoList);
/*     */   }
/*     */   
/*     */ 
/*     */   private int allocVar(int size)
/*     */   {
/* 850 */     int var = 0;
/*     */     
/*     */ 
/* 853 */     while ((this.varUsage.get(var)) || (
/* 854 */       (size != 1) && (this.varUsage.get(var + 1)))) {
/* 850 */       var++;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 859 */     this.varUsage.set(var);
/* 860 */     if (size == 2) {
/* 861 */       this.varUsage.set(var + 1);
/* 862 */       this.methodWeaver.ensureMaxVars(var + 2);
/*     */     } else {
/* 864 */       this.methodWeaver.ensureMaxVars(var + 1);
/*     */     }
/* 866 */     return var;
/*     */   }
/*     */   
/*     */   private void releaseVar(int var, int size) {
/* 870 */     if (var == -1)
/* 871 */       return;
/* 872 */     this.varUsage.clear(var);
/* 873 */     if (size == 2) {
/* 874 */       this.varUsage.clear(var + 1);
/*     */     }
/*     */   }
/*     */   
/*     */   BasicBlock getBasicBlock() {
/* 879 */     return this.bb;
/*     */   }
/*     */ }


/* Location:              /home/cfeindia/Desktop/kilim.jar!/kilim/analysis/CallWeaver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */