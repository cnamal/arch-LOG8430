package com.namal.arch.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.namal.arch.models.services.AudioServiceProvider;

public class Playlist {

	private List<Song> playlist;
	private AudioServiceProvider provider;
	private String name;
	private int id;
	
	/**
	 * 
	 * @param Pos
	 * @return Song at the Pos index of the playlist
	 */
	public Song getSong(int Pos){
		//TODO
		return playlist.get(Pos);
	}
	
	/**
	 * 
	 * @return iterator on all the songs of the playlist
	 */
	public Iterator<Song> getSongs(){
		// TODO
		return playlist.iterator();
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
		if(id!=Integer.MIN_VALUE){
			playlist.add(index,song);
			System.out.println(provider);
			System.out.println(song.getProvider());
			if(!provider.equals(song.getProvider())){
				//TODO
			}else{
				provider.savePlaylist(this);
			}
		}
	}
	
	public String getName(){
		return name;
	}
	
	public int getPos(Song song){
		return playlist.indexOf(song);
	}

	
	public Playlist(String name,int id,AudioServiceProvider p) {
		this.id=id;
		this.name = name;
		this.provider=p;
		this.playlist = new ArrayList<Song>();
	}
	
	public Playlist(String name) {
		this.id=Integer.MIN_VALUE;
		this.name = name;
		this.playlist = new ArrayList<Song>();
	}
	
	//ONLY FOR TESTING
	public void addSong(Song song){
		playlist.add(song);
	}
	
	public String toString(){
		return playlist.toString();
	}
	
	public String toJson(){
		//{"playlist":{"tracks":[{"id":201415513},{"id":201201304},{"id":96379023}]}}
		String json ="{\"playlist\":{\"tracks\":[";
		for(int i=0;i<playlist.size()-1;i++){
			json+="{\"id\":" +playlist.get(i).getId()+"},";
		}
		json+="{\"id\":" +playlist.get(playlist.size()-1).getId()+"}";
		json+="]}}";
		return json;
	}
	
	public int getId(){
		return id;
	}
}
