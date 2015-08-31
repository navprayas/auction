/*     */ package com.navprayas.bidding.engine.redis;
/*     */ 
/*     */ import com.navprayas.bidding.engine.common.AutoBid;
/*     */ import com.navprayas.bidding.engine.common.Bid;
/*     */ import com.navprayas.bidding.engine.common.BidItem;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectOutput;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Set;
/*     */ import redis.clients.jedis.Jedis;
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class RedisCache
/*     */ {
/*  19 */   private static Redis redis = ;
/*  20 */   public static SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmssZ");
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
/*     */   public static void saveSuccessBid(Bid bid)
/*     */   {
/*  35 */     Jedis jedis = null;
/*     */     try {
/*  37 */       jedis = redis.connect();
/*  38 */       jedis.hset("BIDITEM::" + bid.getBidItemId(), "currentAutoBidId", String.valueOf(bid.getBidderId()));
/*  39 */       jedis.hset("BIDITEM::" + bid.getBidItemId(), "auctionId", String.valueOf(bid.getAuctionId()));
/*  40 */       jedis.hset("BIDITEM::" + bid.getBidItemId(), "currentMarketPrice", String.valueOf(bid.bidAmount));
/*  41 */       jedis.hset("BIDITEM::" + bid.getBidItemId(), "currency", String.valueOf(bid.getCurrency()));
/*  42 */       jedis.hset("BIDITEM::" + bid.getBidItemId(), "bidTime", sdf.format(bid.getBidTime()));
/*  43 */       jedis.hset("BIDITEM::" + bid.getBidItemId(), "lastUpDateTime", sdf.format(bid.getBidTime()));
/*  44 */       jedis.hset("BIDITEM::" + bid.getBidItemId(), "statusCode", bid.getStatus());
/*  45 */       jedis.hset("BID::BIDITEM::" + bid.getBidItemId(), "bidderId", String.valueOf(bid.getBidderId()));
/*  46 */       jedis.hset("BID::BIDITEM::" + bid.getBidItemId(), "bidderName", bid.getBidderName());
/*  47 */       jedis.hset("BIDITEM::" + bid.getBidItemId(), "isAutoBid", bid.getBidType() == 2 ? "true" : "false");
/*  48 */       jedis.hset("BIDDER::" + bid.getBidItemId() + "::" + bid.bidderName, "autoBid", bid.getBidType() == 2 ? "true" : "false");
/*  49 */       jedis.hset("BIDITEM::" + bid.getBidItemId(), "autoBidAmount", String.valueOf(bid.getAutoBidAmount()));
/*  50 */       if (bid.bidType == 2) jedis.hset("BIDITEM::" + bid.getBidItemId(), "autoBidderName", bid.getBidderName());
/*     */     } catch (Exception e) {
/*  52 */       System.out.println("FAILED TO STORE BIDITEM SUCCESS: " + e.getMessage());
/*  53 */       e.printStackTrace();
/*     */     }
/*  55 */     redis.close(jedis);
/*     */   }
/*     */   
/*     */   public static void addBidder(long bidItemId, String bidderName, boolean isAutoBid, double bidAmount) {
/*  59 */     Jedis jedis = null;
/*     */     try {
/*  61 */       jedis = redis.connect();
/*  62 */       String key = "BIDDER::" + bidItemId + "::";
/*  63 */       jedis.sadd("::BIDDERS::" + bidItemId, bidderName);
/*  64 */       jedis.hset(key + bidderName, "bidderName", bidderName);
/*  65 */       jedis.hset(key + bidderName, "autoBid", String.valueOf(isAutoBid));
/*  66 */       jedis.hset(key + bidderName, "bidAmount", String.valueOf(bidAmount));
/*     */     } catch (Exception e) {
/*  68 */       System.out.println("FAILED TO STORE BIDDER FOR BIDITEM: " + e.getMessage());
/*  69 */       e.printStackTrace();
/*     */     }
/*  71 */     redis.close(jedis);
/*     */   }
/*     */   
/*     */   public static BidItem getBidItem(long bidItemId)
/*     */   {
/*  76 */     Jedis jedis = null;
/*  77 */     BidItem bidItem = null;
/*  78 */     String s = null;
/*     */     try {
/*  80 */       jedis = redis.connect();
/*  81 */       s = jedis.hget("BIDITEM::" + bidItemId, "bidItemId");
/*  82 */       bidItem = new BidItem(Long.parseLong(s));
/*  83 */       s = jedis.hget("BIDITEM::" + bidItemId, "minBidPrice");
/*  84 */       bidItem.setMinBidPrice((s == null) || (s.equals("null")) || (s.length() == 0) ? 0.0D : Double.parseDouble(s));
/*  85 */       s = jedis.hget("BIDITEM::" + bidItemId, "minBidIncrement");
/*  86 */       bidItem.setMinBidIncrement((s == null) || (s.equals("null")) || (s.length() == 0) ? 0.0D : Double.parseDouble(s));
/*  87 */       s = jedis.hget("BIDITEM::" + bidItemId, "bidStartTime");
/*  88 */       bidItem.setBidStartTime((s == null) || (s.equals("null")) || (s.length() == 0) ? null : sdf.parse(s));
/*  89 */       s = jedis.hget("BIDITEM::" + bidItemId, "bidEndTime");
/*  90 */       bidItem.setBidEndTime((s == null) || (s.equals("null")) || (s.length() == 0) ? null : sdf.parse(s));
/*  91 */       s = jedis.hget("BIDITEM::" + bidItemId, "timeExtAfterBid");
/*  92 */       bidItem.setTimeExtAfterBid((s == null) || (s.equals("null")) || (s.length() == 0) ? 0 : Integer.parseInt(s));
/*  93 */       s = jedis.hget("BIDITEM::" + bidItemId, "statusCode");
/*  94 */       bidItem.setStatusCode(s);
/*  95 */       s = jedis.hget("BIDITEM::" + bidItemId, "currency");
/*  96 */       bidItem.setCurrency(s);
/*  97 */       s = jedis.hget("BIDITEM::" + bidItemId, "auctionId");
/*  98 */       bidItem.setAuctionId((s == null) || (s.equals("null")) || (s.length() == 0) ? 0L : Long.parseLong(s));
/*  99 */       s = jedis.hget("BIDITEM::" + bidItemId, "autoBidderName");
/* 100 */       bidItem.setAutoBidderName(s);
/* 101 */       s = jedis.hget("BIDITEM::" + bidItemId, "bidderId");
/* 102 */       bidItem.setAutoBidderId((s == null) || (s.equals("null")) || (s.length() == 0) ? 0L : Long.parseLong(s));
/* 103 */       s = jedis.hget("BIDITEM::" + bidItemId, "isAutoBid");
/* 104 */       bidItem.setAutoBidFlag(Boolean.parseBoolean(s));
/* 105 */       s = jedis.hget("BIDITEM::" + bidItemId, "autoBidAmount");
/* 106 */       bidItem.setAutoBidAmount((s == null) || (s.equals("null")) || (s.length() == 0) ? 0.0D : Double.parseDouble(s));
/* 107 */       s = jedis.hget("BIDITEM::" + bidItemId, "lastUpDateTime");
/* 108 */       bidItem.setLastUpdateTime((s == null) || (s.equals("null")) || (s.length() == 0) ? null : sdf.parse(s));
/*     */       
/* 110 */       bidItem.setBid(getBid(bidItemId));
/*     */     }
/*     */     catch (Exception e) {
/* 113 */       System.out.println("FAILED TO GET BIDITEM: " + e.getMessage());
/* 114 */       e.printStackTrace();
/*     */     }
/* 116 */     redis.close(jedis);
/* 117 */     return bidItem;
/*     */   }
/*     */   
/*     */   public static void setBidItem(BidItem bidItem) {
/* 121 */     Jedis jedis = null;
/*     */     try {
/* 123 */       jedis = redis.connect();
/* 124 */       jedis.sadd("::BIDITEMS::", String.valueOf(bidItem.getBidItemId()));
/* 125 */       jedis.hset("BIDITEM::" + bidItem.getBidItemId(), "bidItemId", String.valueOf(bidItem.getBidItemId()));
/* 126 */       jedis.hset("BIDITEM::" + bidItem.getBidItemId(), "statusCode", bidItem.getStatusCode());
/* 127 */       jedis.hset("BIDITEM::" + bidItem.getBidItemId(), "autoBidderName", bidItem.getAutoBidderName());
/* 128 */       jedis.hset("BIDITEM::" + bidItem.getBidItemId(), "isAutoBid", String.valueOf(bidItem.isAutoBidFlag()));
/* 129 */       jedis.hset("BIDITEM::" + bidItem.getBidItemId(), "autoBidAmount", String.valueOf(bidItem.getAutoBidAmount()));
/* 130 */       jedis.hset("BIDITEM::" + bidItem.getBidItemId(), "bidderId", String.valueOf(bidItem.getAutoBidderId()));
/* 131 */       jedis.hset("BIDITEM::" + bidItem.getBidItemId(), "lastUpDateTime", sdf.format(bidItem.getLastUpdateTime()));
/* 132 */       jedis.hset("BIDITEM::" + bidItem.getBidItemId(), "bidEndTime", sdf.format(bidItem.getBidEndTime()));
/*     */     }
/*     */     catch (Exception e) {
/* 135 */       System.out.println("FAILED TO SET BIDITEM: " + e.getMessage());
/* 136 */       e.printStackTrace();
/*     */     }
/* 138 */     redis.close(jedis);
/*     */   }
/*     */   
/*     */   public static Bid getBid(long bidItemId) {
/* 142 */     Jedis jedis = null;
/* 143 */     String s = null;
/* 144 */     Bid bid = null;
/*     */     try {
/* 146 */       jedis = redis.connect();
/* 147 */       bid = new Bid(bidItemId);
/* 148 */       s = jedis.hget("BIDITEM::" + bidItemId, "bidItemId");
/* 149 */       bid.setBidItemId((s == null) || (s.equals("null")) || (s.length() == 0) ? 0L : Long.parseLong(s));
/* 150 */       s = jedis.hget("BIDITEM::" + bidItemId, "currentAutoBidId");
/* 151 */       bid.setBidId((s == null) || (s.equals("null")) || (s.length() == 0) ? 0L : Long.parseLong(s));
/* 152 */       s = jedis.hget("BIDITEM::" + bidItemId, "auctionId");
/* 153 */       bid.setAuctionId((s == null) || (s.equals("null")) || (s.length() == 0) ? 0L : Long.parseLong(s));
/* 154 */       s = jedis.hget("BIDITEM::" + bidItemId, "currentMarketPrice");
/* 155 */       bid.setBidAmount((s == null) || (s.equals("null")) || (s.length() == 0) ? 0.0D : Double.parseDouble(s));
/* 156 */       s = jedis.hget("BIDITEM::" + bidItemId, "currency");
/* 157 */       bid.setCurrency(s);
/* 158 */       s = jedis.hget("BIDITEM::" + bidItemId, "bidTime");
/* 159 */       bid.setBidTime((s == null) || (s.equals("null")) || (s.length() == 0) ? null : sdf.parse(s));
/* 160 */       s = jedis.hget("BIDITEM::" + bidItemId, "statusCode");
/* 161 */       bid.setStatus(s);
/* 162 */       s = jedis.hget("BID::BIDITEM::" + bidItemId, "bidderId");
/* 163 */       bid.setStatus(s);
/* 164 */       s = jedis.hget("BID::BIDITEM::" + bidItemId, "bidderName");
/* 165 */       bid.setBidderName(s);
/* 166 */       s = jedis.hget("BIDITEM::" + bidItemId, "isAutoBid");
/* 167 */       bid.setBidType(s.equals("true") ? 2 : (s == null) || (s.equals("null")) || (s.length() == 0) ? 1 : 1);
/* 168 */       if ((s != null) && (s.equals("true"))) {
/* 169 */         bid.setBidderName(jedis.hget("BIDITEM::" + bidItemId, "autoBidderName"));
/*     */       }
/* 171 */       s = jedis.hget("BIDITEM::" + bidItemId, "autoBidAmount");
/* 172 */       bid.setAutoBidAmount((s == null) || (s.equals("null")) || (s.length() == 0) ? 0.0D : Double.parseDouble(s));
/* 173 */       return bid;
/*     */     }
/*     */     catch (Exception e) {
/* 176 */       System.out.println("FAILED TO GET BID: " + e.getMessage());
/* 177 */       e.printStackTrace();
/*     */       
/* 179 */       redis.close(jedis); }
/* 180 */     return null;
/*     */   }
/*     */   
/*     */   public static boolean bidItemExists(BidItem bidItem) {
/* 184 */     Jedis jedis = null;
/*     */     try {
/* 186 */       jedis = redis.connect();
/* 187 */       return jedis.sismember("::BIDITEMS::", String.valueOf(bidItem.getBidItemId())).booleanValue();
/*     */     }
/*     */     catch (Exception e) {
/* 190 */       System.out.println("FAILED TO CHECK BIDITEM IN CACHE: " + e.getMessage());
/* 191 */       e.printStackTrace();
/*     */       
/* 193 */       redis.close(jedis); }
/* 194 */     return false;
/*     */   }
/*     */   
/*     */   public static void saveAutoBid(String key, Bid bid) {
/* 198 */     ByteArrayOutputStream bos = new ByteArrayOutputStream();
/*     */     
/* 200 */     Jedis jedis = null;
/* 201 */     AutoBid autoBid = new AutoBid();
/* 202 */     autoBid.setAuctionId(bid.getAuctionId());
/*     */     
/* 204 */     autoBid.setBidAmount(bid.getAutoBidAmount());
/* 205 */     autoBid.setBidderId(bid.getBidderId());
/* 206 */     autoBid.setBidderName(bid.getBidderName());
/* 207 */     autoBid.setBidItemId(bid.getBidItemId());
/* 208 */     autoBid.setBidTime(bid.getBidTime());
/* 209 */     autoBid.setComments(bid.getComments());
/* 210 */     autoBid.setCurrency(bid.getCurrency());
/* 211 */     autoBid.setVersion(bid.getVersion());
/* 212 */     autoBid.setStatus("A");
/*     */     try {
/* 214 */       jedis = redis.connect();
/* 215 */       ObjectOutput out = new ObjectOutputStream(bos);
/* 216 */       autoBid.setBidId(jedis.incr("::AUTOBIDID::").longValue());
/* 217 */       out.writeObject(autoBid);
/* 218 */       out.close();
/* 219 */       jedis.lpush(key.getBytes(), bos.toByteArray());
/*     */     } catch (IOException e) {
/* 221 */       redis.close(jedis);
/* 222 */       System.out.println("FAILED TO STORE AUTO BID IN REDIS");
/* 223 */       System.out.println("FAILED AUTO BID: " + bid);
/* 224 */       e.printStackTrace();
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 228 */       redis.close(jedis);
/* 229 */       System.out.println("FAILED TO STORE AUTO BID IN REDIS");
/* 230 */       System.out.println("FAILED AUTO BID: " + bid);
/* 231 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   public static void saveBid(String key, Bid bid) {
/* 236 */     ByteArrayOutputStream bos = new ByteArrayOutputStream();
/*     */     
/* 238 */     Jedis jedis = null;
/*     */     try {
/* 240 */       jedis = redis.connect();
/* 241 */       ObjectOutput out = new ObjectOutputStream(bos);
/* 242 */       bid.setBidId(jedis.incr("::BIDID::").longValue());
/* 243 */       out.writeObject(bid);
/* 244 */       out.close();
/* 245 */       jedis.lpush(key.getBytes(), bos.toByteArray());
/*     */     } catch (IOException e) {
/* 247 */       redis.close(jedis);
/* 248 */       System.out.println("FAILED TO STORE BID IN REDIS");
/* 249 */       System.out.println("FAILED BID: " + bid);
/* 250 */       e.printStackTrace();
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 254 */       redis.close(jedis);
/* 255 */       System.out.println("FAILED TO STORE BID IN REDIS");
/* 256 */       System.out.println("FAILED BID: " + bid);
/* 257 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   public static Set<String> getExpiredBids() {
/* 262 */     Jedis jedis = null;
/* 263 */     Set<String> set = null;
/*     */     try {
/* 265 */       jedis = redis.connect();
/* 266 */       set = jedis.smembers("::EXPIRED::BIDITEMS::");
/*     */     }
/*     */     catch (Exception e) {
/* 269 */       System.out.println("FAILED TO GET EXPIRED BIDS LIST ");
/* 270 */       e.printStackTrace();
/*     */     }
/* 272 */     redis.close(jedis);
/* 273 */     return set;
/*     */   }
/*     */   
/*     */   public static void cleanBidItem() {
/* 277 */     Jedis jedis = null;
/*     */     try {
/* 279 */       jedis = redis.connect();
/* 280 */       Set<String> bidItems = getExpiredBids();
/*     */       
/* 282 */       for (String bidItemId : bidItems) {
/* 283 */         Set<String> bidders = jedis.smembers("::BIDDERS::" + bidItemId);
/* 284 */         String key = "BIDDER::" + bidItemId + "::";
/* 285 */         for (String bidder : bidders) {
/* 286 */           jedis.del(new String[] { key + bidder });
/*     */         }
/* 288 */         jedis.del(new String[] { "::BIDDERS::" + bidItemId });
/* 289 */         jedis.del(new String[] { "BIDITEM::" + bidItemId });
/* 290 */         jedis.srem("::BIDITEMS::", bidItemId);
/* 291 */         jedis.srem("::EXPIRED::BIDITEMS::", bidItemId);
/*     */       }
/*     */     }
/*     */     catch (Exception e) {
/* 295 */       System.out.println("FAILED TO CLEAN EXPIRED BIDS LIST ");
/* 296 */       e.printStackTrace();
/*     */     }
/* 298 */     redis.close(jedis);
/*     */   }
/*     */ }


/* Location:              /home/cfeindia/Desktop/bidding-engine.jar!/com/navprayas/bidding/engine/redis/RedisCache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */