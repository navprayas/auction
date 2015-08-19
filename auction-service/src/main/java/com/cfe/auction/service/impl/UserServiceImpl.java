package com.cfe.auction.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cfe.auction.dao.BidderCategoryDao;
import com.cfe.auction.dao.CategoryDao;
import com.cfe.auction.dao.IAuctionDao;
import com.cfe.auction.dao.UserDao;
import com.cfe.auction.model.persist.BidderCategory;
import com.cfe.auction.model.persist.BidderCategoryId;
import com.cfe.auction.model.persist.User;
import com.cfe.auction.service.UserService;

@Service("userServiceImpl")
public class UserServiceImpl extends CRUDServiceImpl<Integer, User, UserDao>
		implements UserService {

	@Autowired
	private CategoryDao categoryDao;
	@Autowired
	private IAuctionDao iAuctionDao;
	@Autowired
	private BidderCategoryDao bidderCategoryDao;

	@Autowired
	public UserServiceImpl(UserDao dao) {
		super(dao);
	}
	
	@Override @Transactional
	public User update(User po) {
		return dao.update(po);
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

	@Override
	public void setAuctionMapping(Long selectedAuctionId,
			String selectedCategoryIdList, String selectedUserIdList) {
		List<String> categoryIds = new ArrayList<String>();
		List<String> userIds = new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(selectedCategoryIdList, "$");
		while (st.hasMoreTokens()) {
			categoryIds.add(st.nextToken());
		}
		st = new StringTokenizer(selectedUserIdList, "$");
		while (st.hasMoreTokens()) {
			userIds.add(st.nextToken());
		}
		for (String userId : userIds) {
			for (String categoryId : categoryIds) {
				BidderCategory bidderCategory = new BidderCategory();
				BidderCategoryId bidderCategoryId = new BidderCategoryId();
				bidderCategoryId.setCategory(categoryDao.get(Integer
						.parseInt(categoryId)));
				bidderCategoryId.setUser(read(Integer.parseInt(userId)));
				bidderCategory.setBidderCategoryId(bidderCategoryId);
				bidderCategory.setAuction(iAuctionDao.get(Integer
						.parseInt(selectedAuctionId.toString())));
				bidderCategoryDao.saveBIdderCategory(bidderCategory);
			}
		}

	}
}
