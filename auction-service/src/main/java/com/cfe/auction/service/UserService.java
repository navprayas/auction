package com.cfe.auction.service;

import java.util.List;

import com.cfe.auction.model.persist.User;

public interface UserService extends CRUDService<Integer, User> {
	public User getUserByUserName(String userName);

	public void addUser(User user);

	public List<User> getUsers();

	public String eccodePassword(String password);

	public void setAuctionMapping(Long selectedAuctionId,
			String selectedCategoryIdList, String selectedUserIdList);

}
