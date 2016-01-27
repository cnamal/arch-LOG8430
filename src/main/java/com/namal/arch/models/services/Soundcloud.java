package com.namal.arch.models.services;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import com.namal.arch.models.Playlist;
import com.namal.arch.models.ProviderInformation;
import com.namal.arch.models.Song;
import com.namal.arch.models.SongBuilder;
import com.namal.arch.models.SongMalformed;

import javafx.scene.image.Image;

public class Soundcloud implements AudioService, AudioServiceProvider {

	private static Soundcloud instance= new Soundcloud();
	private static String clientId="467af8ca6a20d82958569c3c248446f3";
	private static final String BASEURL = "http://api.soundcloud.com/";
	private static final String SONGSURL = BASEURL+"tracks/";
	
	
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

	@Override
	public List<Playlist> getPlaylists() {
		// TODO Auto-generated method stub
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

	@Override
	public void savePlaylist(Playlist playlist) {
		// TODO Auto-generated method stub
		
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
			Playlist playlist = new Playlist("Search results");
			for (JsonObject result : results.getValuesAs(JsonObject.class)) {
				if(result.getBoolean("streamable")){
					Song song = SongBuilder.songBuilder()
							.setTitle(result.getString("title"))
							.setArtist(result.getJsonObject("user").getString("username"))
							.setUri(result.getString("stream_url"))
							.setProvider(this)
							.build();
					playlist.addSong(song);
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
}
