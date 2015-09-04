package com.cfe.auction.dao.impl;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Service;

import com.cfe.auction.dao.BidsDao;
import com.cfe.auction.model.auction.persist.AuctionSearchBean;
import com.cfe.auction.model.persist.Bids;

@Service
public class BidsDaoImpl extends DAOImpl<Integer, Bids> implements BidsDao {
	@Override
	public void createBids(Bids bids, AuctionSearchBean auctionSearchBean) {
		try {
			getEntityManager(auctionSearchBean.getSchemaName()).persist(bids);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public List<Bids> getReportSummary(AuctionSearchBean auctionSearchBean) {
		// TODO Auto-generated method stub
		Query query = null;

		String fromDate = auctionSearchBean.getFromDate();
		String toDate = auctionSearchBean.getToDate();
		if ((fromDate == null || fromDate.equals(""))
				&& (toDate == null || toDate.endsWith(""))) {

			query = getEntityManager(auctionSearchBean.getSchemaName())
					.createQuery("From Bids");

		} else {
			query = getEntityManager(auctionSearchBean.getSchemaName())
					.createQuery(
							" from Bids where  bidTime >='"
									+ auctionSearchBean.getFromDate()
									+ "' and bidTime<='"
									+ auctionSearchBean.getToDate() + "'");
		}
		return query.getResultList();
	}
}
