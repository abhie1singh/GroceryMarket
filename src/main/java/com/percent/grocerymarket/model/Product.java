/**
 * 
 */
package com.percent.grocerymarket.model;

import java.util.List;

/**
 * Product model class
 * 
 * @author abhiesingh
 *
 */
public class Product {

	private String productCode;
	private List<ProductPrice> productPrices;

	public Product() {
		super();
	}

	public Product(String productCode, List<ProductPrice> productPrices) {
		super();
		this.productCode = productCode;
		this.productPrices = productPrices;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public List<ProductPrice> getProductPrices() {
		return productPrices;
	}

	public void setProductPrices(List<ProductPrice> productPrices) {
		this.productPrices = productPrices;
	}

	@Override
	public String toString() {
		return "Product [productCode=" + productCode + ", productPrices=" + productPrices + "]";
	}

}
