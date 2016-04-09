package com.namal.arch.models.services.deezer;

import com.namal.arch.models.services.GenericAuthenticationService;

import java.util.Map;

class DeezerAuthentication extends GenericAuthenticationService{

	private static DeezerAuthentication instance;
	private static final String appId = "175611";


	private DeezerAuthentication (){}
	
	static DeezerAuthentication getInstance(){
		if(instance==null)
			instance = new DeezerAuthentication();
		return instance;
	}
	
	@Override
	protected String serverResponse(Map<String, String> paramsMap) {
		return paramsMap.get("access_token");
	}

	@Override
	public String getAuthentificationUrl() {
		return "https://connect.deezer.com/oauth/auth.php?app_id="+appId+"&redirect_uri=https%3A%2F%2Fcnamal.github.io%2Farch-LOG8430%2Fcallback.html&response_type=token&perms=manage_library,delete_library";
	}

}
