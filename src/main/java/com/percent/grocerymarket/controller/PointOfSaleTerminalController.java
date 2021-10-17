/**
 * 
 */
package com.percent.grocerymarket.controller;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.percent.grocerymarket.exception.PosTerminalException;
import com.percent.grocerymarket.exception.ProductNotFoundException;
import com.percent.grocerymarket.model.Product;
import com.percent.grocerymarket.service.PointOfSaleTerminalService;
import com.percent.grocerymarket.utils.GroceryMarketUtils;


/**
 * This is a Rest controller class for Grocery Market point of sale terminal.
 * It contains methods to start the terminal, load the product inventory, scan product
 * and get total cost after scanning all the products.
 * 
 * @author abhiesingh
 *
 */
@RestController
@RequestMapping("/grocerymarket/pos-terminal")
public class PointOfSaleTerminalController {
	
	private static final Logger logger = LoggerFactory.getLogger(PointOfSaleTerminalController.class);

	//Service class for point of sale terminal
	@Autowired
	private PointOfSaleTerminalService pointOfSaleTerminalService;

	@Autowired
	private GroceryMarketUtils groceryMarketUtils;
	
	/**
	 * This method starts the point of sale terminal and loads the product inventory.
	 * It returns Terminal Started message on successful execution.
	 * Method logs exceptions and returns appropriate response in case of failure.
	 * 
	 * @return
	 * @throws PosTerminalException, Exception
	 */
	@PutMapping("/products")
	ResponseEntity<String> setPricing() {
		logger.info("setPricing :  START");
		ResponseEntity<String> response;
		try {
			final List<Product> products = groceryMarketUtils.getProducts();  // Get product Inventory
			pointOfSaleTerminalService.setPricing(products); // Initialize Pos Terminal with product and pricing
			response = ResponseEntity.status(HttpStatus.CREATED).body("Products loaded successfully.");
			logger.info("Number of products loaded in Terminal : " + products.size());
		} catch (final PosTerminalException e) {
			logger.error("Error starting the POS terminal", e);
			response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (final Exception e) {
			logger.error("Error starting the POS terminal", e); // Log the cause
			response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body("An unexpcted error occurred. Please contact support"); //Provide user friendly error message
		}
		logger.info("setPricing :  END");
		return response;
	}
	
	/**
	 * This method scans one product at a time and calls pointOfSaleTerminalService to calculate total price for each scanned product.
	 * It takes productCode as input and generate String response.
	 * 
	 * @param productCode
	 * @return
	 */
	@PutMapping("/scan/{productCode}")
	ResponseEntity<String> scanProduct(@PathVariable final String productCode) {
		logger.info("scanProduct :  START");
		ResponseEntity<String> response;
		try {
			pointOfSaleTerminalService.scan(productCode); // Call pointOfSaleTerminalService to scan product and calculate total
			response = ResponseEntity.status(HttpStatus.CREATED).body("Product " + productCode + " scanned");
		} catch (final ProductNotFoundException e) {
			logger.error(e.getMessage());
			response = ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product " + productCode + " not found");
		} catch (final PosTerminalException e) {
			logger.error(e.getMessage());
			response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (final Exception e) {
			logger.error("Error while scanning product ", e); // Log the cause
			response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unexcted error occurred while scanning the product.");
		}
		logger.info("scanProduct :  END");
		return response;
	}
	
	/**
	 * This method gets calls the pointOfSaleTerminalService to get the total price after all products are scanned.
	 * 
	 * @return
	 */
	@GetMapping("/total")
	ResponseEntity<BigDecimal> getTotal() {
		logger.info("getTotal :  START");
		ResponseEntity<BigDecimal> response = null;
		try {
			final BigDecimal total = pointOfSaleTerminalService.calculateTotal();
			response = ResponseEntity.status(HttpStatus.OK).body(total);
		} catch (final Exception e) {
			logger.error("Error while scanning product ", e); // Log the cause
			response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BigDecimal.ZERO);
		}
		logger.info("getTotal :  END");
		return response;
	}	
	
}
