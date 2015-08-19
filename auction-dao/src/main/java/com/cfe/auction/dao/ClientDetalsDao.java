package com.cfe.auction.dao;

import com.cfe.auction.model.persist.ClientDetails;

public interface ClientDetalsDao extends DAO<Integer, ClientDetails> {
	public ClientDetails getClientByClientId(Integer clientId);
}
