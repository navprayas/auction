package com.cfe.auction.service.security.impl;

import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cfe.auction.model.persist.Role;
import com.cfe.auction.model.persist.User;

@Service
public class UserDetailsServiceImpl { /*implements UserDetailsService {

	private final static Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class); 
	
	@PersistenceContext(unitName = "PersistenceUnitA")
	private EntityManager entityManagerA;
	
	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		 // assuming that you have a User class that implements UserDetails
        User user1 = entityManagerA.createQuery("from User where username = :username", User.class)
                            .setParameter("username", username)
                            .getSingleResult();
        if(user1 != null) {
			logger.debug("user1 :"+user1 );
			logger.debug("user1.Password :"+user1.getPassword());
			// get the password of employee out
			String password = user1.getPassword();
			boolean enabled = true;
			boolean credentialsNonExpired = true;
			boolean accountNonLocked = true;
			boolean accountNonExpired = user1.isEnabled();
			
			Set<Role> roles = user1.getRoles();
			GrantedAuthority[] grantedAuthority = new GrantedAuthority[roles.size()];
			int i =0;
			for (Role role : roles) {
				String roleName = role.getRoleName();
				grantedAuthority[i++] = new GrantedAuthorityImpl(roleName);
			}
			
			LoggedInUser loggedInUser = new LoggedInUser(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, grantedAuthority);
			loggedInUser.setId(user1.getId());
			loggedInUser.setName(user1.getName());
			loggedInUser.setUserType(user1.getUserType());
			logger.debug("loggedInUser.getName() :"+loggedInUser.getName());
			return loggedInUser;
		} else {
			throw new UsernameNotFoundException(SystemConstant.ErrorCode.INVALID_USERNAME.name());
		}
	} catch (UsernameNotFoundException usernameNotFoundException) {
		usernameNotFoundException.printStackTrace();
		throw usernameNotFoundException;
	} catch (DataAccessException dataAccessException) {
		dataAccessException.printStackTrace();
		throw dataAccessException;
	}

	}*/
}



