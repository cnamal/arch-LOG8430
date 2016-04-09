package com.namal.arch.models.services;

import com.namal.arch.models.ProviderInformation;

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
	public String getAuthentificationUrl();
	
	/**
	 * 
	 * @param response server's response
	 * @return true if authentication succeeded, false otherwise
	 */
	public String serverResponse(String response);

}
