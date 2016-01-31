package com.namal.arch.models.services;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.apache.commons.io.IOUtils;

import com.namal.arch.models.Playlist;
import com.namal.arch.models.ProviderInformation;
import com.namal.arch.models.Song;
import com.namal.arch.models.SongBuilder;
import com.namal.arch.models.SongMalformed;

import javafx.scene.image.Image;

public class Soundcloud implements AudioService, AudioServiceProvider,IAuthentification {

	private static Soundcloud instance= new Soundcloud();
	private static final String clientId="467af8ca6a20d82958569c3c248446f3";
	private static final String BASEURL = "http://api.soundcloud.com/";
	private static final String SONGSURL = BASEURL+"tracks/";
	private static final String MYPLAYLISTS = BASEURL + "me/playlists/";
	private static final String PLAYLISTURL = BASEURL+"playlists/";
	private String authToken;
	private boolean isAuthenticated=false;
	private List<Playlist> playlists = null;
	
	private InputStream inputStream;
	
	private Soundcloud(){}
	
	public static Soundcloud getInstance(){
		return instance;
	}
	
	@Override
	public InputStream getInputStream(String uri) {
		URLConnection urlConnection;
		try {
			//Might be able to refact this code
			urlConnection = new URL ( uri+"?client_id="+clientId ).openConnection();
			urlConnection.connect ();
			return inputStream=urlConnection.getInputStream();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void closeInputStream() {
		// TODO Auto-generated method stub
		try {
			inputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private Song songBuilder(JsonObject result) throws SongMalformed{
		SongBuilder builder = SongBuilder.songBuilder()
				.setId(result.getInt("id"))
				.setTitle(result.getString("title"))
				.setArtist(result.getJsonObject("user").getString("username"))
				.setUri(result.getString("stream_url"))
				.setProvider(this)
				.setDuration(result.getInt("duration"));
		if(!result.isNull("artwork_url"))
			builder.setAlbumCover(new Image(result.getString("artwork_url")));
		return builder.build();
	}

	@Override
	public List<Playlist> getPlaylists() {
		// TODO Auto-generated method stub
		if(!isAuthenticated)
			return null; // TODO add Exception system
		URL url;
		if(playlists != null)
			return playlists;
		try {
			url = new URL(MYPLAYLISTS+"?oauth_token="+authToken);

			InputStream is = url.openStream();
			JsonReader rdr = Json.createReader(is);
			List<Playlist> playlists = new ArrayList<>();
			JsonArray results = rdr.readArray();
			for (JsonObject playlist : results.getValuesAs(JsonObject.class)) {
				if(!playlist.isNull("streamable") && playlist.getBoolean("streamable")){
					Playlist p = new Playlist(playlist.getString("title"), playlist.getInt("id"),this,playlist.getString("sharing").equals("public"));
					JsonArray songs = playlist.getJsonArray("tracks");
					for (JsonObject song : songs.getValuesAs(JsonObject.class)){
						if(song.getBoolean("streamable")){
							p.addSongWithoutUpdating(songBuilder(song));
						}
					}
					playlists.add(p);
				}
			}
			this.playlists = playlists;
			return playlists;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SongMalformed e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	
	@Override
	public void authenticate() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean authenticationNeeded() {
		return false;
	}

	private void updatePlaylist(Playlist playlist){
		System.out.println("savePlaylist");
		if(!isAuthenticated)
			return; // TODO add Exception system
		URL url;
		
		try {
			System.out.println("savePlaylist");
			url = new URL(PLAYLISTURL+playlist.getId()+"?oauth_token="+authToken);
			HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
			httpCon.setDoOutput(true);
			httpCon.setRequestMethod("PUT");
			httpCon.setRequestProperty(
				    "Content-Type", "application/json" );
			OutputStreamWriter out = new OutputStreamWriter(
			    httpCon.getOutputStream());
			System.out.println(playlist.toJson());
			out.write(playlist.toJson());
			out.close();
			//httpCon.getInputStream();
			String theString = IOUtils.toString(httpCon.getInputStream(), "UTF-8");
			System.out.println(theString);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void addSongToPlaylist(Playlist playlist,Song addedSong) {
		updatePlaylist(playlist);
	}

	@Override
	public void removerSongFromPlaylist(Playlist playlist, Song removedSong) {
		updatePlaylist(playlist);
	}
	
	@Override
	public void createPlaylist(Playlist playlist) {
		System.out.println("createPlaylist");
		if(!isAuthenticated)
			return; // TODO add Exception system
		URL url;
		
		try {
			System.out.println("savePlaylist");
			url = new URL(PLAYLISTURL+"?client_id="+clientId);
			HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
			httpCon.setDoOutput(true);
			httpCon.setRequestMethod("POST");
			httpCon.setRequestProperty(
				    "Content-Type", "application/x-www-form-urlencoded" );
			httpCon.setRequestProperty(
				    "Authorization", "OAuth "+authToken );
			OutputStreamWriter out = new OutputStreamWriter(
			    httpCon.getOutputStream());
			String data = URLEncoder.encode("playlist[title]", "UTF-8");
			data+="=";
			data+=URLEncoder.encode(playlist.getName(), "UTF-8");
			data+="&";
			data+=URLEncoder.encode("playlist[sharing]", "UTF-8");
			data+=playlist.getPub()?"public":"private";
			System.out.println(data);
			out.write(data);
			out.close();
			//httpCon.getInputStream();
			
			//String theString = IOUtils.toString(httpCon.getInputStream(), "UTF-8");
			JsonReader rdr = Json.createReader(httpCon.getInputStream());
			JsonObject results = rdr.readObject();
			playlist.setId(results.getInt("id"));	
			playlist.setName(results.getString("title"));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public ProviderInformation getProviderInformation() {
		return SoundcloudProviderInformation.getInstance();
	}

	public String toString(){
		return getProviderInformation().toString();
	}
	
	private static class SoundcloudProviderInformation extends ProviderInformation{
		
		private static final String name = "Soundclound";
		private static final String logoURL = "https://developers.soundcloud.com/assets/logo_big_black-4fbe88aa0bf28767bbfc65a08c828c76.png";
		private static final SoundcloudProviderInformation instance = new SoundcloudProviderInformation();
		
		private SoundcloudProviderInformation(){
			super(name,new Image(logoURL));
			
		}
		
		public static SoundcloudProviderInformation getInstance(){
			return instance;
		}
	}

	@Override
	public Playlist searchTrack(String track) {
		URL url;
		try {
			url = new URL(SONGSURL+"?client_id="+clientId+"&q=\""+URLEncoder.encode(track, "UTF-8")+"\"");

			InputStream is = url.openStream();
			JsonReader rdr = Json.createReader(is);

			JsonArray results = rdr.readArray();
			Playlist playlist = new Playlist("Search results",true,true);
			for (JsonObject result : results.getValuesAs(JsonObject.class)) {
				if(result.getBoolean("streamable")){
					
					playlist.addSongWithoutUpdating(songBuilder(result));
				}
			}
			return playlist;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SongMalformed e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public AudioServiceProvider getAudioServiceProvider() {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public String getAuthentificationUrl() {
		// TODO Auto-generated method stub
		return "https://soundcloud.com/connect?client_id=467af8ca6a20d82958569c3c248446f3&redirect_uri=https%3A%2F%2Fcnamal.github.io%2Farch-LOG8430%2Fcallback.html&response_type=token";
	}

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
		if(paramsMap.get("access_token")!=null){
			authToken=paramsMap.get("access_token");
			isAuthenticated=true;
			return true;
		}
		return false;
	}

	@Override
	public IAuthentification getAuthentification() {
		// TODO Auto-generated method stub
		return this;
	}
}
