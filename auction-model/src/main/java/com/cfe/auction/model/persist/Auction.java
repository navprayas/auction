package com.cfe.auction.model.persist;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "AUCTION")
public class Auction extends AbstractPO<Integer> {

	public Auction() {
		// TODO Auto-generated constructor stub
	}

	public Auction(int id) {
		// TODO Auto-generated constructor stub
		this.id = id;
	}

	@Column(name = "LASTUPDATETIME")
	private Date lastUpdateTime;

	@Column(name = "CREATEDTIME")
	private Date createdTime;

	@Column(name = "AUCTIONSTARTTIME")
	private Date auctionStartTime;

	@Column(name = "AUCTIONENDTIME")
	private Date auctionEndTime;

	@Column(name = "STATUS")
	private String status;

	@Column(name = "NAME")
	private String name;

	@Column(name = "ISAPPROVED")
	private String isApproved;

	@Column(name = "biditemgroupid")
	private Long bidItemGroupId;

	@Id
	@Column(name = "AUCTIONID")
	public Integer getId() {
		// TODO Auto-generated method stub
		return this.id;
	}

	@Override
	public void setId(Integer id) {
		// TODO Auto-generated method stub
		this.id = id;
	}

	/**
	 * @return the lastUpdateTime
	 */
	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	/**
	 * @param lastUpdateTime
	 *            the lastUpdateTime to set
	 */
	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	/**
	 * @return the createdTime
	 */
	public Date getCreatedTime() {
		return createdTime;
	}

	/**
	 * @param createdTime
	 *            the createdTime to set
	 */
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIsApproved() {
		return isApproved;
	}

	public void setIsApproved(String isApproved) {
		this.isApproved = isApproved;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Auction [auctionId=" + getId() + "]";
	}

	public Date getAuctionStartTime() {
		return auctionStartTime;
	}

	public void setAuctionStartTime(Date auctionStartTime) {
		this.auctionStartTime = auctionStartTime;
	}

	public Date getAuctionEndTime() {
		return auctionEndTime;
	}

	public void setAuctionEndTime(Date auctionEndTime) {
		this.auctionEndTime = auctionEndTime;
	}

	public Long getBidItemGroupId() {
		return bidItemGroupId;
	}

	public void setBidItemGroupId(Long bidItemGroupId) {
		this.bidItemGroupId = bidItemGroupId;
	}

}
