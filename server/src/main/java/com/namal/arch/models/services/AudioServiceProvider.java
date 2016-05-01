package com.namal.arch.models.services;

import com.namal.arch.models.ProviderInformation;
import com.namal.arch.models.Song;

import javax.json.JsonObjectBuilder;
import java.util.Iterator;

/**
 * Service provider
 * @author namalgac
 *
 */
public interface AudioServiceProvider {

	/**
	 * 
	 * @return ProviderInformation of the AudioService
	 */
	ProviderInformation getProviderInformation();

	/**
	 * Create a playlist
	 * @param name Name of the playlist
	 * @param pub Public or not
	 * @param authToken authentication Token
     * @return Json
     */
	JsonObjectBuilder createPlaylist(String name, Boolean pub, String authToken);

    /**
     * Update a playlist
     * @param id ID of the playlist
     * @param songs List of songs
     * @param authToken Authentication token
     */
	void updatePlaylist(String id, Iterator<Song> songs, String authToken);

    /**
     * Delete a playlist
     * @param id ID of the playlist
     * @param authToken Authentication token
     */
	void deletePlaylist(String id, String authToken);
}
