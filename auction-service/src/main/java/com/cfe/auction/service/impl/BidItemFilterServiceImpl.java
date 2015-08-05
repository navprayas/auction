package com.cfe.auction.service.impl;
import static ch.lambdaj.Lambda.filter;
import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matchers;

import com.cfe.auction.dao.model.persist.User;
import com.cfe.auction.service.IBidItemFilterService;

/**
 * 
 * @author Vikas Anand
 *
 */
public class BidItemFilterServiceImpl implements IBidItemFilterService {

	@Override
	public void getBidItemListForcategoryId(final String categoryId) {
	/*	List<User> users = new ArrayList<User>();
		List<User> temp = filter(having(on(User.class).getUsername(),Matchers.equalTo(categoryId)), users);
		*/
	}
}
