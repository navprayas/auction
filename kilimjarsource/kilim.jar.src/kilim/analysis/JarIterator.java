/*     */ package kilim.analysis;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Iterator;
/*     */ import java.util.jar.JarEntry;
/*     */ import java.util.jar.JarFile;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class JarIterator
/*     */   implements Iterator<FileLister.Entry>
/*     */ {
/*     */   Enumeration<JarEntry> jarEnum;
/*     */   JarFile jarFile;
/*     */   String nextName;
/*     */   
/*     */   private class JEntry
/*     */     extends FileLister.Entry
/*     */   {
/*     */     private final JarEntry jarEntry;
/*     */     
/*     */     JEntry(JarEntry j)
/*     */     {
/* 122 */       this.jarEntry = j;
/*     */     }
/*     */     
/*     */     public String getFileName() {
/* 126 */       return this.jarEntry.getName();
/*     */     }
/*     */     
/*     */     public InputStream getInputStream() throws IOException
/*     */     {
/* 131 */       return JarIterator.this.jarFile.getInputStream(this.jarEntry);
/*     */     }
/*     */   }
/*     */   
/*     */   JarIterator(JarFile f) {
/* 136 */     this.jarFile = f;
/* 137 */     this.jarEnum = f.entries();
/*     */   }
/*     */   
/*     */   public boolean hasNext() {
/* 141 */     return this.jarEnum.hasMoreElements();
/*     */   }
/*     */   
/*     */   public FileLister.Entry next() {
/* 145 */     return new JEntry((JarEntry)this.jarEnum.nextElement());
/*     */   }
/*     */   
/*     */   public void remove() {
/* 149 */     throw new RuntimeException("FileLister does not remove files");
/*     */   }
/*     */ }


/* Location:              /home/cfeindia/Desktop/kilim.jar!/kilim/analysis/JarIterator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */