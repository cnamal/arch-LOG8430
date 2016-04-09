package com.namal.arch.models.services;

/**
 * Interface for authentication
 * @author namalgac
 *
 */
public interface IAuthentification {

	/**
	 * 
	 * @return url for connection
	 */
	String getAuthentificationUrl();
	
	/**
	 * 
	 * @param response server's response
	 * @return true if authentication succeeded, false otherwise
	 */
	String serverResponse(String response);

}
