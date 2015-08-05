package com.cfe.auction.dao;

import com.cfe.auction.model.persist.User;

public interface UserDao extends DAO<Integer, User> {
	public User getUserByUsername(String username);
	
}
