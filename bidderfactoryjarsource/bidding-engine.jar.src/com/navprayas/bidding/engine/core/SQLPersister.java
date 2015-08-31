/*     */ package com.navprayas.bidding.engine.core;
/*     */ 
/*     */ import com.navprayas.bidding.engine.common.AutoBid;
/*     */ import com.navprayas.bidding.engine.common.Bid;
/*     */ import com.navprayas.bidding.engine.common.BidItemUpdate;
/*     */ import com.navprayas.bidding.engine.redis.Redis;
/*     */ import com.navprayas.bidding.engine.redis.RedisImpl;
/*     */ import java.io.PrintStream;
/*     */ import java.text.SimpleDateFormat;
/*     */ import kilim.Fiber;
/*     */ import kilim.Mailbox;
/*     */ import kilim.Pausable;
/*     */ import kilim.State;
/*     */ import kilim.Task;
/*     */ import org.hibernate.Query;
/*     */ import org.hibernate.SessionFactory;
/*     */ import org.hibernate.Transaction;
/*     */ import redis.clients.jedis.Jedis;
/*     */ 
/*     */ 
/*     */ public class SQLPersister
/*     */   extends Task
/*     */ {
/*     */   public static Mailbox<Bid> _autoBidBox;
/*  25 */   private static Redis redis = ;
/*  26 */   public static SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmssZ");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static SessionFactory sessionFactory;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final boolean $isWoven = true;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  42 */   public SQLPersister(SessionFactory sessionFactory) { sessionFactory = sessionFactory; }
/*     */   
/*     */   public void execute(Fiber paramFiber) throws Pausable {
/*     */     ;
/*  46 */     switch (paramFiber.pc) {default:  Fiber.wrongPC(); case 1:  break; } saveBids(paramFiber.down()); switch (paramFiber.up()) {case 2:  State localState = new State();localState.self = this;localState.pc = 1;paramFiber.setState(localState);return; case 3:   }
/*     */   }
/*     */   
/*     */   public void execute() throws Pausable
/*     */   {}
/*     */   
/*  52 */   public void saveBids(Fiber paramFiber) throws Pausable { switch (paramFiber.pc) {default:  Fiber.wrongPC(); case 1:  break; case 2:  break; } for (;;) { saveBid(AuctioneerImpl.SUCCESSREDISKEY);
/*  53 */       saveAutoBid(AuctioneerImpl.AUTOBIDKEY, paramFiber.down()); State localState; switch (paramFiber.up()) {case 2:  localState = new State();localState.self = this;localState.pc = 1;paramFiber.setState(localState);return; case 3:  return; }
/*  54 */       saveBid(AuctioneerImpl.FAILEDREDISKEY);
/*  55 */       Task.sleep(36000L, paramFiber.down()); switch (paramFiber.up()) {case 2:  localState = new State();localState.self = this;localState.pc = 2;paramFiber.setState(localState);return; case 3:  return; } } }
/*     */   
/*     */   public void saveBids() throws Pausable
/*     */   {}
/*     */   
/*  60 */   public void saveAutoBid(String arg1, Fiber ???) throws Pausable { Object localObject = ???;byte[] bytes = (byte[])null;
/*  61 */     AutoBid autoBid = null;
/*  62 */     Jedis jedis = null;
/*     */     try {
/*  64 */       jedis = redis.connect();
/*     */       
/*  66 */       org.hibernate.Session session = sessionFactory.openSession();
/*  67 */       Transaction transaction = session.beginTransaction();
/*  68 */       while (jedis.llen(key).longValue() > 0L) {
/*  69 */         bytes = jedis.rpop(key.getBytes());
/*  70 */         autoBid = (AutoBid)RedisImpl.decode(bytes);
/*  71 */         System.out.println("DECODE AutoBid: " + autoBid);
/*  72 */         session.save(autoBid);
/*     */       }
/*  74 */       session.flush();
/*  75 */       session.clear();
/*  76 */       transaction.commit();
/*  77 */       session.close();
/*  78 */       redis.close(jedis);
/*     */     }
/*     */     catch (Exception e) {
/*  81 */       redis.close(jedis);
/*  82 */       e.printStackTrace();
/*  83 */       System.out.println("EXCEPTION IN SAVING BID" + e.getMessage()); } }
/*     */   
/*     */   public void saveAutoBid(String paramString) throws Pausable
/*     */   {}
/*     */   
/*  88 */   public void saveBid(String key) { byte[] bytes = (byte[])null;
/*  89 */     Bid bid = null;
/*  90 */     Jedis jedis = null;
/*     */     try {
/*  92 */       jedis = redis.connect();
/*     */       
/*  94 */       org.hibernate.Session session = sessionFactory.openSession();
/*  95 */       Transaction transaction = session.beginTransaction();
/*  96 */       while (jedis.llen(key).longValue() > 0L) {
/*  97 */         bytes = jedis.rpop(key.getBytes());
/*  98 */         bid = (Bid)RedisImpl.decode(bytes);
/*  99 */         System.out.println("DECODE AutoBid: " + bid);
/* 100 */         session.save(bid);
/*     */       }
/* 102 */       session.flush();
/* 103 */       session.clear();
/* 104 */       transaction.commit();
/* 105 */       session.close();
/* 106 */       redis.close(jedis);
/*     */     }
/*     */     catch (Exception e) {
/* 109 */       redis.close(jedis);
/* 110 */       e.printStackTrace();
/* 111 */       System.out.println("EXCEPTION IN SAVING BID" + e.getMessage());
/*     */     }
/*     */   }
/*     */   
/*     */   public void updateBidItem(String bidItemId)
/*     */   {
/* 117 */     Jedis jedis = null;
/*     */     try
/*     */     {
/* 120 */       jedis = redis.connect();
/* 121 */       org.hibernate.Session session = sessionFactory.openSession();
/* 122 */       Transaction transaction = session.beginTransaction();
/* 123 */       Query query = sessionFactory.getCurrentSession().createQuery(" from BidItem as bidItem where bidItem.bidItemId=:bidItemId");
/* 124 */       query.setParameter("bidItemId", bidItemId);
/* 125 */       BidItemUpdate bidItem = (BidItemUpdate)query.uniqueResult();
/* 126 */       bidItem.setBidStartTime(sdf.parse(jedis.hget("BIDITEM::" + bidItemId, "bidStartTime")));
/* 127 */       bidItem.setBidEndTime(sdf.parse(jedis.hget("BIDITEM::" + bidItemId, "bidEndTime")));
/* 128 */       bidItem.setStatusCode(jedis.hget("BIDITEM::" + bidItemId, "statusCode"));
/* 129 */       bidItem.setLastUpdateTime(sdf.parse(jedis.hget("BIDITEM::" + bidItemId, "lastUpDateTime")));
/* 130 */       session.update(bidItemId, bidItem);
/* 131 */       session.flush();
/* 132 */       session.clear();
/* 133 */       transaction.commit();
/* 134 */       session.close();
/* 135 */       redis.close(jedis);
/*     */     }
/*     */     catch (Exception e) {
/* 138 */       redis.close(jedis);
/* 139 */       e.printStackTrace();
/* 140 */       System.out.println("EXCEPTION IN UPDATING BIDITEM" + e.getMessage());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/cfeindia/Desktop/bidding-engine.jar!/com/navprayas/bidding/engine/core/SQLPersister.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */