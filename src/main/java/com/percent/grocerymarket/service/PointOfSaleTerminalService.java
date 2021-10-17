/**
 * 
 */
package com.percent.grocerymarket.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.percent.grocerymarket.controller.PointOfSaleTerminalController;
import com.percent.grocerymarket.exception.PosTerminalException;
import com.percent.grocerymarket.exception.ProductNotFoundException;
import com.percent.grocerymarket.model.Product;
import com.percent.grocerymarket.model.ProductPrice;

/**
 * This is a service class for Pos Terminal.
 * It contains methods to start the Pos Terminal, Scan product and Calculate total price of scanned products;
 * 
 * @author abhiesingh
 *
 */
@Service
public class PointOfSaleTerminalService {
	private static final Logger logger = LoggerFactory.getLogger(PointOfSaleTerminalService.class);

	//List of all the products in the Pos Terminal
	private List<Product> products;
	//Total price of all the scanned products
	private BigDecimal total = BigDecimal.ZERO;
	//Map to maintain count of scanned products
	private Map<String, Integer> productCount = new HashMap<>();

	/**
	 * This method loads the products and starts the Pos Terminal
	 * 
	 * @param products
	 * @throws PosTerminalException
	 */
	public void setPricing(final List<Product> products) throws PosTerminalException{
		logger.info("setPricing :  START");
		if(null == products || products.size() == 0) {
			logger.error("Product list is empty");
			throw new PosTerminalException("Product list is empty. Can not start POS Terminal");
		}
		this.products = products;
		this.total = BigDecimal.ZERO;
		logger.info("setPricing :  END");
	}
	
	public List<Product> getProducts() {
		return this.products;
	}


	/**
	 * This method calculates the total price for each scanned product.
	 * It uses map to keep the count of each scanned products and applies volume pricing.
	 * 
	 * @param productCode
	 * @throws ProductNotFoundException
	 * @throws PosTerminalException
	 */
	public void scan(final String productCode) throws ProductNotFoundException, PosTerminalException{
		
		//Check if the product inventory is available to process
		if(null == products || products.size() == 0) {
			throw new PosTerminalException("There are no products in the terminal. Initialize terminal");
		}

		//Get the product for given product code or return null if product is not found
		final Product product = products.stream().filter(p -> p.getProductCode().equals(productCode)).findAny()
				.orElse(null);
		//If product is not found, throw exception
		if (null == product) {
			throw new ProductNotFoundException(productCode);
		}
		//This map maintains the count of each scanned products
		productCount.put(productCode, productCount.getOrDefault(productCode, 0) + 1);

		//Get unit price and volume price for scanned product	
		final List<ProductPrice> productPrices = product.getProductPrices();
		final ProductPrice unitPrice = productPrices.stream().filter(pp -> pp.getUnit() == 1).findAny().orElse(null);
		final ProductPrice volumePrice = productPrices.stream().filter(pp -> pp.getUnit() != 1).findAny().orElse(null);

		//Apply volume price if the scanned product has reached volume pricing limit
		if (null != volumePrice && productCount.get(productCode) == volumePrice.getUnit()) {
			/*
			 * Substract the total price scanned before applying volume pricing
			 * E.g. If A is scanned for the 3rd time, it becomes eligible for volume pricing. 
			 * In this case substract the amount by the multiple of volume price - 1
			*/
			total = total
					.subtract(unitPrice.getPrice().multiply(BigDecimal.valueOf(productCount.get(productCode) - 1)));
			//Add volume price to the total
			total = total.add(volumePrice.getPrice());
			//Remove the product code from map to start fresh count for volume pricing
			productCount.remove(productCode);
		} else {
			//Add unit price to the total 
			total = total.add(unitPrice.getPrice());
		}

	}

	/**
	 * This method returns the total price of all scanned products and resets the total to Zero for next scan operations
	 * 
	 * @return
	 */
	public BigDecimal calculateTotal() {
		final BigDecimal total = this.total;
		this.total = BigDecimal.ZERO;
		return total;
	}

}
