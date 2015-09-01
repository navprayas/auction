/*     */ package kilim.tools;
/*     */ 
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.Enumeration;
/*     */ import java.util.jar.JarEntry;
/*     */ import java.util.jar.JarFile;
/*     */ import java.util.zip.ZipEntry;
/*     */ import kilim.analysis.Utils;
/*     */ import org.objectweb.asm.AnnotationVisitor;
/*     */ import org.objectweb.asm.Attribute;
/*     */ import org.objectweb.asm.ClassReader;
/*     */ import org.objectweb.asm.ClassVisitor;
/*     */ import org.objectweb.asm.FieldVisitor;
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
/*     */ public class DumpClass
/*     */   implements Opcodes, ClassVisitor
/*     */ {
/*  42 */   static boolean lineNumbers = true;
/*     */   
/*     */   public static void main(String[] args) throws IOException {
/*  45 */     String name = args.length == 2 ? args[1] : args[0];
/*     */     
/*  47 */     boolean flags = false;
/*     */     
/*  49 */     if (name.endsWith(".jar")) {
/*     */       try {
/*  51 */         Enumeration<JarEntry> e = new JarFile(name).entries();
/*  52 */         while (e.hasMoreElements()) {
/*  53 */           ZipEntry en = (ZipEntry)e.nextElement();
/*  54 */           String n = en.getName();
/*  55 */           if (n.endsWith(".class")) {
/*  56 */             n = n.substring(0, n.length() - 6).replace('/', '.');
/*  57 */             new DumpClass(n, flags);
/*     */           }
/*     */         }
/*  60 */       } catch (Exception e) { e.printStackTrace();
/*     */       }
/*     */     } else {
/*  63 */       new DumpClass(name, flags);
/*     */     }
/*     */   }
/*     */   
/*     */   DumpClass(String className, boolean flags) throws IOException
/*     */   {
/*     */     ClassReader cr;
/*     */     ClassReader cr;
/*  71 */     if (className.endsWith(".class")) {
/*  72 */       FileInputStream fis = new FileInputStream(className);
/*  73 */       cr = new ClassReader(fis);
/*     */     } else {
/*  75 */       cr = new ClassReader(className);
/*     */     }
/*  77 */     cr.accept(this, flags);
/*     */   }
/*     */   
/*     */ 
/*     */   public void visit(int version, int access, String name, String signature, String superName, String[] interfaces)
/*     */   {
/*  83 */     Utils.p(".class ");
/*  84 */     Utils.p(Modifier.toString(access));
/*  85 */     Utils.p(" ");
/*  86 */     Utils.pn(name);
/*  87 */     if (superName != null) {
/*  88 */       Utils.pn(".super " + superName);
/*     */     }
/*  90 */     if (interfaces != null) {
/*  91 */       for (int i = 0; i < interfaces.length; i++) {
/*  92 */         Utils.p(".implements ");
/*  93 */         Utils.pn(interfaces[i]);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
/*  99 */     Utils.pn(".annotation " + (visible ? "visible " : "") + desc);
/* 100 */     Utils.pn(".end annotation");
/* 101 */     return new DummyAnnotationVisitor();
/*     */   }
/*     */   
/*     */   public void visitAttribute(Attribute attr) {}
/*     */   
/*     */   public void visitEnd() {}
/*     */   
/*     */   public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
/* 109 */     Utils.p(".field ");
/* 110 */     Utils.p(Modifier.toString(access));
/* 111 */     Utils.p(" ");
/* 112 */     Utils.p(name);
/* 113 */     Utils.p(" ");
/* 114 */     Utils.p(desc);
/* 115 */     if (value != null) {
/* 116 */       Utils.p(" = ");
/* 117 */       if ((value instanceof String)) {
/* 118 */         Utils.pn("\"" + value + "\"");
/*     */       } else {
/* 120 */         Utils.pn(value.toString());
/*     */       }
/*     */     } else {
/* 123 */       Utils.pn();
/*     */     }
/* 125 */     return null;
/*     */   }
/*     */   
/*     */   public void visitInnerClass(String name, String outerName, String innerName, int access) {}
/*     */   
/*     */   public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions)
/*     */   {
/* 132 */     Utils.pn("");
/* 133 */     Utils.pn("; -------------------------------------------------------------");
/* 134 */     Utils.p(".method ");
/* 135 */     Utils.p(Modifier.toString(access));
/* 136 */     Utils.p(" ");
/* 137 */     Utils.p(name);
/* 138 */     Utils.pn(desc);
/* 139 */     Utils.pn("; signature = " + signature);
/* 140 */     Utils.pn("; -------------------------------------------------------------\n");
/* 141 */     if (exceptions != null) {
/* 142 */       for (int i = 0; i < exceptions.length; i++) {
/* 143 */         Utils.p(".throws ");
/* 144 */         Utils.pn(exceptions[i]);
/*     */       }
/*     */     }
/* 147 */     return new DumpMethodVisitor();
/*     */   }
/*     */   
/*     */   public void visitOuterClass(String owner, String name, String desc) {}
/*     */   
/*     */   public void visitSource(String source, String debug) {}
/*     */ }


/* Location:              /home/cfeindia/Desktop/kilim.jar!/kilim/tools/DumpClass.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */