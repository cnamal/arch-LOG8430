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
	 * @return string to test in order to stop the authentication workflow
	 */
	public String testString();
	
	/**
	 * 
	 * @param response server's response
	 * @return true if authentication succeeded, false otherwise
	 */
	public String serverResponse(String response);
	
	/**
	 * 
	 * @return Provider information
	 */
	public ProviderInformation getProviderInformation();
	
	/**
	 * 
	 * @return true if the user is connected, false otherwise
	 */
	public boolean isConnected();
	
	/**
	 * Disconnects the user
	 */
	public void disconnect();
}
