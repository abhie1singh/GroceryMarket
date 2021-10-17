package com.percent.grocerymarket.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.percent.grocerymarket.exception.PosTerminalException;
import com.percent.grocerymarket.exception.ProductNotFoundException;
import com.percent.grocerymarket.model.Product;
import com.percent.grocerymarket.utils.TestUtils;

/**
 * Test class for PointOfSaleTerminalService
 * 
 * @author abhiesingh
 *
 */
class PointOfSaleTerminalServiceTest {

	static List<Product> products = null;
	
	static PointOfSaleTerminalService service;
	
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		products = TestUtils.getProducts();
		service = new PointOfSaleTerminalService();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterAll
	static void tearDownAfterClass() throws Exception {
		products = null;
		service = null;
	}
	
	
	@Test
	void testSetPricing() {
		final List<Product> expectedResponse = products;
		service.setPricing(products);
		final List<Product> actualResponse = service.getProducts();
		assertNotNull(actualResponse);
		assertEquals(expectedResponse.size(), actualResponse.size());
		assertEquals(expectedResponse.get(0), actualResponse.get(0));
		
	}
	
	@Test
	void testSetPricing_null_products() {
		
		Exception exception = assertThrows(PosTerminalException.class, () -> {
			service.setPricing(null);
	    });

	    String expectedMessage = "Product list is empty. Can not start POS Terminal";
	    String actualMessage = exception.getMessage();

	    assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testScan_one_product(){
		service.setPricing(products);
		service.scan("A");
		final BigDecimal expectedOutput = BigDecimal.valueOf(1.25);
		final BigDecimal actualOutput = service.calculateTotal();
		assertNotNull(expectedOutput);
		assertEquals(expectedOutput, actualOutput);
	}
	
	@Test
	void testScan_volume_product(){
		service.setPricing(products);
		service.scan("A");
		service.scan("A");
		service.scan("A");
		final BigDecimal expectedOutput = BigDecimal.valueOf(3);
		final BigDecimal actualOutput = service.calculateTotal();
		assertNotNull(expectedOutput);
		assertTrue(expectedOutput.compareTo(actualOutput) == 0);
	}
	
	@Test
	void testScan_product_not_found() {
		final String productCode = "T";
		service.setPricing(products);
		Exception exception = assertThrows(ProductNotFoundException.class, () -> {
			service.scan(productCode);
	    });

	    String expectedMessage = "Could not find the Product : " + productCode;
	    String actualMessage = exception.getMessage();

	    assertTrue(actualMessage.contains(expectedMessage));
	}

	
	@Test
	void testCalculateTotal() {
		
		service.setPricing(products);
		
		
		String items = "ABCDABA";
		for(final Character ch : items.toCharArray()) {
			service.scan(String.valueOf(ch));
		}
		
		BigDecimal expectedOutput = BigDecimal.valueOf(13.25);
		BigDecimal actualOutput = service.calculateTotal();
		assertTrue(expectedOutput.compareTo(actualOutput) == 0);
		
		
		
		items = "CCCCCCC";
		for(final Character ch : items.toCharArray()) {
			service.scan(String.valueOf(ch));
		}
		
		expectedOutput = BigDecimal.valueOf(6.0);
		actualOutput = service.calculateTotal();
		assertTrue(expectedOutput.compareTo(actualOutput) == 0);
		
		items = "ABCD";
		for(final Character ch : items.toCharArray()) {
			service.scan(String.valueOf(ch));
		}
		
		expectedOutput = BigDecimal.valueOf(7.25);
		actualOutput = service.calculateTotal();
		assertTrue(expectedOutput.compareTo(actualOutput) == 0);
	
		
		items = "ABCDABACCCCCCCABCD";
		for(final Character ch : items.toCharArray()) {
			service.scan(String.valueOf(ch));
		}
		
		expectedOutput = BigDecimal.valueOf(24.75);
		actualOutput = service.calculateTotal();
		assertTrue(expectedOutput.compareTo(actualOutput) == 0);
		
	}

}
