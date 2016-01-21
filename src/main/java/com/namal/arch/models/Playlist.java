package com.namal.arch.models;

import java.util.Iterator;
import java.util.List;

import com.namal.arch.models.services.AudioServiceProvider;

public class Playlist {

	private List<Song> playlist;
	private AudioServiceProvider provider;
	
	/**
	 * 
	 * @param Pos
	 * @return Song at the Pos index of the playlist
	 */
	public Song getSong(int Pos){
		//TODO
		return null;
	}
	
	/**
	 * 
	 * @return iterator on all the songs of the playlist
	 */
	public Iterator<Song> getSongs(){
		// TODO
		return null;
	}
	
	/**
	 * Number of Songs in the playlist
	 * @return 
	 */
	public int getTotalNumberSongs(){
		return playlist.size();
	}
	
	/**
	 * Adds the song to the playlist and saves the playlist.
	 * @param index index at which the song should be added (not used currently)
	 * @param song 
	 */
	public void addSong(int index, Song song){
		// TODO add song 
		// TODO check provider : if same -> use provider to save playlist
		// TODO else -> modify provider for cross-platform provider and save playlist
	}
	
	
}
