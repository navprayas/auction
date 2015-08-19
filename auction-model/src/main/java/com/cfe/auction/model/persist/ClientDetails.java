package com.cfe.auction.model.persist;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "client_details")
public class ClientDetails extends AbstractPO<Integer> {

	private String clientName;
	private String clientSchemaName;
	private String clientDescription;
	private String active;
	private String lastUpdatedOn;
	private String createdOn;
	private String schemaKey;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "clientId")
	public Integer getId() {
		return this.id;
	}

	@Override
	public void setId(Integer id) {
		// TODO Auto-generated method stub
		this.id = id;
	}

	@Column(name = "clientName")
	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	@Column(name = "clientSchemaName")
	public String getClientSchemaName() {
		return clientSchemaName;
	}

	public void setClientSchemaName(String clientSchemaName) {
		this.clientSchemaName = clientSchemaName;
	}

	@Column(name = "clientDescription")
	public String getClientDescription() {
		return clientDescription;
	}

	public void setClientDescription(String clientDescription) {
		this.clientDescription = clientDescription;
	}

	@Column(name = "active")
	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	@Column(name = "lastUpdatedOn")
	public String getLastUpdatedOn() {
		return lastUpdatedOn;
	}

	public void setLastUpdatedOn(String lastUpdatedOn) {
		this.lastUpdatedOn = lastUpdatedOn;
	}

	@Column(name = "createdOn")
	public String getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

	@Column(name = "schemaKey")
	public String getSchemaKey() {
		return schemaKey;
	}

	public void setSchemaKey(String schemaKey) {
		this.schemaKey = schemaKey;
	}

}
