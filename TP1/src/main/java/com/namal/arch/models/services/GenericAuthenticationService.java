package com.namal.arch.models.services;

import java.util.HashMap;
import java.util.Map;

public abstract class GenericAuthenticationService implements IAuthentification{

	protected String authToken;
	protected boolean isAuthenticated=false;
	
	public abstract String getAuthToken();

	@Override
	public String testString() {
		// TODO Auto-generated method stub
		return "https://cnamal.github.io/arch-LOG8430/callback.html";
	}

	@Override
	public boolean serverResponse(String response) {
		// TODO Auto-generated method stub
		String res=response;
		if(response.indexOf("#")>=0)
			res=response.substring(response.indexOf("#")+1);
		String[] params=res.split("&");
		HashMap<String, String> paramsMap = new HashMap<>();
		for(int i=0;i<params.length;i++)
			paramsMap.put(params[i].split("=")[0], params[i].split("=")[1]);
		return serverResponse(paramsMap);
	}

	public boolean isConnected(){
		return isAuthenticated;
	}
	
	protected abstract boolean serverResponse(Map<String, String> s);
}
