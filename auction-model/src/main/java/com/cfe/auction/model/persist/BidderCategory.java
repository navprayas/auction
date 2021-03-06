package com.cfe.auction.model.persist;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "BidderCategory")
public class BidderCategory extends AbstractPO<Integer> {

	public BidderCategory() {
		// TODO Auto-generated constructor stub
	}

	public BidderCategory(int id) {
		// TODO Auto-generated constructor stub
		this.id = id;
	}

	private Integer categoryId;
	private Integer userId;
	private Integer auctionId;

	/*private Auction auction;

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "AUCTIONID", referencedColumnName = "AUCTIONID")
	public Auction getAuction() {
		return auction;
	}

	public void setAuction(Auction auction) {
		this.auction = auction;
	}*/

	
	
	
	/*@Override
	public String toString() {
		return "BidderCategory [auction=" + auction.getAuctionId() + "]";
	}
*/
	public Integer getAuctionId() {
		return auctionId;
	}

	public void setAuctionId(Integer auctionId) {
		this.auctionId = auctionId;
	}

	@Column(name = "categoryid")
	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	@Column(name = "userid")
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@Override
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}
}
