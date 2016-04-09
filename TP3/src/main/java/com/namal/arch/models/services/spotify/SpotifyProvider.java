package com.namal.arch.models.services.spotify;

import com.namal.arch.models.ProviderInformation;
import com.namal.arch.models.Song;
import com.namal.arch.models.services.AudioServiceProvider;
import com.namal.arch.utils.Configuration;

import javax.json.*;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

import static com.namal.arch.utils.Constants.*;

class SpotifyProvider implements AudioServiceProvider {
	
	private Spotify service;
	private static SpotifyProvider instance;

	
	private SpotifyProvider(Spotify service) {
		this.service=service;
	}
	
	static SpotifyProvider getInstance(Spotify service){
		if(instance==null)
			instance=new SpotifyProvider(service);
		return instance;
	}

	@Override
	public ProviderInformation getProviderInformation() {
		return SpotifyProviderInformation.getInstance();
	}

	@Override
	public JsonObjectBuilder createPlaylist(String name, Boolean pub, String authToken) {

        URL url;
        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
        try {
            url = new URL(Spotify.USERURL+SpotifyAuthentication.usernameMap.get(authToken)+"/playlists");
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setDoOutput(true);
            httpCon.setRequestMethod("POST");
            httpCon.setRequestProperty(
                    "Content-Type", "application/json" );
            httpCon.setRequestProperty(
                    "Authorization", "Bearer "+authToken );
            OutputStreamWriter out = new OutputStreamWriter(
                    httpCon.getOutputStream());
            JsonObject data = Json.createObjectBuilder()
                    .add("name",name)
                    .add("public",pub).build();

            out.write(data.toString());
            out.close();

            JsonReader rdr = Json.createReader(httpCon.getInputStream());
            JsonObject results = rdr.readObject();
            objectBuilder.add(ID,results.getInt("id"));
            objectBuilder.add(TITLE,results.getString("name"));
            objectBuilder.add(SERVICEID, Configuration.getAudioServiceLoader().getProviderId(service));
            objectBuilder.add(PUB,pub);
            objectBuilder.add(SONGS,Json.createArrayBuilder());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return objectBuilder;
	}

	@Override
	public void updatePlaylist(String id, Iterator<Song> songs, String authToken) {
        URL url;

        try {
            System.out.println("savePlaylist");
            url = new URL(Spotify.USERURL+SpotifyAuthentication.usernameMap.get(authToken)+"/playlists/"+id+"/tracks");
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setDoOutput(true);
            httpCon.setRequestMethod("PUT");
            httpCon.setRequestProperty(
                    "Content-Type", "application/json" );
            httpCon.setRequestProperty(
                    "Authorization", "Bearer "+authToken );
            OutputStreamWriter out = new OutputStreamWriter(
                    httpCon.getOutputStream());
            JsonArrayBuilder array =Json.createArrayBuilder();
            while(songs.hasNext()){
                array.add("spotify:track:"+songs.next().getId());
            }
            JsonObject data = Json.createObjectBuilder()
                    .add("uris",array).build();
            out.write(data.toString());
            out.close();
            httpCon.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	@Override
	public void deletePlaylist(String id, String authToken) {

	}

	private static class SpotifyProviderInformation extends ProviderInformation{
		
		private static final String name = "Spotify";
		private static final String logoURL = Configuration.getUrlBase()+"/img/Spotify.png";
		private static final SpotifyProviderInformation instance = new SpotifyProviderInformation();
		
		private SpotifyProviderInformation(){
			super(name,logoURL);
			
		}
		
		public static SpotifyProviderInformation getInstance(){
			return instance;
		}
	}
}
