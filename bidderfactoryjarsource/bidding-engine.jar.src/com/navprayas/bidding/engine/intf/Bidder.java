package com.navprayas.bidding.engine.intf;

import com.navprayas.bidding.engine.common.Bid;
import com.navprayas.bidding.engine.except.BiddingException;

public abstract interface Bidder
{
  public abstract void call(Bid paramBid)
    throws BiddingException;
}


/* Location:              /home/cfeindia/Desktop/bidding-engine.jar!/com/navprayas/bidding/engine/intf/Bidder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */