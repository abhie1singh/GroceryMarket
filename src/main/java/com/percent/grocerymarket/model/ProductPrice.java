/**
 * 
 */
package com.percent.grocerymarket.model;

import java.math.BigDecimal;

/**
 * This class represents mapping between product unit and respective prices.
 * 
 * @author abhiesingh
 *
 */
public class ProductPrice {
	private Integer unit;
	private BigDecimal price;

	public ProductPrice() {
		super();
	}

	public ProductPrice(Integer unit, BigDecimal price) {
		super();
		this.unit = unit;
		this.price = price;
	}

	public Integer getUnit() {
		return unit;
	}

	public void setUnit(Integer unit) {
		this.unit = unit;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "ProductPrice [unit=" + unit + ", price=" + price + "]";
	}

}
