/*      */ package kilim.analysis;
/*      */ 
/*      */ import java.io.PrintStream;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Stack;
/*      */ import kilim.KilimException;
/*      */ import org.objectweb.asm.Label;
/*      */ import org.objectweb.asm.tree.AbstractInsnNode;
/*      */ import org.objectweb.asm.tree.FieldInsnNode;
/*      */ import org.objectweb.asm.tree.IincInsnNode;
/*      */ import org.objectweb.asm.tree.IntInsnNode;
/*      */ import org.objectweb.asm.tree.JumpInsnNode;
/*      */ import org.objectweb.asm.tree.LdcInsnNode;
/*      */ import org.objectweb.asm.tree.LookupSwitchInsnNode;
/*      */ import org.objectweb.asm.tree.MethodInsnNode;
/*      */ import org.objectweb.asm.tree.MultiANewArrayInsnNode;
/*      */ import org.objectweb.asm.tree.TableSwitchInsnNode;
/*      */ import org.objectweb.asm.tree.TypeInsnNode;
/*      */ import org.objectweb.asm.tree.VarInsnNode;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class BasicBlock
/*      */   implements Comparable<BasicBlock>
/*      */ {
/*      */   public int id;
/*      */   int flags;
/*      */   static final int ENQUEUED = 1;
/*      */   static final int SUBROUTINE_CLAIMED = 2;
/*      */   static final int COALESCED = 4;
/*      */   static final int PAUSABLE = 16;
/*      */   static final int IS_SUBROUTINE = 32;
/*      */   static final int SUB_BLOCK = 64;
/*      */   static final int INLINE_CHECKED = 128;
/*      */   static final int PAUSABLE_SUB = 256;
/*      */   public MethodFlow flow;
/*      */   public Label startLabel;
/*  166 */   public int startPos = -1;
/*      */   
/*  168 */   public int endPos = -1;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  173 */   public ArrayList<BasicBlock> successors = new ArrayList(3);
/*      */   
/*  175 */   public ArrayList<Handler> handlers = new ArrayList(2);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   int numPredecessors;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Usage usage;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   ArrayList<Usage> succUsage;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Frame startFrame;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   String caughtExceptionType;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   BasicBlock follower;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   ArrayList<BasicBlock> subBlocks;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public BasicBlock(MethodFlow aflow, Label aStartLabel)
/*      */   {
/*  217 */     this.flow = aflow;
/*  218 */     this.startLabel = aStartLabel;
/*  219 */     this.usage = new Usage(aflow.maxLocals);
/*  220 */     this.successors = new ArrayList(2);
/*      */   }
/*      */   
/*      */   Detector detector() {
/*  224 */     return this.flow.detector();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   int initialize(int pos)
/*      */   {
/*  242 */     this.startPos = pos;
/*      */     
/*      */ 
/*  245 */     boolean endOfBB = false;
/*  246 */     boolean hasFollower = true;
/*  247 */     int size = this.flow.instructions.size();
/*  248 */     BasicBlock bb; for (; pos < size; pos++)
/*  249 */       if ((pos > this.startPos) && (this.flow.getLabelAt(pos) != null)) {
/*  250 */         pos--;
/*  251 */         hasFollower = true;
/*  252 */         endOfBB = true;
/*      */       }
/*      */       else {
/*  255 */         AbstractInsnNode ain = getInstruction(pos);
/*  256 */         int opcode = ain.getOpcode();
/*  257 */         Label l; switch (opcode) {
/*      */         case 21: 
/*      */         case 22: 
/*      */         case 23: 
/*      */         case 24: 
/*      */         case 25: 
/*  263 */           this.usage.read(((VarInsnNode)ain).var);
/*  264 */           break;
/*      */         
/*      */         case 54: 
/*      */         case 55: 
/*      */         case 56: 
/*      */         case 57: 
/*      */         case 58: 
/*  271 */           this.usage.write(((VarInsnNode)ain).var);
/*  272 */           break;
/*      */         
/*      */         case 132: 
/*  275 */           int v = ((IincInsnNode)ain).var;
/*  276 */           this.usage.read(v);
/*  277 */           this.usage.write(v);
/*  278 */           break;
/*      */         
/*      */         case 153: 
/*      */         case 154: 
/*      */         case 155: 
/*      */         case 156: 
/*      */         case 157: 
/*      */         case 158: 
/*      */         case 159: 
/*      */         case 160: 
/*      */         case 161: 
/*      */         case 162: 
/*      */         case 163: 
/*      */         case 164: 
/*      */         case 165: 
/*      */         case 166: 
/*      */         case 167: 
/*      */         case 168: 
/*      */         case 198: 
/*      */         case 199: 
/*  298 */           l = ((JumpInsnNode)ain).label;
/*  299 */           bb = this.flow.getOrCreateBasicBlock(l);
/*  300 */           if (opcode == 168) {
/*  301 */             bb.setFlag(32);
/*  302 */             hasFollower = false;
/*      */           }
/*  304 */           addSuccessor(bb);
/*  305 */           if (opcode == 167) {
/*  306 */             hasFollower = false;
/*      */           }
/*  308 */           endOfBB = true;
/*  309 */           break;
/*      */         
/*      */         case 169: 
/*      */         case 172: 
/*      */         case 173: 
/*      */         case 174: 
/*      */         case 175: 
/*      */         case 176: 
/*      */         case 177: 
/*      */         case 191: 
/*  319 */           hasFollower = false;
/*  320 */           endOfBB = true;
/*  321 */           break;
/*      */         case 170: 
/*      */         case 171: 
/*      */           List<Label> otherLabels;
/*      */           Label defaultLabel;
/*      */           List<Label> otherLabels;
/*  327 */           if (opcode == 170) {
/*  328 */             Label defaultLabel = ((TableSwitchInsnNode)ain).dflt;
/*  329 */             otherLabels = ((TableSwitchInsnNode)ain).labels;
/*      */           } else {
/*  331 */             defaultLabel = ((LookupSwitchInsnNode)ain).dflt;
/*  332 */             otherLabels = ((LookupSwitchInsnNode)ain).labels;
/*      */           }
/*  334 */           for (Iterator<Label> it = otherLabels.iterator(); it.hasNext();) {
/*  335 */             l = (Label)it.next();
/*  336 */             addSuccessor(this.flow.getOrCreateBasicBlock(l));
/*      */           }
/*  338 */           addSuccessor(this.flow.getOrCreateBasicBlock(defaultLabel));
/*  339 */           endOfBB = true;
/*  340 */           hasFollower = false;
/*  341 */           break;
/*      */         
/*      */         case 182: 
/*      */         case 183: 
/*      */         case 184: 
/*      */         case 185: 
/*  347 */           if (this.flow.isPausableMethodInsn((MethodInsnNode)ain)) {
/*  348 */             if (pos == this.startPos) {
/*  349 */               setFlag(16);
/*      */             } else {
/*  351 */               Label l = this.flow.getOrCreateLabelAtPos(pos);
/*  352 */               bb = this.flow.getOrCreateBasicBlock(l);
/*  353 */               bb.setFlag(16);
/*  354 */               addSuccessor(bb);
/*  355 */               pos--;
/*  356 */               hasFollower = true;
/*  357 */               endOfBB = true;
/*      */             }
/*      */           }
/*      */           break;
/*      */         case 26: case 27: case 28: case 29: case 30: case 31: case 32: case 33: case 34: case 35: case 36: case 37: case 38: case 39: case 40: case 41: case 42: case 43: case 44: case 45: case 46: case 47: case 48: case 49: case 50: case 51: case 52: case 53: case 59: case 60: case 61: case 62: case 63: case 64: case 65: case 66: case 67: case 68: case 69: case 70: case 71: case 72: case 73: case 74: case 75: case 76: case 77: case 78: case 79: case 80: case 81: case 82: case 83: case 84: case 85: case 86: case 87: case 88: case 89: case 90: case 91: case 92: case 93: case 94: case 95: case 96: case 97: case 98: case 99: 
/*      */         case 100: case 101: case 102: case 103: case 104: case 105: case 106: case 107: case 108: case 109: case 110: case 111: case 112: case 113: case 114: case 115: case 116: case 117: case 118: case 119: case 120: case 121: case 122: case 123: case 124: case 125: case 126: case 127: case 128: case 129: case 130: case 131: case 133: case 134: case 135: case 136: case 137: case 138: case 139: case 140: case 141: case 142: case 143: case 144: case 145: case 146: case 147: case 148: case 149: case 150: case 151: case 152: case 178: case 179: case 180: case 181: case 186: case 187: case 188: case 189: case 190: case 192: case 193: case 194: case 195: case 196: case 197: default: 
/*  363 */           if ((opcode >= 26) && (opcode <= 45)) {
/*  364 */             throw new IllegalStateException("instruction variants not expected here");
/*      */           }
/*      */           break;
/*      */         }
/*      */         
/*  369 */         if (endOfBB) break;
/*      */       }
/*  371 */     this.endPos = pos;
/*  372 */     if ((hasFollower) && (pos + 1 < this.flow.instructions.size()))
/*      */     {
/*  374 */       Label l = this.flow.getOrCreateLabelAtPos(pos + 1);
/*  375 */       bb = this.flow.getOrCreateBasicBlock(l);
/*  376 */       addFollower(bb);
/*      */     }
/*      */     
/*  379 */     return pos;
/*      */   }
/*      */   
/*      */   void addFollower(BasicBlock bb) {
/*  383 */     this.follower = bb;
/*  384 */     addSuccessor(bb);
/*      */   }
/*      */   
/*      */   void addSuccessor(BasicBlock bb) {
/*  388 */     if (!this.successors.contains(bb)) {
/*  389 */       this.successors.add(bb);
/*  390 */       bb.numPredecessors += 1;
/*      */     }
/*      */   }
/*      */   
/*      */   public Usage getVarUsage() {
/*  395 */     return this.usage;
/*      */   }
/*      */   
/*      */   int lastInstruction() {
/*  399 */     AbstractInsnNode ainode = getInstruction(this.endPos);
/*  400 */     return ainode.getOpcode();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   void coalesceTrivialFollowers()
/*      */   {
/*  421 */     while (this.successors.size() == 1) {
/*  422 */       BasicBlock succ = (BasicBlock)this.successors.get(0);
/*  423 */       if ((succ.numPredecessors == 1) && (lastInstruction() != 167) && (lastInstruction() != 168) && (!succ.isPausable()))
/*      */       {
/*      */ 
/*      */ 
/*  427 */         this.successors = succ.successors;
/*  428 */         this.follower = succ.follower;
/*  429 */         this.usage.absorb(succ.usage);
/*  430 */         this.endPos = succ.endPos;
/*  431 */         succ.setFlag(4);
/*      */       }
/*      */       else {}
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setFlag(int bitFlag)
/*      */   {
/*  445 */     this.flags |= bitFlag;
/*      */   }
/*      */   
/*      */   public void unsetFlag(int bitFlag) {
/*  449 */     this.flags &= (bitFlag ^ 0xFFFFFFFF);
/*      */   }
/*      */   
/*      */   public boolean hasFlag(int bitFlag) {
/*  453 */     return (this.flags & bitFlag) != 0;
/*      */   }
/*      */   
/*      */   public int compareTo(BasicBlock o) {
/*  457 */     if (this.id == o.id) {
/*  458 */       assert (this == o);
/*      */       
/*  460 */       return 0;
/*      */     }
/*  462 */     return this.id < o.id ? -1 : 1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   void interpret()
/*      */   {
/*  474 */     Frame frame = this.startFrame.dup();
/*  475 */     if (isCatchHandler())
/*      */     {
/*      */ 
/*  478 */       frame.clearStack();
/*  479 */       frame.push(Value.make(this.startPos, this.caughtExceptionType));
/*  480 */     } else if (hasFlag(32))
/*      */     {
/*      */ 
/*      */ 
/*  484 */       frame.push(Value.make(this.startPos, "A"));
/*      */     }
/*  486 */     String componentType = null;
/*      */     
/*  488 */     boolean canThrowException = false;
/*  489 */     boolean propagateFrame = true;
/*  490 */     int i = 0;
/*      */     try {
/*  492 */       for (i = this.startPos; i <= this.endPos; i++) {
/*  493 */         AbstractInsnNode ain = getInstruction(i);
/*  494 */         int opcode = ain.getOpcode();
/*      */         int var;
/*  496 */         Value v; int val; Value v1; Value v2; Value v3; Value v2; Value v2; Value v2; Value v; switch (opcode) {
/*      */         case 0: 
/*      */           break;
/*      */         case 1: 
/*  500 */           frame.push(Value.make(i, "NULL"));
/*  501 */           break;
/*      */         
/*      */         case 2: 
/*      */         case 3: 
/*      */         case 4: 
/*      */         case 5: 
/*      */         case 6: 
/*      */         case 7: 
/*      */         case 8: 
/*  510 */           frame.push(Value.make(i, "I", new Integer(opcode - 3)));
/*      */           
/*  512 */           break;
/*      */         
/*      */ 
/*      */         case 9: 
/*      */         case 10: 
/*  517 */           frame.push(Value.make(i, "J", new Long(opcode - 9)));
/*  518 */           break;
/*      */         
/*      */         case 21: 
/*      */         case 22: 
/*      */         case 23: 
/*      */         case 24: 
/*      */         case 25: 
/*  525 */           var = ((VarInsnNode)ain).var;
/*  526 */           v = frame.getLocal(var, opcode);
/*  527 */           frame.push(v);
/*  528 */           break;
/*      */         
/*      */         case 11: 
/*      */         case 12: 
/*      */         case 13: 
/*  533 */           frame.push(Value.make(i, "F", new Float(opcode - 11)));
/*      */           
/*  535 */           break;
/*      */         
/*      */         case 14: 
/*      */         case 15: 
/*  539 */           frame.push(Value.make(i, "D", new Double(opcode - 14)));
/*      */           
/*  541 */           break;
/*      */         
/*      */ 
/*      */         case 16: 
/*  545 */           val = ((IntInsnNode)ain).operand;
/*  546 */           frame.push(Value.make(i, "B", new Integer(val)));
/*  547 */           break;
/*      */         
/*      */         case 17: 
/*  550 */           val = ((IntInsnNode)ain).operand;
/*  551 */           frame.push(Value.make(i, "S", new Integer(val)));
/*  552 */           break;
/*      */         
/*      */         case 18: 
/*  555 */           Object cval = ((LdcInsnNode)ain).cst;
/*  556 */           frame.push(Value.make(i, TypeDesc.getTypeDesc(cval), cval));
/*  557 */           break;
/*      */         
/*      */ 
/*      */         case 46: 
/*      */         case 47: 
/*      */         case 48: 
/*      */         case 49: 
/*      */         case 50: 
/*      */         case 51: 
/*      */         case 52: 
/*      */         case 53: 
/*  568 */           canThrowException = true;
/*  569 */           frame.popWord();
/*  570 */           v = frame.popWord();
/*  571 */           frame.push(Value.make(i, TypeDesc.getComponentType(v.getTypeDesc())));
/*      */           
/*      */ 
/*      */ 
/*  575 */           break;
/*      */         
/*      */         case 54: 
/*      */         case 55: 
/*      */         case 56: 
/*      */         case 57: 
/*      */         case 58: 
/*  582 */           v1 = frame.pop();
/*  583 */           var = ((VarInsnNode)ain).var;
/*  584 */           frame.setLocal(var, v1);
/*  585 */           break;
/*      */         
/*      */         case 79: 
/*      */         case 80: 
/*      */         case 81: 
/*      */         case 82: 
/*      */         case 83: 
/*      */         case 84: 
/*      */         case 85: 
/*      */         case 86: 
/*  595 */           canThrowException = true;
/*  596 */           frame.popn(3);
/*  597 */           break;
/*      */         
/*      */         case 87: 
/*  600 */           frame.popWord();
/*  601 */           break;
/*      */         
/*      */         case 88: 
/*  604 */           if (frame.pop().isCategory1()) {
/*  605 */             frame.popWord();
/*      */           }
/*      */           
/*      */ 
/*      */           break;
/*      */         case 89: 
/*  611 */           v = frame.popWord();
/*  612 */           frame.push(v);
/*  613 */           frame.push(v);
/*  614 */           break;
/*      */         
/*      */ 
/*      */ 
/*      */         case 90: 
/*  619 */           v1 = frame.popWord();
/*  620 */           v2 = frame.popWord();
/*  621 */           frame.push(v1);
/*  622 */           frame.push(v2);
/*  623 */           frame.push(v1);
/*  624 */           break;
/*      */         
/*      */ 
/*      */         case 91: 
/*  628 */           v1 = frame.popWord();
/*  629 */           v2 = frame.pop();
/*  630 */           if (v2.isCategory1()) {
/*  631 */             Value v3 = frame.pop();
/*  632 */             if (v3.isCategory1())
/*      */             {
/*  634 */               frame.push(v1);
/*  635 */               frame.push(v3);
/*  636 */               frame.push(v2);
/*  637 */               frame.push(v1);
/*  638 */               continue;
/*      */             }
/*      */           }
/*      */           else {
/*  642 */             frame.push(v1);
/*  643 */             frame.push(v2);
/*  644 */             frame.push(v1);
/*  645 */             continue;
/*      */           }
/*  647 */           throw new InternalError("Illegal use of DUP_X2");
/*      */         
/*      */ 
/*      */         case 92: 
/*  651 */           v1 = frame.pop();
/*  652 */           if (v1.isCategory1()) {
/*  653 */             v2 = frame.pop();
/*  654 */             if (v2.isCategory1())
/*      */             {
/*  656 */               frame.push(v2);
/*  657 */               frame.push(v1);
/*  658 */               frame.push(v2);
/*  659 */               frame.push(v1);
/*  660 */               continue;
/*      */             }
/*      */           }
/*      */           else {
/*  664 */             frame.push(v1);
/*  665 */             frame.push(v1);
/*  666 */             continue;
/*      */           }
/*  668 */           throw new InternalError("Illegal use of DUP2");
/*      */         
/*      */ 
/*      */         case 93: 
/*  672 */           v1 = frame.pop();
/*  673 */           if (v1.isCategory1()) {
/*  674 */             v2 = frame.pop();
/*  675 */             if (v2.isCategory1()) {
/*  676 */               v3 = frame.popWord();
/*      */               
/*  678 */               frame.push(v2);
/*  679 */               frame.push(v1);
/*  680 */               frame.push(v3);
/*  681 */               frame.push(v2);
/*  682 */               frame.push(v1);
/*  683 */               continue;
/*      */             }
/*      */           }
/*      */           else {
/*  687 */             v2 = frame.popWord();
/*  688 */             frame.push(v1);
/*  689 */             frame.push(v2);
/*  690 */             frame.push(v1);
/*  691 */             continue;
/*      */           }
/*  693 */           throw new InternalError("Illegal use of DUP2_X1");
/*      */         
/*      */ 
/*      */         case 94: 
/*  697 */           v1 = frame.pop();
/*  698 */           if (v1.isCategory1()) {
/*  699 */             v2 = frame.pop();
/*  700 */             if (v2.isCategory1()) {
/*  701 */               Value v3 = frame.pop();
/*  702 */               if (v3.isCategory1()) {
/*  703 */                 Value v4 = frame.pop();
/*  704 */                 if (v4.isCategory1())
/*      */                 {
/*  706 */                   frame.push(v2);
/*  707 */                   frame.push(v1);
/*  708 */                   frame.push(v4);
/*  709 */                   frame.push(v3);
/*  710 */                   frame.push(v2);
/*  711 */                   frame.push(v1);
/*  712 */                   continue;
/*      */                 }
/*      */               }
/*      */               else {
/*  716 */                 frame.push(v2);
/*  717 */                 frame.push(v1);
/*  718 */                 frame.push(v3);
/*  719 */                 frame.push(v2);
/*  720 */                 frame.push(v1);
/*  721 */                 continue;
/*      */               }
/*      */             }
/*      */           } else {
/*  725 */             v2 = frame.pop();
/*  726 */             if (v2.isCategory1()) {
/*  727 */               Value v3 = frame.pop();
/*  728 */               if (v3.isCategory1())
/*      */               {
/*  730 */                 frame.push(v1);
/*  731 */                 frame.push(v3);
/*  732 */                 frame.push(v2);
/*  733 */                 frame.push(v1);
/*  734 */                 continue;
/*      */               }
/*      */             }
/*      */             else {
/*  738 */               frame.push(v1);
/*  739 */               frame.push(v2);
/*  740 */               frame.push(v1);
/*  741 */               continue;
/*      */             }
/*      */           }
/*  744 */           throw new InternalError("Illegal use of DUP2_X2");
/*      */         
/*      */ 
/*      */         case 95: 
/*  748 */           v1 = frame.popWord();
/*  749 */           v2 = frame.popWord();
/*  750 */           frame.push(v1);
/*  751 */           frame.push(v2);
/*  752 */           break;
/*      */         
/*      */         case 108: 
/*      */         case 109: 
/*      */         case 112: 
/*      */         case 113: 
/*  758 */           frame.pop();
/*  759 */           canThrowException = true;
/*  760 */           break;
/*      */         
/*      */ 
/*      */         case 96: 
/*      */         case 97: 
/*      */         case 98: 
/*      */         case 99: 
/*      */         case 100: 
/*      */         case 101: 
/*      */         case 102: 
/*      */         case 103: 
/*      */         case 104: 
/*      */         case 105: 
/*      */         case 106: 
/*      */         case 107: 
/*      */         case 110: 
/*      */         case 111: 
/*      */         case 114: 
/*      */         case 115: 
/*      */         case 120: 
/*      */         case 121: 
/*      */         case 122: 
/*      */         case 123: 
/*      */         case 124: 
/*      */         case 125: 
/*      */         case 126: 
/*      */         case 127: 
/*      */         case 128: 
/*      */         case 129: 
/*      */         case 130: 
/*      */         case 131: 
/*  791 */           frame.pop();
/*  792 */           v = frame.pop();
/*      */           
/*  794 */           frame.push(Value.make(i, v.getTypeDesc()));
/*  795 */           break;
/*      */         
/*      */         case 148: 
/*      */         case 149: 
/*      */         case 150: 
/*      */         case 151: 
/*      */         case 152: 
/*  802 */           frame.popn(2);
/*  803 */           frame.push(Value.make(i, "I"));
/*  804 */           break;
/*      */         
/*      */         case 116: 
/*      */         case 117: 
/*      */         case 118: 
/*      */         case 119: 
/*  810 */           v = frame.pop();
/*  811 */           frame.push(Value.make(i, v.getTypeDesc()));
/*  812 */           break;
/*      */         
/*      */         case 132: 
/*  815 */           var = ((IincInsnNode)ain).var;
/*  816 */           frame.setLocal(var, Value.make(i, "I"));
/*  817 */           break;
/*      */         
/*      */         case 133: 
/*      */         case 140: 
/*      */         case 143: 
/*  822 */           frame.pop();
/*  823 */           frame.push(Value.make(i, "J"));
/*  824 */           break;
/*      */         
/*      */         case 135: 
/*      */         case 138: 
/*      */         case 141: 
/*  829 */           frame.pop();
/*  830 */           frame.push(Value.make(i, "D"));
/*  831 */           break;
/*      */         
/*      */         case 134: 
/*      */         case 137: 
/*      */         case 144: 
/*  836 */           frame.pop();
/*  837 */           frame.push(Value.make(i, "F"));
/*  838 */           break;
/*      */         
/*      */         case 136: 
/*      */         case 139: 
/*      */         case 142: 
/*  843 */           frame.pop();
/*  844 */           frame.push(Value.make(i, "I"));
/*  845 */           break;
/*      */         
/*      */         case 145: 
/*  848 */           frame.popWord();
/*  849 */           frame.push(Value.make(i, "Z"));
/*  850 */           break;
/*      */         
/*      */         case 146: 
/*  853 */           frame.popWord();
/*  854 */           frame.push(Value.make(i, "C"));
/*  855 */           break;
/*      */         
/*      */         case 147: 
/*  858 */           frame.popWord();
/*  859 */           frame.push(Value.make(i, "S"));
/*  860 */           break;
/*      */         
/*      */         case 153: 
/*      */         case 154: 
/*      */         case 155: 
/*      */         case 156: 
/*      */         case 157: 
/*      */         case 158: 
/*      */         case 198: 
/*      */         case 199: 
/*  870 */           frame.popWord();
/*  871 */           break;
/*      */         
/*      */         case 159: 
/*      */         case 160: 
/*      */         case 161: 
/*      */         case 162: 
/*      */         case 163: 
/*      */         case 164: 
/*      */         case 165: 
/*      */         case 166: 
/*  881 */           frame.popn(2);
/*  882 */           break;
/*      */         
/*      */ 
/*      */         case 167: 
/*      */         case 168: 
/*      */         case 169: 
/*      */           break;
/*      */         
/*      */ 
/*      */         case 170: 
/*      */         case 171: 
/*  893 */           frame.pop();
/*  894 */           break;
/*      */         
/*      */         case 172: 
/*      */         case 173: 
/*      */         case 174: 
/*      */         case 175: 
/*      */         case 176: 
/*      */         case 177: 
/*  902 */           canThrowException = true;
/*  903 */           if (opcode != 177) {
/*  904 */             frame.pop();
/*      */           }
/*  906 */           if (frame.stacklen != 0) {
/*  907 */             throw new InternalError("stack non null at method return");
/*      */           }
/*      */           
/*      */           break;
/*      */         case 178: 
/*  912 */           canThrowException = true;
/*  913 */           v = Value.make(i, TypeDesc.getInterned(((FieldInsnNode)ain).desc));
/*  914 */           frame.push(v);
/*  915 */           break;
/*      */         
/*      */         case 179: 
/*  918 */           canThrowException = true;
/*  919 */           frame.pop();
/*  920 */           break;
/*      */         
/*      */         case 180: 
/*  923 */           canThrowException = true;
/*  924 */           v1 = frame.pop();
/*  925 */           v = Value.make(i, TypeDesc.getInterned(((FieldInsnNode)ain).desc));
/*      */           
/*      */ 
/*      */ 
/*  929 */           frame.push(v);
/*  930 */           break;
/*      */         
/*      */         case 181: 
/*  933 */           canThrowException = true;
/*  934 */           v1 = frame.pop();
/*  935 */           v = frame.pop();
/*      */           
/*      */ 
/*      */ 
/*  939 */           break;
/*      */         
/*      */ 
/*      */         case 182: 
/*      */         case 183: 
/*      */         case 184: 
/*      */         case 185: 
/*  946 */           MethodInsnNode min = (MethodInsnNode)ain;
/*  947 */           String desc = min.desc;
/*  948 */           if ((this.flow.isPausableMethodInsn(min)) && (frame.numMonitorsActive > 0)) {
/*  949 */             throw new KilimException("Error: Can not call pausable nethods from within a synchronized block\nCaller: " + this.flow.name + "\nCallee: " + ((MethodInsnNode)ain).name);
/*      */           }
/*      */           
/*      */ 
/*  953 */           canThrowException = true;
/*  954 */           frame.popn(TypeDesc.getNumArgumentTypes(desc));
/*  955 */           if (opcode != 184) {
/*  956 */             v = frame.pop();
/*      */           }
/*      */           
/*  959 */           desc = TypeDesc.getReturnTypeDesc(desc);
/*  960 */           if (desc != "V") {
/*  961 */             frame.push(Value.make(i, desc));
/*      */           }
/*      */           
/*      */           break;
/*      */         case 187: 
/*  966 */           canThrowException = true;
/*  967 */           v = Value.make(i, TypeDesc.getInterned(((TypeInsnNode)ain).desc));
/*  968 */           frame.push(v);
/*  969 */           break;
/*      */         
/*      */         case 188: 
/*  972 */           canThrowException = true;
/*  973 */           frame.popWord();
/*  974 */           int atype = ((IntInsnNode)ain).operand;
/*      */           String t;
/*  976 */           switch (atype) {
/*      */           case 4: 
/*  978 */             t = "[Z";
/*  979 */             break;
/*      */           case 5: 
/*  981 */             t = "[C";
/*  982 */             break;
/*      */           case 6: 
/*  984 */             t = "[F";
/*  985 */             break;
/*      */           case 7: 
/*  987 */             t = "[D";
/*  988 */             break;
/*      */           case 8: 
/*  990 */             t = "[B";
/*  991 */             break;
/*      */           case 9: 
/*  993 */             t = "[S";
/*  994 */             break;
/*      */           case 10: 
/*  996 */             t = "[I";
/*  997 */             break;
/*      */           case 11: 
/*  999 */             t = "[J";
/* 1000 */             break;
/*      */           default: 
/* 1002 */             throw new InternalError("Illegal argument to NEWARRAY: " + atype);
/*      */           }
/*      */           
/* 1005 */           frame.push(Value.make(i, t));
/* 1006 */           break;
/*      */         case 189: 
/* 1008 */           canThrowException = true;
/* 1009 */           frame.popWord();
/* 1010 */           componentType = TypeDesc.getInterned(((TypeInsnNode)ain).desc);
/* 1011 */           v = Value.make(i, TypeDesc.getInterned("[" + componentType));
/* 1012 */           frame.push(v);
/* 1013 */           break;
/*      */         
/*      */         case 190: 
/* 1016 */           canThrowException = true;
/* 1017 */           frame.popWord();
/* 1018 */           frame.push(Value.make(i, "I"));
/* 1019 */           break;
/*      */         
/*      */         case 191: 
/* 1022 */           canThrowException = true;
/* 1023 */           frame.pop();
/* 1024 */           propagateFrame = false;
/* 1025 */           break;
/*      */         
/*      */         case 192: 
/* 1028 */           canThrowException = true;
/* 1029 */           frame.pop();
/* 1030 */           v = Value.make(i, TypeDesc.getInterned(((TypeInsnNode)ain).desc));
/* 1031 */           frame.push(v);
/* 1032 */           break;
/*      */         
/*      */         case 193: 
/* 1035 */           canThrowException = true;
/* 1036 */           frame.pop();
/* 1037 */           frame.push(Value.make(i, "I"));
/* 1038 */           break;
/*      */         
/*      */         case 194: 
/*      */         case 195: 
/* 1042 */           if (opcode == 194) {
/* 1043 */             frame.numMonitorsActive += 1;
/*      */           } else {
/* 1045 */             frame.numMonitorsActive -= 1;
/*      */           }
/* 1047 */           canThrowException = true;
/* 1048 */           frame.pop();
/* 1049 */           canThrowException = true;
/* 1050 */           break;
/*      */         
/*      */         case 197: 
/* 1053 */           MultiANewArrayInsnNode minode = (MultiANewArrayInsnNode)ain;
/* 1054 */           int dims = minode.dims;
/* 1055 */           frame.popn(dims);
/* 1056 */           componentType = TypeDesc.getInterned(minode.desc);
/* 1057 */           StringBuffer sb = new StringBuffer(componentType.length() + dims);
/*      */           
/* 1059 */           for (int j = 0; j < dims; j++)
/* 1060 */             sb.append('[');
/* 1061 */           sb.append(componentType);
/* 1062 */           v = Value.make(i, TypeDesc.getInterned(sb.toString()));
/* 1063 */           frame.push(v);
/* 1064 */           break;
/*      */         case 19: case 20: case 26: case 27: case 28: case 29: case 30: case 31: case 32: case 33: case 34: case 35: case 36: case 37: case 38: case 39: case 40: case 41: case 42: case 43: case 44: case 45: case 59: case 60: case 61: case 62: case 63: case 64: case 65: case 66: case 67: case 68: case 69: case 70: case 71: case 72: case 73: case 74: case 75: case 76: case 77: case 78: case 186: case 196: default: 
/* 1066 */           if (!$assertionsDisabled) throw new AssertionError("Unexpected opcode: " + ain.getOpcode());
/*      */           break; }
/*      */       }
/* 1069 */       i = -1;
/* 1070 */       if (propagateFrame) {
/* 1071 */         mergeSuccessors(frame);
/*      */       }
/* 1073 */       if (this.handlers != null) {
/* 1074 */         for (Handler handler : this.handlers) {
/* 1075 */           handler.catchBB.merge(frame, true);
/*      */         }
/*      */         
/*      */ 
/* 1079 */         canThrowException = false;
/*      */       }
/*      */     } catch (AssertionError ae) {
/* 1082 */       System.err.println("**** Assertion Error analyzing " + this.flow.classFlow.name + "." + this.flow.name);
/* 1083 */       System.err.println("Basic block " + this);
/* 1084 */       System.err.println("i = " + i);
/* 1085 */       System.err.println("Frame: " + frame);
/* 1086 */       throw ae;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isCatchHandler()
/*      */   {
/* 1101 */     return this.caughtExceptionType != null;
/*      */   }
/*      */   
/*      */   void mergeSuccessors(Frame frame) {
/* 1105 */     for (BasicBlock s : this.successors) {
/* 1106 */       s.merge(frame, false);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   void merge(Frame inframe, boolean localsOnly)
/*      */   {
/* 1115 */     boolean enqueue = true;
/* 1116 */     if (this.startFrame == null) {
/* 1117 */       this.startFrame = inframe.dup();
/*      */     }
/*      */     else
/*      */     {
/* 1121 */       Frame ret = this.startFrame.merge(inframe, localsOnly, this.usage);
/* 1122 */       if (ret == this.startFrame) {
/* 1123 */         enqueue = false;
/*      */       } else {
/* 1125 */         this.startFrame = ret;
/*      */       }
/*      */     }
/* 1128 */     if (enqueue) {
/* 1129 */       this.flow.enqueue(this);
/*      */     }
/*      */   }
/*      */   
/*      */   public void chooseCatchHandlers(ArrayList<Handler> handlerList) {
/* 1134 */     for (Handler h : handlerList) {
/* 1135 */       if (this == h.catchBB)
/*      */       {
/* 1137 */         this.caughtExceptionType = TypeDesc.getInterned(h.type == null ? "java/lang/Throwable" : h.type);
/*      */       }
/*      */       else {
/* 1140 */         Range ri = Range.intersect(this.startPos, this.endPos, h.from, h.to);
/* 1141 */         if (ri != null) {
/* 1142 */           this.handlers.add(new Handler(ri.from, ri.to, h.type, h.catchBB));
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public AbstractInsnNode getInstruction(int pos) {
/* 1149 */     return (AbstractInsnNode)this.flow.instructions.get(pos);
/*      */   }
/*      */   
/*      */   public boolean flowVarUsage()
/*      */   {
/* 1154 */     if (this.succUsage == null) {
/* 1155 */       this.succUsage = new ArrayList(this.successors.size() + this.handlers.size());
/*      */       
/* 1157 */       for (BasicBlock succ : this.successors) {
/* 1158 */         this.succUsage.add(succ.usage);
/*      */       }
/* 1160 */       for (Handler h : this.handlers) {
/* 1161 */         this.succUsage.add(h.catchBB.usage);
/*      */       }
/*      */     }
/* 1164 */     return this.usage.evalLiveIn(this.succUsage);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   ArrayList<BasicBlock> inline()
/*      */     throws KilimException
/*      */   {
/* 1189 */     HashMap<BasicBlock, BasicBlock> bbCopyMap = null;
/* 1190 */     HashMap<Label, Label> labelCopyMap = null;
/* 1191 */     BasicBlock targetBB = (BasicBlock)this.successors.get(0);
/* 1192 */     Label returnToLabel = this.flow.getOrCreateLabelAtPos(this.endPos + 1);
/* 1193 */     BasicBlock returnToBB = this.flow.getOrCreateBasicBlock(returnToLabel);
/* 1194 */     boolean isPausableSub = targetBB.hasFlag(256);
/*      */     
/* 1196 */     if (!targetBB.hasFlag(2))
/*      */     {
/*      */ 
/*      */ 
/* 1200 */       targetBB.setFlag(2);
/*      */       
/* 1202 */       for (BasicBlock b : targetBB.getSubBlocks()) {
/* 1203 */         if (b.lastInstruction() == 169) {
/* 1204 */           assert (b.successors.size() == 0) : toString();
/* 1205 */           b.addSuccessor(returnToBB);
/*      */         }
/*      */       }
/* 1208 */       return null;
/*      */     }
/* 1210 */     bbCopyMap = new HashMap(10);
/* 1211 */     labelCopyMap = new HashMap(10);
/* 1212 */     this.successors.clear();
/*      */     
/* 1214 */     targetBB.dupBBAndLabels(isPausableSub, bbCopyMap, labelCopyMap, returnToBB);
/* 1215 */     addSuccessor((BasicBlock)bbCopyMap.get(targetBB));
/*      */     
/* 1217 */     return dupCopyContents(isPausableSub, targetBB, returnToBB, bbCopyMap, labelCopyMap);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   void dupBBAndLabels(boolean deepCopy, HashMap<BasicBlock, BasicBlock> bbCopyMap, HashMap<Label, Label> labelCopyMap, BasicBlock returnToBB)
/*      */     throws KilimException
/*      */   {
/* 1225 */     for (BasicBlock orig : getSubBlocks()) {
/* 1226 */       BasicBlock dup = new BasicBlock(this.flow, orig.startLabel);
/* 1227 */       bbCopyMap.put(orig, dup);
/* 1228 */       if (deepCopy)
/*      */       {
/*      */ 
/* 1231 */         for (int i = orig.startPos; i <= orig.endPos; i++) {
/* 1232 */           Label origLabel = this.flow.getLabelAt(i);
/* 1233 */           if (origLabel != null) {
/* 1234 */             Label l = (Label)labelCopyMap.put(origLabel, new Label());
/* 1235 */             assert (l == null);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static ArrayList<BasicBlock> dupCopyContents(boolean deepCopy, BasicBlock targetBB, BasicBlock returnToBB, HashMap<BasicBlock, BasicBlock> bbCopyMap, HashMap<Label, Label> labelCopyMap)
/*      */     throws KilimException
/*      */   {
/* 1249 */     ArrayList<BasicBlock> newBBs = new ArrayList(targetBB.getSubBlocks().size());
/* 1250 */     for (BasicBlock orig : targetBB.getSubBlocks()) {
/* 1251 */       BasicBlock dup = (BasicBlock)bbCopyMap.get(orig);
/* 1252 */       dup.flags = orig.flags;
/* 1253 */       dup.caughtExceptionType = orig.caughtExceptionType;
/* 1254 */       dup.startPos = orig.startPos;
/* 1255 */       dup.endPos = orig.endPos;
/* 1256 */       dup.flow = orig.flow;
/* 1257 */       dup.numPredecessors = orig.numPredecessors;
/* 1258 */       dup.startFrame = null;
/* 1259 */       dup.usage = orig.usage.copy();
/* 1260 */       dup.handlers = orig.handlers;
/* 1261 */       if (orig.follower != null) {
/* 1262 */         dup.follower = ((BasicBlock)bbCopyMap.get(orig.follower));
/* 1263 */         if ((dup.follower == null) && 
/* 1264 */           (!$assertionsDisabled) && (dup.lastInstruction() != 169)) { throw new AssertionError();
/*      */         }
/*      */       }
/* 1267 */       dup.successors = new ArrayList(orig.successors.size());
/* 1268 */       if (orig.lastInstruction() == 169) {
/* 1269 */         dup.addSuccessor(returnToBB);
/*      */       } else {
/* 1271 */         for (BasicBlock s : orig.successors) {
/* 1272 */           BasicBlock b = (BasicBlock)bbCopyMap.get(s);
/* 1273 */           dup.addSuccessor(b);
/*      */         }
/*      */       }
/*      */       
/* 1277 */       if (deepCopy) {
/* 1278 */         MethodFlow flow = targetBB.flow;
/* 1279 */         List instructions = flow.instructions;
/*      */         
/* 1281 */         dup.startLabel = ((Label)labelCopyMap.get(orig.startLabel));
/* 1282 */         dup.startPos = instructions.size();
/* 1283 */         dup.endPos = (dup.startPos + (orig.endPos - orig.startPos));
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 1288 */         int newPos = instructions.size();
/* 1289 */         int end = orig.endPos;
/*      */         
/*      */ 
/* 1292 */         for (int i = orig.startPos; i <= end; newPos++) {
/* 1293 */           Label l = flow.getLabelAt(i);
/* 1294 */           if (l != null) {
/* 1295 */             l = (Label)labelCopyMap.get(l);
/* 1296 */             assert (l != null);
/* 1297 */             flow.setLabel(newPos, l);
/*      */           }
/* 1299 */           if (i != end)
/*      */           {
/* 1301 */             instructions.add(instructions.get(i));
/*      */           }
/* 1292 */           i++;
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1305 */         AbstractInsnNode lastInsn = (AbstractInsnNode)instructions.get(orig.endPos);
/*      */         
/* 1307 */         int opcode = lastInsn.getOpcode();
/* 1308 */         if ((lastInsn instanceof JumpInsnNode)) {
/* 1309 */           JumpInsnNode jin = (JumpInsnNode)lastInsn;
/* 1310 */           if (lastInsn.getOpcode() != 168) {
/* 1311 */             Label dupLabel = (Label)labelCopyMap.get(jin.label);
/* 1312 */             assert (dupLabel != null);
/* 1313 */             lastInsn = new JumpInsnNode(lastInsn.getOpcode(), dupLabel);
/*      */           }
/*      */         }
/* 1316 */         else if (opcode == 170) {
/* 1317 */           TableSwitchInsnNode tsin = (TableSwitchInsnNode)lastInsn;
/* 1318 */           Label[] labels = new Label[tsin.labels.size()];
/* 1319 */           for (i = 0; i < labels.length; i++) {
/* 1320 */             Label dupLabel = (Label)labelCopyMap.get(tsin.labels.get(i));
/* 1321 */             assert (dupLabel != null);
/* 1322 */             labels[i] = dupLabel;
/*      */           }
/* 1324 */           Label dupLabel = (Label)labelCopyMap.get(tsin.dflt);
/* 1325 */           assert (dupLabel != null);
/* 1326 */           lastInsn = new TableSwitchInsnNode(tsin.min, tsin.max, dupLabel, labels);
/* 1327 */         } else if (opcode == 171) {
/* 1328 */           LookupSwitchInsnNode lsin = (LookupSwitchInsnNode)lastInsn;
/* 1329 */           Label[] labels = new Label[lsin.labels.size()];
/* 1330 */           for (i = 0; i < labels.length; i++) {
/* 1331 */             Label dupLabel = (Label)labelCopyMap.get(lsin.labels.get(i));
/* 1332 */             assert (dupLabel != null);
/* 1333 */             labels[i] = dupLabel;
/*      */           }
/* 1335 */           Label dupLabel = (Label)labelCopyMap.get(lsin.dflt);
/* 1336 */           assert (dupLabel != null);
/* 1337 */           int[] keys = new int[lsin.keys.size()];
/* 1338 */           for (i = 0; i < keys.length; i++) {
/* 1339 */             keys[i] = ((Integer)lsin.keys.get(i)).intValue();
/*      */           }
/* 1341 */           lastInsn = new LookupSwitchInsnNode(dupLabel, keys, labels);
/*      */         }
/* 1343 */         instructions.add(lastInsn);
/*      */         
/* 1345 */         dup.handlers = new ArrayList(orig.handlers.size());
/* 1346 */         if (orig.handlers.size() > 0) {
/* 1347 */           for (Handler oh : orig.handlers) {
/* 1348 */             Handler h = new Handler(dup.startPos + (oh.from - orig.startPos), dup.endPos + (oh.to - orig.endPos), oh.type, oh.catchBB);
/*      */             
/*      */ 
/* 1351 */             dup.handlers.add(h);
/*      */           }
/*      */         }
/*      */       }
/* 1355 */       newBBs.add(dup);
/*      */     }
/* 1357 */     return newBBs;
/*      */   }
/*      */   
/*      */   public BasicBlock getJSRTarget() {
/* 1361 */     return lastInstruction() == 168 ? (BasicBlock)this.successors.get(0) : null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public ArrayList<BasicBlock> getSubBlocks()
/*      */     throws KilimException
/*      */   {
/* 1369 */     if (this.subBlocks == null) {
/* 1370 */       if (!hasFlag(32))
/* 1371 */         return null;
/* 1372 */       this.subBlocks = new ArrayList(10);
/* 1373 */       Stack<BasicBlock> stack = new Stack();
/* 1374 */       setFlag(64);
/* 1375 */       stack.add(this);
/* 1376 */       while (!stack.isEmpty()) {
/* 1377 */         BasicBlock b = (BasicBlock)stack.pop();
/* 1378 */         this.subBlocks.add(b);
/* 1379 */         if (b.lastInstruction() == 168)
/*      */         {
/* 1381 */           BasicBlock follower = b.getFollowingBlock();
/* 1382 */           if (!follower.hasFlag(64)) {
/* 1383 */             follower.setFlag(64);
/* 1384 */             stack.push(follower);
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/* 1389 */           for (BasicBlock succ : b.successors) {
/* 1390 */             if (succ == this) {
/* 1391 */               throw new KilimException("JSRs looping back to themselves are not supported");
/*      */             }
/* 1393 */             if (!succ.hasFlag(64)) {
/* 1394 */               succ.setFlag(64);
/* 1395 */               stack.push(succ);
/*      */             }
/*      */           }
/*      */         } }
/* 1399 */       Collections.sort(this.subBlocks);
/*      */     }
/* 1401 */     return this.subBlocks;
/*      */   }
/*      */   
/*      */   BasicBlock getFollowingBlock() {
/* 1405 */     if (this.follower != null) { return this.follower;
/*      */     }
/*      */     
/*      */ 
/* 1409 */     Label l = this.flow.getLabelAt(this.endPos + 1);
/* 1410 */     assert (l != null) : ("No block follows this block: " + this);
/* 1411 */     return this.flow.getBasicBlock(l);
/*      */   }
/*      */   
/*      */   public String toString()
/*      */   {
/* 1416 */     StringBuffer sb = new StringBuffer(200);
/* 1417 */     sb.append("\n========== BB #").append(this.id).append("[").append(System.identityHashCode(this)).append("]\n");
/* 1418 */     sb.append("method: ").append(this.flow.name).append("\n");
/* 1419 */     sb.append("start = ").append(this.startPos).append(",end = ").append(this.endPos).append('\n').append("Successors:");
/* 1420 */     if (this.successors.isEmpty()) {
/* 1421 */       sb.append(" None");
/*      */     } else {
/* 1423 */       for (int i = 0; i < this.successors.size(); i++) {
/* 1424 */         BasicBlock succ = (BasicBlock)this.successors.get(i);
/* 1425 */         sb.append(" ").append(succ.id).append("[").append(System.identityHashCode(succ)).append("]");
/*      */       }
/*      */     }
/* 1428 */     sb.append("\nHandlers:");
/* 1429 */     if (this.handlers.isEmpty()) {
/* 1430 */       sb.append(" None");
/*      */     } else {
/* 1432 */       for (int i = 0; i < this.handlers.size(); i++) {
/* 1433 */         sb.append(" ").append(((Handler)this.handlers.get(i)).catchBB.id);
/*      */       }
/*      */     }
/* 1436 */     sb.append("\nStart frame:\n").append(this.startFrame);
/* 1437 */     sb.append("\nUsage: ").append(this.usage);
/* 1438 */     return sb.toString();
/*      */   }
/*      */   
/*      */   public boolean isPausable() {
/* 1442 */     return hasFlag(16);
/*      */   }
/*      */   
/*      */   void setId(int aid) {
/* 1446 */     this.id = aid;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   void checkPausableJSR()
/*      */     throws KilimException
/*      */   {
/* 1455 */     BasicBlock sub = getJSRTarget();
/* 1456 */     boolean isPausableJSR = false;
/* 1457 */     if (sub != null) {
/* 1458 */       ArrayList<BasicBlock> subBlocks = sub.getSubBlocks();
/* 1459 */       for (BasicBlock b : subBlocks) {
/* 1460 */         if (b.hasFlag(16)) {
/* 1461 */           isPausableJSR = true;
/* 1462 */           break;
/*      */         }
/*      */       }
/* 1465 */       if (isPausableJSR) {
/* 1466 */         for (BasicBlock b : subBlocks) {
/* 1467 */           b.setFlag(256);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   void changeJSR_RET_toGOTOs() throws KilimException {
/* 1474 */     int lastInsn = getInstruction(this.endPos).getOpcode();
/* 1475 */     if (lastInsn == 168) {
/* 1476 */       BasicBlock targetBB = (BasicBlock)this.successors.get(0);
/* 1477 */       if (!targetBB.hasFlag(256)) return;
/* 1478 */       changeLastInsnToGOTO(targetBB.startLabel);
/* 1479 */       this.successors.clear();
/* 1480 */       this.successors.add(targetBB);
/*      */       
/*      */ 
/* 1483 */       assert (targetBB.getInstruction(targetBB.startPos).getOpcode() == 58);
/* 1484 */       targetBB.setInstruction(targetBB.startPos, new NopInsn());
/* 1485 */       targetBB.unsetFlag(32);
/* 1486 */     } else if ((lastInsn == 169) && (hasFlag(256))) {
/* 1487 */       changeLastInsnToGOTO(((BasicBlock)this.successors.get(0)).startLabel);
/*      */     }
/*      */   }
/*      */   
/*      */   void setInstruction(int pos, AbstractInsnNode insn)
/*      */   {
/* 1493 */     this.flow.instructions.set(pos, insn);
/*      */   }
/*      */   
/*      */   void changeLastInsnToGOTO(Label label) {
/* 1497 */     setInstruction(this.endPos, new JumpInsnNode(167, label));
/*      */   }
/*      */   
/*      */   public boolean isGetCurrentTask() {
/* 1501 */     AbstractInsnNode ain = getInstruction(this.startPos);
/* 1502 */     if (ain.getOpcode() == 184) {
/* 1503 */       MethodInsnNode min = (MethodInsnNode)ain;
/* 1504 */       return (min.owner.equals("kilim/Task")) && (min.name.equals("getCurrentTask"));
/*      */     }
/* 1506 */     return false;
/*      */   }
/*      */   
/*      */   boolean isInitialized() {
/* 1510 */     return (this.startPos >= 0) && (this.endPos >= 0);
/*      */   }
/*      */ }


/* Location:              /home/cfeindia/Desktop/kilim.jar!/kilim/analysis/BasicBlock.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */