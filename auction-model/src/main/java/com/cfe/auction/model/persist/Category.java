package com.cfe.auction.model.persist;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
/* @Cache(usage = CacheConcurrencyStrategy.READ_ONLY, region = "Category" ) */
@Table(name = "CATEGORY")
public class Category extends AbstractPO<Integer> {

	private static final long serialVersionUID = -8525037069840827431L;

	@Column(name = "CATEGORYNAME")
	private String categoryName;

	public Category() {
		
	}

	public Category(int id) {
		this.id = id;
	}

	@Id
	@GeneratedValue
	@Column(name = "CATEGORYID")
	public Integer getId() {
		return this.id;
	}

	@Override
	public void setId(Integer id) {
		this.id=id;

	}

	/**
	 * @return the categoryName
	 */
	public String getCategoryName() {
		return categoryName;
	}

	/**
	 * @param categoryName
	 *            the categoryName to set
	 */
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Category [categoryId=" + getId() + ", categoryName="
				+ categoryName + "]";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */

}
