package com.cfe.auction.db.dao.impl;

import org.hibernate.Query;
import org.springframework.stereotype.Service;

import com.cfe.auction.db.dao.UserDao;
import com.cfe.auction.model.persist.User;

@Service
public class UserDaoImpl extends DAOImpl<Integer, User> implements UserDao {

	@Override
	public User getUserByUsername(String username) {
		Query query = getSessionFactory().getCurrentSession().createQuery("from User where username = :username");
		query.setString("username", username);
		return (User)query.uniqueResult();
	}
	
	

}
