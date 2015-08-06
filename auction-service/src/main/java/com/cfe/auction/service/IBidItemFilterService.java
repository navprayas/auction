package com.cfe.auction.service;

import java.util.List;

import com.cfe.auction.model.persist.BidItem;

/**
 * 
 * @author Vikas Anand
 *
 */
public interface IBidItemFilterService {


	List<BidItem> getBidItemListForcategoryId(List<BidItem> bidItems, String categoryId);

}
