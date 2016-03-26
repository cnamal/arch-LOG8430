package com.namal.arch.models.services.spotify;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.apache.commons.io.IOUtils;

import com.namal.arch.models.Playlist;
import com.namal.arch.models.ProviderInformation;
import com.namal.arch.models.Song;
import com.namal.arch.models.services.AudioServiceProvider;

class SpotifyProvider implements AudioServiceProvider {
	
	private Spotify service;
	private static SpotifyProvider instance;
	private InputStream inputStream;
	
	
	private SpotifyProvider(Spotify service) {
		// TODO Auto-generated constructor stub
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
			service.notify(SpotifyEvent.USERPLAYLISTSUPDATED);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void addSongToPlaylist(Playlist playlist, Song addedSong) {
		updatePlaylist(playlist);
	}

	@Override
	public void removerSongFromPlaylist(Playlist playlist, Song removedSong) {
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
			//System.out.println("savePlaylist");
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
			//httpCon.getInputStream();
			
			//String theString = IOUtils.toString(httpCon.getInputStream(), "UTF-8");
			JsonReader rdr = Json.createReader(httpCon.getInputStream());
			JsonObject results = rdr.readObject();
			playlist.setId(results.getString("id"));	
			playlist.setName(results.getString("name"));
			service.notify(SpotifyEvent.USERPLAYLISTSUPDATED);
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
		return SpotifyProviderInformation.getInstance();
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
