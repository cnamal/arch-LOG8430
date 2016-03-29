package com.namal.arch.models.services.deezer;

import java.util.Map;

import com.namal.arch.models.ProviderInformation;
import com.namal.arch.models.services.GenericAuthenticationService;

class DeezerAuthentication extends GenericAuthenticationService{

	private Deezer service;
	private static DeezerAuthentication instance;
	static String appId = "175611";
	
	
	private DeezerAuthentication (Deezer service){
		this.service=service;
	}
	
	static DeezerAuthentication getInstance(Deezer service){
		if(instance==null)
			instance = new DeezerAuthentication(service);
		return instance;
	}
	
	@Override
	protected boolean serverResponse(Map<String, String> paramsMap) {
		if(paramsMap.get("access_token")!=null){
			authToken=paramsMap.get("access_token");
			isAuthenticated=true;
			return true;
		}
		return false;
	}

	@Override
	public ProviderInformation getProviderInformation() {
		return service.getProviderInformation();
	}

	@Override
	public String getAuthentificationUrl() {
		return "https://connect.deezer.com/oauth/auth.php?app_id="+appId+"&redirect_uri=https%3A%2F%2Fcnamal.github.io%2Farch-LOG8430%2Fcallback.html&response_type=token&perms=manage_library";
	}

	@Override
	protected String getAuthToken() {
		return authToken;
	}

	@Override
	public void disconnect() {
		isAuthenticated = false;
	}

}
