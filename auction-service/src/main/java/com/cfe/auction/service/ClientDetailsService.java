package com.cfe.auction.service;

import com.cfe.auction.model.persist.ClientDetails;

public interface ClientDetailsService extends
		CRUDService<Integer, ClientDetails> {

	public ClientDetails getClientByClintId(Integer clientId);

}
