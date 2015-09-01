/*    */ package kilim.analysis;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.util.Iterator;
/*    */ import java.util.jar.JarFile;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FileLister
/*    */   implements Iterable<Entry>
/*    */ {
/*    */   Iterator<Entry> iter;
/*    */   
/*    */   public FileLister(String dirOrJarName)
/*    */     throws IOException
/*    */   {
/* 33 */     if (dirOrJarName.endsWith(".jar")) {
/* 34 */       this.iter = openJar(dirOrJarName);
/*    */     } else {
/* 36 */       File f = new File(dirOrJarName);
/* 37 */       if ((f.exists()) && (f.isDirectory())) {
/* 38 */         this.iter = new DirIterator(f);
/*    */       } else {
/* 40 */         throw new IOException("Expected jar file or directory name");
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   private Iterator<Entry> openJar(String jarFile) throws IOException {
/* 46 */     return new JarIterator(new JarFile(jarFile));
/*    */   }
/*    */   
/*    */   public Iterator<Entry> iterator() {
/* 50 */     return this.iter;
/*    */   }
/*    */   
/*    */   public static abstract class Entry
/*    */   {
/*    */     public abstract String getFileName();
/*    */     
/*    */     public abstract InputStream getInputStream()
/*    */       throws IOException;
/*    */   }
/*    */ }


/* Location:              /home/cfeindia/Desktop/kilim.jar!/kilim/analysis/FileLister.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */