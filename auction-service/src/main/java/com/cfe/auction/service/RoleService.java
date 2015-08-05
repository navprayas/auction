package com.cfe.auction.service;

import java.util.List;

import com.cfe.auction.dao.model.persist.Role;

public interface RoleService extends CRUDService<Integer, Role>  {
	public List<Role>  getRoles();
	public void  addRoles( Role role);

}
