/**
 * 
 */
package com.percent.grocerymarket.exception;

/**
 * Exception class for Pos Terminal related failure.
 * 
 * @author abhiesingh
 *
 */
public class PosTerminalException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PosTerminalException(final String message) {
		super(message);
	}
}
