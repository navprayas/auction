package com.cfe.auction.dao.model.persist;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "AUTOBIDS")
public class AutoBids extends AbstractPO<Integer> {
	public AutoBids() {
		// TODO Auto-generated constructor stub
	}

	public AutoBids(int id) {
		this.id = id;
	}

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "AUCTIONID", referencedColumnName = "AUCTIONID")
	private Auction auction;

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "bidItemId", referencedColumnName = "bidItemId")
	private BidItem bidItemId;

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
	public Integer getId() {
		// TODO Auto-generated method stub
		return this.id;
	}

	
	public void setId(Integer id) {
		this.id = id;

	}

	/**
	 * @return the auction
	 */
	public Auction getAuction() {
		return auction;
	}

	/**
	 * @param auctionId
	 *            the auction to set
	 */
	public void setAuction(Auction auction) {
		this.auction = auction;
	}

	/**
	 * @return the lotId
	 */
	public BidItem getBidItemId() {
		return bidItemId;
	}

	/**
	 * @param lotId
	 *            the lotId to set
	 */
	public void setBidItemId(BidItem bidItemId) {
		this.bidItemId = bidItemId;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AutoBids [autoBidId=" + getId() + "]";
	}

}
