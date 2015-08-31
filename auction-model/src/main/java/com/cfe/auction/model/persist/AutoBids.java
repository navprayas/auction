package com.cfe.auction.model.persist;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "AUTOBIDS")
public class AutoBids extends AbstractPO<Integer> {

	@Column(name = "auctionId")
	private Integer auctionId;
	
	@Column(name="bidItemId")
	private Long bidItemId;

	@Column(name = "BIDAMOUNT")
	private Double bidAmount;

	@Column(name = "CURRENCY")
	private String currency;

	@Column(name = "BIDTIME")
	private Date bidTime;
	@Column(name = "BIDSTATUS")
	private String bidStatus;

	@Column(name = "BIDDERNAME")
	private String bidderName;

	@Column(name = "COMMENTS")
	private String comments;

	@Id
	@Column(name = "AUTOBIDID")
	@GeneratedValue
	private Long autoBidId;
	
	public Long getAutoBidId() {
		return autoBidId;
	}

	public void setId(Long autoBidId) {
		this.autoBidId = autoBidId;
	}

	/**
	 * @return the bidAmount
	 */
	public Double getBidAmount() {
		return bidAmount;
	}

	/**
	 * @param bidAmount
	 *            the bidAmount to set
	 */
	public void setBidAmount(Double bidAmount) {
		this.bidAmount = bidAmount;
	}

	/**
	 * @return the currency
	 */
	public String getCurrency() {
		return currency;
	}

	/**
	 * @param currency
	 *            the currency to set
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}

	/**
	 * @return the bidTime
	 */
	public Date getBidTime() {
		return bidTime;
	}

	/**
	 * @param bidTime
	 *            the bidTime to set
	 */
	public void setBidTime(Date bidTime) {
		this.bidTime = bidTime;
	}

	/**
	 * @return the bidStatus
	 */
	public String getBidStatus() {
		return bidStatus;
	}

	/**
	 * @param bidStatus
	 *            the bidStatus to set
	 */
	public void setBidStatus(String bidStatus) {
		this.bidStatus = bidStatus;
	}

	/**
	 * @return the bidderName
	 */
	public String getBidderName() {
		return bidderName;
	}

	/**
	 * @param bidderName
	 *            the bidderName to set
	 */
	public void setBidderName(String bidderName) {
		this.bidderName = bidderName;
	}

	/**
	 * @return the comments
	 */
	public String getComments() {
		return comments;
	}

	/**
	 * @param comments
	 *            the comments to set
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}

	@Override
	public String toString() {
		return "AutoBids [autoBidId=" + getId() + "]";
	}

	public Integer getAuctionId() {
		return auctionId;
	}

	public void setAuctionId(Integer auctionId) {
		this.auctionId = auctionId;
	}

	public Long getBidItemId() {
		return bidItemId;
	}

	public void setBidItemId(Long bidItemId) {
		this.bidItemId = bidItemId;
	}
	
}
