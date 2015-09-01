/*     */ package kilim.nio;
/*     */ 
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.FileChannel;
/*     */ import java.nio.channels.SocketChannel;
/*     */ import java.nio.channels.spi.AbstractSelectableChannel;
/*     */ import kilim.Fiber;
/*     */ import kilim.Mailbox;
/*     */ import kilim.Pausable;
/*     */ import kilim.S_I3;
/*     */ import kilim.S_O2I;
/*     */ import kilim.S_O2I2;
/*     */ import kilim.S_O2I3;
/*     */ import kilim.S_O2IL2;
/*     */ import kilim.S_O2L2;
/*     */ import kilim.State;
/*     */ import kilim.Task;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EndPoint
/*     */   extends Mailbox<SockEvent>
/*     */ {
/*  39 */   static final int YIELD_COUNT = Integer.parseInt(System.getProperty("kilim.nio.yieldCount", "4"));
/*     */   
/*     */ 
/*     */   public AbstractSelectableChannel sockch;
/*     */   
/*     */ 
/*     */   public Mailbox<SockEvent> sockEvMbx;
/*     */   
/*     */   public static final boolean $isWoven = true;
/*     */   
/*     */ 
/*     */   public EndPoint()
/*     */   {
/*  52 */     super(2, 2);
/*     */   }
/*     */   
/*     */   public EndPoint(Mailbox<SockEvent> mbx, AbstractSelectableChannel ch)
/*     */   {
/*  57 */     this.sockch = ch;
/*  58 */     this.sockEvMbx = mbx;
/*     */   }
/*     */   
/*     */ 
/*  62 */   public SocketChannel dataChannel() { return (SocketChannel)this.sockch; }
/*     */   
/*     */   public void write(ByteBuffer arg1, Fiber localObject1) throws IOException, Pausable { ;
/*     */     Object localObject2;
/*     */     int i;
/*     */     int j;
/*     */     int k;
/*  69 */     SocketChannel ch; int remaining; int n; int yieldCount; switch ((localObject2 = localObject1).pc) {default:  Fiber.wrongPC(); case 1:  localObject1 = null;i = 0;j = 0;k = 0; break; case 2:  localObject1 = null;i = 0;j = 0;k = 0; break; case 0:  ch = dataChannel();
/*  70 */       remaining = buf.remaining();
/*  71 */       if (remaining == 0)
/*  72 */         return;
/*  73 */       n = ch.write(buf);
/*  74 */       remaining -= n;
/*  75 */       yieldCount = 0; }
/*  76 */     while (remaining > 0) {
/*  77 */       if (n == 0)
/*     */       {
/*     */ 
/*  80 */         Task.yield(((Fiber)localObject2).down()); Object localObject3; switch (((Fiber)localObject2).up()) {case 2:  localObject3 = new S_O2I2();((State)localObject3).self = this;((State)localObject3).pc = 1;((S_O2I2)localObject3).f0 = buf;((S_O2I2)localObject3).f1 = ch;((S_O2I2)localObject3).f2 = remaining;((S_O2I2)localObject3).f3 = yieldCount;((Fiber)localObject2).setState((State)localObject3);return; case 3:  return; case 1:  localObject3 = (S_O2I2)((Fiber)localObject2).curState;buf = (ByteBuffer)((S_O2I2)localObject3).f0;ch = (SocketChannel)((S_O2I2)localObject3).f1;remaining = ((S_O2I2)localObject3).f2;yieldCount = ((S_O2I2)localObject3).f3;
/*     */         }
/*  82 */         pauseUntilWritable(((Fiber)localObject2).down()); switch (((Fiber)localObject2).up()) {case 2:  localObject3 = new S_O2I();((State)localObject3).self = this;((State)localObject3).pc = 2;((S_O2I)localObject3).f0 = buf;((S_O2I)localObject3).f1 = ch;((S_O2I)localObject3).f2 = remaining;((Fiber)localObject2).setState((State)localObject3);return; case 3:  return; case 1:  localObject3 = (S_O2I)((Fiber)localObject2).curState;buf = (ByteBuffer)((S_O2I)localObject3).f0;ch = (SocketChannel)((S_O2I)localObject3).f1;remaining = ((S_O2I)localObject3).f2; }
/*  83 */         yieldCount = ++yieldCount < YIELD_COUNT ? ??? : 0;
/*     */       }
/*     */       
/*  86 */       n = ch.write(buf);
/*  87 */       remaining -= n;
/*     */     }
/*     */   }
/*     */   
/*     */   public void write(ByteBuffer paramByteBuffer)
/*     */     throws IOException, Pausable
/*     */   {}
/*     */   
/*     */   public ByteBuffer fill(ByteBuffer arg1, int arg2, Fiber localObject1)
/*     */     throws IOException, Pausable
/*     */   {
/*     */     ;
/*     */     Object localObject2;
/*     */     int i;
/*     */     int j;
/*     */     SocketChannel ch;
/*     */     int yieldCount;
/* 104 */     switch ((localObject2 = localObject1).pc) {default:  Fiber.wrongPC(); case 1:  localObject1 = null;i = 0;j = 0; break; case 2:  localObject1 = null;i = 0;j = 0; break; case 0:  if (buf.remaining() < atleastN) {
/* 105 */         ByteBuffer newbb = ByteBuffer.allocate(Math.max(buf.capacity() * 3 / 2, buf.position() + atleastN));
/* 106 */         buf.rewind();
/* 107 */         newbb.put(buf);
/* 108 */         buf = newbb;
/*     */       }
/*     */       
/* 111 */       ch = dataChannel();
/* 112 */       if (!ch.isOpen()) {
/* 113 */         throw new EOFException();
/*     */       }
/* 115 */       yieldCount = 0; }
/*     */     do {
/* 117 */       int n = ch.read(buf);
/*     */       
/* 119 */       if (n == -1) {
/* 120 */         close();
/* 121 */         throw new EOFException();
/*     */       }
/* 123 */       if (n == 0)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 129 */         Task.yield(((Fiber)localObject2).down()); Object localObject3; switch (((Fiber)localObject2).up()) {case 2:  localObject3 = new S_O2I3();((State)localObject3).self = this;((State)localObject3).pc = 1;((S_O2I3)localObject3).f0 = buf;((S_O2I3)localObject3).f1 = ch;((S_O2I3)localObject3).f2 = atleastN;((S_O2I3)localObject3).f3 = yieldCount;((S_O2I3)localObject3).f4 = n;((Fiber)localObject2).setState((State)localObject3);return null; case 3:  return null; case 1:  localObject3 = (S_O2I3)((Fiber)localObject2).curState;buf = (ByteBuffer)((S_O2I3)localObject3).f0;atleastN = ((S_O2I3)localObject3).f2;ch = (SocketChannel)((S_O2I3)localObject3).f1;yieldCount = ((S_O2I3)localObject3).f3;n = ((S_O2I3)localObject3).f4;
/*     */         }
/* 131 */         pauseUntilReadble(((Fiber)localObject2).down()); switch (((Fiber)localObject2).up()) {case 2:  localObject3 = new S_O2I2();((State)localObject3).self = this;((State)localObject3).pc = 2;((S_O2I2)localObject3).f0 = buf;((S_O2I2)localObject3).f1 = ch;((S_O2I2)localObject3).f2 = atleastN;((S_O2I2)localObject3).f3 = n;((Fiber)localObject2).setState((State)localObject3);return null; case 3:  return null; case 1:  localObject3 = (S_O2I2)((Fiber)localObject2).curState;buf = (ByteBuffer)((S_O2I2)localObject3).f0;atleastN = ((S_O2I2)localObject3).f2;ch = (SocketChannel)((S_O2I2)localObject3).f1;n = ((S_O2I2)localObject3).f3; }
/* 132 */         yieldCount = ++yieldCount < YIELD_COUNT ? ??? : 0;
/*     */       }
/*     */       
/* 135 */       atleastN -= n;
/* 136 */     } while (atleastN > 0);
/* 137 */     return buf;
/*     */   }
/*     */   
/*     */   public ByteBuffer fill(ByteBuffer paramByteBuffer, int paramInt)
/*     */     throws IOException, Pausable
/*     */   {
/*     */     Task.errNotWoven();
/*     */     return null;
/*     */   }
/*     */   
/*     */   public ByteBuffer fillMessage(ByteBuffer arg1, int arg2, boolean arg3, Fiber localObject1)
/*     */     throws IOException, Pausable
/*     */   {
/*     */     ;
/*     */     Object localObject2;
/*     */     int i;
/*     */     int j;
/*     */     int pos;
/*     */     int opos;
/* 155 */     switch ((localObject2 = localObject1).pc) {default:  Fiber.wrongPC(); case 1:  i = 0;j = 0; break; case 2:  i = 0;j = 0;int k = 0;int m = 0;int n = 0;int i1 = 0;int i2 = 0;int i3 = 0; break; case 0:  pos = bb.position();
/* 156 */       opos = pos; }
/* 157 */     Object localObject3; switch (((Fiber)localObject2).up()) {case 2:  fill(bb, lengthLength, ((Fiber)localObject2).down());localObject3 = new S_I3();((State)localObject3).self = this;((State)localObject3).pc = 1;((S_I3)localObject3).f0 = lengthLength;((S_I3)localObject3).f1 = lengthIncludesItself;((S_I3)localObject3).f2 = pos;((Fiber)localObject2).setState((State)localObject3);return null; case 3:  0;return null; case 1:  localObject3 = (S_I3)((Fiber)localObject2).curState;lengthLength = ((S_I3)localObject3).f0;lengthIncludesItself = ((S_I3)localObject3).f1;pos = ((S_I3)localObject3).f2;opos = pos; } bb = null;
/*     */     byte d;
/* 159 */     byte c; byte b; byte a = b = c = d = 0;
/* 160 */     switch (lengthLength) {
/* 161 */     case 4:  a = bb.get(pos);
/* 162 */       b = bb.get(pos);
/* 163 */     case 2:  c = bb.get(pos);
/* 164 */     case 1:  d = bb.get(pos); break;
/* 165 */     case 3: default:  throw new IllegalArgumentException("Incorrect lengthLength (may only be 1, 2 or 4): " + lengthLength);
/*     */     }
/* 167 */     int contentLen = (;;
/*     */     
/* 169 */     if (lengthIncludesItself)
/*     */     {
/* 162 */       pos++;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 170 */       contentLen -= lengthLength;
/*     */     }
/*     */     
/* 173 */     int remaining = ;;
/* 174 */     if (remaining > 0) {
/* 175 */       switch (((Fiber)localObject2).up()) {case 2:  fill(bb, remaining, ((Fiber)localObject2).down());localObject3 = new State();((State)localObject3).self = this;((State)localObject3).pc = 2;((Fiber)localObject2).setState((State)localObject3);return null; case 3:  return null; } bb = 0;
/*     */     }
/* 177 */     return bb;
/*     */   }
/*     */   
/*     */   public ByteBuffer fillMessage(ByteBuffer paramByteBuffer, int paramInt, boolean paramBoolean)
/*     */     throws IOException, Pausable
/*     */   {
/*     */     Task.errNotWoven();
/*     */     return null;
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public void pauseUntilReadble(Fiber arg1)
/*     */     throws Pausable, IOException
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_1
/*     */     //   1: dup
/*     */     //   2: astore_2
/*     */     //   3: getfield 46	kilim/Fiber:pc	I
/*     */     //   6: tableswitch	default:+22->28, 0:+31->37, 1:+25->31
/*     */     //   28: invokestatic 49	kilim/Fiber:wrongPC	()V
/*     */     //   31: aconst_null
/*     */     //   32: astore_1
/*     */     //   33: aload_0
/*     */     //   34: goto +27 -> 61
/*     */     //   37: new 230	kilim/nio/SockEvent
/*     */     //   40: dup
/*     */     //   41: aload_0
/*     */     //   42: aload_0
/*     */     //   43: getfield 27	kilim/nio/EndPoint:sockch	Ljava/nio/channels/spi/AbstractSelectableChannel;
/*     */     //   46: iconst_1
/*     */     //   47: invokespecial 233	kilim/nio/SockEvent:<init>	(Lkilim/Mailbox;Ljava/nio/channels/spi/AbstractSelectableChannel;I)V
/*     */     //   50: astore_1
/*     */     //   51: aload_0
/*     */     //   52: getfield 29	kilim/nio/EndPoint:sockEvMbx	Lkilim/Mailbox;
/*     */     //   55: aload_1
/*     */     //   56: invokevirtual 237	kilim/Mailbox:putnb	(Ljava/lang/Object;)Z
/*     */     //   59: pop
/*     */     //   60: aload_0
/*     */     //   61: aload_2
/*     */     //   62: invokevirtual 66	kilim/Fiber:down	()Lkilim/Fiber;
/*     */     //   65: invokespecial 240	kilim/Mailbox:get	(Lkilim/Fiber;)Ljava/lang/Object;
/*     */     //   68: aload_2
/*     */     //   69: invokevirtual 75	kilim/Fiber:up	()I
/*     */     //   72: tableswitch	default:+59->131, 0:+59->131, 1:+59->131, 2:+32->104, 3:+57->129
/*     */     //   104: pop
/*     */     //   105: new 80	kilim/State
/*     */     //   108: dup
/*     */     //   109: invokespecial 216	kilim/State:<init>	()V
/*     */     //   112: astore_3
/*     */     //   113: aload_3
/*     */     //   114: aload_0
/*     */     //   115: putfield 84	kilim/State:self	Ljava/lang/Object;
/*     */     //   118: aload_3
/*     */     //   119: iconst_1
/*     */     //   120: putfield 85	kilim/State:pc	I
/*     */     //   123: aload_2
/*     */     //   124: aload_3
/*     */     //   125: invokevirtual 101	kilim/Fiber:setState	(Lkilim/State;)V
/*     */     //   128: return
/*     */     //   129: pop
/*     */     //   130: return
/*     */     //   131: pop
/*     */     //   132: return
/*     */     // Line number table:
/*     */     //   Java source line #181	-> byte code offset #37
/*     */     //   Java source line #182	-> byte code offset #51
/*     */     //   Java source line #184	-> byte code offset #60
/*     */     //   Java source line #185	-> byte code offset #132
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   37	96	0	this	EndPoint
/*     */     //   51	82	1	ev	SockEvent
/*     */   }
/*     */   
/*     */   public void pauseUntilReadble()
/*     */     throws Pausable, IOException
/*     */   {}
/*     */   
/*     */   /* Error */
/*     */   public void pauseUntilWritable(Fiber arg1)
/*     */     throws Pausable, IOException
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_1
/*     */     //   1: dup
/*     */     //   2: astore_2
/*     */     //   3: getfield 46	kilim/Fiber:pc	I
/*     */     //   6: tableswitch	default:+22->28, 0:+31->37, 1:+25->31
/*     */     //   28: invokestatic 49	kilim/Fiber:wrongPC	()V
/*     */     //   31: aconst_null
/*     */     //   32: astore_1
/*     */     //   33: aload_0
/*     */     //   34: goto +27 -> 61
/*     */     //   37: new 230	kilim/nio/SockEvent
/*     */     //   40: dup
/*     */     //   41: aload_0
/*     */     //   42: aload_0
/*     */     //   43: getfield 27	kilim/nio/EndPoint:sockch	Ljava/nio/channels/spi/AbstractSelectableChannel;
/*     */     //   46: iconst_4
/*     */     //   47: invokespecial 233	kilim/nio/SockEvent:<init>	(Lkilim/Mailbox;Ljava/nio/channels/spi/AbstractSelectableChannel;I)V
/*     */     //   50: astore_1
/*     */     //   51: aload_0
/*     */     //   52: getfield 29	kilim/nio/EndPoint:sockEvMbx	Lkilim/Mailbox;
/*     */     //   55: aload_1
/*     */     //   56: invokevirtual 237	kilim/Mailbox:putnb	(Ljava/lang/Object;)Z
/*     */     //   59: pop
/*     */     //   60: aload_0
/*     */     //   61: aload_2
/*     */     //   62: invokevirtual 66	kilim/Fiber:down	()Lkilim/Fiber;
/*     */     //   65: invokespecial 240	kilim/Mailbox:get	(Lkilim/Fiber;)Ljava/lang/Object;
/*     */     //   68: aload_2
/*     */     //   69: invokevirtual 75	kilim/Fiber:up	()I
/*     */     //   72: tableswitch	default:+59->131, 0:+59->131, 1:+59->131, 2:+32->104, 3:+57->129
/*     */     //   104: pop
/*     */     //   105: new 80	kilim/State
/*     */     //   108: dup
/*     */     //   109: invokespecial 216	kilim/State:<init>	()V
/*     */     //   112: astore_3
/*     */     //   113: aload_3
/*     */     //   114: aload_0
/*     */     //   115: putfield 84	kilim/State:self	Ljava/lang/Object;
/*     */     //   118: aload_3
/*     */     //   119: iconst_1
/*     */     //   120: putfield 85	kilim/State:pc	I
/*     */     //   123: aload_2
/*     */     //   124: aload_3
/*     */     //   125: invokevirtual 101	kilim/Fiber:setState	(Lkilim/State;)V
/*     */     //   128: return
/*     */     //   129: pop
/*     */     //   130: return
/*     */     //   131: pop
/*     */     //   132: return
/*     */     // Line number table:
/*     */     //   Java source line #188	-> byte code offset #37
/*     */     //   Java source line #189	-> byte code offset #51
/*     */     //   Java source line #191	-> byte code offset #60
/*     */     //   Java source line #192	-> byte code offset #132
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   37	96	0	this	EndPoint
/*     */     //   51	82	1	ev	SockEvent
/*     */   }
/*     */   
/*     */   public void pauseUntilWritable()
/*     */     throws Pausable, IOException
/*     */   {}
/*     */   
/*     */   /* Error */
/*     */   public void pauseUntilAcceptable(Fiber arg1)
/*     */     throws Pausable, IOException
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_1
/*     */     //   1: dup
/*     */     //   2: astore_2
/*     */     //   3: getfield 46	kilim/Fiber:pc	I
/*     */     //   6: tableswitch	default:+22->28, 0:+31->37, 1:+25->31
/*     */     //   28: invokestatic 49	kilim/Fiber:wrongPC	()V
/*     */     //   31: aconst_null
/*     */     //   32: astore_1
/*     */     //   33: aload_0
/*     */     //   34: goto +28 -> 62
/*     */     //   37: new 230	kilim/nio/SockEvent
/*     */     //   40: dup
/*     */     //   41: aload_0
/*     */     //   42: aload_0
/*     */     //   43: getfield 27	kilim/nio/EndPoint:sockch	Ljava/nio/channels/spi/AbstractSelectableChannel;
/*     */     //   46: bipush 16
/*     */     //   48: invokespecial 233	kilim/nio/SockEvent:<init>	(Lkilim/Mailbox;Ljava/nio/channels/spi/AbstractSelectableChannel;I)V
/*     */     //   51: astore_1
/*     */     //   52: aload_0
/*     */     //   53: getfield 29	kilim/nio/EndPoint:sockEvMbx	Lkilim/Mailbox;
/*     */     //   56: aload_1
/*     */     //   57: invokevirtual 237	kilim/Mailbox:putnb	(Ljava/lang/Object;)Z
/*     */     //   60: pop
/*     */     //   61: aload_0
/*     */     //   62: aload_2
/*     */     //   63: invokevirtual 66	kilim/Fiber:down	()Lkilim/Fiber;
/*     */     //   66: invokespecial 240	kilim/Mailbox:get	(Lkilim/Fiber;)Ljava/lang/Object;
/*     */     //   69: aload_2
/*     */     //   70: invokevirtual 75	kilim/Fiber:up	()I
/*     */     //   73: tableswitch	default:+58->131, 0:+58->131, 1:+58->131, 2:+31->104, 3:+56->129
/*     */     //   104: pop
/*     */     //   105: new 80	kilim/State
/*     */     //   108: dup
/*     */     //   109: invokespecial 216	kilim/State:<init>	()V
/*     */     //   112: astore_3
/*     */     //   113: aload_3
/*     */     //   114: aload_0
/*     */     //   115: putfield 84	kilim/State:self	Ljava/lang/Object;
/*     */     //   118: aload_3
/*     */     //   119: iconst_1
/*     */     //   120: putfield 85	kilim/State:pc	I
/*     */     //   123: aload_2
/*     */     //   124: aload_3
/*     */     //   125: invokevirtual 101	kilim/Fiber:setState	(Lkilim/State;)V
/*     */     //   128: return
/*     */     //   129: pop
/*     */     //   130: return
/*     */     //   131: pop
/*     */     //   132: return
/*     */     // Line number table:
/*     */     //   Java source line #195	-> byte code offset #37
/*     */     //   Java source line #196	-> byte code offset #52
/*     */     //   Java source line #197	-> byte code offset #61
/*     */     //   Java source line #198	-> byte code offset #132
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   37	96	0	this	EndPoint
/*     */     //   52	81	1	ev	SockEvent
/*     */   }
/*     */   
/*     */   public void pauseUntilAcceptable()
/*     */     throws Pausable, IOException
/*     */   {}
/*     */   
/*     */   public void write(FileChannel arg1, long arg2, long arg4, Fiber localObject1)
/*     */     throws IOException, Pausable
/*     */   {
/*     */     ;
/*     */     Object localObject2;
/*     */     long l1;
/*     */     long l2;
/*     */     int i;
/*     */     SocketChannel ch;
/*     */     long remaining;
/*     */     long n;
/*     */     int yieldCount;
/* 210 */     switch ((localObject2 = localObject1).pc) {default:  Fiber.wrongPC(); case 1:  localObject1 = null;l1 = 0L;l2 = 0L;i = 0; break; case 2:  localObject1 = null;l1 = 0L;l2 = 0L;i = 0; break; case 0:  ch = dataChannel();
/* 211 */       remaining = length - start;
/* 212 */       if (remaining == 0L) {
/* 213 */         return;
/*     */       }
/* 215 */       n = fc.transferTo(start, remaining, ch);
/* 216 */       start += n;
/* 217 */       remaining -= n;
/* 218 */       yieldCount = 0; }
/* 219 */     while (remaining > 0L) {
/* 220 */       if (n == 0L)
/*     */       {
/*     */ 
/* 223 */         Task.yield(((Fiber)localObject2).down()); Object localObject3; switch (((Fiber)localObject2).up()) {case 2:  localObject3 = new S_O2IL2();((State)localObject3).self = this;((State)localObject3).pc = 1;((S_O2IL2)localObject3).f0 = fc;((S_O2IL2)localObject3).f1 = ch;((S_O2IL2)localObject3).f2 = yieldCount;((S_O2IL2)localObject3).f3 = start;((S_O2IL2)localObject3).f4 = remaining;((Fiber)localObject2).setState((State)localObject3);return; case 3:  return; case 1:  localObject3 = (S_O2IL2)((Fiber)localObject2).curState;fc = (FileChannel)((S_O2IL2)localObject3).f0;start = ((S_O2IL2)localObject3).f3;ch = (SocketChannel)((S_O2IL2)localObject3).f1;remaining = ((S_O2IL2)localObject3).f4;yieldCount = ((S_O2IL2)localObject3).f2;
/*     */         }
/* 225 */         pauseUntilWritable(((Fiber)localObject2).down()); switch (((Fiber)localObject2).up()) {case 2:  localObject3 = new S_O2L2();((State)localObject3).self = this;((State)localObject3).pc = 2;((S_O2L2)localObject3).f0 = fc;((S_O2L2)localObject3).f1 = ch;((S_O2L2)localObject3).f2 = start;((S_O2L2)localObject3).f3 = remaining;((Fiber)localObject2).setState((State)localObject3);return; case 3:  return; case 1:  localObject3 = (S_O2L2)((Fiber)localObject2).curState;fc = (FileChannel)((S_O2L2)localObject3).f0;start = ((S_O2L2)localObject3).f2;ch = (SocketChannel)((S_O2L2)localObject3).f1;remaining = ((S_O2L2)localObject3).f3; }
/* 226 */         yieldCount = ++yieldCount < YIELD_COUNT ? ??? : 0;
/*     */       }
/*     */       
/* 229 */       n = fc.transferTo(start, remaining, ch);
/* 230 */       start += n;
/* 231 */       remaining -= n;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void write(FileChannel paramFileChannel, long paramLong1, long paramLong2)
/*     */     throws IOException, Pausable
/*     */   {}
/*     */   
/*     */ 
/*     */   public void close()
/*     */   {
/*     */     try
/*     */     {
/* 245 */       this.sockch.close();
/*     */     } catch (Exception ignore) {
/* 247 */       ignore.printStackTrace();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/cfeindia/Desktop/kilim.jar!/kilim/nio/EndPoint.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */