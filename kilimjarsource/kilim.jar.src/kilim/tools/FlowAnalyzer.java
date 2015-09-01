/*     */ package kilim.tools;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.jar.JarEntry;
/*     */ import java.util.jar.JarFile;
/*     */ import java.util.zip.ZipEntry;
/*     */ import kilim.analysis.BasicBlock;
/*     */ import kilim.analysis.ClassFlow;
/*     */ import kilim.analysis.Detector;
/*     */ import kilim.analysis.Frame;
/*     */ import kilim.analysis.MethodFlow;
/*     */ import kilim.analysis.TypeDesc;
/*     */ import kilim.analysis.Usage;
/*     */ import kilim.analysis.Utils;
/*     */ import kilim.analysis.Value;
/*     */ import org.objectweb.asm.tree.AbstractInsnNode;
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
/*     */ public class FlowAnalyzer
/*     */ {
/*     */   public static void main(String[] args)
/*     */     throws Exception
/*     */   {
/*  42 */     if (args.length == 0) {
/*  43 */       System.err.println("Usage <class name | jar file name> [methodName]");
/*  44 */       System.exit(1);
/*     */     }
/*  46 */     String name = args[0];
/*  47 */     if (name.endsWith(".jar")) {
/*  48 */       analyzeJar(name, Detector.DEFAULT);
/*     */     } else {
/*  50 */       analyzeClass(name, Detector.DEFAULT);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void analyzeClass(String className, Detector detector) {
/*     */     try {
/*  56 */       Utils.pn("-------------------------------------------------");
/*  57 */       Utils.pn("Class: " + className);
/*  58 */       System.out.flush();
/*  59 */       ArrayList<MethodFlow> flows = new ClassFlow(className, detector).analyze(true);
/*  60 */       for (MethodFlow flow : flows) {
/*  61 */         reportFlow(flow, className);
/*     */       }
/*     */     } catch (IOException e) {
/*  64 */       Utils.pn("##################################################");
/*  65 */       stackTrace(e);
/*     */     } catch (Throwable ie) {
/*  67 */       Utils.pn("##################################################");
/*  68 */       stackTrace(ie);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void stackTrace(Throwable t) {
/*  73 */     PrintStream ps = new PrintStream(System.out);
/*  74 */     t.printStackTrace(ps);
/*     */   }
/*     */   
/*     */   private static void reportFlow(MethodFlow method, String className) {
/*  78 */     Utils.resetIndentation();
/*  79 */     Utils.pn("Method : " + className + '.' + method.name);
/*     */     
/*  81 */     Utils.pn("MaxStack: " + method.maxStack);
/*  82 */     Utils.pn("MaxLocals: " + method.maxLocals);
/*  83 */     ArrayList<BasicBlock> bbs = method.getBasicBlocks();
/*  84 */     Collections.sort(bbs);
/*  85 */     Utils.indent(2);
/*  86 */     for (BasicBlock bb : bbs) {
/*  87 */       AbstractInsnNode ainode = bb.getInstruction(bb.startPos);
/*  88 */       if ((ainode instanceof MethodInsnNode)) {
/*  89 */         MethodInsnNode m = (MethodInsnNode)ainode;
/*  90 */         int n = getNumArgs(m);
/*  91 */         Utils.pn("Call(" + n + "): " + m.owner + "." + m.name + m.desc);
/*  92 */         Utils.indent(2);
/*  93 */         Utils.pn("Inframe: ");
/*  94 */         Utils.indent(2);
/*  95 */         Frame f = bb.startFrame;
/*  96 */         Utils.pn(f.toString());
/*  97 */         Utils.dedent(2);
/*  98 */         Utils.pn("Live locals:");
/*  99 */         Utils.indent(2);
/* 100 */         Usage u = bb.getVarUsage();
/* 101 */         Utils.pn(u.toString());
/* 102 */         Utils.dedent(2);
/* 103 */         Utils.pn("Actual usage: " + uniqueItems(bb, f, u, n));
/* 104 */         Utils.dedent(2);
/*     */       }
/*     */     }
/* 107 */     Utils.dedent(2);
/*     */   }
/*     */   
/*     */   private static String uniqueItems(BasicBlock bb, Frame f, Usage u, int nStack) {
/* 111 */     StringBuffer sb = new StringBuffer(80);
/* 112 */     int numNonConstants = 0;
/* 113 */     int numLive = 0;
/* 114 */     ArrayList<Value> set = new ArrayList(10);
/* 115 */     for (int i = 0; i < f.getMaxLocals(); i++) {
/* 116 */       if (u.isLiveIn(i)) {
/* 117 */         numLive++;
/* 118 */         Value v = f.getLocal(i);
/* 119 */         if (!set.contains(v)) set.add(v);
/*     */       }
/*     */     }
/* 122 */     nStack = f.getStackLen() - nStack;
/* 123 */     for (int i = 0; i < nStack; i++) {
/* 124 */       Value v = f.getStack(i);
/* 125 */       if (!set.contains(v)) set.add(v);
/*     */     }
/* 127 */     char[] sig = new char[set.size()];
/*     */     
/*     */ 
/*     */ 
/* 131 */     for (int i = 0; i < set.size(); i++) {
/* 132 */       Value v = (Value)set.get(i);
/* 133 */       char c = v.getTypeDesc().charAt(0);
/* 134 */       switch (c) {
/*     */       case 'L': case 'N': case '[': 
/* 136 */         c = 'O'; break;
/*     */       case 'B': case 'C': case 'I': case 'S': case 'Z': 
/* 138 */         c = 'I'; break;
/*     */       case 'J': 
/* 140 */         c = 'J'; break;
/*     */       case 'F': 
/* 142 */         c = 'F'; break;
/*     */       case 'D': case 'E': case 'G': case 'H': case 'K': case 'M': case 'O': case 'P': case 'Q': 
/*     */       case 'R': case 'T': case 'U': case 'V': case 'W': case 'X': case 'Y': default: 
/* 145 */         c = 'U';
/* 146 */         System.err.println("***************************************");
/* 147 */         System.err.println("Undefined/unrecognized value " + v);
/* 148 */         System.err.println("BasicBlock:\n" + bb);
/*     */       }
/*     */       
/*     */       
/* 152 */       sig[i] = c;
/* 153 */       if (v.getConstVal() == Value.NO_VAL) {
/* 154 */         numNonConstants++;
/*     */       }
/*     */     }
/* 157 */     Arrays.sort(sig);
/* 158 */     numLive += nStack;
/* 159 */     sb.append("avail: ").append(nStack + f.getMaxLocals());
/* 160 */     sb.append(", live: " + numLive);
/* 161 */     sb.append(", unique: ").append(set.size());
/* 162 */     sb.append(", unique non-const: ").append(numNonConstants);
/* 163 */     sb.append("\nState signature: ").append(set.size() == 0 ? "None" : new String(sig));
/* 164 */     return sb.toString();
/*     */   }
/*     */   
/*     */   private static int getNumArgs(MethodInsnNode m) {
/* 168 */     int ret = TypeDesc.getNumArgumentTypes(m.desc);
/* 169 */     if (m.getOpcode() != 184) ret++;
/* 170 */     return ret;
/*     */   }
/*     */   
/*     */   public static void analyzeJar(String jarFile, Detector detector) {
/*     */     try {
/* 175 */       Enumeration<JarEntry> e = new JarFile(jarFile).entries();
/* 176 */       while (e.hasMoreElements()) {
/* 177 */         ZipEntry en = (ZipEntry)e.nextElement();
/* 178 */         String n = en.getName();
/* 179 */         if (n.endsWith(".class")) {
/* 180 */           n = n.substring(0, n.length() - 6).replace('/', '.');
/* 181 */           analyzeClass(n, detector);
/*     */         }
/*     */       }
/* 184 */     } catch (Exception e) { e.printStackTrace();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/cfeindia/Desktop/kilim.jar!/kilim/tools/FlowAnalyzer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */