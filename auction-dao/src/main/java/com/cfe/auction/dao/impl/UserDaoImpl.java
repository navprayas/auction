package com.cfe.auction.dao.impl;


import javax.persistence.Query;

import org.springframework.stereotype.Service;

import com.cfe.auction.dao.UserDao;
import com.cfe.auction.model.persist.User;

@Service
public class UserDaoImpl extends DAOImpl<Integer, User> implements UserDao {

	@Override
	public User getUserByUsername(String username) {
		Query query = getMasterEntityManager().createQuery("from User where username = :username");
		query.setParameter("username", username);
		return (User)query.getSingleResult();
	}
	
	@Override
	public User update(User po) {
		getMasterEntityManager().merge(po);
		return po;
	}
	

}
