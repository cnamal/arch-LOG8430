package com.namal.arch.models.services.soundcloud;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.apache.commons.io.IOUtils;

import com.namal.arch.models.Playlist;
import com.namal.arch.models.ProviderInformation;
import com.namal.arch.models.Song;
import com.namal.arch.models.services.AudioServiceProvider;

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
			urlConnection = new URL ( uri+"?client_id="+service.clientId ).openConnection();
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
	public void addSongToPlaylist(Playlist playlist, Song addedSong) {
		// TODO Auto-generated method stub
		updatePlaylist(playlist);
	}

	@Override
	public void removerSongFromPlaylist(Playlist playlist, Song removedSong) {
		// TODO Auto-generated method stub
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
			//httpCon.getInputStream();
			
			//String theString = IOUtils.toString(httpCon.getInputStream(), "UTF-8");
			JsonReader rdr = Json.createReader(httpCon.getInputStream());
			JsonObject results = rdr.readObject();
			playlist.setId(results.getInt("id"));	
			playlist.setName(results.getString("title"));
			service.notify(SoundcloudEvent.USERPLAYLISTSUPDATED);
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
		// TODO Auto-generated method stub
		return SoundcloudProviderInformation.getInstance();
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
