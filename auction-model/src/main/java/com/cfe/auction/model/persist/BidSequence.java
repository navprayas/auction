package com.cfe.auction.model.persist;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
/*@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, region = "BidSequence" )*/
@Table(name="bidsequence")
public class BidSequence extends AbstractPO<Integer> {
	
	private static final long serialVersionUID = -600920698458513L;
	
	@ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
    @JoinColumn(name="auctionId", referencedColumnName = "auctionId")
	private Auction auction;
	
	@Column(name="bidItemId")
	private Long bidItemId;
	
	@Id
	@Column(name="SEQUENCEID")
	@GeneratedValue
	private Long sequenceId;
	
	@Column(name="BIDSPAN")
	private Long bidspan;

	/**
	 * @return the auction
	 */
	public Auction getAuction() {
		return auction;
	}

	/**
	 * @param auction the auction to set
	 */
	public void setAuction(Auction auction) {
		this.auction = auction;
	}

	/**
	 * @return the sequenceId
	 */
	public Long getSequenceId() {
		return sequenceId;
	}

	/**
	 * @param sequenceId the sequenceId to set
	 */
	public void setSequenceId(Long sequenceId) {
		this.sequenceId = sequenceId;
	}

	/**
	 * @return the bidspan
	 */
	public Long getBidspan() {
		return bidspan;
	}

	/**
	 * @param bidspan the bidspan to set
	 */
	public void setBidspan(Long bidspan) {
		this.bidspan = bidspan;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "BidSequence [auction="
				+ auction.getAuctionId()+ ", bidItem=" + bidItemId + ", sequenceId="
				+ sequenceId + ", bidspan=" + bidspan + "]";
	}
	
	public Long getBidItemId() {
		return bidItemId;
	}

	public void setBidItemId(Long bidItemId) {
		this.bidItemId = bidItemId;
	}

	@Override
	public Integer getId() {
		return null;
	}

	@Override
	public void setId(Integer id) {
	}
	
}