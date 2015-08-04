package com.cfe.auction.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cfe.auction.db.dao.RoleDao;
import com.cfe.auction.model.persist.Role;
import com.cfe.auction.service.RoleService;

@Service
@Transactional
public class RoleServiceImpl extends CRUDServiceImpl<Integer, Role, RoleDao>
		implements RoleService {
	@Autowired
	public RoleServiceImpl(RoleDao dao) {
		super(dao);

	}

	@Override
	@Transactional
	public List<Role> getRoles() {

	return(findAll(Role.class));
	}

	@Override
	@Transactional
	public void addRoles(Role role) {
		dao.create(role);
		
	}

}
