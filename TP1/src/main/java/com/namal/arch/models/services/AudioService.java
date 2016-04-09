package com.namal.arch.models.services;

/**
 * Model for an audioservice (3rd lab)
 * @author Fabien Berquez
 *
 */
public class AudioService {
	
	private String serviceId;
	private String name;
	private String connectUrl;
	private String imageUrl;
	private boolean searchAvailable;
	private boolean isConnected;
	
	
	public AudioService(String serviceId, String name, String connectUrl, String imageUrl, boolean searchAvailable) {
		this.serviceId = serviceId;
		this.name = name;
		this.connectUrl = connectUrl;
		this.imageUrl = imageUrl;
		this.searchAvailable = searchAvailable;
		this.isConnected = false;
	}
	
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getConnectUrl() {
		return connectUrl;
	}
	public void setConnectUrl(String connectUrl) {
		this.connectUrl = connectUrl;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public boolean isSearchAvailable() {
		return searchAvailable;
	}
	public void setSearchAvailable(boolean searchAvailable) {
		this.searchAvailable = searchAvailable;
	}

	public boolean isConnected() {
		return isConnected;
	}

	public void setConnected(boolean isConnected) {
		this.isConnected = isConnected;
	}
	
	
}
