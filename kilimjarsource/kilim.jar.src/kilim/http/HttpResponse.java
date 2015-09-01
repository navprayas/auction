/*     */ package kilim.http;
/*     */ 
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.TimeZone;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import kilim.Fiber;
/*     */ import kilim.Pausable;
/*     */ import kilim.S_O;
/*     */ import kilim.State;
/*     */ import kilim.Task;
/*     */ import kilim.nio.EndPoint;
/*     */ import kilim.nio.ExposedBaos;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HttpResponse
/*     */   extends HttpMsg
/*     */ {
/*  31 */   public static final byte[] ST_CONTINUE = "100 Continue\r\n".getBytes();
/*  32 */   public static final byte[] ST_SWITCHING_PROTOCOLS = "101 Switching Protocols\r\n".getBytes();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  37 */   public static final byte[] ST_OK = "200 OK\r\n".getBytes();
/*  38 */   public static final byte[] ST_CREATED = "201 Created\r\n".getBytes();
/*  39 */   public static final byte[] ST_ACCEPTED = "202 Accepted\r\n".getBytes();
/*  40 */   public static final byte[] ST_NON_AUTHORITATIVE = "203 Non-Authoritative Information\r\n".getBytes();
/*     */   
/*  42 */   public static final byte[] ST_NO_CONTENT = "204 No Content\r\n".getBytes();
/*  43 */   public static final byte[] ST_RESET_CONTENT = "205 Reset Content\r\n".getBytes();
/*     */   
/*  45 */   public static final byte[] ST_PARTIAL_CONTENT = "206 Partial Content\r\n".getBytes();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  50 */   public static final byte[] ST_MULTIPLE_CHOICES = "300 Multiple Choices\r\n".getBytes();
/*     */   
/*  52 */   public static final byte[] ST_MOVED_PERMANENTLY = "301 Moved Permanently\r\n".getBytes();
/*     */   
/*  54 */   public static final byte[] ST_FOUND = "302 Found\r\n".getBytes();
/*  55 */   public static final byte[] ST_SEE_OTHER = "303 See Other\r\n".getBytes();
/*  56 */   public static final byte[] ST_NOT_MODIFIED = "304 Not Modified\r\n".getBytes();
/*     */   
/*  58 */   public static final byte[] ST_USE_PROXY = "305 Use Proxy\r\n".getBytes();
/*  59 */   public static final byte[] ST_TEMPORARY_REDIRECT = "307 Temporary Redirect\r\n".getBytes();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  64 */   public static final byte[] ST_BAD_REQUEST = "400 Bad Request\r\n".getBytes();
/*  65 */   public static final byte[] ST_UNAUTHORIZED = "401 Unauthorized\r\n".getBytes();
/*     */   
/*  67 */   public static final byte[] ST_PAYMENT_REQUIRED = "402 Payment Required\r\n".getBytes();
/*     */   
/*  69 */   public static final byte[] ST_FORBIDDEN = "403 Forbidden\r\n".getBytes();
/*  70 */   public static final byte[] ST_NOT_FOUND = "404 Not Found\r\n".getBytes();
/*  71 */   public static final byte[] ST_METHOD_NOT_ALLOWED = "405 Method Not Allowed\r\n".getBytes();
/*     */   
/*  73 */   public static final byte[] ST_NOT_ACCEPTABLE = "406 Not Acceptable\r\n".getBytes();
/*     */   
/*  75 */   public static final byte[] ST_PROXY_AUTHENTICATION_REQUIRED = "407 Proxy Authentication Required\r\n".getBytes();
/*     */   
/*  77 */   public static final byte[] ST_REQUEST_TIMEOUT = "408 Request Time-out\r\n".getBytes();
/*     */   
/*  79 */   public static final byte[] ST_CONFLICT = "409 Conflict\r\n".getBytes();
/*  80 */   public static final byte[] ST_GONE = "410 Gone\r\n".getBytes();
/*  81 */   public static final byte[] ST_LENGTH_REQUIRED = "411 Length Required\r\n".getBytes();
/*     */   
/*  83 */   public static final byte[] ST_PRECONDITION_FAILED = "412 Precondition Failed\r\n".getBytes();
/*     */   
/*  85 */   public static final byte[] ST_REQUEST_ENTITY_TOO_LARGE = "413 Request Entity Too Large\r\n".getBytes();
/*     */   
/*  87 */   public static final byte[] ST_REQUEST_URI_TOO_LONG = "414 Request-URI Too Large\r\n".getBytes();
/*     */   
/*  89 */   public static final byte[] ST_UNSUPPORTED_MEDIA_TYPE = "415 Unsupported Media Type\r\n".getBytes();
/*     */   
/*  91 */   public static final byte[] ST_REQUEST_RANGE_NOT_SATISFIABLE = "416 Requested range not satisfiable\r\n".getBytes();
/*     */   
/*  93 */   public static final byte[] ST_EXPECTATION_FAILED = "417 Expectation Failed\r\n".getBytes();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  98 */   public static final byte[] ST_INTERNAL_SERVER_ERROR = "500 Internal Server Error\r\n".getBytes();
/*     */   
/* 100 */   public static final byte[] ST_NOT_IMPLEMENTED = "501 Not Implemented\r\n".getBytes();
/*     */   
/* 102 */   public static final byte[] ST_BAD_GATEWAY = "502 Bad Gateway\r\n".getBytes();
/* 103 */   public static final byte[] ST_SERVICE_UNAVAILABLE = "503 Service Unavailable\r\n".getBytes();
/*     */   
/* 105 */   public static final byte[] ST_GATEWAY_TIMEOUT = "504 Gateway Time-out\r\n".getBytes();
/*     */   
/* 107 */   public static final byte[] ST_HTTP_VERSION_NOT_SUPPORTED = "505 HTTP Version not supported\r\n".getBytes();
/*     */   
/*     */ 
/*     */ 
/* 111 */   public static final byte[] PROTOCOL = "HTTP/1.1 ".getBytes();
/* 112 */   public static final byte[] F_SERVER = "Server: kilim 0.7.3\r\n".getBytes();
/*     */   
/*     */ 
/* 115 */   public static final byte[] F_DATE = "Date: ".getBytes();
/* 116 */   public static final byte[] CRLF = "\r\n".getBytes();
/* 117 */   public static final byte[] FIELD_SEP = ": ".getBytes();
/*     */   
/* 119 */   public static ConcurrentHashMap<String, byte[]> byteCache = new ConcurrentHashMap();
/*     */   
/*     */ 
/*     */   public byte[] status;
/*     */   
/*     */ 
/* 125 */   public ArrayList<String> keys = new ArrayList();
/* 126 */   public ArrayList<String> values = new ArrayList();
/*     */   
/*     */ 
/*     */   public ExposedBaos bodyStream;
/*     */   
/*     */ 
/* 132 */   public static final SimpleDateFormat gmtdf = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss");
/* 133 */   static { gmtdf.setTimeZone(TimeZone.getTimeZone("GMT:00")); }
/*     */   
/*     */   public static final boolean $isWoven = true;
/*     */   public HttpResponse() {
/* 137 */     this(ST_OK);
/*     */   }
/*     */   
/*     */   public HttpResponse(byte[] statusb) {
/* 141 */     this.status = statusb;
/*     */   }
/*     */   
/*     */   public void reuse() {
/* 145 */     this.status = ST_OK;
/* 146 */     this.keys.clear();
/* 147 */     this.values.clear();
/* 148 */     if (this.bodyStream != null) {
/* 149 */       this.bodyStream.reset();
/*     */     }
/* 151 */     if (this.buffer != null) {
/* 152 */       this.buffer.clear();
/*     */     }
/*     */   }
/*     */   
/*     */   public void setStatus(String statusMsg) {
/* 157 */     if (!statusMsg.endsWith("\r\n")) {
/* 158 */       statusMsg = statusMsg + "\r\n";
/*     */     }
/* 160 */     this.status = statusMsg.getBytes();
/*     */   }
/*     */   
/*     */   public HttpResponse(String statusMsg) {
/* 164 */     this();
/* 165 */     setStatus(statusMsg);
/*     */   }
/*     */   
/*     */   public HttpResponse addField(String key, String value) {
/* 169 */     this.keys.add(key);
/* 170 */     this.values.add(value);
/* 171 */     return this;
/*     */   }
/*     */   
/*     */   public void writeHeader(OutputStream os) throws IOException {
/* 175 */     DataOutputStream dos = new DataOutputStream(os);
/* 176 */     dos.write(PROTOCOL);
/* 177 */     dos.write(this.status);
/*     */     
/* 179 */     dos.write(F_DATE);
/* 180 */     byte[] date = gmtdf.format(new Date()).getBytes();
/* 181 */     dos.write(date);
/* 182 */     dos.write(CRLF);
/*     */     
/* 184 */     dos.write(F_SERVER);
/*     */     
/* 186 */     if (this.bodyStream != null) {
/* 187 */       setContentLength(this.bodyStream.size());
/*     */     }
/*     */     
/*     */ 
/* 191 */     int nfields = this.keys.size();
/* 192 */     for (int i = 0; i < nfields; i++) {
/* 193 */       String key = (String)this.keys.get(i);
/* 194 */       byte[] keyb = (byte[])byteCache.get(key);
/* 195 */       if (keyb == null) {
/* 196 */         keyb = key.getBytes();
/* 197 */         byteCache.put(key, keyb);
/*     */       }
/* 199 */       dos.write(keyb);
/* 200 */       dos.write(FIELD_SEP);
/* 201 */       dos.write(((String)this.values.get(i)).getBytes());
/* 202 */       dos.write(CRLF);
/*     */     }
/* 204 */     dos.write(CRLF);
/*     */   }
/*     */   
/*     */   public OutputStream getOutputStream() {
/* 208 */     if (this.bodyStream == null)
/* 209 */       this.bodyStream = new ExposedBaos(2048);
/* 210 */     return this.bodyStream; }
/*     */   
/*     */   public void writeTo(EndPoint arg1, Fiber ???) throws IOException, Pausable { Object localObject2;
/*     */     Object localObject1;
/* 214 */     ExposedBaos headerStream; ByteBuffer bb; switch ((localObject2 = ???).pc) {default:  Fiber.wrongPC(); case 1:  ??? = null;localObject1 = null; break; case 2:  ??? = null;localObject1 = null; break; case 0:  headerStream = new ExposedBaos();
/* 215 */       writeHeader(headerStream);
/* 216 */       bb = headerStream.toByteBuffer(); }
/* 217 */     endpoint.write(bb, ((Fiber)localObject2).down()); Object localObject3; switch (((Fiber)localObject2).up()) {case 2:  localObject3 = new S_O();((State)localObject3).self = this;((State)localObject3).pc = 1;((S_O)localObject3).f0 = endpoint;((Fiber)localObject2).setState((State)localObject3);return; case 3:  return; case 1:  localObject3 = (S_O)((Fiber)localObject2).curState;endpoint = (EndPoint)((S_O)localObject3).f0; }
/* 218 */     if ((this.bodyStream != null) && (this.bodyStream.size() > 0)) {
/* 219 */       bb = this.bodyStream.toByteBuffer();
/* 220 */       endpoint.write(bb, ((Fiber)localObject2).down()); switch (((Fiber)localObject2).up()) {case 2:  localObject3 = new State();((State)localObject3).self = this;((State)localObject3).pc = 2;((Fiber)localObject2).setState((State)localObject3);return; case 3:   }
/*     */     }
/*     */   }
/*     */   
/*     */   public void setContentLength(long length) {
/* 225 */     addField("Content-Length", Long.toString(length));
/*     */   }
/*     */   
/*     */   public void setContentType(String contentType) {
/* 229 */     addField("Content-Type", contentType);
/*     */   }
/*     */   
/*     */   public void writeTo(EndPoint paramEndPoint)
/*     */     throws IOException, Pausable
/*     */   {}
/*     */ }


/* Location:              /home/cfeindia/Desktop/kilim.jar!/kilim/http/HttpResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */