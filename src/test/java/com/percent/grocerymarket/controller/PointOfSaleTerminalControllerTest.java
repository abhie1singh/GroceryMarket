/**
 * 
 */
package com.percent.grocerymarket.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.percent.grocerymarket.exception.PosTerminalException;
import com.percent.grocerymarket.exception.ProductNotFoundException;
import com.percent.grocerymarket.model.Product;
import com.percent.grocerymarket.service.PointOfSaleTerminalService;
import com.percent.grocerymarket.utils.GroceryMarketUtils;
import com.percent.grocerymarket.utils.TestUtils;

/**
 * 
 * Test class for PointOfSaleTerminalController
 * @author abhiesingh
 *
 */


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
class PointOfSaleTerminalControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private PointOfSaleTerminalService service;
	
	@MockBean
	private GroceryMarketUtils utils;
	
	
	static List<Product> products = null;
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		products = TestUtils.getProducts();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterAll
	static void tearDownAfterClass() throws Exception {
		products = null;
	}

	

	/**
	 * Test method for {@link com.percent.grocerymarket.controller.PointOfSaleTerminalController#startTerminal()}.
	 */
	@Test
	void testStartTerminal() throws Exception{
		doReturn(products).when(utils).getProducts();
		doNothing().when(service).setPricing(products);
		MvcResult result =  mockMvc.perform(put("/grocerymarket/pos-terminal/products"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
		
		
		final String expectedResponse = "Products loaded successfully.";
		final String actualResponse = result.getResponse().getContentAsString();
		assertEquals(expectedResponse, actualResponse);
		Mockito.verify(utils).getProducts();
		Mockito.verify(service).setPricing(products);
		
		
	}
	
	/**
	 * Test method for {@link com.percent.grocerymarket.controller.PointOfSaleTerminalController#startTerminal()}.
	 */
	@Test
	void testStartTerminal_products_is_null() throws Exception{
		
		doReturn(null).when(utils).getProducts();
		doThrow(PosTerminalException.class).when(service).setPricing(null);
		
		MvcResult result = mockMvc.perform(put("/grocerymarket/pos-terminal/products"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
		
		final String expectedResponse = "";
		final String actualResponse = result.getResponse().getContentAsString();
		assertEquals(expectedResponse, actualResponse);

		Mockito.verify(utils).getProducts();
		Mockito.verify(service).setPricing(null);
		
	}
	
	/**
	 * Test method for {@link com.percent.grocerymarket.controller.PointOfSaleTerminalController#startTerminal()}.
	 */
	@Test
	void testStartTerminal_unexpcted_error() throws Exception{
		
		doReturn(null).when(utils).getProducts();
		doThrow(RuntimeException.class).when(service).setPricing(null);
		
		MvcResult result = mockMvc.perform(put("/grocerymarket/pos-terminal/products"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
		
		final String expectedResponse = "An unexpcted error occurred. Please contact support";
		final String actualResponse = result.getResponse().getContentAsString();
		assertEquals(expectedResponse, actualResponse);

		Mockito.verify(utils).getProducts();
		Mockito.verify(service).setPricing(null);
		
	}


	/**
	 * Test method for {@link com.percent.grocerymarket.controller.PointOfSaleTerminalController#scanProduct(java.lang.String)}.
	 */
	@Test
	void testScanProduct() throws Exception{
		final String productCode = "A";
		doNothing().when(service).scan(productCode);
		MvcResult result =  mockMvc.perform(put("/grocerymarket/pos-terminal/scan/" + productCode))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
		
		
		final String expectedResponse = "Product " + productCode + " scanned";
		final String actualResponse = result.getResponse().getContentAsString();
		assertEquals(expectedResponse, actualResponse);
		Mockito.verify(service).scan(productCode);
	}
	
	/**
	 * Test method for {@link com.percent.grocerymarket.controller.PointOfSaleTerminalController#scanProduct(java.lang.String)}.
	 */
	@Test
	void testScanProduct_product_not_found() throws Exception{
		final String productCode = "Z";
		doThrow(ProductNotFoundException.class).when(service).scan(productCode);
		MvcResult result =  mockMvc.perform(put("/grocerymarket/pos-terminal/scan/" + productCode))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
		
		
		final String expectedResponse = "Product " + productCode + " not found";
		final String actualResponse = result.getResponse().getContentAsString();
		assertEquals(expectedResponse, actualResponse);
		Mockito.verify(service).scan(productCode);
	}
	
	/**
	 * Test method for {@link com.percent.grocerymarket.controller.PointOfSaleTerminalController#scanProduct(java.lang.String)}.
	 */
	@Test
	void testScanProduct_pos_terminal_not_ready() throws Exception{
		final String productCode = "Z";
		doThrow(PosTerminalException.class).when(service).scan(productCode);
		MvcResult result =  mockMvc.perform(put("/grocerymarket/pos-terminal/scan/" + productCode))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
		
		
		final String expectedResponse = "";
		final String actualResponse = result.getResponse().getContentAsString();
		assertEquals(expectedResponse, actualResponse);
		Mockito.verify(service).scan(productCode);
	}
	
	/**
	 * Test method for {@link com.percent.grocerymarket.controller.PointOfSaleTerminalController#scanProduct(java.lang.String)}.
	 */
	@Test
	void testScanProduct_pos_terminal_unexpcted_error() throws Exception{
		final String productCode = "A";
		doThrow(RuntimeException.class).when(service).scan(productCode);
		MvcResult result =  mockMvc.perform(put("/grocerymarket/pos-terminal/scan/" + productCode))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
		
		
		final String expectedResponse = "Unexcted error occurred while scanning the product.";
		final String actualResponse = result.getResponse().getContentAsString();
		assertEquals(expectedResponse, actualResponse);
		Mockito.verify(service).scan(productCode);
	}

	/**
	 * Test method for {@link com.percent.grocerymarket.controller.PointOfSaleTerminalController#getTotal()}.
	 * @throws Exception 
	 */
	@Test
	void testGetTotal() throws Exception {
	
		doReturn(BigDecimal.ZERO).when(service).calculateTotal();
		MvcResult result =  mockMvc.perform(get("/grocerymarket/pos-terminal/total"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
		
		
		final BigDecimal expectedResponse = BigDecimal.ZERO;
		final BigDecimal actualResponse = BigDecimal.valueOf(Double.valueOf(result.getResponse().getContentAsString()));
		assertTrue(expectedResponse.compareTo(actualResponse) == 0);
		Mockito.verify(service).calculateTotal();
	}
	
	@Test
	void testGetTotal_unexpected_error() throws Exception {
	
		doThrow(RuntimeException.class).when(service).calculateTotal();
		MvcResult result =  mockMvc.perform(get("/grocerymarket/pos-terminal/total"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
		
		
		final BigDecimal expectedResponse = BigDecimal.ZERO;
		final BigDecimal actualResponse = BigDecimal.valueOf(Double.valueOf(result.getResponse().getContentAsString()));
		assertTrue(expectedResponse.compareTo(actualResponse) == 0);
		Mockito.verify(service).calculateTotal();
	}

}
