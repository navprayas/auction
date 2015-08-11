package com.cfe.auction.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Service;

import com.cfe.auction.dao.AutoBidDao;
import com.cfe.auction.model.persist.Auction;
import com.cfe.auction.model.persist.AutoBids;

@Service
public class AutoBidDaoImpl extends DAOImpl<Integer, AutoBids> implements
		AutoBidDao {

	@Override
	public List<AutoBids> getAutoBids() {

		Query query = getSessionFactory()
				.getCurrentSession()
				.createSQLQuery(
						"select AUTOBIDID,AUCTIONID, bidAmount,bidStatus,bidderName from autobids");

		List<Object[]> objectsList = query.list();
		List<AutoBids> autoBids = new ArrayList<>();

		System.out.println("" + objectsList);
		for (Object[] objects : objectsList) {

			AutoBids autobid = new AutoBids();
			autobid.setId(Integer.parseInt(objects[0].toString()));
			autobid.setAuctionId(Integer.parseInt(objects[1].toString()));
			autobid.setBidAmount(Double.parseDouble(objects[2].toString()));
			autobid.setBidStatus(objects[3].toString());
			autobid.setBidderName(objects[4].toString());
			autoBids.add(autobid);

		}

		return autoBids;

	}
}
