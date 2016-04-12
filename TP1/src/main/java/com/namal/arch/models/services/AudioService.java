package com.namal.arch.models.services;

/**
 * Model for an audioservice (3rd lab). Class representing the audio service
 * and the needed information for the required functionalities
 * @author Fabien Berquez
 *
 */
public class AudioService {
	
	/**
	 * Id du service, as known by the server
	 */
	private String serviceId;
	/**
	 * Name of the service, as known by the server
	 */
	private String name;
	/**
	 * The URL of the login form of this service
	 */
	private String connectUrl;
	/**
	 * The URL of this service's logo
	 */
	private String imageUrl;
	/**
	 * Indicates if the search functionality is available for this service
	 */
	private boolean searchAvailable;
	/**
	 * Indicates if the user is connected to this service.
	 */
	private boolean isConnected;
	
	/**
	 * Constructor initialising a service
	 * @param serviceId : the id of the service as known by the server
	 * @param name : the name of the service as known by the server
	 * @param connectUrl : the URL of this service's login form
	 * @param imageUrl : the URL of this service's logo
	 * @param searchAvailable : indicates if the service allows search.
	 */
	public AudioService(String serviceId, String name, String connectUrl, String imageUrl, boolean searchAvailable) {
		this.serviceId = serviceId;
		this.name = name;
		this.connectUrl = connectUrl;
		this.imageUrl = imageUrl;
		this.searchAvailable = searchAvailable;
		this.isConnected = false;
	}
	
	/**
	 * Getter for the service id
	 * @return the service id
	 */
	public String getServiceId() {
		return serviceId;
	}
	
	/**
	 * Setter for the service id
	 * @param serviceId : the id of the service, as known by the server
	 */
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	
	/**
	 * Getter for the name of the service
	 * @return the name of the service
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Setter for the name of the service
	 * @param name : the name of the service, as known by the server
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Getter for the URL of the service's login form
	 * @return the URL of the service's login form
	 */
	public String getConnectUrl() {
		return connectUrl;
	}
	
	/**
	 * Setter for the URL of the service's login form
	 * @param connectUrl : the new URL of the service's login form
	 */
	public void setConnectUrl(String connectUrl) {
		this.connectUrl = connectUrl;
	}
	
	/**
	 * Getter for the URL of the service's logo
	 * @return the URL of the service logo
	 */
	public String getImageUrl() {
		return imageUrl;
	}
	
	/**
	 * Setter for the URL of the service's logo
	 * @param imageUrl : the new URL of the service's logo
	 */
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	/**
	 * Indicates if the service allows users to search for songs
	 * @return true if the service allows searching for songs, false otherwise
	 */
	public boolean isSearchAvailable() {
		return searchAvailable;
	}
	
	/**
	 * Setter for the service acceptance of search
	 * @param searchAvailable : the new value of the searchAvailable attribute
	 */
	public void setSearchAvailable(boolean searchAvailable) {
		this.searchAvailable = searchAvailable;
	}

	/**
	 * Indicates if the user is connected to this service
	 * @return true if the user is connected to the service, false otherwise.
	 */
	public boolean isConnected() {
		return isConnected;
	}

	/**
	 * Sets the current connection status of the user for this service.
	 * @param isConnected : the new value
	 */
	public void setConnected(boolean isConnected) {
		this.isConnected = isConnected;
	}
	
	
}
