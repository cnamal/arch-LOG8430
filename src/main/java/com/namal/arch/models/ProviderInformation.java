package com.namal.arch.models;

import javafx.scene.image.Image;

public abstract class ProviderInformation {

	private String name;
	private Image logo;
	
	public ProviderInformation(String name,Image logo){
		this.name=name;
		this.logo=logo;
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
	public Image getLogo(){
		return logo;
	}
	
}
