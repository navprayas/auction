package com.cfe.auction.service;

import com.cfe.auction.model.persist.UserRole;
import com.cfe.auction.model.persist.UserRoleId;

public interface UserRoleService extends CRUDService<UserRoleId, UserRole>{
	public void addUserRole(Integer userid,Integer roleid);
		
	
	

}
