/*     */ package kilim;
/*     */ 
/*     */ public class RingQueue<T> {
/*     */   protected T[] elements;
/*     */   protected int iprod;
/*     */   protected int icons;
/*     */   protected int maxSize;
/*     */   protected int size;
/*     */   
/*     */   public RingQueue(int initialSize) {
/*  11 */     this(initialSize, Integer.MAX_VALUE);
/*     */   }
/*     */   
/*     */   public RingQueue(int initialSize, int maxSize)
/*     */   {
/*  16 */     this.elements = ((Object[])new Object[initialSize]);
/*  17 */     this.size = 0;
/*  18 */     this.maxSize = maxSize;
/*     */   }
/*     */   
/*     */   public int size() {
/*  22 */     return this.size;
/*     */   }
/*     */   
/*     */ 
/*     */   public T get()
/*     */   {
/*  28 */     int n = this.size;
/*  29 */     T elem; if (n > 0) {
/*  30 */       T[] elems = this.elements;
/*  31 */       int ic = this.icons;
/*  32 */       T elem = elems[ic];
/*  33 */       elems[ic] = null;
/*  34 */       this.icons = ((ic + 1) % elems.length);
/*  35 */       this.size = (n - 1);
/*     */     } else {
/*  37 */       elem = null;
/*     */     }
/*  39 */     return elem;
/*     */   }
/*     */   
/*     */   public boolean put(T elem)
/*     */   {
/*  44 */     boolean ret = true;
/*  45 */     if (elem == null) {
/*  46 */       throw new NullPointerException("Null message supplied to put");
/*     */     }
/*  48 */     int ip = this.iprod;
/*  49 */     int ic = this.icons;
/*  50 */     int n = this.size;
/*  51 */     if (n == this.elements.length) {
/*  52 */       assert (ic == ip) : "numElements == elements.length && ic != ip";
/*  53 */       if (n < this.maxSize) {
/*  54 */         T[] newmsgs = (Object[])new Object[Math.min(n * 2, this.maxSize)];
/*  55 */         System.arraycopy(this.elements, ic, newmsgs, 0, n - ic);
/*  56 */         if (ic > 0) {
/*  57 */           System.arraycopy(this.elements, 0, newmsgs, n - ic, ic);
/*     */         }
/*  59 */         this.elements = newmsgs;
/*  60 */         ip = n;
/*  61 */         ic = 0;
/*     */       } else {
/*  63 */         ret = false;
/*     */       }
/*     */     }
/*  66 */     if (ret) {
/*  67 */       this.size = (n + 1);
/*  68 */       this.elements[ip] = elem;
/*  69 */       this.iprod = ((ip + 1) % this.elements.length);
/*  70 */       this.icons = ic;
/*     */     }
/*  72 */     return ret;
/*     */   }
/*     */   
/*     */   public boolean contains(T obj) {
/*  76 */     int i = this.icons;
/*  77 */     int c = 0;
/*  78 */     T[] elems = this.elements;
/*  79 */     while (c < this.size) {
/*  80 */       if (obj == elems[i])
/*  81 */         return true;
/*  82 */       i = (i + 1) % elems.length;
/*  83 */       c++;
/*     */     }
/*  85 */     return false;
/*     */   }
/*     */   
/*     */   public void reset() {
/*  89 */     this.icons = (this.iprod = 0);
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/*  94 */     StringBuilder sb = new StringBuilder();
/*  95 */     int i = this.icons;
/*  96 */     int c = 0;
/*  97 */     T[] elems = this.elements;
/*  98 */     while (c < this.size) {
/*  99 */       sb.append(elems[i]);
/* 100 */       i = (i + 1) % elems.length;
/* 101 */       c++;
/*     */     }
/* 103 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              /home/cfeindia/Desktop/kilim.jar!/kilim/RingQueue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */