package com.cfe.auction.service.security.impl;

import java.io.Serializable;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class LoggedInUser extends User implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 12399967892L;
	
	/**
     * Construct the User with the details required.
     *
     * @param username the username presented to the
     *        AuthenticationProvider.
     * @param password the password that should be presented to the
     *        AuthenticationProvider.
     * @param enabled set to true if the user is enabled
     * @param accountNonExpired set to true if the account has not
     *        expired
     * @param credentialsNonExpired set to true if the credentials
     *        have not expired
     * @param accountNonLocked set to true if the account is not
     *        locked
     * @param authorities the authorities that should be granted to the caller
     *        if they presented the correct username and password and the user
     *        is enabled
     *
     * @throws IllegalArgumentException if a null value was passed
     *         either as a parameter or as an element in the
     *         GrantedAuthority[] array
     */
	public LoggedInUser(String username, String password, boolean enabled,
			boolean accountNonExpired, boolean credentialsNonExpired,
			boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities)
			throws IllegalArgumentException {
		super(username, password, enabled, accountNonExpired,
				credentialsNonExpired, accountNonLocked, authorities);
	}


	/**
	 * used to store user id.
	 */
	private Long id;
	
	/**
	 * used to store user name.
	 */
	private String name;
	
	/**
	 * used to store user name.
	 */
	private String userType;
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param Name the Name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @param userType the userType to set
	 */
	public void setUserType(String userType) {
		this.userType = userType;
	}

	/**
	 * @return the userType
	 */
	public String getUserType() {
		return userType;
	}

}