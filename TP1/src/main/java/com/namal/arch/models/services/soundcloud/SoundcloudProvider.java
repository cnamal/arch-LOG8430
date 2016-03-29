package com.namal.arch.models.services.soundcloud;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.apache.commons.io.IOUtils;

import com.namal.arch.models.Playlist;
import com.namal.arch.models.ProviderInformation;
import com.namal.arch.models.Song;
import com.namal.arch.models.services.AudioServiceProvider;
import com.namal.arch.models.services.ServiceEvent;

class SoundcloudProvider implements AudioServiceProvider {
	
	private Soundcloud service;
	private static SoundcloudProvider instance;
	private InputStream inputStream;
	
	
	private SoundcloudProvider(Soundcloud service) {
		// TODO Auto-generated constructor stub
		this.service=service;
	}
	
	static SoundcloudProvider getInstance(Soundcloud service){
		if(instance==null)
			instance=new SoundcloudProvider(service);
		return instance;
	}
	
	@Override
	public InputStream getInputStream(String uri) {
		URLConnection urlConnection;
		try {
			//Might be able to refact this code
			urlConnection = new URL ( uri+"?client_id="+Soundcloud.clientId ).openConnection();
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
			url = new URL(Soundcloud.PLAYLISTURL+playlist.getId()+"?oauth_token="+service.getAuthToken());
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

			String theString = IOUtils.toString(httpCon.getInputStream(), "UTF-8");
			System.out.println(theString);
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

	@Override
	public void createPlaylist(Playlist playlist) {
		// TODO Auto-generated method stub
		System.out.println("createPlaylist");
		if(!service.isConnected())
			return; // TODO add Exception system
		URL url;
		
		try {
			System.out.println("savePlaylist");
			url = new URL(Soundcloud.PLAYLISTURL+"?client_id="+Soundcloud.clientId);
			HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
			httpCon.setDoOutput(true);
			httpCon.setRequestMethod("POST");
			httpCon.setRequestProperty(
				    "Content-Type", "application/x-www-form-urlencoded" );
			httpCon.setRequestProperty(
				    "Authorization", "OAuth "+service.getAuthToken() );
			OutputStreamWriter out = new OutputStreamWriter(
			    httpCon.getOutputStream());
			String data = URLEncoder.encode("playlist[title]", "UTF-8");
			data+="=";
			data+=URLEncoder.encode(playlist.getName(), "UTF-8");
			data+="&";
			data+=URLEncoder.encode("playlist[sharing]", "UTF-8");
			data+="=";
			data+=playlist.getPub()?"public":"private";
			System.out.println(data);
			out.write(data);
			out.close();

			JsonReader rdr = Json.createReader(httpCon.getInputStream());
			JsonObject results = rdr.readObject();
			playlist.setId(results.getInt("id"));	
			playlist.setName(results.getString("title"));
			service.update(ServiceEvent.USERPLAYLISTSUPDATED);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public ProviderInformation getProviderInformation() {
		return SoundcloudProviderInformation.getInstance();
	}

	@Override
	public void update(ServiceEvent ev) {
		service.update(ev);
	}
	
	private static class SoundcloudProviderInformation extends ProviderInformation{
		
		private static final String name = "Soundcloud";
		private static final String logoURL = "https://developers.soundcloud.com/assets/logo_big_black-4fbe88aa0bf28767bbfc65a08c828c76.png";
		private static final SoundcloudProviderInformation instance = new SoundcloudProviderInformation();
		
		private SoundcloudProviderInformation(){
			super(name,logoURL);
			
		}
		
		public static SoundcloudProviderInformation getInstance(){
			return instance;
		}
	}
}
