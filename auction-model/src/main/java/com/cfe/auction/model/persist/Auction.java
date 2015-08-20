package com.cfe.auction.model.persist;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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

	private Integer auctionId;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LASTUPDATETIME")
	private Date lastUpdateTime;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATEDTIME")
	private Date createdTime;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "AUCTIONSTARTTIME")
	private Date auctionStartTime;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "AUCTIONENDTIME")
	private Date auctionEndTime;

	@Column(name = "STATUS")
	private String status;

	@Column(name = "NAME")
	private String name;

	@Column(name = "ISAPPROVED")
	private String isApproved;

	@Column(name = "BIDITEMGROUPID")
	private Long bidItemGroupId;

	/**
	 * @return the auctionId
	 */

	public Integer getAuctionId() {
		return id;
	}

	/**
	 * @param auctionId
	 *            the auctionId to set
	 */
	public void setAuctionId(Integer auctionId) {
		this.id = auctionId;
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
		return "Auction [auctionId=" + auctionId + "]";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	/*
	 * @Override public int hashCode() { final int prime = 31; int result = 1;
	 * result = prime * result + ((auctionId == null) ? 0 :
	 * auctionId.hashCode()); return result; }
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	/*
	 * @Override public boolean equals(Object obj) { if (this == obj) return
	 * true; if (obj == null) return false; if (getClass() != obj.getClass())
	 * return false; Auction other = (Auction) obj; if (auctionId == null) { if
	 * (other.auctionId != null) return false; } else if
	 * (!auctionId.equals(other.auctionId)) return false; return true; }
	 */
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

	@Override
	@Id
	@Column(name = "AUCTIONID")
	@GeneratedValue(strategy = IDENTITY)
	public Integer getId() {
		// TODO Auto-generated method stub
		return this.id;
	}

	@Override
	public void setId(Integer id) {
		// TODO Auto-generated method stub
		this.id = id;
	}

}
