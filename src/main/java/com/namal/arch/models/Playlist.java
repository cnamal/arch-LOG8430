package com.namal.arch.models;

import java.util.Iterator;
import java.util.List;

import com.namal.arch.models.services.AudioServiceProvider;

public class Playlist {

	private List<Song> playlist;
	private AudioServiceProvider provider;
	
	
	public Song getSong(int Pos){
		//TODO
		return null;
	}
	
	public Iterator<Song> getSongs(){
		// TODO
		return null;
	}
	
	public void addSong(int index, Song song){
		// TODO add song 
		// TODO check provider : if same -> use provider to save playlist
		// TODO else -> modify provider for cross-platform provider and save playlist
	}
	
	
}
