/*     */ package kilim.tools;
/*     */ 
/*     */ import java.util.Formatter;
/*     */ import java.util.HashMap;
/*     */ import kilim.analysis.TypeDesc;
/*     */ import kilim.analysis.Utils;
/*     */ import org.objectweb.asm.AnnotationVisitor;
/*     */ import org.objectweb.asm.Attribute;
/*     */ import org.objectweb.asm.Label;
/*     */ import org.objectweb.asm.MethodVisitor;
/*     */ import org.objectweb.asm.Opcodes;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class DumpMethodVisitor
/*     */   implements Opcodes, MethodVisitor
/*     */ {
/* 184 */   static String[] os = { "nop", "aconst_null", "iconst_m1", "iconst_0", "iconst_1", "iconst_2", "iconst_3", "iconst_4", "iconst_5", "lconst_0", "lconst_1", "fconst_0", "fconst_1", "fconst_2", "dconst_0", "dconst_1", "bipush", "sipush", "ldc", "ldc_w", "ldc_w", "iload", "lload", "fload", "dload", "aload", "iload_0", "iload_1", "iload_2", "iload_3", "lload_0", "lload_1", "lload_2", "lload_3", "fload_0", "fload_1", "fload_2", "fload_3", "dload_0", "dload_1", "dload_2", "dload_3", "aload_0", "aload_1", "aload_2", "aload_3", "iaload", "laload", "faload", "daload", "aaload", "baload", "caload", "saload", "istore", "lstore", "fstore", "dstore", "astore", "istore_0", "istore_1", "istore_2", "istore_3", "lstore_0", "lstore_1", "lstore_2", "lstore_3", "fstore_0", "fstore_1", "fstore_2", "fstore_3", "dstore_0", "dstore_1", "dstore_2", "dstore_3", "astore_0", "astore_1", "astore_2", "astore_3", "iastore", "lastore", "fastore", "dastore", "aastore", "bastore", "castore", "sastore", "pop", "pop2", "dup", "dup_x1", "dup_x2", "dup2", "dup2_x1", "dup2_x2", "swap", "iadd", "ladd", "fadd", "dadd", "isub", "lsub", "fsub", "dsub", "imul", "lmul", "fmul", "dmul", "idiv", "ldiv", "fdiv", "ddiv", "irem", "lrem", "frem", "drem", "ineg", "lneg", "fneg", "dneg", "ishl", "lshl", "ishr", "lshr", "iushr", "lushr", "iand", "land", "ior", "lor", "ixor", "lxor", "iinc", "i2l", "i2f", "i2d", "l2i", "l2f", "l2d", "f2i", "f2l", "f2d", "d2i", "d2l", "d2f", "i2b", "i2c", "i2s", "lcmp", "fcmpl", "fcmpg", "dcmpl", "dcmpg", "ifeq", "ifne", "iflt", "ifge", "ifgt", "ifle", "if_icmpeq", "if_icmpne", "if_icmplt", "if_icmpge", "if_icmpgt", "if_icmple", "if_acmpeq", "if_acmpne", "goto", "jsr", "ret", "tableswitch", "lookupswitch", "ireturn", "lreturn", "freturn", "dreturn", "areturn", "return", "getstatic", "putstatic", "getfield", "putfield", "invokevirtual", "invokespecial", "invokestatic", "invokeinterface", "unused", "new", "newarray", "anewarray", "arraylength", "athrow", "checkcast", "instanceof", "monitorenter", "monitorexit", "wide", "multianewarray", "ifnull", "ifnonnull", "goto_w", "jsr_w" };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 214 */   int line = 0;
/* 215 */   static StringBuilder fsb = new StringBuilder(100);
/* 216 */   static Formatter formatter = new Formatter(fsb);
/*     */   
/* 218 */   public void ppn(String s) { if (DumpClass.lineNumbers) {
/* 219 */       fsb.setLength(0);
/* 220 */       formatter.format("%-70s ; %d", new Object[] { s, Integer.valueOf(this.line++) });
/* 221 */       Utils.pn(fsb.toString());
/*     */     } else {
/* 223 */       Utils.pn(s);
/*     */     }
/*     */   }
/*     */   
/*     */   public void visitFieldInsn(int opcode, String owner, String name, String desc) {
/* 228 */     ppn(os[opcode] + " " + owner + "/" + name + " " + desc);
/*     */   }
/*     */   
/*     */   public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
/* 232 */     Utils.pn("; VISIT FRAME");
/*     */   }
/*     */   
/*     */   public void visitIincInsn(int var, int increment) {
/* 236 */     ppn("iinc " + var + " " + increment);
/*     */   }
/*     */   
/*     */   public void visitInsn(int opcode) {
/* 240 */     ppn(os[opcode]);
/*     */   }
/*     */   
/*     */   public void visitIntInsn(int opcode, int operand) {
/* 244 */     if (opcode == 188) {
/* 245 */       String t = "UNDEFINED";
/* 246 */       switch (operand) {
/* 247 */       case 4:  t = " boolean"; break;
/* 248 */       case 5:  t = " char"; break;
/* 249 */       case 6:  t = " float"; break;
/* 250 */       case 7:  t = " double"; break;
/* 251 */       case 8:  t = " byte"; break;
/* 252 */       case 9:  t = " short"; break;
/* 253 */       case 10:  t = " int"; break;
/* 254 */       case 11:  t = " long";
/*     */       }
/* 256 */       ppn(os[opcode] + t);
/*     */     } else {
/* 258 */       ppn(os[opcode] + " " + operand);
/*     */     }
/*     */   }
/*     */   
/*     */   public void visitJumpInsn(int opcode, Label label) {
/* 263 */     ppn(os[opcode] + " " + lab(label));
/*     */   }
/*     */   
/*     */   public void visitLabel(Label label) {
/* 267 */     Utils.dedent(2);
/* 268 */     Utils.pn(lab(label) + ":");
/* 269 */     Utils.indent(2);
/*     */   }
/*     */   
/*     */   public void visitLdcInsn(Object cst) {
/* 273 */     String op = ((cst instanceof Double)) || ((cst instanceof Long)) ? "ldc2_w " : "ldc ";
/* 274 */     String type = (cst instanceof String) ? "\"" + esc((String)cst) + "\"" : cst.toString();
/* 275 */     ppn(op + type);
/*     */   }
/*     */   
/*     */   public void visitLineNumber(int line, Label start) {
/* 279 */     Utils.pn(".line " + line);
/*     */   }
/*     */   
/*     */   public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
/* 283 */     Utils.pn(".var " + index + " is " + name + " " + desc + " from " + lab(start) + " to " + lab(end));
/*     */   }
/*     */   
/*     */   public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
/* 287 */     ppn("lookupswitch");
/* 288 */     Utils.indent(4);
/* 289 */     for (int i = 0; i < keys.length; i++) {
/* 290 */       Utils.pn(keys[i] + ": " + lab(labels[i]));
/*     */     }
/* 292 */     Utils.pn("default: " + lab(dflt));
/* 293 */     Utils.dedent(4);
/*     */   }
/*     */   
/*     */   public void visitMethodInsn(int opcode, String owner, String name, String desc) {
/* 297 */     String str = os[opcode] + " " + owner + "/" + name + desc;
/* 298 */     if (opcode == 185) {
/* 299 */       ppn(str + ", " + (TypeDesc.getNumArgumentTypes(desc) + 1));
/*     */     } else {
/* 301 */       ppn(str);
/*     */     }
/*     */   }
/*     */   
/*     */   public void visitMultiANewArrayInsn(String desc, int dims) {
/* 306 */     ppn("multinewarray " + desc + " " + dims);
/*     */   }
/*     */   
/*     */   public void visitTableSwitchInsn(int min, int max, Label dflt, Label[] labels) {
/* 310 */     ppn("tableswitch  " + min);
/* 311 */     Utils.indent(4);
/* 312 */     for (int i = min; i <= max; i++) {
/* 313 */       Utils.pn(lab(labels[(i - min)]));
/*     */     }
/* 315 */     Utils.pn("default: " + lab(dflt));
/* 316 */     Utils.dedent(4);
/*     */   }
/*     */   
/*     */   public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
/* 320 */     Utils.pn(".catch " + type + " from " + lab(start) + " to " + lab(end) + " using " + lab(handler));
/*     */   }
/*     */   
/*     */   public void visitTypeInsn(int opcode, String desc)
/*     */   {
/* 325 */     ppn(os[opcode] + " " + desc);
/*     */   }
/*     */   
/*     */   public void visitVarInsn(int opcode, int var) {
/* 329 */     ppn(os[opcode] + " " + var);
/*     */   }
/*     */   
/* 332 */   HashMap<Label, String> labels = new HashMap();
/* 333 */   int labCount = 1;
/*     */   
/* 335 */   private String lab(Label label) { String ret = (String)this.labels.get(label);
/* 336 */     if (ret == null) {
/* 337 */       ret = "L" + this.labCount++;
/* 338 */       this.labels.put(label, ret);
/*     */     }
/* 340 */     return ret;
/*     */   }
/*     */   
/* 343 */   public AnnotationVisitor visitAnnotationDefault() { return new DummyAnnotationVisitor(); }
/*     */   
/*     */   public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
/* 346 */     Utils.pn(".annotation " + (visible ? "visible " : "") + desc);
/* 347 */     Utils.pn(".end annotation");
/* 348 */     return new DummyAnnotationVisitor();
/*     */   }
/*     */   
/* 351 */   public AnnotationVisitor visitParameterAnnotation(int parameter, String desc, boolean visible) { return new DummyAnnotationVisitor(); }
/*     */   
/*     */ 
/*     */   public void visitAttribute(Attribute attr) {}
/*     */   
/* 356 */   public void visitCode() { Utils.indent(4); }
/*     */   
/*     */   public void visitMaxs(int maxStack, int maxLocals) {
/* 359 */     Utils.pn(".limit stack " + maxStack);
/* 360 */     Utils.pn(".limit locals " + maxLocals);
/*     */   }
/*     */   
/* 363 */   public void visitEnd() { Utils.resetIndentation();
/* 364 */     Utils.pn(".end method");
/*     */   }
/*     */   
/*     */   private static String esc(String s) {
/* 368 */     return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n");
/*     */   }
/*     */ }


/* Location:              /home/cfeindia/Desktop/kilim.jar!/kilim/tools/DumpMethodVisitor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */