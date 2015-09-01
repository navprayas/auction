/*    */ package kilim.analysis;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.PrintStream;
/*    */ import java.util.HashMap;
/*    */ import java.util.Iterator;
/*    */ import java.util.LinkedList;
/*    */ import java.util.List;
/*    */ import org.objectweb.asm.ClassReader;
/*    */ import org.objectweb.asm.tree.ClassNode;
/*    */ import org.objectweb.asm.tree.MethodNode;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AsmDetector
/*    */ {
/* 17 */   static HashMap<String, ClassCache> classCacheMap = new HashMap();
/*    */   
/*    */   public static int getPausableStatus(String className, String methodName, String desc, Detector detector)
/*    */   {
/*    */     try {
/* 22 */       ClassCache classCache = (ClassCache)classCacheMap.get(className);
/* 23 */       if (classCache == null) {
/* 24 */         ClassReader cr = new ClassReader(className);
/* 25 */         ClassNode cn = new ClassNode();
/* 26 */         cr.accept(cn, false);
/* 27 */         classCache = cache(className, cn);
/*    */       }
/* 29 */       int status = classCache.getPausableStatus(methodName, desc);
/* 30 */       if (status == 0)
/*    */       {
/* 32 */         for (String superName : classCache.superClasses) {
/* 33 */           status = detector.getPausableStatus(superName, methodName, desc);
/* 34 */           if (status != 0)
/*    */             break;
/*    */         }
/*    */       }
/* 38 */       return status;
/*    */     } catch (IOException ioe) {
/* 40 */       System.err.println("***Error reading " + className + ": " + ioe.getMessage()); }
/* 41 */     return 0;
/*    */   }
/*    */   
/*    */   private static ClassCache cache(String className, ClassNode cn) {
/* 45 */     ClassCache classCache = new ClassCache();
/* 46 */     classCache.className = className;
/* 47 */     classCacheMap.put(className, classCache);
/*    */     
/* 49 */     for (Object m : cn.methods) {
/* 50 */       MethodNode mn = (MethodNode)m;
/* 51 */       Iterator i$ = mn.exceptions.iterator(); for (;;) { if (!i$.hasNext()) break label137; Object exception = i$.next();
/* 52 */         if ("kilim/Pausable".equals(exception)) {
/* 53 */           classCache.pausableMethods.add(mn.name + mn.desc);
/* 54 */           break;
/*    */         }
/*    */       }
/* 57 */       classCache.otherMethods.add(mn.name + mn.desc); }
/*    */     label137:
/* 59 */     classCache.addSuper(cn.superName);
/* 60 */     for (Object interfaceName : cn.interfaces) {
/* 61 */       classCache.addSuper((String)interfaceName);
/*    */     }
/*    */     
/* 64 */     return classCache;
/*    */   }
/*    */   
/* 67 */   public static void main(String[] args) { getPausableStatus("com/sleepycat/je/Database", "putInternal", "Lcom/sleepycat/je/Transaction;Lcom/sleepycat/je/DatabaseEntry;Lcom/sleepycat/je/DatabaseEntry;Lcom/sleepycat/je/dbi/PutMode;Lkilim/Fiber;)Lcom/sleepycat/je/OperationStatus;)V", Detector.DEFAULT); }
/*    */   
/*    */   static class ClassCache
/*    */   {
/*    */     String className;
/* 72 */     LinkedList<String> pausableMethods = new LinkedList();
/* 73 */     LinkedList<String> otherMethods = new LinkedList();
/* 74 */     LinkedList<String> superClasses = new LinkedList();
/*    */     
/* 76 */     public void addSuper(String superName) { if (superName.equals("java/lang/Object")) return;
/* 77 */       if (!this.superClasses.contains(superName)) this.superClasses.add(superName);
/*    */     }
/*    */     
/* 80 */     public int getPausableStatus(String methodName, String desc) { String md = methodName + desc;
/* 81 */       if (this.pausableMethods.contains(md))
/* 82 */         return 1;
/* 83 */       if (this.otherMethods.contains(md)) {
/* 84 */         return 2;
/*    */       }
/* 86 */       return 0;
/*    */     }
/*    */     
/*    */     public String toString()
/*    */     {
/* 91 */       return this.className + "\nPausable Methods: " + this.pausableMethods + "\nOthers:" + this.otherMethods;
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/cfeindia/Desktop/kilim.jar!/kilim/analysis/AsmDetector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */