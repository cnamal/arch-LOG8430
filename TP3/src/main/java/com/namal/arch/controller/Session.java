package com.namal.arch.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

class Session {

	private static Map<String,Map<String,String>> map = new HashMap<>();
	
	static Iterator<Entry<String, String>> getServices(String token) throws UnauthorizedException {
		if(map.get(token)==null)
			throw new UnauthorizedException();
		return map.get(token).entrySet().iterator();
	}
	
	static void addService(String token,String serviceId,String authToken){
		Map<String,String> tmp = map.get(token);
		if(tmp==null){
			tmp = new HashMap<>();
			map.put(token, tmp);
		}
		tmp.put(serviceId, authToken);
	}

	static String getAuthToken(String token, String serviceId)throws UnauthorizedException {
        Map<String,String> tmp =map.get(token);
        if(tmp==null)
            throw new UnauthorizedException();
        String authToken = tmp.get(serviceId);
        if(authToken==null)
            throw new UnauthorizedException();
        return authToken;
	}
}
