/*     */ package com.navprayas.bidding.engine.redis;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.apache.commons.pool.impl.GenericObjectPool.Config;
/*     */ import redis.clients.jedis.Connection;
/*     */ import redis.clients.jedis.Jedis;
/*     */ import redis.clients.jedis.JedisPool;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RedisImpl
/*     */   implements Redis
/*     */ {
/*     */   private static JedisPool pool;
/*     */   private static RedisImpl _instance;
/*  24 */   GenericObjectPool.Config config = new GenericObjectPool.Config();
/*     */   RedisImpl()
/*     */   {
/*  41 */     if (pool == null) {
/*  42 */       this.config.maxActive = -1;
/*  43 */       this.config.whenExhaustedAction = 2;
/*     */       
/*  45 */       this.config.testOnBorrow = true;
/*  46 */       this.config.testOnReturn = true;
/*  47 */       this.config.numTestsPerEvictionRun = 20;
/*  48 */       this.config.timeBetweenEvictionRunsMillis = 5000L;
/*  49 */       this.config.minEvictableIdleTimeMillis = 3000L;
/*  50 */       this.config.testWhileIdle = true;
/*  51 */       this.config.softMinEvictableIdleTimeMillis = 1000L;
/*  52 */       pool = new JedisPool(this.config, "localhost", 6379);
/*     */     }
/*     */   }
/*     */   
/*     */   public static synchronized RedisImpl getInstance() {
/*  57 */     if (_instance == null) {
/*  58 */       _instance = new RedisImpl();
/*     */     }
/*  60 */     return _instance;
/*     */   }
/*     */   
/*     */   public Jedis connect() {
/*  64 */     return (Jedis)pool.getResource();
/*     */   }
/*     */   
/*     */   public void close(Jedis redis) {
/*  68 */     pool.returnResource(redis);
/*  69 */     pool.returnBrokenResource(redis);
/*     */   }
/*     */   
/*     */   public void destroy() {
/*  73 */     pool.destroy();
/*     */   }
/*     */   
/*     */   public JedisPool getPool() {
/*  77 */     return pool;
/*     */   }
/*     */   
/*     */   public List<Connection> createPool(int poolSize) {
/*  81 */     List<Connection> list = new ArrayList();
/*     */     
/*  83 */     for (int i = 0; i < poolSize; i++) {
/*  84 */       Connection conn = new Connection("localhost", 6379);
/*  85 */       conn.setTimeoutInfinite();
/*  86 */       list.add(conn);
/*     */     }
/*  88 */     return list;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static final <T extends Serializable> T decode(byte[] bytes)
/*     */   {
/*  95 */     T t = null;
/*  96 */     Exception thrown = null;
/*     */     try {
/*  98 */       ObjectInputStream oin = new ObjectInputStream(new ByteArrayInputStream(bytes));
/*  99 */       t = (Serializable)oin.readObject();
/*     */     }
/*     */     catch (IOException e) {
/* 102 */       e.printStackTrace();
/* 103 */       thrown = e;
/*     */     }
/*     */     catch (ClassNotFoundException e) {
/* 106 */       e.printStackTrace();
/* 107 */       thrown = e;
/*     */     }
/*     */     catch (ClassCastException e) {
/* 110 */       e.printStackTrace();
/* 111 */       thrown = e;
/*     */     }
/*     */     finally {
/* 114 */       if (thrown != null) {
/* 115 */         throw new RuntimeException(
/* 116 */           "Error decoding byte[] data to instantiate java object - data at key may not have been of this type or even an object", 
/* 117 */           thrown);
/*     */       }
/*     */     }
/* 114 */     if (thrown != null) {
/* 115 */       throw new RuntimeException(
/* 116 */         "Error decoding byte[] data to instantiate java object - data at key may not have been of this type or even an object", 
/* 117 */         thrown);
/*     */     }
/*     */     
/* 120 */     return t;
/*     */   }
/*     */ }


/* Location:              /home/cfeindia/Desktop/bidding-engine.jar!/com/navprayas/bidding/engine/redis/RedisImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */