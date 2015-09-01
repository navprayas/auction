/*     */ package kilim.http;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.URLDecoder;
/*     */ import java.nio.ByteBuffer;
/*     */ import kilim.Fiber;
/*     */ import kilim.Pausable;
/*     */ import kilim.S_O;
/*     */ import kilim.S_O2;
/*     */ import kilim.S_O2I2;
/*     */ import kilim.S_OI;
/*     */ import kilim.State;
/*     */ import kilim.Task;
/*     */ import kilim.nio.EndPoint;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HttpRequest
/*     */   extends HttpMsg
/*     */ {
/*     */   public String method;
/*     */   public String uriPath;
/*     */   public int nFields;
/*     */   public String[] keys;
/*     */   public int versionRange;
/*     */   public int uriFragmentRange;
/*     */   public int queryStringRange;
/*     */   public int[] valueRanges;
/*     */   public int contentOffset;
/*     */   public int contentLength;
/*     */   public int iread;
/*     */   
/*     */   public HttpRequest()
/*     */   {
/*  58 */     this.keys = new String[5];
/*  59 */     this.valueRanges = new int[5];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getHeader(String key)
/*     */   {
/*  68 */     for (int i = 0; i < this.nFields; i++) {
/*  69 */       if (key.equalsIgnoreCase(this.keys[i])) {
/*  70 */         return extractRange(this.valueRanges[i]);
/*     */       }
/*     */     }
/*  73 */     return "";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getQuery()
/*     */   {
/*  80 */     return extractRange(this.queryStringRange);
/*     */   }
/*     */   
/*     */   public String version() {
/*  84 */     return extractRange(this.versionRange);
/*     */   }
/*     */   
/*     */   public boolean keepAlive() {
/*  88 */     return !"close".equals(getHeader("Connection")) ? true : isOldHttp() ? "Keep-Alive".equals(getHeader("Connection;")) : false;
/*     */   }
/*     */   
/*     */   public KeyValues getQueryComponents() {
/*  92 */     String q = getQuery();
/*  93 */     int len = q.length();
/*  94 */     if ((q == null) || (len == 0)) {
/*  95 */       return new KeyValues(0);
/*     */     }
/*  97 */     int numPairs = 0;
/*  98 */     for (int i = 0; i < len; i++) {
/*  99 */       if (q.charAt(i) == '=')
/* 100 */         numPairs++;
/*     */     }
/* 102 */     KeyValues components = new KeyValues(numPairs);
/*     */     
/* 104 */     int beg = 0;
/* 105 */     String key = null;
/* 106 */     boolean url_encoded = false;
/* 107 */     for (int i = 0; i <= len; i++) {
/* 108 */       char c = i == len ? '&' : q.charAt(i);
/*     */       
/*     */ 
/*     */ 
/* 112 */       if ((c == '+') || (c == '%'))
/* 113 */         url_encoded = true;
/* 114 */       if ((c == '=') || (c == '&')) {
/* 115 */         String comp = q.substring(beg, i);
/* 116 */         if (url_encoded) {
/*     */           try {
/* 118 */             comp = URLDecoder.decode(comp, "UTF-8");
/*     */           }
/*     */           catch (UnsupportedEncodingException ignore) {}
/*     */         }
/* 122 */         if (key == null) {
/* 123 */           key = comp;
/*     */         } else {
/* 125 */           components.put(key, comp);
/* 126 */           key = null;
/*     */         }
/* 128 */         beg = i + 1;
/* 129 */         url_encoded = false;
/*     */       }
/*     */     }
/* 132 */     return components;
/*     */   }
/*     */   
/*     */   public String uriFragment() {
/* 136 */     return extractRange(this.uriFragmentRange);
/*     */   }
/*     */   
/*     */   public String toString() {
/* 140 */     StringBuilder sb = new StringBuilder(500);
/* 141 */     sb.append("method: ").append(this.method).append('\n').append("version: ").append(version()).append('\n').append("path = ").append(this.uriPath).append('\n').append("uri_fragment = ").append(uriFragment()).append('\n').append("query = ").append(getQueryComponents()).append('\n');
/*     */     
/*     */ 
/* 144 */     for (int i = 0; i < this.nFields; i++) {
/* 145 */       sb.append(this.keys[i]).append(": ").append(extractRange(this.valueRanges[i])).append('\n');
/*     */     }
/*     */     
/* 148 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isOldHttp()
/*     */   {
/* 155 */     byte b1 = 49;
/* 156 */     int offset = this.versionRange >> 16;
/* 157 */     return (this.buffer.get(offset) < 49) || (this.buffer.get(offset + 2) < 49);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void reuse()
/*     */   {
/* 164 */     this.method = null;
/* 165 */     this.uriPath = null;
/* 166 */     this.versionRange = 0;
/* 167 */     this.uriFragmentRange = (this.queryStringRange = 0);
/* 168 */     this.contentOffset = 0;
/* 169 */     this.contentLength = 0;
/*     */     
/* 171 */     if (this.buffer != null) {
/* 172 */       this.buffer.clear();
/*     */     }
/* 174 */     for (int i = 0; i < this.nFields; i++) {
/* 175 */       this.keys[i] = null;
/*     */     }
/* 177 */     this.nFields = 0;
/*     */   }
/*     */   
/*     */   public void readFrom(EndPoint paramEndPoint, Fiber paramFiber)
/*     */     throws Pausable, IOException
/*     */   {
/*     */     ;
/*     */     EndPoint endpoint;
/* 185 */     switch (paramFiber.pc) {default:  Fiber.wrongPC(); case 1:  break; case 2:  break; case 0:  this.iread = 0; }
/* 186 */     readHeader(endpoint, paramFiber.down()); Object localObject; switch (paramFiber.up()) {case 2:  localObject = new S_O();((State)localObject).self = this;((State)localObject).pc = 1;((S_O)localObject).f0 = endpoint;paramFiber.setState((State)localObject);return; case 3:  return; case 1:  localObject = (S_O)paramFiber.curState;endpoint = (EndPoint)((S_O)localObject).f0; }
/* 187 */     readBody(endpoint, paramFiber.down()); switch (paramFiber.up()) {case 2:  localObject = new State();((State)localObject).self = this;((State)localObject).pc = 2;paramFiber.setState((State)localObject);return; case 3:   } }
/*     */   
/*     */   public void readFrom(EndPoint paramEndPoint) throws Pausable, IOException
/*     */   {}
/* 191 */   public void readHeader(EndPoint arg1, Fiber localObject1) throws Pausable, IOException { ; Object localObject2; int headerLength; switch ((localObject2 = localObject1).pc) {default:  Fiber.wrongPC(); case 1:  int i = 0; break; case 0:  this.buffer = ByteBuffer.allocate(1024);
/* 192 */       headerLength = 0; }
/*     */     int n;
/*     */     do { S_OI localS_OI;
/* 195 */       switch (((Fiber)localObject2).up()) {case 2:  readLine(endpoint, ((Fiber)localObject2).down());localS_OI = new S_OI();localS_OI.self = this;localS_OI.pc = 1;localS_OI.f0 = endpoint;localS_OI.f1 = headerLength;((Fiber)localObject2).setState(localS_OI);return; case 3:  null;return; case 1:  localS_OI = (S_OI)((Fiber)localObject2).curState;endpoint = (EndPoint)localS_OI.f0;headerLength = localS_OI.f1; } n = ???;
/* 196 */       headerLength += n;
/* 197 */     } while (n > 2);
/*     */     
/* 199 */     HttpRequestParser.initHeader(this, headerLength);
/* 200 */     this.contentOffset = headerLength;
/* 201 */     String cl = getHeader("Content-Length");
/* 202 */     if (cl.length() > 0) {
/*     */       try {
/* 204 */         this.contentLength = Integer.parseInt(cl);
/*     */       } catch (NumberFormatException nfe) {
/* 206 */         throw new IOException("Malformed Content-Length hdr");
/*     */       }
/* 208 */     } else if ((getHeader("Transfer-Encoding").indexOf("chunked") >= 0) || (getHeader("TE").indexOf("chunked") >= 0))
/*     */     {
/* 210 */       this.contentLength = -1;
/*     */     } else
/* 212 */       this.contentLength = 0; }
/*     */   
/*     */   public void readHeader(EndPoint paramEndPoint) throws Pausable, IOException
/*     */   {}
/*     */   
/* 217 */   public void dumpBuffer(ByteBuffer buffer) { byte[] ba = buffer.array();
/* 218 */     int len = buffer.position();
/* 219 */     for (int i = 0; i < len; i++) {
/* 220 */       System.out.print((char)ba[i]);
/*     */     }
/*     */   }
/*     */   
/*     */   public void addField(String key, int valRange) {
/* 225 */     if (this.keys.length == this.nFields) {
/* 226 */       this.keys = ((String[])Utils.growArray(this.keys, 5));
/* 227 */       this.valueRanges = Utils.growArray(this.valueRanges, 5);
/*     */     }
/* 229 */     this.keys[this.nFields] = key;
/* 230 */     this.valueRanges[this.nFields] = valRange;
/* 231 */     this.nFields += 1;
/*     */   }
/*     */   
/*     */ 
/*     */   public String extractRange(int range)
/*     */   {
/* 237 */     int beg = range >> 16;
/* 238 */     int end = range & 0xFFFF;
/* 239 */     return extractRange(beg, end);
/*     */   }
/*     */   
/*     */   public String extractRange(int beg, int end) {
/* 243 */     return new String(this.buffer.array(), beg, end - beg);
/*     */   }
/*     */   
/*     */   public void readBody(EndPoint paramEndPoint, Fiber paramFiber)
/*     */     throws Pausable, IOException
/*     */   {
/*     */     ;
/*     */     EndPoint endpoint;
/* 251 */     switch (paramFiber.pc) {default:  Fiber.wrongPC(); case 1:  break; case 2:  break; case 0:  this.iread = this.contentOffset;
/*     */     }
/* 253 */     fill(endpoint, this.contentOffset, this.contentLength, paramFiber.down()); S_O localS_O; switch (paramFiber.up()) {case 2:  localS_O = new S_O();localS_O.self = this;localS_O.pc = 1;localS_O.f0 = endpoint;paramFiber.setState(localS_O);return; case 3:  return; case 1:  localS_O = (S_O)paramFiber.curState;endpoint = (EndPoint)localS_O.f0; }
/* 254 */     this.iread = (this.contentOffset + this.contentLength);
/* 255 */     if (this.contentLength == -1)
/*     */     {
/* 257 */       readAllChunks(endpoint, paramFiber.down()); switch (paramFiber.up()) {case 2:  localS_O = new S_O();localS_O.self = this;localS_O.pc = 2;localS_O.f0 = endpoint;paramFiber.setState(localS_O);return; case 3:  return; case 1:  localS_O = (S_O)paramFiber.curState;endpoint = this.contentLength > 0 ? null : (EndPoint)localS_O.f0; }
/*     */     }
/* 259 */     readTrailers(endpoint);
/*     */   }
/*     */   
/*     */   public void readBody(EndPoint paramEndPoint) throws Pausable, IOException
/*     */   {}
/*     */   
/*     */   public void readTrailers(EndPoint endpoint) {}
/*     */   
/*     */   public void readAllChunks(EndPoint arg1, Fiber localObject1) throws IOException, Pausable
/*     */   {
/*     */     ;
/*     */     Object localObject2;
/* 271 */     switch ((localObject2 = localObject1).pc) {default:  Fiber.wrongPC(); case 1:  localObject1 = null; break; case 2:  localObject1 = null;int i = 0;int j = 0;int k = 0; break; } for (IntList chunkRanges = new IntList();; tmpTernaryOp = ???) {
/*     */       Object localObject3;
/* 273 */       switch (((Fiber)localObject2).up()) {case 2:  readLine(endpoint, ((Fiber)localObject2).down());localObject3 = new S_O2();((State)localObject3).self = this;((State)localObject3).pc = 1;((S_O2)localObject3).f0 = endpoint;((S_O2)localObject3).f1 = chunkRanges;((Fiber)localObject2).setState((State)localObject3);return; case 3:  0;return; case 1:  localObject3 = (S_O2)((Fiber)localObject2).curState;endpoint = (EndPoint)((S_O2)localObject3).f0;chunkRanges = (IntList)((S_O2)localObject3).f1; } int n = 0;
/* 274 */       int beg = this.iread;
/* 275 */       int size = parseChunkSize(this.buffer, this.iread - n, this.iread);
/*     */       
/*     */ 
/*     */ 
/* 279 */       fill(endpoint, this.iread, size + 2, ((Fiber)localObject2).down()); switch (((Fiber)localObject2).up()) {case 2:  localObject3 = new S_O2I2();((State)localObject3).self = this;((State)localObject3).pc = 2;((S_O2I2)localObject3).f0 = endpoint;((S_O2I2)localObject3).f1 = chunkRanges;((S_O2I2)localObject3).f2 = beg;((S_O2I2)localObject3).f3 = size;((Fiber)localObject2).setState((State)localObject3);return; case 3:  return; case 1:  localObject3 = (S_O2I2)((Fiber)localObject2).curState;endpoint = (EndPoint)((S_O2I2)localObject3).f0;chunkRanges = (IntList)((S_O2I2)localObject3).f1;beg = ((S_O2I2)localObject3).f2;size = ((S_O2I2)localObject3).f3;
/*     */       }
/* 281 */       chunkRanges.add(beg);
/* 282 */       chunkRanges.add(beg + size);
/* 283 */       this.iread = (size == 0 ? null : this.iread + (size + 2));
/*     */     }
/*     */     
/*     */ 
/* 287 */     if (chunkRanges.numElements == 0) {
/* 288 */       this.contentLength = 0;
/* 289 */       return;
/*     */     }
/* 291 */     this.contentOffset = chunkRanges.get(0);
/* 292 */     int endOfLastChunk = chunkRanges.get(1);
/*     */     
/* 294 */     byte[] bufa = this.buffer.array();
/* 295 */     int i = 2;
/* 296 */     int beg = chunkRanges.get(i);
/* 297 */     int chunkSize = chunkRanges.get(i + 1) - beg;
/* 298 */     System.arraycopy(bufa, beg, bufa, endOfLastChunk, chunkSize);
/* 299 */     endOfLastChunk += chunkSize;i += 2;
/*     */     
/*     */ 
/* 302 */     this.contentLength = (endOfLastChunk - this.contentOffset);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 308 */   public static byte CR = 13;
/* 309 */   public static byte LF = 10;
/*     */   static final byte b0 = 48;
/*     */   static final byte b9 = 57;
/*     */   
/*     */   public void readAllChunks(EndPoint paramEndPoint) throws IOException, Pausable
/*     */   {}
/*     */   
/* 316 */   public static int parseChunkSize(ByteBuffer buffer, int start, int end) throws IOException { byte[] bufa = buffer.array();
/* 317 */     int size = 0;
/* 318 */     for (int i = start; i < end; i++) {
/* 319 */       byte b = bufa[i];
/* 320 */       if ((b >= 48) && (b <= 57)) {
/* 321 */         size = size * 16 + (b - 48);
/* 322 */       } else if ((b >= 97) && (b <= 102)) {
/* 323 */         size = size * 16 + (b - 97 + 10);
/* 324 */       } else if ((b >= 65) && (b <= 70)) {
/* 325 */         size = size * 16 + (b - 65 + 10);
/* 326 */       } else { if ((b == CR) || (b == 59)) {
/*     */           break;
/*     */         }
/*     */         
/* 330 */         throw new IOException("Error parsing chunk size; unexpected char " + b + " at offset " + i);
/*     */       }
/*     */     }
/* 333 */     return size; }
/*     */   
/*     */   static final byte ba = 97;
/*     */   
/*     */   public void fill(EndPoint arg1, int arg2, int arg3, Fiber ???) throws IOException, Pausable { Object localObject1;
/* 338 */     int total; int currentPos; switch ((localObject1 = ???).pc) {default:  Fiber.wrongPC(); case 1:  int i = 0;int j = 0; break; case 0:  total = offset + size;
/* 339 */       currentPos = this.buffer.position();
/* 340 */       if (total <= this.buffer.position()) return;
/*     */       break; } Object localObject2; Object localObject3; switch (((Fiber)localObject1).up()) {case 2:  endpoint.fill(this.buffer, total - currentPos, ((Fiber)localObject1).down());localObject2 = new S_O();((State)localObject2).self = this;((State)localObject2).pc = 1;localObject3 = this;((S_O)localObject2).f0 = localObject3;((Fiber)localObject1).setState((State)localObject2);return; case 3:  0;null;return; case 1:  localObject2 = (EndPoint)((Fiber)localObject1).getCallee();null;localObject3 = (S_O)((Fiber)localObject1).curState; } ((HttpRequest)((S_O)localObject3).f0).buffer = ((ByteBuffer)localObject2);
/*     */   }
/*     */   
/*     */   static final byte bf = 102;
/*     */   static final byte bA = 65;
/*     */   static final byte bF = 70;
/*     */   static final byte SEMI = 59;
/*     */   public static final boolean $isWoven = true;
/*     */   public void fill(EndPoint paramEndPoint, int paramInt1, int paramInt2)
/*     */     throws IOException, Pausable
/*     */   {}
/*     */   
/*     */   /* Error */
/*     */   public int readLine(EndPoint arg1, Fiber arg2)
/*     */     throws IOException, Pausable
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_2
/*     */     //   1: dup
/*     */     //   2: astore 7
/*     */     //   4: getfield 216	kilim/Fiber:pc	I
/*     */     //   7: tableswitch	default:+25->32, 0:+76->83, 1:+28->35, 2:+52->59
/*     */     //   32: invokestatic 219	kilim/Fiber:wrongPC	()V
/*     */     //   35: iconst_0
/*     */     //   36: istore_2
/*     */     //   37: iconst_0
/*     */     //   38: istore_3
/*     */     //   39: iconst_0
/*     */     //   40: istore 4
/*     */     //   42: aconst_null
/*     */     //   43: astore 5
/*     */     //   45: aconst_null
/*     */     //   46: aload 7
/*     */     //   48: invokevirtual 428	kilim/Fiber:getCallee	()Ljava/lang/Object;
/*     */     //   51: checkcast 254	kilim/nio/EndPoint
/*     */     //   54: aconst_null
/*     */     //   55: iconst_0
/*     */     //   56: goto +84 -> 140
/*     */     //   59: iconst_0
/*     */     //   60: istore_2
/*     */     //   61: iconst_0
/*     */     //   62: istore_3
/*     */     //   63: iconst_0
/*     */     //   64: istore 4
/*     */     //   66: aconst_null
/*     */     //   67: astore 5
/*     */     //   69: aconst_null
/*     */     //   70: aload 7
/*     */     //   72: invokevirtual 428	kilim/Fiber:getCallee	()Ljava/lang/Object;
/*     */     //   75: checkcast 254	kilim/nio/EndPoint
/*     */     //   78: aconst_null
/*     */     //   79: iconst_0
/*     */     //   80: goto +283 -> 363
/*     */     //   83: aload_0
/*     */     //   84: getfield 221	kilim/http/HttpRequest:iread	I
/*     */     //   87: istore_2
/*     */     //   88: iload_2
/*     */     //   89: istore_3
/*     */     //   90: aload_0
/*     */     //   91: getfield 188	kilim/http/HttpRequest:buffer	Ljava/nio/ByteBuffer;
/*     */     //   94: invokevirtual 325	java/nio/ByteBuffer:position	()I
/*     */     //   97: istore 4
/*     */     //   99: aload_0
/*     */     //   100: getfield 188	kilim/http/HttpRequest:buffer	Ljava/nio/ByteBuffer;
/*     */     //   103: invokevirtual 322	java/nio/ByteBuffer:array	()[B
/*     */     //   106: astore 5
/*     */     //   108: iload_3
/*     */     //   109: iload 4
/*     */     //   111: if_icmpge +245 -> 356
/*     */     //   114: aload 5
/*     */     //   116: iload_3
/*     */     //   117: baload
/*     */     //   118: getstatic 414	kilim/http/HttpRequest:CR	B
/*     */     //   121: if_icmpne +229 -> 350
/*     */     //   124: iinc 3 1
/*     */     //   127: iload_3
/*     */     //   128: iload 4
/*     */     //   130: if_icmplt +166 -> 296
/*     */     //   133: aload_0
/*     */     //   134: aload_1
/*     */     //   135: aload_0
/*     */     //   136: getfield 188	kilim/http/HttpRequest:buffer	Ljava/nio/ByteBuffer;
/*     */     //   139: iconst_1
/*     */     //   140: aload 7
/*     */     //   142: invokevirtual 225	kilim/Fiber:down	()Lkilim/Fiber;
/*     */     //   145: invokevirtual 431	kilim/nio/EndPoint:fill	(Ljava/nio/ByteBuffer;ILkilim/Fiber;)Ljava/nio/ByteBuffer;
/*     */     //   148: aload 7
/*     */     //   150: invokevirtual 231	kilim/Fiber:up	()I
/*     */     //   153: tableswitch	default:+122->275, 0:+122->275, 1:+87->240, 2:+31->184, 3:+83->236
/*     */     //   184: pop
/*     */     //   185: new 436	kilim/S_OI2
/*     */     //   188: dup
/*     */     //   189: invokespecial 437	kilim/S_OI2:<init>	()V
/*     */     //   192: astore 8
/*     */     //   194: aload 8
/*     */     //   196: aload_0
/*     */     //   197: putfield 240	kilim/State:self	Ljava/lang/Object;
/*     */     //   200: aload 8
/*     */     //   202: iconst_1
/*     */     //   203: putfield 241	kilim/State:pc	I
/*     */     //   206: astore 9
/*     */     //   208: aload 8
/*     */     //   210: aload 9
/*     */     //   212: putfield 438	kilim/S_OI2:f0	Ljava/lang/Object;
/*     */     //   215: aload 8
/*     */     //   217: iload_2
/*     */     //   218: putfield 439	kilim/S_OI2:f1	I
/*     */     //   221: aload 8
/*     */     //   223: iload_3
/*     */     //   224: putfield 440	kilim/S_OI2:f2	I
/*     */     //   227: aload 7
/*     */     //   229: aload 8
/*     */     //   231: invokevirtual 248	kilim/Fiber:setState	(Lkilim/State;)V
/*     */     //   234: iconst_0
/*     */     //   235: ireturn
/*     */     //   236: pop
/*     */     //   237: pop
/*     */     //   238: iconst_0
/*     */     //   239: ireturn
/*     */     //   240: astore 8
/*     */     //   242: pop
/*     */     //   243: aload 7
/*     */     //   245: getfield 252	kilim/Fiber:curState	Lkilim/State;
/*     */     //   248: checkcast 436	kilim/S_OI2
/*     */     //   251: astore 9
/*     */     //   253: aload 9
/*     */     //   255: getfield 439	kilim/S_OI2:f1	I
/*     */     //   258: istore_2
/*     */     //   259: aload 9
/*     */     //   261: getfield 440	kilim/S_OI2:f2	I
/*     */     //   264: istore_3
/*     */     //   265: aload 9
/*     */     //   267: getfield 438	kilim/S_OI2:f0	Ljava/lang/Object;
/*     */     //   270: checkcast 2	kilim/http/HttpRequest
/*     */     //   273: aload 8
/*     */     //   275: putfield 188	kilim/http/HttpRequest:buffer	Ljava/nio/ByteBuffer;
/*     */     //   278: aload_0
/*     */     //   279: getfield 188	kilim/http/HttpRequest:buffer	Ljava/nio/ByteBuffer;
/*     */     //   282: invokevirtual 322	java/nio/ByteBuffer:array	()[B
/*     */     //   285: astore 5
/*     */     //   287: aload_0
/*     */     //   288: getfield 188	kilim/http/HttpRequest:buffer	Ljava/nio/ByteBuffer;
/*     */     //   291: invokevirtual 325	java/nio/ByteBuffer:position	()I
/*     */     //   294: istore 4
/*     */     //   296: aload 5
/*     */     //   298: iload_3
/*     */     //   299: baload
/*     */     //   300: getstatic 442	kilim/http/HttpRequest:LF	B
/*     */     //   303: if_icmpeq +31 -> 334
/*     */     //   306: new 211	java/io/IOException
/*     */     //   309: dup
/*     */     //   310: new 147	java/lang/StringBuilder
/*     */     //   313: dup
/*     */     //   314: invokespecial 415	java/lang/StringBuilder:<init>	()V
/*     */     //   317: ldc_w 444
/*     */     //   320: invokevirtual 154	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   323: iload_3
/*     */     //   324: invokevirtual 420	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
/*     */     //   327: invokevirtual 182	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   330: invokespecial 302	java/io/IOException:<init>	(Ljava/lang/String;)V
/*     */     //   333: athrow
/*     */     //   334: iinc 3 1
/*     */     //   337: iload_3
/*     */     //   338: iload_2
/*     */     //   339: isub
/*     */     //   340: istore 6
/*     */     //   342: aload_0
/*     */     //   343: iload_3
/*     */     //   344: putfield 221	kilim/http/HttpRequest:iread	I
/*     */     //   347: iload 6
/*     */     //   349: ireturn
/*     */     //   350: iinc 3 1
/*     */     //   353: goto -245 -> 108
/*     */     //   356: aload_0
/*     */     //   357: aload_1
/*     */     //   358: aload_0
/*     */     //   359: getfield 188	kilim/http/HttpRequest:buffer	Ljava/nio/ByteBuffer;
/*     */     //   362: iconst_1
/*     */     //   363: aload 7
/*     */     //   365: invokevirtual 225	kilim/Fiber:down	()Lkilim/Fiber;
/*     */     //   368: invokevirtual 431	kilim/nio/EndPoint:fill	(Ljava/nio/ByteBuffer;ILkilim/Fiber;)Ljava/nio/ByteBuffer;
/*     */     //   371: aload 7
/*     */     //   373: invokevirtual 231	kilim/Fiber:up	()I
/*     */     //   376: tableswitch	default:+138->514, 0:+138->514, 1:+94->470, 2:+32->408, 3:+90->466
/*     */     //   408: pop
/*     */     //   409: new 384	kilim/S_O2I2
/*     */     //   412: dup
/*     */     //   413: invokespecial 385	kilim/S_O2I2:<init>	()V
/*     */     //   416: astore 8
/*     */     //   418: aload 8
/*     */     //   420: aload_0
/*     */     //   421: putfield 240	kilim/State:self	Ljava/lang/Object;
/*     */     //   424: aload 8
/*     */     //   426: iconst_2
/*     */     //   427: putfield 241	kilim/State:pc	I
/*     */     //   430: astore 9
/*     */     //   432: aload 8
/*     */     //   434: aload 9
/*     */     //   436: putfield 386	kilim/S_O2I2:f0	Ljava/lang/Object;
/*     */     //   439: aload 8
/*     */     //   441: aload_1
/*     */     //   442: putfield 387	kilim/S_O2I2:f1	Ljava/lang/Object;
/*     */     //   445: aload 8
/*     */     //   447: iload_2
/*     */     //   448: putfield 390	kilim/S_O2I2:f2	I
/*     */     //   451: aload 8
/*     */     //   453: iload_3
/*     */     //   454: putfield 393	kilim/S_O2I2:f3	I
/*     */     //   457: aload 7
/*     */     //   459: aload 8
/*     */     //   461: invokevirtual 248	kilim/Fiber:setState	(Lkilim/State;)V
/*     */     //   464: iconst_0
/*     */     //   465: ireturn
/*     */     //   466: pop
/*     */     //   467: pop
/*     */     //   468: iconst_0
/*     */     //   469: ireturn
/*     */     //   470: astore 8
/*     */     //   472: pop
/*     */     //   473: aload 7
/*     */     //   475: getfield 252	kilim/Fiber:curState	Lkilim/State;
/*     */     //   478: checkcast 384	kilim/S_O2I2
/*     */     //   481: astore 9
/*     */     //   483: aload 9
/*     */     //   485: getfield 387	kilim/S_O2I2:f1	Ljava/lang/Object;
/*     */     //   488: checkcast 254	kilim/nio/EndPoint
/*     */     //   491: astore_1
/*     */     //   492: aload 9
/*     */     //   494: getfield 390	kilim/S_O2I2:f2	I
/*     */     //   497: istore_2
/*     */     //   498: aload 9
/*     */     //   500: getfield 393	kilim/S_O2I2:f3	I
/*     */     //   503: istore_3
/*     */     //   504: aload 9
/*     */     //   506: getfield 386	kilim/S_O2I2:f0	Ljava/lang/Object;
/*     */     //   509: checkcast 2	kilim/http/HttpRequest
/*     */     //   512: aload 8
/*     */     //   514: putfield 188	kilim/http/HttpRequest:buffer	Ljava/nio/ByteBuffer;
/*     */     //   517: goto -427 -> 90
/*     */     // Line number table:
/*     */     //   Java source line #346	-> byte code offset #83
/*     */     //   Java source line #347	-> byte code offset #88
/*     */     //   Java source line #349	-> byte code offset #90
/*     */     //   Java source line #350	-> byte code offset #99
/*     */     //   Java source line #351	-> byte code offset #108
/*     */     //   Java source line #352	-> byte code offset #114
/*     */     //   Java source line #353	-> byte code offset #124
/*     */     //   Java source line #354	-> byte code offset #127
/*     */     //   Java source line #355	-> byte code offset #133
/*     */     //   Java source line #356	-> byte code offset #278
/*     */     //   Java source line #357	-> byte code offset #287
/*     */     //   Java source line #359	-> byte code offset #296
/*     */     //   Java source line #360	-> byte code offset #306
/*     */     //   Java source line #362	-> byte code offset #334
/*     */     //   Java source line #363	-> byte code offset #337
/*     */     //   Java source line #364	-> byte code offset #342
/*     */     //   Java source line #365	-> byte code offset #347
/*     */     //   Java source line #351	-> byte code offset #350
/*     */     //   Java source line #368	-> byte code offset #356
/*     */     //   Java source line #369	-> byte code offset #517
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   83	437	0	this	HttpRequest
/*     */     //   83	437	1	endpoint	EndPoint
/*     */     //   88	432	2	ireadSave	int
/*     */     //   90	430	3	i	int
/*     */     //   99	418	4	end	int
/*     */     //   108	409	5	bufa	byte[]
/*     */     //   342	8	6	lineLength	int
/*     */   }
/*     */   
/*     */   public int readLine(EndPoint paramEndPoint)
/*     */     throws IOException, Pausable
/*     */   {
/*     */     Task.errNotWoven();
/*     */     return 0;
/*     */   }
/*     */ }


/* Location:              /home/cfeindia/Desktop/kilim.jar!/kilim/http/HttpRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */