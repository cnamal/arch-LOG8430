package com.namal.arch.models;

import javafx.scene.image.Image;

public abstract class ProviderInformation {

	private String name;
	private Image logo;
	
	public ProviderInformation(String name,Image logo){
		this.name=name;
		this.logo=logo;
	}
	
	public String getName(){
		return name;
	}
	
	public Image getLogo(){
		return logo;
	}
	
}
