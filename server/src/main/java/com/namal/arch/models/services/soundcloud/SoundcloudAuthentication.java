package com.namal.arch.models.services.soundcloud;

import com.namal.arch.models.services.GenericAuthenticationService;

import java.util.Map;

class SoundcloudAuthentication extends GenericAuthenticationService{

	private static SoundcloudAuthentication instance;
	
	private SoundcloudAuthentication (){

	}
	
	static SoundcloudAuthentication getInstance(){
		if(instance==null)
			instance = new SoundcloudAuthentication();
		return instance;
	}
	
	@Override
	protected String serverResponse(Map<String, String> paramsMap) {
		return paramsMap.get("access_token");
	}

	@Override
	public String getAuthentificationUrl() {
		return "https://soundcloud.com/connect?client_id=467af8ca6a20d82958569c3c248446f3&redirect_uri=https%3A%2F%2Fcnamal.github.io%2Farch-LOG8430%2Fcallback.html&response_type=token&scope=non-expiring";
	}

}
