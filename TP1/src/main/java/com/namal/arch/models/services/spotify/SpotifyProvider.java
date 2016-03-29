package com.namal.arch.models.services.spotify;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonReader;

import com.namal.arch.models.Playlist;
import com.namal.arch.models.ProviderInformation;
import com.namal.arch.models.Song;
import com.namal.arch.models.services.AudioServiceProvider;
import com.namal.arch.models.services.ServiceEvent;

class SpotifyProvider implements AudioServiceProvider {
	
	private Spotify service;
	private static SpotifyProvider instance;
	private InputStream inputStream;
	
	
	private SpotifyProvider(Spotify service) {
		this.service=service;
	}
	
	static SpotifyProvider getInstance(Spotify service){
		if(instance==null)
			instance=new SpotifyProvider(service);
		return instance;
	}
	
	@Override
	public InputStream getInputStream(String uri) {
		URLConnection urlConnection;
		try {
			//Might be able to refact this code
			urlConnection = new URL ( uri).openConnection();
			urlConnection.connect ();
			return inputStream=urlConnection.getInputStream();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void closeInputStream() {
		try {
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void updatePlaylist(Playlist playlist){
		if(!service.isConnected())
			return; // TODO add Exception system
		URL url;
		
		try {
			System.out.println("savePlaylist");
			url = new URL(Spotify.USERURL+SpotifyAuthentication.username+"/playlists/"+playlist.getId()+"/tracks");
			HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
			httpCon.setDoOutput(true);
			httpCon.setRequestMethod("PUT");
			httpCon.setRequestProperty(
				    "Content-Type", "application/json" );
			httpCon.setRequestProperty(
				    "Authorization", "Bearer "+service.getAuthToken() );
			OutputStreamWriter out = new OutputStreamWriter(
			    httpCon.getOutputStream());
			JsonArrayBuilder array =Json.createArrayBuilder();
			Iterator<Song> it =playlist.getSongs();
			while(it.hasNext()){
				array.add("spotify:track:"+it.next().getId());
			}
			JsonObject data = Json.createObjectBuilder()
					.add("uris",array).build();
			out.write(data.toString());
			out.close();
			httpCon.getInputStream();
			service.update(ServiceEvent.USERPLAYLISTSUPDATED);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void addSongToPlaylist(Playlist playlist, Song addedSong) {
		updatePlaylist(playlist);
	}

	@Override
	public void removeSongFromPlaylist(Playlist playlist, Song removedSong) {
		updatePlaylist(playlist);
	}

	/**
	 * Creates a playlist on the provider server from a Playlist of the software
	 * @param playlist the Playlist to save
	 */
	@Override
	public void createPlaylist(Playlist playlist) {
		// TODO Auto-generated method stub
		System.out.println("createPlaylist");
		if(!service.isConnected())
			return; // TODO add Exception system
		URL url;
		
		try {
			url = new URL(Spotify.USERURL+SpotifyAuthentication.username+"/playlists");
			HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
			httpCon.setDoOutput(true);
			httpCon.setRequestMethod("POST");
			httpCon.setRequestProperty(
				    "Content-Type", "application/json" );
			httpCon.setRequestProperty(
				    "Authorization", "Bearer "+service.getAuthToken() );
			OutputStreamWriter out = new OutputStreamWriter(
			    httpCon.getOutputStream());
			JsonObject data = Json.createObjectBuilder()
					.add("name",playlist.getName())
					.add("public",playlist.getPub()).build();
			
			out.write(data.toString());
			out.close();

			JsonReader rdr = Json.createReader(httpCon.getInputStream());
			JsonObject results = rdr.readObject();
			playlist.setId(results.getString("id"));	
			playlist.setName(results.getString("name"));
			service.update(ServiceEvent.USERPLAYLISTSUPDATED);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public ProviderInformation getProviderInformation() {
		return SpotifyProviderInformation.getInstance();
	}

	@Override
	public void update(ServiceEvent ev) {
		service.update(ev);
	}
	
	private static class SpotifyProviderInformation extends ProviderInformation{
		
		private static final String name = "Spotify";
		private static final String logoURL = "Spotify.png";
		private static final SpotifyProviderInformation instance = new SpotifyProviderInformation();
		
		private SpotifyProviderInformation(){
			super(name,logoURL);
			
		}
		
		public static SpotifyProviderInformation getInstance(){
			return instance;
		}
	}
}
