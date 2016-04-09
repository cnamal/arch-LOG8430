package com.namal.arch.models.services.spotify;

import com.namal.arch.models.services.GenericAuthenticationService;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

class SpotifyAuthentication extends GenericAuthenticationService{

	private static SpotifyAuthentication instance;
	private static final String clientId = "87824d0c4d1a4d2c8c72662b3b1a9f6e";
	static Map<String,String> usernameMap=new HashMap<>();
	private static final String MEURL = Spotify.BASEURL+ "me";
	
	
	private SpotifyAuthentication (){}
	
	static SpotifyAuthentication getInstance(){
		if(instance==null)
			instance = new SpotifyAuthentication();
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
				e.printStackTrace();
			}
		}
		return token;
	}

	@Override
	public String getAuthentificationUrl() {
		return "https://accounts.spotify.com/authorize?client_id="+clientId+"&redirect_uri=https%3A%2F%2Fcnamal.github.io%2Farch-LOG8430%2Fcallback.html&response_type=token&scope=playlist-read-private%20playlist-modify-public%20playlist-modify-private";
	}

}
