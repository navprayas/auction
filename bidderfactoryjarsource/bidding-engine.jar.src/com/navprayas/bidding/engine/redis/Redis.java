package com.navprayas.bidding.engine.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public abstract interface Redis
{
  public abstract Jedis connect()
    throws Exception;
  
  public abstract void close(Jedis paramJedis);
  
  public abstract void destroy();
  
  public abstract JedisPool getPool();
}


/* Location:              /home/cfeindia/Desktop/bidding-engine.jar!/com/navprayas/bidding/engine/redis/Redis.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */