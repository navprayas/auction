/*     */ package kilim.analysis;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Iterator;
/*     */ import java.util.Stack;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class DirIterator
/*     */   implements Iterator<FileLister.Entry>
/*     */ {
/*     */   private static class DirEntry
/*     */     extends FileLister.Entry
/*     */   {
/*     */     final File file;
/*     */     
/*     */     DirEntry(File f)
/*     */     {
/*  61 */       this.file = f;
/*     */     }
/*     */     
/*     */     public String getFileName() {
/*     */       try {
/*  66 */         return this.file.getCanonicalPath();
/*     */       } catch (IOException ignore) {}
/*  68 */       return null;
/*     */     }
/*     */     
/*     */     public InputStream getInputStream() throws IOException
/*     */     {
/*  73 */       return new BufferedInputStream(new FileInputStream(this.file));
/*     */     }
/*     */   }
/*     */   
/*  77 */   Stack<File> stack = new Stack();
/*     */   
/*     */   DirIterator(File f) {
/*  80 */     this.stack.push(f);
/*     */   }
/*     */   
/*     */   public boolean hasNext() {
/*  84 */     return !this.stack.isEmpty();
/*     */   }
/*     */   
/*     */   public FileLister.Entry next() {
/*  88 */     File ret = (File)this.stack.pop();
/*  89 */     if (ret.isDirectory())
/*     */     {
/*  91 */       File[] files = ret.listFiles();
/*     */       
/*     */ 
/*  94 */       for (int i = files.length - 1; i >= 0; i--) {
/*  95 */         File ff = files[i];
/*  96 */         if (ff.isDirectory()) {
/*  97 */           this.stack.push(ff);
/*     */         }
/*     */       }
/* 100 */       for (int i = files.length - 1; i >= 0; i--) {
/* 101 */         File ff = files[i];
/* 102 */         if (!ff.isDirectory()) {
/* 103 */           this.stack.push(ff);
/*     */         }
/*     */       }
/*     */     }
/* 107 */     return new DirEntry(ret);
/*     */   }
/*     */   
/*     */   public void remove() {
/* 111 */     throw new RuntimeException("FileLister does not remove files");
/*     */   }
/*     */ }


/* Location:              /home/cfeindia/Desktop/kilim.jar!/kilim/analysis/DirIterator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */