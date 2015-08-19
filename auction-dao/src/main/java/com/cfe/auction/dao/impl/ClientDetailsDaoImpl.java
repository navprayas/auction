package com.cfe.auction.dao.impl;

import javax.persistence.Query;

import org.springframework.stereotype.Service;

import com.cfe.auction.dao.ClientDetalsDao;
import com.cfe.auction.model.persist.ClientDetails;

@Service
public class ClientDetailsDaoImpl extends DAOImpl<Integer, ClientDetails>
		implements ClientDetalsDao {

	@Override
	public ClientDetails getClientByClientId(Integer clientId) {
		
			Query query = getMasterEntityManager().createQuery("from ClientDetails where id = :clientId");
			query.setParameter("clientId", clientId);
			return (ClientDetails)query.getSingleResult();
		}
	

}
