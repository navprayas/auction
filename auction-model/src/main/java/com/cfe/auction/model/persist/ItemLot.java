package com.cfe.auction.model.persist;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
/* @Cache(usage = CacheConcurrencyStrategy.READ_ONLY, region = "ItemLot" ) */
@Table(name = "itemlot")
public class ItemLot extends AbstractPO<Integer> {

	public ItemLot() {
		// TODO Auto-generated constructor stub
	}

	public ItemLot(int id) {
		// TODO Auto-generated constructor stub
		this.id = id;
	}

	@Column(name = "BIDITEMID")
	private Long bidItemId;

	@Column(name = "NAME")
	private String name;

	@Column(name = "LENGTHRANGE")
	private String lengthRange;

	@Column(name = "ACTUALLENGTH")
	private String actualLengh;

	@Column(name = "QUANTITY")
	private Integer quantity;

	@Column(name = "UNIT")
	private String unit;

	@Column(name = "CURRENCY")
	private String currency;

	@Column(name = "REMARK")
	private String remark;

	@Column(name = "ZONE")
	private String zone;

	@Column(name = "LOTNO")
	private String lotNo;

	public String getLotNo() {
		return lotNo;
	}

	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	/*@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "bidItemId", referencedColumnName = "bidItemId")
	private BidItem biditem;
*/
	@Id
	@Column(name = "LOTID")
	@GeneratedValue
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
	 * @return the bidItemId
	 */
	public Long getBidItemId() {
		return bidItemId;
	}

	/**
	 * @param bidItemId
	 *            the bidItemId to set
	 */
	public void setBidItemId(Long bidItemId) {
		this.bidItemId = bidItemId;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the lengthRange
	 */
	public String getLengthRange() {
		return lengthRange;
	}

	/**
	 * @param lengthRange
	 *            the lengthRange to set
	 */
	public void setLengthRange(String lengthRange) {
		this.lengthRange = lengthRange;
	}

	/**
	 * @return the actualLengh
	 */
	public String getActualLengh() {
		return actualLengh;
	}

	/**
	 * @param actualLengh
	 *            the actualLengh to set
	 */
	public void setActualLengh(String actualLengh) {
		this.actualLengh = actualLengh;
	}

	/**
	 * @return the quantity
	 */
	public Integer getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity
	 *            the quantity to set
	 */
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	/**
	 * @return the unit
	 */
	public String getUnit() {
		return unit;
	}

	/**
	 * @param unit
	 *            the unit to set
	 */
	public void setUnit(String unit) {
		this.unit = unit;
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
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark
	 *            the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * @return the biditem
	 */
	/*public BidItem getBiditem() {
		return biditem;
	}*/

	/**
	 * @param biditem
	 *            the biditem to set
	 */
	/*public void setBiditem(BidItem biditem) {
		this.biditem = biditem;
	}
*/
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ItemLot [lotId=" + getId() + ", name=" + name + "]";
	}

}
