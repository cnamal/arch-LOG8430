package com.namal.arch.models;

import javafx.scene.image.Image;

public abstract class ProviderInformation {

	private String name;
	private String logoUrl;
	
	public ProviderInformation(String name,String logo){
		this.name=name;
		this.logoUrl=logo;
	}
	
	/**
	 * 
	 * @return Name of the AudioService
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
