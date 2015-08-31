package com.navprayas.bidding.engine.core;

import com.navprayas.bidding.engine.common.Bid;
import kilim.Mailbox;
import org.hibernate.SessionFactory;

public abstract interface Auctioneer
{
  public abstract Mailbox<Bid> getMailBox();
  
  public abstract void stopAuction();
  
  public abstract boolean startAuction()
    throws Exception;
  
  public abstract void registerSessionFactory(SessionFactory paramSessionFactory);
  
  public abstract void restart();
}


/* Location:              /home/cfeindia/Desktop/bidding-engine.jar!/com/navprayas/bidding/engine/core/Auctioneer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */