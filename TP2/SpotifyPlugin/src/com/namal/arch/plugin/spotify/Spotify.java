package com.namal.arch.plugin.spotify;

import java.util.List;

import com.namal.arch.models.Playlist;
import com.namal.arch.models.ProviderInformation;
import com.namal.arch.models.services.AudioService;
import com.namal.arch.models.services.AudioServiceProvider;
import com.namal.arch.models.services.IAuthentification;
import com.namal.arch.utils.ServiceListener;

/**
 * Proxy class for Spotify
 * @author namalgac
 *
 */
public class Spotify implements AudioService {

	private static com.namal.arch.models.services.spotify.Spotify spotify = com.namal.arch.models.services.spotify.Spotify.getInstance();
	//private static  Spotify spotify = new 
	@Override
	public void getPlaylists(ServiceListener<List<Playlist>> callback) {
		spotify.getPlaylists(callback);
	}

	@Override
	public boolean isConnected() {
		return spotify.isConnected();
	}

	@Override
	public void disconnect() {
		spotify.disconnect();
	}

	@Override
	public boolean authenticationNeeded() {
		return spotify.authenticationNeeded();
	}

	@Override
	public void searchTrack(String track, ServiceListener<Playlist> callback) {
		spotify.searchTrack(track, callback);
	}

	@Override
	public ProviderInformation getProviderInformation() {
		// TODO Auto-generated method stub
		return spotify.getProviderInformation();
	}

	@Override
	public AudioServiceProvider getAudioServiceProvider() {
		return spotify.getAudioServiceProvider();
	}

	@Override
	public IAuthentification getAuthentification() {
		return spotify.getAuthentification();
	}

	@Override
	public boolean searchAvailable() {
		return spotify.searchAvailable();
	}

}
