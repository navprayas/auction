/*     */ package kilim.tools;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.FileReader;
/*     */ import java.io.IOException;
/*     */ import java.io.LineNumberReader;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.objectweb.asm.ClassWriter;
/*     */ import org.objectweb.asm.Label;
/*     */ import org.objectweb.asm.MethodVisitor;
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
/*     */ public class Asm
/*     */ {
/*  40 */   static boolean quiet = false;
/*  41 */   static String outputDir = ".";
/*  42 */   static Pattern wsPattern = Pattern.compile("\\s+");
/*  43 */   static Pattern commentPattern = Pattern.compile("^;.*$| ;[^\"]*");
/*     */   
/*  45 */   private boolean eofOK = false;
/*     */   private ClassWriter cv;
/*     */   private MethodVisitor mv;
/*  48 */   private int maxLocals = 1;
/*  49 */   private int maxStack = 1;
/*  50 */   private HashSet<String> declaredLabels = new HashSet();
/*  51 */   private HashMap<String, Label> labels = new HashMap();
/*     */   private String className;
/*     */   private String methodName;
/*     */   private String fileName;
/*     */   private Line line;
/*  56 */   private Line bufferedLine; private Matcher lastMatch = null;
/*  57 */   private Pattern lastPattern = null;
/*     */   
/*     */ 
/*     */   private LineNumberReader reader;
/*     */   
/*  62 */   static HashMap<String, Integer> modifiers = new HashMap();
/*     */   private static String classNamePatternStr;
/*     */   private static Pattern classPattern;
/*     */   private static Pattern superPattern;
/*     */   private static Pattern implementsPattern;
/*     */   private static String modifierPatternStr;
/*     */   private static String namePatternStr;
/*     */   private static String descPatternStr;
/*     */   private static Pattern fieldPattern;
/*     */   private static String methodNamePatternStr;
/*     */   private static Pattern methodPattern;
/*     */   private static Pattern throwsPattern;
/*     */   private static Pattern labelPattern;
/*     */   static Pattern localsPattern;
/*     */   static Pattern stackPattern;
/*     */   static Pattern catchPattern;
/*     */   static Pattern annotationPattern;
/*     */   static String[] opcodeStrs;
/*     */   private static final HashMap<String, Integer> opcodeMap;
/*     */   
/*  82 */   public static void main(String[] args) throws IOException { List<String> files = parseArgs(args);
/*  83 */     for (String arg : files) {
/*  84 */       if (!quiet) System.out.println("Asm: " + arg);
/*  85 */       new Asm(arg).write();
/*     */     }
/*     */   }
/*     */   
/*     */   public Asm(String afileName) throws IOException {
/*  90 */     this.fileName = afileName;
/*  91 */     this.reader = new LineNumberReader(new FileReader(this.fileName));
/*  92 */     this.cv = new ClassWriter(false);
/*     */     try {
/*  94 */       parseClass();
/*     */     } catch (EOF eof) {
/*  96 */       if (!this.eofOK) {
/*  97 */         System.err.println("Premature end of file: " + this.fileName);
/*  98 */         System.exit(1);
/*     */       }
/*     */     } catch (AsmException e) {
/* 101 */       System.err.println(e.getMessage());
/* 102 */       System.exit(1);
/*     */     } catch (RuntimeException e) {
/* 104 */       System.out.println("File: " + this.fileName);
/* 105 */       if (this.methodName != null) {
/* 106 */         System.out.println("Method: " + this.methodName);
/*     */       }
/* 108 */       System.out.println("");
/* 109 */       System.out.println("Line " + this.line);
/* 110 */       System.out.println("Last pattern match: " + this.lastPattern);
/* 111 */       throw e;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void parseClass()
/*     */   {
/* 119 */     readLine();
/*     */     
/* 121 */     int acc = 0;
/* 122 */     if (!lineMatch(classPattern)) {
/* 123 */       err("Expected .class or .interface declaration");
/*     */     }
/*     */     
/* 126 */     if (this.line.startsWith(".interface")) {
/* 127 */       acc = 512;
/*     */     }
/*     */     
/* 130 */     acc |= parseModifiers(group(2));
/* 131 */     this.className = group(3);
/* 132 */     String superClassName = parseSuper();
/* 133 */     String[] interfaces = parseInterfaces();
/* 134 */     this.cv.visit(49, acc, this.className, null, superClassName, interfaces);
/*     */     
/* 136 */     parseClassBody();
/*     */     
/* 138 */     this.eofOK = true;
/*     */   }
/*     */   
/*     */   private int parseModifiers(String s) {
/* 142 */     if (s == null) return 0;
/* 143 */     s = s.trim();
/* 144 */     if (s.equals("")) return 0;
/* 145 */     int acc = 0;
/* 146 */     for (String modifier : split(wsPattern, s)) {
/* 147 */       if (!modifiers.containsKey(modifier)) {
/* 148 */         err("Modifier " + modifier + " not recognized");
/*     */       }
/* 150 */       acc |= ((Integer)modifiers.get(modifier)).intValue();
/*     */     }
/* 152 */     return acc;
/*     */   }
/*     */   
/*     */ 
/*     */   private String parseSuper()
/*     */   {
/* 158 */     readLine();
/* 159 */     if (!lineMatch(superPattern)) {
/* 160 */       err("Expected .super <superclass>");
/*     */     }
/* 162 */     return group(1);
/*     */   }
/*     */   
/*     */ 
/*     */   private String[] parseInterfaces()
/*     */   {
/* 168 */     StringList interfaces = new StringList();
/*     */     for (;;) {
/* 170 */       readLine();
/* 171 */       if (!lineMatch(implementsPattern)) {
/* 172 */         putBackLine();
/* 173 */         return interfaces.toArray();
/*     */       }
/* 175 */       interfaces.add(group(1));
/*     */     }
/*     */   }
/*     */   
/*     */   private void parseClassBody() {
/*     */     for (;;) {
/* 181 */       readLine();
/* 182 */       if (lineMatch(fieldPattern)) {
/* 183 */         parseField();
/* 184 */       } else if (lineMatch(methodPattern)) {
/* 185 */         parseMethod();
/* 186 */       } else if (lineMatch(annotationPattern)) {
/* 187 */         readLine();
/* 188 */         if (!this.line.startsWith(".end annotation")) {
/* 189 */           err(".end annotation not present");
/*     */         }
/*     */       } else {
/* 192 */         err("Expected field, method or annotation in class body");
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
/*     */   private void parseField()
/*     */   {
/* 206 */     String name = group(2);
/* 207 */     String desc = group(3);
/* 208 */     String valueStr = group(5);
/* 209 */     Object value = valueStr == null ? null : parseValue(valueStr, (desc.equals("D")) || (desc.equals("J")));
/*     */     
/*     */ 
/* 212 */     this.cv.visitField(parseModifiers(group(1)), name, desc, null, value);
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
/*     */   private void parseMethod()
/*     */   {
/* 229 */     this.eofOK = false;
/* 230 */     this.methodName = group(2);
/* 231 */     int acc = parseModifiers(group(1));
/* 232 */     String desc = group(3);
/*     */     
/* 234 */     String[] exceptions = parseMethodExceptions();
/* 235 */     this.mv = this.cv.visitMethod(acc, this.methodName, desc, null, exceptions);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 242 */     parseMethodBody();
/* 243 */     this.eofOK = true;
/*     */   }
/*     */   
/*     */ 
/*     */   private String[] parseMethodExceptions()
/*     */   {
/* 249 */     StringList l = new StringList();
/*     */     for (;;) {
/* 251 */       readLine();
/* 252 */       if (!lineMatch(throwsPattern)) {
/* 253 */         putBackLine();
/* 254 */         return l.toArray();
/*     */       }
/* 256 */       l.add(group(1));
/*     */     }
/*     */   }
/*     */   
/*     */   private void parseMethodBody() {
/* 261 */     this.labels.clear();
/* 262 */     this.declaredLabels.clear();
/* 263 */     this.mv.visitCode();
/*     */     for (;;) {
/* 265 */       readLine();
/* 266 */       if (this.line.startsWith(".end method"))
/*     */         break;
/* 268 */       if (this.line.startsWith(".")) {
/* 269 */         parseMethodDirective();
/* 270 */       } else if (lineMatch(labelPattern)) {
/* 271 */         parseLabel();
/*     */       } else {
/* 273 */         parseInstructions();
/*     */       }
/*     */     }
/* 276 */     checkLabelDeclarations();
/* 277 */     this.mv.visitMaxs(this.maxStack, this.maxLocals);
/* 278 */     this.mv.visitEnd();
/*     */   }
/*     */   
/*     */   private void parseLabel()
/*     */   {
/* 283 */     String str = group(1);
/* 284 */     if (this.declaredLabels.contains(str)) {
/* 285 */       err("Duplicate label " + str);
/*     */     } else {
/* 287 */       this.declaredLabels.add(str);
/* 288 */       Label l = getLabel(str);
/* 289 */       this.mv.visitLabel(l);
/*     */     }
/*     */   }
/*     */   
/*     */   private void checkLabelDeclarations() {
/* 294 */     for (String key : this.labels.keySet()) {
/* 295 */       if (!this.declaredLabels.contains(key)) {
/* 296 */         throw new AsmException("Label " + key + " not declared in " + this.methodName);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void parseMethodDirective()
/*     */   {
/* 306 */     if (lineMatch(localsPattern)) {
/* 307 */       this.maxLocals = parseInt(group(1));
/* 308 */     } else if (lineMatch(stackPattern)) {
/* 309 */       this.maxStack = parseInt(group(1));
/* 310 */     } else if (lineMatch(catchPattern)) {
/* 311 */       String exceptionType = group(1);
/* 312 */       if (exceptionType.equals("all")) {
/* 313 */         exceptionType = null;
/*     */       }
/* 315 */       Label fromLabel = getLabel(group(2));
/* 316 */       Label toLabel = getLabel(group(3));
/* 317 */       Label usingLabel = getLabel(group(4));
/* 318 */       this.mv.visitTryCatchBlock(fromLabel, toLabel, usingLabel, exceptionType);
/* 319 */     } else if (lineMatch(annotationPattern)) {
/* 320 */       parseAnnotation();
/* 321 */     } else if (!quiet) {
/* 322 */       System.err.println("Directive ignored: " + this.line);
/*     */     }
/*     */   }
/*     */   
/*     */   private void parseAnnotation() {
/* 327 */     String s = group(2);
/* 328 */     boolean visible = s == null ? false : s.equals("visible");
/* 329 */     String desc = group(3);
/* 330 */     this.mv.visitAnnotation(desc, visible);
/* 331 */     readLine();
/* 332 */     if (!this.line.startsWith(".end annotation")) {
/* 333 */       err(".end annotation not present");
/*     */     }
/*     */   }
/*     */   
/*     */   static
/*     */   {
/*  65 */     modifiers.put("public", Integer.valueOf(1));
/*  66 */     modifiers.put("private", Integer.valueOf(2));
/*  67 */     modifiers.put("protected", Integer.valueOf(4));
/*  68 */     modifiers.put("static", Integer.valueOf(8));
/*  69 */     modifiers.put("final", Integer.valueOf(16));
/*  70 */     modifiers.put("super", Integer.valueOf(32));
/*  71 */     modifiers.put("synchronized", Integer.valueOf(32));
/*  72 */     modifiers.put("volatile", Integer.valueOf(64));
/*  73 */     modifiers.put("transient", Integer.valueOf(128));
/*  74 */     modifiers.put("native", Integer.valueOf(256));
/*  75 */     modifiers.put("interface", Integer.valueOf(512));
/*  76 */     modifiers.put("abstract", Integer.valueOf(1024));
/*  77 */     modifiers.put("strict", Integer.valueOf(2048));
/*  78 */     modifiers.put("enum", Integer.valueOf(16384));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 116 */     classNamePatternStr = "[\\w/]+";
/* 117 */     classPattern = Pattern.compile("\\.(class|interface) (.*?)?(" + classNamePatternStr + ")$");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 155 */     superPattern = Pattern.compile("\\.super (" + classNamePatternStr + ")$");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 165 */     implementsPattern = Pattern.compile("\\.implements +(" + classNamePatternStr + ")$");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 200 */     modifierPatternStr = "public|private|protected|static|final|synchronized|volatile|transient|native|abstract|strict| ";
/* 201 */     namePatternStr = "[$\\w]+";
/* 202 */     descPatternStr = "[$\\[\\w/;]+";
/* 203 */     fieldPattern = Pattern.compile(".field +(" + modifierPatternStr + ")* +(" + namePatternStr + ") +(" + descPatternStr + ") *(= *(.*))?");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 225 */     methodNamePatternStr = "[<>\\w]+";
/* 226 */     methodPattern = Pattern.compile(".method +(" + modifierPatternStr + ")* (" + methodNamePatternStr + ") *([(][^\\s]+)");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 246 */     throwsPattern = Pattern.compile("^ *\\.throws +(" + classNamePatternStr + ")");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 281 */     labelPattern = Pattern.compile("^(\\w+) *: *$");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 301 */     localsPattern = Pattern.compile(".limit +locals +([0-9]+)");
/* 302 */     stackPattern = Pattern.compile(".limit +stack +([0-9]+)");
/* 303 */     catchPattern = Pattern.compile(".catch +(" + classNamePatternStr + ") +from +([\\w]+) +to +([\\w]+) +using +([\\w]+)");
/* 304 */     annotationPattern = Pattern.compile(".annotation +((visible) )?([\\w/;]+)");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 337 */     opcodeStrs = new String[] { "nop", "aconst_null", "iconst_m1", "iconst_0", "iconst_1", "iconst_2", "iconst_3", "iconst_4", "iconst_5", "lconst_0", "lconst_1", "fconst_0", "fconst_1", "fconst_2", "dconst_0", "dconst_1", "bipush", "sipush", "ldc", "ldc_w", "ldc2_w", "iload", "lload", "fload", "dload", "aload", "iload_0", "iload_1", "iload_2", "iload_3", "lload_0", "lload_1", "lload_2", "lload_3", "fload_0", "fload_1", "fload_2", "fload_3", "dload_0", "dload_1", "dload_2", "dload_3", "aload_0", "aload_1", "aload_2", "aload_3", "iaload", "laload", "faload", "daload", "aaload", "baload", "caload", "saload", "istore", "lstore", "fstore", "dstore", "astore", "istore_0", "istore_1", "istore_2", "istore_3", "lstore_0", "lstore_1", "lstore_2", "lstore_3", "fstore_0", "fstore_1", "fstore_2", "fstore_3", "dstore_0", "dstore_1", "dstore_2", "dstore_3", "astore_0", "astore_1", "astore_2", "astore_3", "iastore", "lastore", "fastore", "dastore", "aastore", "bastore", "castore", "sastore", "pop", "pop2", "dup", "dup_x1", "dup_x2", "dup2", "dup2_x1", "dup2_x2", "swap", "iadd", "ladd", "fadd", "dadd", "isub", "lsub", "fsub", "dsub", "imul", "lmul", "fmul", "dmul", "idiv", "ldiv", "fdiv", "ddiv", "irem", "lrem", "frem", "drem", "ineg", "lneg", "fneg", "dneg", "ishl", "lshl", "ishr", "lshr", "iushr", "lushr", "iand", "land", "ior", "lor", "ixor", "lxor", "iinc", "i2l", "i2f", "i2d", "l2i", "l2f", "l2d", "f2i", "f2l", "f2d", "d2i", "d2l", "d2f", "i2b", "i2c", "i2s", "lcmp", "fcmpl", "fcmpg", "dcmpl", "dcmpg", "ifeq", "ifne", "iflt", "ifge", "ifgt", "ifle", "if_icmpeq", "if_icmpne", "if_icmplt", "if_icmpge", "if_icmpgt", "if_icmple", "if_acmpeq", "if_acmpne", "goto", "jsr", "ret", "tableswitch", "lookupswitch", "ireturn", "lreturn", "freturn", "dreturn", "areturn", "return", "getstatic", "putstatic", "getfield", "putfield", "invokevirtual", "invokespecial", "invokestatic", "invokeinterface", "unused", "new", "newarray", "anewarray", "arraylength", "athrow", "checkcast", "instanceof", "monitorenter", "monitorexit", "wide", "multianewarray", "ifnull", "ifnonnull", "goto_w", "jsr_w" };
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 372 */     opcodeMap = new HashMap();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 388 */     for (int i = 0; i < opcodeStrs.length; i++) {
/* 389 */       opcodeMap.put(opcodeStrs[i], Integer.valueOf(i));
/*     */     }
/* 391 */     opcodeMap.put("invokenonvirtual", opcodeMap.get("invokespecial"));
/*     */   }
/*     */   
/* 394 */   private static final byte[] visitTypes = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10, 10, 2, 2, 2, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 11, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 1, 4, 5, 0, 0, 0, 0, 0, 0, 6, 6, 6, 6, 7, 7, 7, 7, 0, 8, 10, 8, 0, 0, 8, 8, 0, 0, 0, 9, 3, 3, 3, 3 };
/*     */   
/*     */   private static final int INSN = 0;
/*     */   
/*     */   private static final int VAR = 1;
/*     */   
/*     */   private static final int LDC = 2;
/*     */   
/*     */   private static final int JUMP = 3;
/*     */   
/*     */   private static final int TABLESWITCH = 4;
/*     */   
/*     */   private static final int LOOKUPSWITCH = 5;
/*     */   
/*     */   private static final int FIELD = 6;
/*     */   
/*     */   private static final int METHOD = 7;
/*     */   
/*     */   private static final int TYPE = 8;
/*     */   
/*     */   private static final int MULTIANEWARRAY = 9;
/*     */   
/*     */   private static final int INT = 10;
/*     */   
/*     */   private static final int IINC = 11;
/* 419 */   static final Pattern insnPattern = Pattern.compile("(\\w+)( +(.*))?");
/* 420 */   static final Pattern quotedPattern = Pattern.compile("(.*)");
/* 421 */   static final Pattern casePattern = Pattern.compile("(\\w+) *: *(\\w+)");
/* 422 */   static final Pattern methodInvokePattern = Pattern.compile("(" + classNamePatternStr + ")[/.](" + methodNamePatternStr + ") *([(].*?[)]" + descPatternStr + ") *(, *\\d+)?");
/* 423 */   static final Pattern fieldSpecPattern = Pattern.compile("([\\w/.$]+)[/.]([\\w$]+) +([^\\s]+)");
/*     */   
/*     */   private void parseInstructions()
/*     */   {
/* 427 */     if (!lineMatch(insnPattern)) {
/* 428 */       err("Instruction is not well-formed");
/*     */     }
/* 430 */     String insn = group(1);
/* 431 */     String operand = null;
/* 432 */     if (groupCount() == 3) {
/* 433 */       operand = group(3);
/* 434 */       if (operand != null) {
/* 435 */         operand = operand.trim();
/*     */       }
/*     */     }
/* 438 */     if (!opcodeMap.containsKey(insn)) {
/* 439 */       err("Instruction " + insn + " not recognized");
/*     */     }
/* 441 */     int opcode = ((Integer)opcodeMap.get(insn)).intValue();
/* 442 */     switch (visitTypes[opcode]) {
/*     */     case 0: 
/* 444 */       this.mv.visitInsn(opcode);
/* 445 */       break;
/*     */     case 1: 
/* 447 */       this.mv.visitVarInsn(opcode, parseInt(operand));
/* 448 */       break;
/*     */     case 2: 
/* 450 */       this.mv.visitLdcInsn(parseValue(operand, opcode == 20));
/* 451 */       break;
/*     */     case 3: 
/* 453 */       Label l = getLabel(operand);
/* 454 */       this.mv.visitJumpInsn(opcode, l);
/* 455 */       break;
/*     */     
/*     */     case 4: 
/* 458 */       int min = parseInt(operand);
/* 459 */       ArrayList<Label> labelList = new ArrayList(10);
/* 460 */       Label defLabel = null;
/*     */       for (;;) {
/* 462 */         readLine();
/* 463 */         if (this.line.startsWith("default")) {
/* 464 */           lineMatch(casePattern);
/* 465 */           defLabel = getLabel(group(2));
/* 466 */           break;
/*     */         }
/* 468 */         labelList.add(getLabel(this.line.s));
/*     */       }
/*     */       
/* 471 */       Label[] labels = (Label[])labelList.toArray(new Label[labelList.size()]);
/* 472 */       int max = labels.length - 1;
/* 473 */       this.mv.visitTableSwitchInsn(min, max, defLabel, labels);
/* 474 */       break;
/*     */     
/*     */     case 5: 
/* 477 */       ArrayList<Integer> keyList = new ArrayList(10);
/* 478 */       ArrayList<Label> labelList = new ArrayList(10);
/* 479 */       Label defLabel = null;
/*     */       for (;;) {
/* 481 */         readLine();
/*     */         
/* 483 */         if (lineMatch(casePattern)) {
/* 484 */           Label lab = getLabel(group(2));
/* 485 */           String keystr = group(1);
/* 486 */           if (keystr.equals("default")) {
/* 487 */             defLabel = lab;
/* 488 */             break;
/*     */           }
/* 490 */           int key = parseInt(keystr);
/* 491 */           keyList.add(Integer.valueOf(key));
/* 492 */           labelList.add(lab);
/*     */         }
/*     */         else {
/* 495 */           err("Ill-formed switch instruction");
/*     */         }
/*     */       }
/* 498 */       Label[] labels = (Label[])labelList.toArray(new Label[labelList.size()]);
/* 499 */       int[] keys = new int[keyList.size()];
/* 500 */       for (int i = 0; i < keys.length; i++) {
/* 501 */         keys[i] = ((Integer)keyList.get(i)).intValue();
/*     */       }
/* 503 */       this.mv.visitLookupSwitchInsn(defLabel, keys, labels);
/* 504 */       break;
/*     */     
/*     */ 
/*     */     case 6: 
/* 508 */       if ((operand == null) || (!match(operand, fieldSpecPattern))) {
/* 509 */         err("Expected field access of the form foo/Bar/fieldName I");
/*     */       }
/* 511 */       String owner = group(1);
/* 512 */       String name = group(2);
/* 513 */       String desc = group(3);
/* 514 */       this.mv.visitFieldInsn(opcode, owner, name, desc);
/* 515 */       break;
/*     */     
/*     */ 
/*     */     case 7: 
/* 519 */       if ((operand == null) || (!match(operand, methodInvokePattern))) {
/* 520 */         err("Expected method invocation of the form /foo/Bar/methodName(IJ)V");
/*     */       }
/* 522 */       String owner = group(1);
/* 523 */       String name = group(2);
/* 524 */       String desc = group(3);
/* 525 */       this.mv.visitMethodInsn(opcode, owner, name, desc);
/* 526 */       break;
/*     */     
/*     */     case 8: 
/* 529 */       opcheck("expected type", operand);
/* 530 */       this.mv.visitTypeInsn(opcode, operand);
/* 531 */       break;
/*     */     
/*     */     case 9: 
/* 534 */       opcheck("expected array type and dimensions", operand);
/* 535 */       String[] words = split(wsPattern, operand);
/* 536 */       this.mv.visitMultiANewArrayInsn(words[0], parseInt(words[1]));
/* 537 */       break;
/*     */     
/*     */ 
/*     */     case 10: 
/* 541 */       int op = -1;
/* 542 */       if (opcode == 188) {
/* 543 */         if (operand.equals("boolean")) {
/* 544 */           op = 4;
/* 545 */         } else if (operand.equals("char")) {
/* 546 */           op = 5;
/* 547 */         } else if (operand.equals("float")) {
/* 548 */           op = 6;
/* 549 */         } else if (operand.equals("double")) {
/* 550 */           op = 7;
/* 551 */         } else if (operand.equals("byte")) {
/* 552 */           op = 8;
/* 553 */         } else if (operand.equals("short")) {
/* 554 */           op = 9;
/* 555 */         } else if (operand.equals("int")) {
/* 556 */           op = 10;
/* 557 */         } else if (operand.equals("long")) {
/* 558 */           op = 11;
/*     */         } else {
/* 560 */           err("Unknown type for newarray: " + operand);
/*     */         }
/*     */       } else {
/* 563 */         op = parseInt(operand);
/*     */       }
/*     */       
/* 566 */       this.mv.visitIntInsn(opcode, op);
/* 567 */       break;
/*     */     
/*     */ 
/*     */     case 11: 
/* 571 */       opcheck("Expected iinc <var> <inc amount>", operand);
/* 572 */       String[] words = split(wsPattern, operand);
/* 573 */       int var = parseInt(words[0]);
/* 574 */       int increment = parseInt(words[1]);
/* 575 */       this.mv.visitIincInsn(var, increment);
/* 576 */       break;
/*     */     
/*     */ 
/*     */     default: 
/* 580 */       err("INTERNAL ERROR: UNKNOWN TYPE OF INSTRUCTION");
/*     */     }
/*     */   }
/*     */   
/*     */   private void opcheck(String errMessage, String operand) {
/* 585 */     if (operand == null) {
/* 586 */       err(errMessage);
/*     */     }
/*     */   }
/*     */   
/*     */   private Object parseValue(String s, boolean isDoubleWord) {
/* 591 */     Object ret = null;
/* 592 */     if (s == null) {
/* 593 */       err("Expected constant value ");
/*     */     }
/* 595 */     if (s.startsWith("\"")) {
/* 596 */       if (isDoubleWord) {
/* 597 */         err("long or double value expected instead of string");
/*     */       }
/* 599 */       if (s.charAt(s.length() - 1) != '"') {
/* 600 */         err("Ill-formed string");
/*     */       }
/* 602 */       ret = s.substring(1, s.length() - 1);
/* 603 */     } else if (s.startsWith("L"))
/*     */     {
/* 605 */       ret = Type.getType(s);
/*     */     }
/* 607 */     else if (s.indexOf('.') == -1) {
/* 608 */       if (isDoubleWord) {
/* 609 */         ret = Long.valueOf(parseLong(s));
/*     */       } else {
/* 611 */         ret = Integer.valueOf(parseInt(s));
/*     */       }
/*     */     }
/* 614 */     else if (isDoubleWord) {
/* 615 */       ret = Double.valueOf(parseDouble(s));
/*     */     } else {
/* 617 */       ret = Float.valueOf(parseFloat(s));
/*     */     }
/*     */     
/*     */ 
/* 621 */     return ret;
/*     */   }
/*     */   
/*     */   int parseInt(String s) {
/* 625 */     if (s == null) {
/* 626 */       err("Expected integer");
/*     */     }
/*     */     try {
/* 629 */       return Integer.parseInt(s.trim());
/*     */     } catch (NumberFormatException nfe) {
/* 631 */       err("Expected integer value, got " + s);
/*     */     }
/* 633 */     return 0;
/*     */   }
/*     */   
/*     */   long parseLong(String s) {
/* 637 */     if (s == null) {
/* 638 */       err("Expected long");
/*     */     }
/*     */     try {
/* 641 */       return Long.parseLong(s.trim());
/*     */     } catch (NumberFormatException nfe) {
/* 643 */       err("Expected long value, got " + s);
/*     */     }
/* 645 */     return 0L;
/*     */   }
/*     */   
/*     */   float parseFloat(String s) {
/* 649 */     if (s == null) {
/* 650 */       err("Expected float");
/*     */     }
/*     */     try {
/* 653 */       return Float.parseFloat(s);
/*     */     } catch (NumberFormatException nfe) {
/* 655 */       err("Expected float, got " + s);
/*     */     }
/* 657 */     return 0.0F;
/*     */   }
/*     */   
/*     */   double parseDouble(String s) {
/* 661 */     if (s == null) {
/* 662 */       err("Expected float");
/*     */     }
/*     */     try {
/* 665 */       return Double.parseDouble(s);
/*     */     } catch (NumberFormatException nfe) {
/* 667 */       err("Expected double, got " + s);
/*     */     }
/* 669 */     return 0.0D;
/*     */   }
/*     */   
/*     */   Label getLabel(String s) {
/* 673 */     if (s == null) {
/* 674 */       err("Expected label string");
/*     */     }
/* 676 */     Label ret = (Label)this.labels.get(s);
/* 677 */     if (ret == null) {
/* 678 */       ret = new Label();
/* 679 */       this.labels.put(s, ret);
/*     */     }
/* 681 */     return ret;
/*     */   }
/*     */   
/*     */   private void err(String s) {
/* 685 */     String msg = String.format("%s: %d: %s\n", new Object[] { this.fileName, Integer.valueOf(this.line.n), s });
/* 686 */     msg = msg + this.line.s;
/* 687 */     throw new AsmException(msg);
/*     */   }
/*     */   
/*     */   private Line readLine()
/*     */   {
/* 692 */     if (this.bufferedLine != null) {
/* 693 */       this.line = this.bufferedLine;
/* 694 */       this.bufferedLine = null;
/* 695 */       return this.line;
/*     */     }
/*     */     for (;;) {
/* 698 */       Line l = getLine();
/* 699 */       String s = l.s.trim();
/* 700 */       s = commentPattern.matcher(s).replaceAll("");
/* 701 */       if (s.length() > 0) {
/* 702 */         l.s = s;
/* 703 */         this.line = l;
/* 704 */         return l;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void putBackLine()
/*     */   {
/* 711 */     this.bufferedLine = this.line;
/*     */   }
/*     */   
/* 714 */   boolean eofSeen = false;
/*     */   
/*     */   private Line getLine() {
/* 717 */     if (this.eofSeen) {
/* 718 */       throw new EOF();
/*     */     }
/*     */     try {
/* 721 */       String s = this.reader.readLine();
/* 722 */       if (s == null) {
/* 723 */         this.eofSeen = true;
/* 724 */         s = "";
/*     */       }
/* 726 */       return new Line(this.reader.getLineNumber(), s);
/*     */     } catch (IOException ioe) {
/* 728 */       ioe.printStackTrace();
/* 729 */       throw new EOF();
/*     */     }
/*     */   }
/*     */   
/*     */   boolean match(String s, Pattern p)
/*     */   {
/* 735 */     this.lastMatch = p.matcher(s);
/* 736 */     this.lastPattern = p;
/* 737 */     return this.lastMatch.find();
/*     */   }
/*     */   
/*     */   boolean lineMatch(Pattern p) {
/* 741 */     this.lastMatch = p.matcher(this.line.s);
/* 742 */     this.lastPattern = p;
/* 743 */     return this.lastMatch.find();
/*     */   }
/*     */   
/*     */   String group(int i) {
/* 747 */     return this.lastMatch.group(i);
/*     */   }
/*     */   
/*     */   int groupCount() {
/* 751 */     return this.lastMatch.groupCount();
/*     */   }
/*     */   
/*     */   static String[] split(Pattern p, String s) {
/* 755 */     return p.split(s);
/*     */   }
/*     */   
/*     */   private void write() throws IOException {
/* 759 */     String dir = outputDir + "/" + getDirName(this.className);
/* 760 */     mkdir(dir);
/* 761 */     String fileName = outputDir + '/' + this.className + ".class";
/* 762 */     FileOutputStream fos = new FileOutputStream(fileName);
/* 763 */     fos.write(this.cv.toByteArray());
/* 764 */     fos.close();
/* 765 */     System.out.println("Wrote: " + fileName);
/*     */   }
/*     */   
/*     */   private static void mkdir(String dir) throws IOException {
/* 769 */     File f = new File(dir);
/* 770 */     if ((!f.exists()) && 
/* 771 */       (!f.mkdirs())) {
/* 772 */       throw new IOException("Unable to create directory: " + dir);
/*     */     }
/*     */   }
/*     */   
/*     */   private static String getDirName(String className)
/*     */   {
/* 778 */     int end = className.lastIndexOf('/');
/* 779 */     return end == -1 ? "" : className.substring(0, end);
/*     */   }
/*     */   
/*     */   private static List<String> parseArgs(String[] args) {
/* 783 */     ArrayList<String> ret = new ArrayList(args.length);
/* 784 */     for (int i = 0; i < args.length; i++) {
/* 785 */       String arg = args[i];
/* 786 */       if (arg.equals("-d")) {
/* 787 */         outputDir = args[(++i)];
/* 788 */       } else if (arg.equals("-q")) {
/* 789 */         quiet = true;
/*     */       } else {
/* 791 */         ret.add(arg);
/*     */       }
/*     */     }
/* 794 */     return ret;
/*     */   }
/*     */ }


/* Location:              /home/cfeindia/Desktop/kilim.jar!/kilim/tools/Asm.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */