package com.cfe.auction.common;

import java.util.Comparator;

public class BidderAmountComparator implements Comparator<Bidder>{

	public int compare(Bidder o1, Bidder o2) {
		if(o2.getCurrentBidAmount() != o1.getCurrentBidAmount())
			return (int)(o2.getCurrentBidAmount() - o1.getCurrentBidAmount());
		else
			return (int)(o1.getBiddingTime().getTime() - o2.getBiddingTime().getTime());
	}

}
