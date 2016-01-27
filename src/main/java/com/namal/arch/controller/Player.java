package com.namal.arch.controller;

import com.namal.arch.models.Playlist;
import com.namal.arch.models.Song;

/**
 * Music player class. Provides the basic functionalities of a player (play, pause, ..).
 * The player always plays a playlist, even if a single Song is played. 
 * @author namalgac
 *
 */
public class Player {
	
	private Song song;
	private Playlist playlist;
	private int currentPos;
	
	/**
	 * Plays a song
	 * @param song Song played
	 */
	public void play(Song song){
		playlist = new Playlist(song.getTitle());
		playlist.addSong(song);
		// TODO call play()
	}

	/**
	 * Plays an entire playlist
	 * @param playlist
	 */
	public void play(Playlist playlist){
		// TODO call play
	}
	
	/**
	 * Plays the current song on the playlist
	 */
	public void play(){
		//TODO stop playing song if any 
		//get current song and use song.getInputStream()
		// create a thread and play using JLayer (Player(inputStream).play)
		// probably will need to call stop at the end to cleanup
	}
	
	/**
	 * Pauses the current song
	 */
	public void pause(){
		//TODO Pause thread (or find a way to pause the player)
	}
	
	/**
	 * Stops the current song
	 */
	public void stop(){
		//TODO stop song (player.close()), cleanup (song.cleanup)
	}
	
	/**
	 * Sets the current song but does NOT play it.
	 * This method should typically be called when the playlist is paused or stopped.
	 * @param i if i is within the bounds of the playlist size then it is set to that index
	 * if i<0 then it is set to the last Song of the playlist
	 * if (i>playlist size) it is set to the first Song of the playlist
	 */
	public void setCurrentSongIndex(int i){
		//TODO read carefully the Javadoc
		
		// As multiple calls to getTotalNumberSongs will be made in this method,
		// we store it.
		int sizePlaylist = playlist.getTotalNumberSongs();
		
		if(i<0) {
			currentPos = sizePlaylist - 1;
		}
		else if(i > sizePlaylist - 1) {
			currentPos = 0;
		}
		else {
			currentPos = i;
		}
		
		song = playlist.getSong(currentPos);
	}
	
	/**
	 * Sets the current song and plays it right after.
	 * @param i
	 */
	public void setCurrentSongIndexAndPlay(int i){
		setCurrentSongIndex(i);
		// TODO call play
	}
	
	/**
	 * Index of the current song in the playlist
	 * @return 
	 */
	public int getCurrentSongIndex(){
		return currentPos;
	}
	
	/**
	 * Number of Songs in the playlist
	 * @return 
	 */
	public int getTotalNumberSongs(){
		return playlist.getTotalNumberSongs();
	}
	
	/**
	 * Set the current song to the next one in the playlist but does NOT play it.
	 * If the current song is the last one, the next one is the first Song of the playlist
	 * The current song is NOT stopped.
	 * Therefore this method should typically be called when the playlist is paused or stopped.
	 */
	public void next(){
		setCurrentSongIndex(currentPos+1);
	}
	
	/**
	 * Set the current song to the previous one in the playlist but does NOT play it.
	 * If the current song is the first one, the previous one is the last Song of the playlist
	 * The current song is NOT stopped.
	 * Therefore this method should typically be called when the playlist is paused or stopped.
	 */
	public void previous(){
		setCurrentSongIndex(currentPos-1);
	}
	
	/**
	 * Set the current song to the next one in the playlist and plays it right after.
	 * If the current song is the last one, the next one is the first Song of the playlist.
	 */
	public void nextAndPlay(){
		setCurrentSongIndex(currentPos+1);
		// TODO call play
	}
	
	/**
	 * Set the current song to the previous one in the playlist and plays it right after.
	 * If the current song is the first one, the previous one is the last Song of the playlist.
	 */
	public void previousAndPlay(){
		setCurrentSongIndex(currentPos-1);
		// TODO call play
	}
	
	/**
	 * 
	 * @return true if the current song is the first song of the playlist, false otherwise.
	 */
	public boolean isFirst(){		
		if(playlist == null) {
			return true;
		}
			return currentPos == 0;
	}
	
	/**
	 * 
	 * @return true if the current song is the last song of the playlist, false otherwise.
	 */
	public boolean isLast(){
		if(playlist == null) {
			return true;
		}
			return currentPos == playlist.getTotalNumberSongs()-1;
	}
	
	/**
	 * 
	 * @return true if a music is playing, false otherwise.
	 */
	public boolean isPlaying(){
		//TODO
		return false;
	}
	
	public Playlist getCurrentPlaylist() {
		return playlist;
	}
	
}
