package com.cfe.auction.dao.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.cfe.auction.dao.IAuctionDao;
import com.cfe.auction.model.auction.persist.AuctionSearchBean;
import com.cfe.auction.model.persist.Auction;

@Repository
public class AuctionDaoImpl extends DAOImpl<Integer, Auction> implements
		IAuctionDao {

	@SuppressWarnings("unchecked")
	@Override
	public Auction getActiveAuction(AuctionSearchBean auctionSearchBean) {
		
		Query query = getEntityManager(auctionSearchBean.getSchemaName()).createQuery(
				"From Auction auction where auction.id = :auctionId");
		query.setParameter("auctionId", auctionSearchBean.getAuctionId());
		List<Auction> list = query.getResultList();
		if (list != null && list.size() >= 1) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public List<Auction> getAuctionList(String schemaKey) {
		if (getEntityManager(schemaKey) != null) {
			Query query = getEntityManager(schemaKey).createQuery(
							"From Auction auction where auction.status != 'Closed' and auction.status != 'Terminated' order by auction.auctionStartTime desc");
			return query.getResultList();
		}
		return null;
	}

	@Override
	public void closeAuction(AuctionSearchBean auctionSearchBean) {
		Query query = getEntityManager(auctionSearchBean.getSchemaName()).createQuery(
						" update Auction as auction set auction.auctionEndTime = :auctionEndTime, "
								+ "auction.lastUpdateTime = :lastUpdateTime, auction.status = 'Closed'  where auction.id = :auctionId ");
		query.setParameter("auctionEndTime", new Date());
		query.setParameter("lastUpdateTime", new Date());
		query.setParameter("auctionId", auctionSearchBean.getAuctionId());
		query.executeUpdate();
	}

	@Override
	public boolean isValidAuction(AuctionSearchBean auctionSearchBean) {
		Query query = getEntityManager(auctionSearchBean.getSchemaName()).createQuery(
				" from Auction as auction where auction.id = :auctionId ");
		query.setParameter("auctionId", auctionSearchBean.getAuctionId());
		Auction auction = (Auction) query.getSingleResult();
		return auction.getStatus().equals("Start")
				&& "1".equalsIgnoreCase(auction.getIsApproved());
	}

	@Override
	public Auction getActiveAuction() {
		Query query = getEntityManager().createQuery(
				"From Auction as auction  where auction.status = :status");
		query.setParameter("status", "Running");
		
		List<Auction> list = (List<Auction>) query.getResultList();
		if (list != null && list.size() >= 1) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public void setActualAuctionStartTime(Integer auctionId,
			Date actualAuctionStartTime) {
		Query query = getEntityManager().createQuery(
						" Update Auction as auction set auction.status = 'Running', auction.auctionStartTime = :actualAuctionStartTime, auction.lastUpdateTime = :lastUpdateTime where auction.id = :auctionId ");
		query.setParameter("actualAuctionStartTime", actualAuctionStartTime);
		query.setParameter("auctionId", auctionId);
		query.setParameter("lastUpdateTime", new Date());
		query.executeUpdate();
	}

}
