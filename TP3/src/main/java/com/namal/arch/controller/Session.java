package com.namal.arch.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Our own session handler
 * @author Namal
 */
class Session {

	private static Map<String,Map<String,String>> map = new HashMap<>();

    /**
     * Get authenticated services
     * @param token token of our server
     * @return iterator &lt;serviceID,authToken&gt;
     * @throws UnauthorizedException if token isn't registered
     */
	static Iterator<Entry<String, String>> getServices(String token) throws UnauthorizedException {
		if(map.get(token)==null)
			throw new UnauthorizedException();
		return map.get(token).entrySet().iterator();
	}

    /**
     * Add a connected service
     * @param token Token of our server
     * @param serviceId Service ID
     * @param authToken Token of the service
     */
	static void addService(String token,String serviceId,String authToken){
		Map<String,String> tmp = map.get(token);
		if(tmp==null){
			tmp = new HashMap<>();
			map.put(token, tmp);
		}
		tmp.put(serviceId, authToken);
	}

    /**
     * Get the authentication token of a service
     * @param token Token of our server
     * @param serviceId Service ID
     * @return Authentication token
     * @throws UnauthorizedException if not connected to our server, or the service
     */
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
