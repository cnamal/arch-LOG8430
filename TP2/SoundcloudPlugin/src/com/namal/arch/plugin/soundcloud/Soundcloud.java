package com.namal.arch.plugin.soundcloud;

import java.util.List;

import com.namal.arch.models.Playlist;
import com.namal.arch.models.ProviderInformation;
import com.namal.arch.models.services.AudioService;
import com.namal.arch.models.services.AudioServiceProvider;
import com.namal.arch.models.services.IAuthentification;
import com.namal.arch.utils.ServiceListener;

public class Soundcloud implements AudioService {

	private static com.namal.arch.models.services.soundcloud.Soundcloud soundcloud = com.namal.arch.models.services.soundcloud.Soundcloud.getInstance();
	
	@Override
	public void getPlaylists(ServiceListener<List<Playlist>> callback) {
		soundcloud.getPlaylists(callback);
	}

	@Override
	public boolean isConnected() {
		return soundcloud.isConnected();
	}

	@Override
	public void disconnect() {
		soundcloud.disconnect();
	}

	@Override
	public boolean authenticationNeeded() {
		return soundcloud.authenticationNeeded();
	}

	@Override
	public void searchTrack(String track, ServiceListener<Playlist> callback) {
		soundcloud.searchTrack(track, callback);
	}

	@Override
	public ProviderInformation getProviderInformation() {
		return soundcloud.getProviderInformation();
	}

	@Override
	public AudioServiceProvider getAudioServiceProvider() {
		return soundcloud.getAudioServiceProvider();
	}

	@Override
	public IAuthentification getAuthentification() {
		return soundcloud.getAuthentification();
	}

}
