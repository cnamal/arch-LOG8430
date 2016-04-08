package com.namal.arch.models.services;

import java.util.List;

import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;

import com.namal.arch.models.Playlist;
import com.namal.arch.models.ProviderInformation;
import com.namal.arch.utils.ServiceListener;

/**
 * Main interface for the audio services
 * @author namalgac
 *
 */
public interface AudioService {
	
	/**
	 * Get the playlists of the user 
	 * @param callback callback function called once the playlists have been retrieved
	 */
	public void getPlaylists(ServiceListener<List<Playlist>> callback);
	
	/**
	 * Get the playlists of the user 
	 */
	public JsonArrayBuilder getPlaylists(String authToken);
	
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
	 * @param track Name of the track that is researched
	 * @param callback callback function called once the search is completed
	 */
	public void searchTrack(String track, ServiceListener<Playlist> callback);
	
	/**
	 * 
	 * @param track Name of the track that is researched
	 * @param callback callback function called once the search is completed
	 */
	public JsonArrayBuilder searchTrack(String track);
	
	/**
	 * 
	 * @return ProviderInformation of the AudioService
	 */
	public ProviderInformation getProviderInformation();
	
	/**
	 * 
	 * @return the unique instance of this AudioService
	 */
	public static AudioService getInstance() {
		return null;
	}
	
	/**
	 * 
	 * @return the AudioServiceProvider associated with the AudioService
	 */
	public AudioServiceProvider getAudioServiceProvider();
	
	/**
	 * 
	 * @return the authentication module associated with the AudioService
	 */
	public IAuthentification getAuthentification();

	/**
	 * 
	 * @return true if you can search tracks
	 */
	public boolean searchAvailable();
	
}
