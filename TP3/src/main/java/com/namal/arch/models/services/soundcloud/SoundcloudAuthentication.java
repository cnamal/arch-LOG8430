package com.namal.arch.models.services.soundcloud;

import java.util.Map;

import com.namal.arch.models.ProviderInformation;
import com.namal.arch.models.services.GenericAuthenticationService;
import com.namal.arch.utils.SharedPreferences;

class SoundcloudAuthentication extends GenericAuthenticationService{

	private Soundcloud service;
	private static SoundcloudAuthentication instance;
	
	private SoundcloudAuthentication (Soundcloud service){
		this.service=service;
		String token=SharedPreferences.getPreferences().node("soundcloud").get("oauth_token","RANDOMBUTNOTRANDOM");
		if(!token.equals("RANDOMBUTNOTRANDOM")){
			isAuthenticated=true;
			authToken=token;
		}
	}
	
	static SoundcloudAuthentication getInstance(Soundcloud service){
		if(instance==null)
			instance = new SoundcloudAuthentication(service);
		return instance;
	}
	
	@Override
	protected String serverResponse(Map<String, String> paramsMap) {
		return paramsMap.get("access_token");
	}

	@Override
	public ProviderInformation getProviderInformation() {
		return service.getProviderInformation();
	}

	@Override
	public String getAuthentificationUrl() {
		return "https://soundcloud.com/connect?client_id=467af8ca6a20d82958569c3c248446f3&redirect_uri=https%3A%2F%2Fcnamal.github.io%2Farch-LOG8430%2Fcallback.html&response_type=token&scope=non-expiring";
	}

	@Override
	protected String getAuthToken() {
		return authToken;
	}

	@Override
	public void disconnect() {
		isAuthenticated = false;
		SharedPreferences.getPreferences().node("soundcloud").remove("oauth_token");
	}

}
