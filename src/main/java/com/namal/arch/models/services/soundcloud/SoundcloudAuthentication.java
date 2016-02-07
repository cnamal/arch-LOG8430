package com.namal.arch.models.services.soundcloud;

import java.util.Map;

import com.namal.arch.models.ProviderInformation;
import com.namal.arch.models.services.GenericAuthenticationService;
import com.namal.arch.utils.SharedPreferences;

public class SoundcloudAuthentication extends GenericAuthenticationService{

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
	protected boolean serverResponse(Map<String, String> paramsMap) {
		if(paramsMap.get("access_token")!=null){
			authToken=paramsMap.get("access_token");
			isAuthenticated=true;
			SharedPreferences.getPreferences().node("soundcloud").put("oauth_token", authToken);
			return true;
		}
		return false;
	}

	@Override
	public ProviderInformation getProviderInformation() {
		// TODO Auto-generated method stub
		return service.getProviderInformation();
	}

	@Override
	public String getAuthentificationUrl() {
		// TODO Auto-generated method stub
		return "https://soundcloud.com/connect?client_id=467af8ca6a20d82958569c3c248446f3&redirect_uri=https%3A%2F%2Fcnamal.github.io%2Farch-LOG8430%2Fcallback.html&response_type=token&scope=non-expiring";
	}

	@Override
	public String getAuthToken() {
		// TODO Auto-generated method stub
		return authToken;
	}

	@Override
	public void disconnect() {
		isAuthenticated = false;
		SharedPreferences.getPreferences().node("soundcloud").remove("oauth_token");
	}

}
