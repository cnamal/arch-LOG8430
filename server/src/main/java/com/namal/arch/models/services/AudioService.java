package com.namal.arch.models.services;

import com.namal.arch.models.ProviderInformation;

import javax.json.JsonArrayBuilder;

/**
 * Main interface for the audio services
 * @author namalgac
 *
 */
public interface AudioService {

	/**
	 * Get the playlists of the user
	 * @param authToken Authentication token
	 * @return Json
     */
	JsonArrayBuilder getPlaylists(String authToken);

    /**
     * Search for a track
     * @param track Name of the track that is researched
     * @return Json
     */
    JsonArrayBuilder searchTrack(String track);
	
	/**
	 * 
	 * @return ProviderInformation of the AudioService
	 */
	ProviderInformation getProviderInformation();
	
	/**
	 * 
	 * @return the unique instance of this AudioService
	 */
	static AudioService getInstance() {
		return null;
	}
	
	/**
	 * 
	 * @return the AudioServiceProvider associated with the AudioService
	 */
	AudioServiceProvider getAudioServiceProvider();
	
	/**
	 * 
	 * @return the authentication module associated with the AudioService
	 */
	IAuthentification getAuthentification();

	/**
	 * 
	 * @return true if you can search tracks
	 */
	boolean searchAvailable();
	
}
