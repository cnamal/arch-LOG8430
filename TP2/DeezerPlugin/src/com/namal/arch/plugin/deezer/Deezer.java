package com.namal.arch.plugin.deezer;

import java.util.List;

import com.namal.arch.models.Playlist;
import com.namal.arch.models.ProviderInformation;
import com.namal.arch.models.services.AudioService;
import com.namal.arch.models.services.AudioServiceProvider;
import com.namal.arch.models.services.IAuthentification;
import com.namal.arch.utils.ServiceListener;

/**
 * Proxy class for Deezer
 * @author namalgac
 *
 */
public class Deezer implements AudioService {

	private static com.namal.arch.models.services.deezer.Deezer deezer = com.namal.arch.models.services.deezer.Deezer.getInstance();
	
	@Override
	public void getPlaylists(ServiceListener<List<Playlist>> callback) {
		deezer.getPlaylists(callback);
	}

	@Override
	public boolean isConnected() {
		return deezer.isConnected();
	}

	@Override
	public void disconnect() {
		deezer.disconnect();
	}

	@Override
	public boolean authenticationNeeded() {
		return deezer.authenticationNeeded();
	}

	@Override
	public void searchTrack(String track, ServiceListener<Playlist> callback) {
		deezer.searchTrack(track, callback);
	}

	@Override
	public ProviderInformation getProviderInformation() {
		return deezer.getProviderInformation();
	}

	@Override
	public AudioServiceProvider getAudioServiceProvider() {
		return deezer.getAudioServiceProvider();
	}

	@Override
	public IAuthentification getAuthentification() {
		return deezer.getAuthentification();
	}

	@Override
	public boolean searchAvailable() {
		return deezer.searchAvailable();
	}

}
