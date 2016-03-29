package com.namal.arch.models.services.cp;

import com.namal.arch.models.ProviderInformation;
import com.namal.arch.models.services.IAuthentification;
import com.namal.arch.utils.MongoDB;

class CrossPlatformAuthentication implements IAuthentification {

	private CrossPlatform service;
	private static CrossPlatformAuthentication instance;
	
	
	private CrossPlatformAuthentication (CrossPlatform service){
		this.service=service;
	}
	
	static CrossPlatformAuthentication getInstance(CrossPlatform service){
		if(instance==null)
			instance = new CrossPlatformAuthentication(service);
		return instance;
	}

	@Override
	public ProviderInformation getProviderInformation() {
		return service.getProviderInformation();
	}

	@Override
	public String getAuthentificationUrl() {
		return null;
	}

	@Override
	public void disconnect() {

	}

	@Override
	public String testString() {
		return null;
	}

	@Override
	public boolean serverResponse(String response) {
		return false;
	}

	@Override
	public boolean isConnected() {
		return MongoDB.isConnected();
	}
	
}
