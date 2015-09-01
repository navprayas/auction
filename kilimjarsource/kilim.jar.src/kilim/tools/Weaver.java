/*     */ package kilim.tools;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import kilim.KilimException;
/*     */ import kilim.analysis.ClassInfo;
/*     */ import kilim.analysis.ClassWeaver;
/*     */ import kilim.analysis.Detector;
/*     */ import kilim.analysis.FileLister;
/*     */ import kilim.analysis.FileLister.Entry;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Weaver
/*     */ {
/*  31 */   public static String outputDir = null;
/*  32 */   public static boolean verbose = true;
/*  33 */   public static Pattern excludePattern = null;
/*  34 */   static int err = 0;
/*     */   
/*     */   public static void main(String[] args)
/*     */     throws IOException
/*     */   {
/*  39 */     Detector detector = Detector.DEFAULT;
/*     */     
/*  41 */     String currentName = null;
/*  42 */     for (String name : parseArgs(args)) {
/*     */       try {
/*  44 */         if (name.endsWith(".class")) {
/*  45 */           if (exclude(name))
/*     */             continue;
/*  47 */           currentName = name;
/*  48 */           weaveFile(name, new BufferedInputStream(new FileInputStream(name)), detector);
/*     */         }
/*  50 */         else if (name.endsWith(".jar")) {
/*  51 */           for (FileLister.Entry fe : new FileLister(name)) {
/*  52 */             currentName = fe.getFileName();
/*  53 */             if (currentName.endsWith(".class")) {
/*  54 */               currentName = currentName.substring(0, currentName.length() - 6).replace('/', '.');
/*     */               
/*  56 */               if (!exclude(currentName))
/*     */               {
/*  58 */                 weaveFile(currentName, fe.getInputStream(), detector); }
/*     */             }
/*     */           }
/*  61 */         } else if (new File(name).isDirectory()) {
/*  62 */           for (FileLister.Entry fe : new FileLister(name)) {
/*  63 */             currentName = fe.getFileName();
/*  64 */             if (currentName.endsWith(".class")) {
/*  65 */               if (!exclude(currentName))
/*     */               {
/*  67 */                 weaveFile(currentName, fe.getInputStream(), detector); }
/*     */             }
/*     */           }
/*     */         } else {
/*  71 */           weaveClass(name, detector);
/*     */         }
/*     */       } catch (KilimException ke) {
/*  74 */         System.err.println("Error weaving " + currentName + ". " + ke.getMessage());
/*     */         
/*  76 */         System.exit(1);
/*     */       } catch (IOException ioe) {
/*  78 */         System.err.println("Unable to find/process '" + currentName + "'");
/*  79 */         System.exit(1);
/*     */       } catch (Throwable t) {
/*  81 */         System.err.println("Error weaving " + currentName);
/*  82 */         t.printStackTrace();
/*  83 */         System.exit(1);
/*     */       }
/*     */     }
/*  86 */     System.exit(err);
/*     */   }
/*     */   
/*     */   private static boolean exclude(String name) {
/*  90 */     return excludePattern == null ? false : excludePattern.matcher(name).find();
/*     */   }
/*     */   
/*     */   static void weaveFile(String name, InputStream is, Detector detector) throws IOException
/*     */   {
/*     */     try {
/*  96 */       ClassWeaver cw = new ClassWeaver(is, detector);
/*  97 */       writeClasses(cw);
/*     */     } catch (KilimException ke) {
/*  99 */       System.err.println("***** Error weaving " + name + ". " + ke.getMessage());
/*     */       
/* 101 */       err = 1;
/*     */     } catch (RuntimeException re) {
/* 103 */       System.err.println("***** Error weaving " + name + ". " + re.getMessage());
/* 104 */       re.printStackTrace();
/* 105 */       err = 1;
/*     */     } catch (IOException ioe) {
/* 107 */       err = 1;
/* 108 */       System.err.println("***** Unable to find/process '" + name + "'\n" + ioe.getMessage());
/*     */     }
/*     */   }
/*     */   
/*     */   public static void weaveClass(String name, Detector detector) {
/*     */     try {
/* 114 */       ClassWeaver cw = new ClassWeaver(name, detector);
/* 115 */       writeClasses(cw);
/*     */     } catch (KilimException ke) {
/* 117 */       err = 1;
/* 118 */       System.err.println("***** Error weaving " + name + ". " + ke.getMessage());
/*     */     }
/*     */     catch (IOException ioe)
/*     */     {
/* 122 */       err = 1;
/* 123 */       System.err.println("***** Unable to find/process '" + name + "'\n" + ioe.getMessage());
/*     */     }
/*     */   }
/*     */   
/*     */   public static void weaveClass2(String name, Detector detector) throws IOException {
/*     */     try {
/* 129 */       ClassWeaver cw = new ClassWeaver(name, detector);
/* 130 */       writeClasses(cw);
/*     */     } catch (KilimException ke) {
/* 132 */       err = 1;
/* 133 */       System.err.println("***** Error weaving " + name + ". " + ke.getMessage());
/*     */       
/* 135 */       throw ke;
/*     */     }
/*     */     catch (IOException ioe) {
/* 138 */       err = 1;
/* 139 */       System.err.println("***** Unable to find/process '" + name + "'\n" + ioe.getMessage());
/* 140 */       throw ioe;
/*     */     }
/*     */   }
/*     */   
/*     */   static void writeClasses(ClassWeaver cw) throws IOException {
/* 145 */     List<ClassInfo> cis = cw.getClassInfos();
/* 146 */     if (cis.size() > 0) {
/* 147 */       for (ClassInfo ci : cis) {
/* 148 */         writeClass(ci);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   static void writeClass(ClassInfo ci) throws IOException {
/* 154 */     String dir = outputDir + "/" + getDirName(ci.className);
/* 155 */     mkdir(dir);
/* 156 */     String className = outputDir + '/' + ci.className + ".class";
/* 157 */     if (ci.className.startsWith("kilim/S_"))
/*     */     {
/* 159 */       if (new File(className).exists())
/* 160 */         return;
/*     */     }
/* 162 */     FileOutputStream fos = new FileOutputStream(className);
/* 163 */     fos.write(ci.bytes);
/* 164 */     fos.close();
/* 165 */     if (verbose) {
/* 166 */       System.out.println("Wrote: " + className);
/*     */     }
/*     */   }
/*     */   
/*     */   static void mkdir(String dir) throws IOException {
/* 171 */     File f = new File(dir);
/* 172 */     if ((!f.exists()) && 
/* 173 */       (!f.mkdirs())) {
/* 174 */       throw new IOException("Unable to create directory: " + dir);
/*     */     }
/*     */   }
/*     */   
/*     */   static String getDirName(String className)
/*     */   {
/* 180 */     int end = className.lastIndexOf('/');
/* 181 */     return end == -1 ? "" : className.substring(0, end);
/*     */   }
/*     */   
/*     */   static void help() {
/* 185 */     System.err.println("java kilim.tools.Weaver opts -d <outputDir> (class/directory/jar)+");
/*     */     
/* 187 */     System.err.println("   where opts are   -q : quiet");
/* 188 */     System.err.println("                    -x <regex> : exclude all classes matching regex");
/*     */     
/* 190 */     System.exit(1);
/*     */   }
/*     */   
/*     */   static ArrayList<String> parseArgs(String[] args) throws IOException {
/* 194 */     if (args.length == 0) {
/* 195 */       help();
/*     */     }
/* 197 */     ArrayList<String> ret = new ArrayList(args.length);
/* 198 */     String regex = null;
/* 199 */     for (int i = 0; i < args.length; i++) {
/* 200 */       String arg = args[i];
/* 201 */       if (arg.equals("-d")) {
/* 202 */         outputDir = args[(++i)];
/* 203 */       } else if (arg.equals("-q")) {
/* 204 */         verbose = false;
/* 205 */       } else if (arg.equals("-h")) {
/* 206 */         help();
/* 207 */       } else if (arg.equals("-x")) {
/* 208 */         regex = args[(++i)];
/* 209 */         excludePattern = Pattern.compile(regex);
/*     */       } else {
/* 211 */         ret.add(arg);
/*     */       }
/*     */     }
/* 214 */     if (outputDir == null) {
/* 215 */       System.err.println("Specify output directory with -d option");
/* 216 */       System.exit(1);
/*     */     }
/* 218 */     mkdir(outputDir);
/* 219 */     return ret;
/*     */   }
/*     */ }


/* Location:              /home/cfeindia/Desktop/kilim.jar!/kilim/tools/Weaver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */