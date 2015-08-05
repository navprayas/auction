package com.cfe.auction.dao.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cfe.auction.dao.RoleDao;
import com.cfe.auction.dao.model.persist.Role;
@Service 
public class RoleDaoImpl extends DAOImpl<Integer, Role> implements RoleDao {
	

}
