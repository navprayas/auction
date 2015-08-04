package com.cfe.auction.db.dao.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cfe.auction.db.dao.RoleDao;
import com.cfe.auction.model.persist.Role;
@Service @Transactional
public class RoleDaoImpl extends DAOImpl<Integer, Role> implements RoleDao {
	

}
