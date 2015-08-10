package com.cfe.auction.dao.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.cfe.auction.dao.IAuctionDao;
import com.cfe.auction.model.persist.Auction;

@Repository
public class AuctionDaoImpl extends DAOImpl<Integer, Auction> implements
		IAuctionDao {

	@SuppressWarnings("unchecked")
	@Override
	public Auction getActiveAuction(Integer auctionId) {
		Criteria criteria = getSessionFactory().getCurrentSession()
				.createCriteria(Auction.class);
		criteria = criteria.add(Restrictions.eq("id", auctionId));
		List<Auction> list = (List<Auction>) criteria.list();
		if (list != null && list.size() >= 1) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public List<Auction> getAuctionList() {
		Query query = getSessionFactory()
				.getCurrentSession()
				.createQuery(
						"From Auction auction where auction.status != 'Closed' and auction.status != 'Terminated' order by auction.auctionStartTime desc");
		return query.list();
	}
	
	@Override
	public void closeAuction(Integer auctionId) {
		Query query = getSessionFactory().getCurrentSession().createQuery(" update Auction as auction set auction.auctionEndTime = :auctionEndTime, " +
				"auction.lastUpdateTime = :lastUpdateTime, auction.status = 'Closed'  where id = :auctionId ");
		query.setDate("auctionEndTime", new Date());
		query.setDate("lastUpdateTime", new Date());
		query.setLong("auctionId", auctionId);	
		query.executeUpdate();
	}
	
	@Override
	public boolean isValidAuction(Integer auctionId) {
		Query query = getSessionFactory().getCurrentSession().createQuery(" from Auction as auction where auction.id = :auctionId ");
		query.setLong("auctionId", auctionId);	
		Auction auction =(Auction) query.uniqueResult();
		return auction.getStatus().equals("Start") && "1".equalsIgnoreCase(auction.getIsApproved());
	}

	@Override
	public Auction getActiveAuction() {
		Criteria criteria = getSessionFactory().getCurrentSession()
				.createCriteria(Auction.class);
		criteria = criteria.add(Restrictions.eq("status", "Running"));
		List<Auction> list = (List<Auction>) criteria.list();
		if (list != null && list.size() >= 1) {
			return list.get(0);
		}
		return null;
	}
	
	@Override
	public void setActualAuctionStartTime(Integer auctionId, Date actualAuctionStartTime) {
		Query query = getSessionFactory().getCurrentSession().createQuery(" Update Auction as auction set auction.status = 'Running', auction.auctionStartTime = :actualAuctionStartTime, auction.lastUpdateTime = :lastUpdateTime where auction.id = :auctionId ");
		query.setParameter("actualAuctionStartTime", actualAuctionStartTime);
		query.setParameter("auctionId", auctionId);
		query.setParameter("lastUpdateTime", new Date());
		query.executeUpdate();
	}	

}
