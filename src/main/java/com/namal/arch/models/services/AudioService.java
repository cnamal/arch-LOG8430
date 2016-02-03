package com.namal.arch.models.services;

import java.util.List;

import com.namal.arch.models.Playlist;
import com.namal.arch.models.ProviderInformation;
import com.namal.arch.models.Song;

public interface AudioService {

	/**
	 * 
	 * @return List of all the playlist for this AudioService
	 */
	public List<Playlist> getPlaylists();
	
	/**
	 * @return True if the service is connected
	 */
	public boolean isConnected();
	
	
	/**
	 * Disconnect the service
	 */
	public void disconnect();
	
	/**
	 * 
	 * @return true if an authentication is needed
	 */
	public boolean authenticationNeeded();
	
	/**
	 * 
	 * @return list of tracks found 
	 */
	public Playlist searchTrack(String track);
	
	/**
	 * 
	 * @return ProviderInformation of the AudioService
	 */
	public ProviderInformation getProviderInformation();
	
	public static AudioService getInstance() {
		return null;
	}
	
	public AudioServiceProvider getAudioServiceProvider();
	
	public IAuthentification getAuthentification();
}
