package com.cfe.auction.dao.impl;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Service;

import com.cfe.auction.dao.AutoBidDao;
import com.cfe.auction.model.auction.persist.AuctionSearchBean;
import com.cfe.auction.model.persist.AutoBids;

@Service
public class AutoBidDaoImpl extends DAOImpl<Integer, AutoBids> implements
		AutoBidDao {

	@Override
	@SuppressWarnings("unchecked")
	public List<AutoBids> getAutoBids(AuctionSearchBean auctionSearchBean) {

		Query query = getEntityManager(auctionSearchBean.getSchemaName()).createQuery(
				"from AutoBids");
		List<AutoBids> autoBids = query.getResultList();
		
		return autoBids;

	}

	@Override
	public AutoBids create(AutoBids autoBid, AuctionSearchBean auctionSearchBean) {
		try {
			getEntityManager(auctionSearchBean.getSchemaName()).persist(autoBid);
			return autoBid;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
