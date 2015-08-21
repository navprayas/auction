package com.cfe.auction.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cfe.auction.dao.UserRoleDao;
import com.cfe.auction.model.persist.UserRole;
import com.cfe.auction.model.persist.UserRoleId;
import com.cfe.auction.service.UserRoleService;

@Service("userRoleServiceImpl")
@Transactional
public class UserRoleServiceImpl extends
		CRUDServiceImpl<UserRoleId, UserRole, UserRoleDao> implements
		UserRoleService {

	@Autowired
	public UserRoleServiceImpl(UserRoleDao dao) {
		super(dao);
	}

	@Override
	@Transactional
	public void addUserRole(Integer userid, Integer roleid) {
		UserRole userRole = new UserRole();
		userRole.setId(new UserRoleId(userid, roleid.shortValue()));
		create(userRole);

	}

}
