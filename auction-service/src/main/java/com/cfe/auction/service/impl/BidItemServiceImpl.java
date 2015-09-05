package com.cfe.auction.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cfe.auction.common.Bid;
import com.cfe.auction.common.BidItemUi;
import com.cfe.auction.dao.BidItemDao;
import com.cfe.auction.dao.CategoryDao;
import com.cfe.auction.dao.IAuctionDao;
import com.cfe.auction.model.auction.persist.AuctionSearchBean;
import com.cfe.auction.model.persist.BidItem;
import com.cfe.auction.model.persist.Bids;
import com.cfe.auction.model.persist.Category;
import com.cfe.auction.service.BidItemService;

;

@Service("bidItemServiceImpl")
@Transactional
public class BidItemServiceImpl extends
		CRUDServiceImpl<Integer, BidItem, BidItemDao> implements BidItemService {
	@Autowired
	private BidItemDao bidItemDao;
	@Autowired
	private IAuctionDao iAuctionDao;
	@Autowired
	private CategoryDao categoryDao;

	@Autowired
	public BidItemServiceImpl(BidItemDao dao) {
		super(dao);
	}

	@Transactional
	@Override
	public List<BidItem> getBidItemsbyAuctionId(
			AuctionSearchBean auctionSearchBean) {
		return bidItemDao.getBidItems(auctionSearchBean);
	}

	@Transactional
	@Override
	public void updateBidItem(AuctionSearchBean auctionSearchBean,
			BidItem bidItem) {
		bidItemDao.updateBidItem(auctionSearchBean, bidItem);
	}

	@Transactional
	@Override
	public List<BidItemUi> getWonList(String username,
			AuctionSearchBean auctionSearchBean) {
		List<BidItemUi> bidItemsList = new ArrayList<BidItemUi>();

		converterBidsToBidEntity(bidItemsList,
				bidItemDao.getWonList(username, auctionSearchBean),
				auctionSearchBean);
		return bidItemsList;
	}

	private void converterBidsToBidEntity(List<BidItemUi> bidList,
			List<BidItem> bidItems, AuctionSearchBean auctionSearchBean) {
		BidItemUi bidItemUi = null;
		for (BidItem bidItem : bidItems) {
			bidItemUi = new BidItemUi();
			bidItemUi.setBidItemId(bidItem.getBidItemId());
			bidItemUi.setName(bidItem.getName());
			bidItemUi.setItemLots(bidItem.getItemLots());
			Category category = categoryDao.get(bidItem.getCategoryId());
			bidItemUi.setCategoryName(category.getCategoryName());
			bidItemUi.setUnit(bidItem.getUnit());
			bidList.add(bidItemUi);

		}

	}

}
