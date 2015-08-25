package com.cfe.auction.service.cache.manager;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import com.cfe.auction.model.auction.persist.AuctionCacheBean;
import com.cfe.auction.model.persist.BidSequence;

public class AuctionCacheService {

	
	private static Map<Integer,Queue<Long>> bidSequenceQueueMap = new HashMap<Integer,Queue<Long>>();

	public static void setBidSequenceQueue(AuctionCacheBean auctionCacheBean, List<BidSequence> bidSequenceList) {
		Queue<Long> bidSequenceQueue = new LinkedList<Long>();
		if (bidSequenceList != null && !bidSequenceList.isEmpty()) {
			for (BidSequence bidSequence : bidSequenceList) {
				if (bidSequence != null && bidSequence.getBidItemId() != null
						&& bidSequence.getBidItemId() != null) {
					bidSequenceQueue.add(bidSequence.getBidItemId());
					bidSequenceQueueMap.put(auctionCacheBean.getClientId(), bidSequenceQueue);
				}
			}
		}
	}

	public static Long pollActiveBidItemId(AuctionCacheBean auctionCacheBean) {
		Queue<Long> bidSequenceQueue = bidSequenceQueueMap.get(auctionCacheBean.getClientId());
		if (bidSequenceQueue != null) {
			Long activeBidItemId = bidSequenceQueue.poll();
			return activeBidItemId;
		}
		return null;
	}


	public static void flushCache() {
		//bidSequenceMap = new HashMap<Long, BidSequence>();
		//activeBidItemsMap = new HashMap<Long, BidItem>();
	}


/*	public static Long getActiveBidSequenceId(AuctionSearchBean auctionSearchBean) {
		
		Long bidItemId = activeBidItemsId.get(auctionSearchBean.getClientId());
		
		if (bidItemId != null && activeBidItemsMap.get(auctionSearchBean.getClientId()) != null
				&& activeBidItemsMap.get(auctionSearchBean.getClientId()).get(bidItemId) != null) {
			return activeBidItemsMap.get(auctionSearchBean.getClientId()).get(bidItemId).getSeqId();
		}
		return null;
	}

*/	/*public static Long getActiveBidSequenceId() {
		if (getActiveBidItem(activeBidItemId) != null) {
			return getActiveBidItem(activeBidItemId).getSeqId();
		}
		return null;
	}*/

}