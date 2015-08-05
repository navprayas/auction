package com.cfe.auction.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cfe.auction.dao.UserDao;
import com.cfe.auction.dao.model.persist.User;
import com.cfe.auction.service.UserService;

@Service
public class UserServiceImpl extends CRUDServiceImpl<Integer, User, UserDao>
		implements UserService {

	@Autowired
	public UserServiceImpl(UserDao dao) {
		super(dao);
	}

	@Override
	@Transactional
	public User getUserByUserName(String userName) {
		return dao.getUserByUsername(userName);
	}

	@Override
	@Transactional
	public void addUser(User user) {
		String password = user.getPassword();
		user.setPassword(eccodePassword(password));

		user.setEnabled(true);
		user.setRetryCount((byte) 5);

		create(user);

	}

	public String eccodePassword(String password) {
		return new MessageDigestPasswordEncoder("MD5").encodePassword(password,
				null);
	}

	@Override
	@Transactional
	public List<User> getUsers() {
		return findAll(User.class);

	}
}
