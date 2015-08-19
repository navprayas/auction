package com.cfe.auction.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Service;

import com.cfe.auction.dao.AutoBidDao;
import com.cfe.auction.model.persist.AutoBids;

@Service
public class AutoBidDaoImpl extends DAOImpl<Integer, AutoBids> implements
		AutoBidDao {

	@Override
	public List<AutoBids> getAutoBids() {

		Query query = getEntityManager().createNativeQuery(
				"select AUTOBIDID,AUCTIONID, bidAmount,bidStatus,bidderName,bidItemId,bidTime,comments,currency from autobids");

		List<Object[]> objectsList = query.getResultList();
		List<AutoBids> autoBids = new ArrayList<>();

		System.out.println("" + objectsList);
		for (Object[] objects : objectsList) {

			AutoBids autobid = new AutoBids();
			System.out.println(objects[0].toString());
			autobid.setId(Integer.parseInt(objects[0].toString()));
			autobid.setAuctionId(Integer.parseInt(objects[1].toString()));
			autobid.setBidAmount(Double.parseDouble(objects[2].toString()));
			autobid.setBidStatus(objects[3].toString());
			autobid.setBidderName(objects[4].toString());
			autobid.setBidItemId(Long.parseLong(objects[5].toString()));
			autobid.setBidTime((Date) objects[6]);
			autobid.setComments(objects[7].toString());
			autobid.setCurrency(objects[8].toString());
			autoBids.add(autobid);

		}

		return autoBids;

	}
}
