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
	public ProviderInformation getProviderInformation();
	
	JsonObjectBuilder createPlaylist(String name, Boolean pub, String authToken);

	void updatePlaylist(String id, Iterator<Song> songs, String authToken);

	void deletePlaylist(String id, String authToken);
}
