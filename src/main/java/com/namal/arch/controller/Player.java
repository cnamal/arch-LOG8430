package com.namal.arch.controller;

import com.namal.arch.models.Playlist;
import com.namal.arch.models.Song;

public class Player {
	
	private Song song;
	private Playlist playlist;
	private int currentPos;
	
	/**
	 * Plays a song
	 * @param song Song played
	 */
	public void play(Song song){
		// TODO Create a playlist with just song 
	}

	public void play(Playlist playlist){
		// TODO
	}
	
	/**
	 * Plays the current song on the playlist
	 */
	public void play(){
		//TODO get current song and use song.getInputStream()
		// create a thread and play using JLayer (Player(inputStream).play)
		// (stop playing song if any)
	}
	public void pause(){
		//TODO Pause thread (or find a way to pause the player)
	}
	
	public void stop(){
		//TODO stop song (player.close()), cleanup (song.cleanup)
	}
	
	public void setCurrentSongIndex(int i){
		//TODO
	}
	
	public void next(){
		//TODO
	}
	
	public void previous(){
		// TODO
	}
	
	public boolean isFirst(){
		// TODO 
		return false;
	}
	
	public boolean isLast(){
		// TODO 
		return false;
	}
}
