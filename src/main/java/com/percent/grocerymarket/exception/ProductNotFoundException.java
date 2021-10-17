/**
 * 
 */
package com.percent.grocerymarket.exception;

/**
 * 
 * Exception class for Product not found scenario.
 * 
 * @author abhiesingh
 *
 */
public class ProductNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ProductNotFoundException(final String productCode) {
		super("Could not find the Product : " + productCode);
	}
}
