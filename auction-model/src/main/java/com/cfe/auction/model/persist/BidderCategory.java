package com.cfe.auction.model.persist;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "BidderCategory")
public class BidderCategory extends AbstractPO<Integer> {

	private static final long serialVersionUID = -6312688222365987584L;

	@Id
	private BidderCategoryId bidderCategoryId;

	public BidderCategoryId getBidderCategoryId() {
		return bidderCategoryId;
	}

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "AUCTIONID", referencedColumnName = "AUCTIONID")
	private Auction auction;

	public Auction getAuction() {
		return auction;
	}

	public void setAuction(Auction auction) {
		this.auction = auction;
	}

	@Override
	public String toString() {
		return "BidderCategory [auction=" + auction.getAuctionId() + "]";
	}

	public Integer getCategoryId() {
		return bidderCategoryId.getCategory().getId();
	}

	public String getUserName() {
		return bidderCategoryId.getUser().getUsername();
	}

	public void setBidderCategoryId(BidderCategoryId bidderCategoryId) {
		this.bidderCategoryId = bidderCategoryId;
	}

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}
}
