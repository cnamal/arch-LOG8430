package com.namal.arch.models;

/**
 * Basic information provider
 * @author namalgac
 *
 */
public abstract class ProviderInformation {

	private String name;
	private String logoUrl;
	
	/**
	 * Creates a new ProviderInformation
	 * @param name name of the provider
	 * @param logo URL to the logo of the provider
	 */
	public ProviderInformation(String name,String logo){
		this.name=name;
		this.logoUrl=logo;
	}
	
	/**
	 * 
	 * @return name name of the AudioService
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * 
	 * @return logo of the AudioService
	 */
	public String getLogoUrl(){
		return logoUrl;
	}
	
	public String toString(){
		return "Name : " + name;
	}
}
