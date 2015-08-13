package com.cfe.auction.service.cache.manager;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import com.cfe.auction.model.persist.BidItem;
import com.cfe.auction.model.persist.BidSequence;

public class AuctionCacheService {
	
	private static Queue<Long> bidSequenceQueue = new LinkedList<Long>();
	
	private static Map<Long, BidSequence> bidSequenceMap  = new HashMap<Long, BidSequence>();
	private static Map<Long, BidItem> activeBidItemsMap = new HashMap<Long, BidItem>();
	
	private static Long activeBidItemId;
	
	private static Long activeBidSequenceId;
	
	public static void setBidSequenceQueue(List<BidSequence> bidSequenceList) {
		if(bidSequenceList != null && !bidSequenceList.isEmpty()) {
			for (BidSequence bidSequence : bidSequenceList) {
				if (bidSequence != null && bidSequence.getBidItem() != null && bidSequence.getBidItem().getBidItemId() != null) {
					bidSequenceQueue.add(bidSequence.getBidItem().getBidItemId());
					bidSequenceMap.put(bidSequence.getBidItem().getBidItemId(), bidSequence);
				}
			}
		}
	}

	public static void setActiveBidItemId() {
		activeBidItemId = bidSequenceQueue.poll();
	}
	public static Long getActiveBidItemId(){
		return activeBidItemId;
	}
	
	public static void setActiveBidItem(Long bidItemId, BidItem bidItem) {
		activeBidItemsMap.put(bidItemId, bidItem);
	}
	
	public static BidItem getActiveBidItem(Long bidItemId) {
		return activeBidItemsMap.get(bidItemId);
	}
	public static BidSequence getBidSequenceDetails(Long bidItemId){
		return bidSequenceMap.get(bidItemId);
	}
	public static void flushCache() {
		activeBidItemId = null;
		bidSequenceMap =  new HashMap<Long, BidSequence>();
		bidSequenceQueue = new LinkedList<Long>();
		activeBidItemsMap =  new HashMap<Long, BidItem>();
	}

	public static Long getActiveBidSequenceId() {
		if(getActiveBidItem(activeBidItemId) != null) {
			return getActiveBidItem(activeBidItemId).getSeqId();
		}
		return null;
	}
	
	
}