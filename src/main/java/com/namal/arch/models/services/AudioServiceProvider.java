package com.namal.arch.models.services;

import java.io.InputStream;

import com.namal.arch.models.Playlist;
import com.namal.arch.models.ProviderInformation;
import com.namal.arch.models.Song;

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
	 * Save a playlist
	 * @param playlist
	 */
	public void addSongToPlaylist(Playlist playlist, Song addedSong);
	
	public void removerSongFromPlaylist(Playlist playlist,Song removedSong);
	
	public void createPlaylist(Playlist playlist);
	/**
	 * 
	 * @return ProviderInformation of the AudioService
	 */
	public ProviderInformation getProviderInformation();
	
}
