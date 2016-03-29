package com.namal.arch.models.services;

import java.io.InputStream;

import com.namal.arch.models.Playlist;
import com.namal.arch.models.ProviderInformation;
import com.namal.arch.models.Song;

/**
 * Service provider
 * @author namalgac
 *
 */
public interface AudioServiceProvider {

	/**
	 * 
	 * @param uri an uri (website or path on computer)
	 * @return an input stream. null if there was an error
	 */
	public InputStream getInputStream(String uri);
	
	/**
	 * closes the InputStream.
	 */
	public void closeInputStream();
	
	/**
	 * Update the playlist in the service, by adding a song
	 * @param playlist playlist that needs to be updated
	 * @param addedSong added song
	 */
	public void addSongToPlaylist(Playlist playlist, Song addedSong);
	
	/**
	 * Update the playlist in the service, by removing a song
	 * @param playlist playlist that needs to be updated
	 * @param removedSong removed song
	 */
	public void removeSongFromPlaylist(Playlist playlist,Song removedSong);
	
	/**
	 * Creates a playlist in ther service
	 * @param playlist playlist that will be created
	 */
	public void createPlaylist(Playlist playlist);
	
	/**
	 * 
	 * @return ProviderInformation of the AudioService
	 */
	public ProviderInformation getProviderInformation();
	
}
