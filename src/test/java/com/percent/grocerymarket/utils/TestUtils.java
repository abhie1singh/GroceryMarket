/**
 * 
 */
package com.percent.grocerymarket.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.percent.grocerymarket.model.Product;
import com.percent.grocerymarket.model.ProductPrice;

/**
 * @author abhiesingh
 *
 */
public class TestUtils {
	
	/**
	 * This method returns the list of all the products needed to start the Pos Terminal.
	 * 
	 * @return
	 */
	public static List<Product> getProducts(){
		final List<Product> products = new ArrayList<>();
		List<ProductPrice> productPrices = new ArrayList<>();
		
		//Add product A
		ProductPrice productPrice = new ProductPrice(1, BigDecimal.valueOf(1.25));
		productPrices.add(productPrice);
		
		productPrice = new ProductPrice(3, BigDecimal.valueOf(3.00));
		productPrices.add(productPrice);
		
		Product product = new Product("A", productPrices);
		products.add(product);
		
		productPrices = new ArrayList<>();
		
		productPrice = new ProductPrice(1, BigDecimal.valueOf(4.25));
		productPrices.add(productPrice);
		
		//Add product B
		product = new Product("B", productPrices);
		products.add(product);
		
		productPrices = new ArrayList<>();
		
		productPrice = new ProductPrice(1, BigDecimal.valueOf(1.00));
		productPrices.add(productPrice);
		
		productPrice = new ProductPrice(6, BigDecimal.valueOf(5.00));
		productPrices.add(productPrice);
		
		//Add Product C
		product = new Product("C", productPrices);
		products.add(product);
		
		productPrices = new ArrayList<>();
		
		productPrice = new ProductPrice(1, BigDecimal.valueOf(0.75));
		productPrices.add(productPrice);
		
		//Add product D
		product = new Product("D", productPrices);
		products.add(product);
		
		return products;
	}

}
