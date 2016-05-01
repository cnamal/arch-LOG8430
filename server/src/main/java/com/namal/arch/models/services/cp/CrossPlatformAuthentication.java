package com.namal.arch.models.services.cp;

import com.namal.arch.models.services.IAuthentification;

class CrossPlatformAuthentication implements IAuthentification {

	private static CrossPlatformAuthentication instance;
	
	
	private CrossPlatformAuthentication (){

	}
	
	static CrossPlatformAuthentication getInstance(){
		if(instance==null)
			instance = new CrossPlatformAuthentication();
		return instance;
	}

	@Override
	public String getAuthentificationUrl() {
		return null;
	}

	@Override
	public String serverResponse(String response) {
		return null;
	}

}
