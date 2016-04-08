package com.namal.arch.models.services.spotify;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import com.namal.arch.models.ProviderInformation;
import com.namal.arch.models.services.GenericAuthenticationService;

class SpotifyAuthentication extends GenericAuthenticationService{

	private Spotify service;
	private static SpotifyAuthentication instance;
	static String clientId = "87824d0c4d1a4d2c8c72662b3b1a9f6e";
	static Map<String,String> usernameMap=new HashMap<>();
	private static final String MEURL = Spotify.BASEURL+ "me";
	
	
	private SpotifyAuthentication (Spotify service){
		this.service=service;
	}
	
	static SpotifyAuthentication getInstance(Spotify service){
		if(instance==null)
			instance = new SpotifyAuthentication(service);
		return instance;
	}
	
	@Override
	protected String serverResponse(Map<String, String> paramsMap) {
		String token = paramsMap.get("access_token");
		if(token!=null){
			try {
				URL url = new URL(MEURL);
				HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
				httpCon.setRequestMethod("GET");
				httpCon.setRequestProperty(
						"Authorization", "Bearer "+token );
				JsonReader rdr = Json.createReader(httpCon.getInputStream());
				JsonObject obj = rdr.readObject();
				usernameMap.put(token, obj.getString("id"));
			}catch(Exception e){
			}
		}
		return token;
	}

	@Override
	public ProviderInformation getProviderInformation() {
		return service.getProviderInformation();
	}

	@Override
	public String getAuthentificationUrl() {
		return "https://accounts.spotify.com/authorize?client_id="+clientId+"&redirect_uri=https%3A%2F%2Fcnamal.github.io%2Farch-LOG8430%2Fcallback.html&response_type=token&scope=playlist-read-private%20playlist-modify-public%20playlist-modify-private";
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
