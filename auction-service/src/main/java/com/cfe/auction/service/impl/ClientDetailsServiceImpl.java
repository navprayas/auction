package com.cfe.auction.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cfe.auction.dao.ClientDetalsDao;
import com.cfe.auction.model.persist.ClientDetails;
import com.cfe.auction.service.ClientDetailsService;

@Service
@Transactional
public class ClientDetailsServiceImpl extends
		CRUDServiceImpl<Integer, ClientDetails, ClientDetalsDao> implements
		ClientDetailsService {
	@Autowired
	private ClientDetalsDao clientDetalsDao;

	@Autowired
	public ClientDetailsServiceImpl(ClientDetalsDao dao) {
		super(dao);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ClientDetails getClientByClintId(Integer clientId) {
		
		return clientDetalsDao.getClientByClientId(clientId);
	}

	
	
	
	
}
