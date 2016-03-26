package com.namal.arch.models.services.spotify;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import com.namal.arch.models.ProviderInformation;
import com.namal.arch.models.services.GenericAuthenticationService;
import com.namal.arch.utils.WebListener;
import com.namal.arch.utils.WebThread;

public class SpotifyAuthentication extends GenericAuthenticationService{

	private Spotify service;
	private static SpotifyAuthentication instance;
	static String clientId = "87824d0c4d1a4d2c8c72662b3b1a9f6e";
	static String username=null;
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
	protected boolean serverResponse(Map<String, String> paramsMap) {
		/*if(secretId==null)
			return false;*/
		if(paramsMap.get("access_token")!=null){
			try {
				authToken=paramsMap.get("access_token");
				URL url = new URL(MEURL);
				HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
				httpCon.setRequestMethod("GET");
				//System.out.println(getAuthToken());
				httpCon.setRequestProperty(
						"Authorization", "Bearer "+getAuthToken() );
				JsonReader rdr = Json.createReader(httpCon.getInputStream());
				JsonObject obj = rdr.readObject();
				username = obj.getString("id");
				isAuthenticated=true;
				return true;
			}catch(Exception e){
			}
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
		return "https://accounts.spotify.com/authorize?client_id="+clientId+"&redirect_uri=https%3A%2F%2Fcnamal.github.io%2Farch-LOG8430%2Fcallback.html&response_type=token&scope=playlist-read-private%20playlist-modify-public%20playlist-modify-private";
	}

	@Override
	public String getAuthToken() {
		return authToken;
	}

	@Override
	public void disconnect() {
		isAuthenticated = false;
	}

}
