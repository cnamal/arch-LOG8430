package com.namal.arch.models.services;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import com.namal.arch.models.Playlist;
import com.namal.arch.models.ProviderInformation;
import com.namal.arch.models.Song;

import javafx.scene.image.Image;

public class Soundcloud implements AudioService, AudioServiceProvider {

	private static Soundcloud instance= new Soundcloud();
	private static String clientId="467af8ca6a20d82958569c3c248446f3";
	
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
		// TODO Auto-generated method stub
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
	public List<Song> searchTrack(String track) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AudioServiceProvider getAudioServiceProvider() {
		// TODO Auto-generated method stub
		return this;
	}
}
