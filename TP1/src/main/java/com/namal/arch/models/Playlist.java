package com.namal.arch.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Model of a playlist
 * @author namalgac
 *
 */
public class Playlist {

	private List<Song> playlist;
	private String serviceId;
	private String name;
	private boolean searchPlaylist=false;
	/**
	 * Is the playlist public or private
	 */
	private boolean pub;
	private String id;
	
	/**
	 * 
	 * @param Pos position
	 * @return Song at the Pos index of the playlist
	 */
	public Song getSong(int Pos){
		//TODO
		return playlist.get(Pos);
	}
	
	/**
	 * Gets the list of the songs in the playlist
	 * @return iterator on all the songs of the playlist
	 */
	public Iterator<Song> getSongs(){
		// TODO
		return playlist.iterator();
	}
	
	/**
	 * Number of Songs in the playlist
	 * @return the number of songs in the playlist 
	 */
	public int getTotalNumberSongs(){
		return playlist.size();
	}

	
	/**
	 * Gets the name of the Playlist
	 * @return name the name of the playlist.
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Gets the position of the desired song in the playlist.
	 * @param song the desired song
	 * @return pos the position of the song in the playlist
	 */
	public int getPos(Song song){
		return playlist.indexOf(song);
	}

	private void init(String name, String serviceId, boolean pub){
		this.name = name;
		this.pub=pub;
		this.serviceId = serviceId;
		this.playlist = new ArrayList<>();
	}
	
	/**
	 * Creates a new playlist (constructor)
	 * @param name the name of the playlist
	 * @param id the id of the playlist
	 * @param serviceId the id of the Audio Service of the playlist
	 * @param pub a boolean, true if the playlist is public, false otherwise.
	 */	
	public Playlist(String name,int id,String serviceId,boolean pub) {
		this.id=id+"";
		init(name, serviceId,pub);
	}
	
	public Playlist(String name,String id,String serviceId,boolean pub) {
		this.id=id;
		init(name, serviceId,pub);
	}
	
	
	/**
	 * Creates a new playlist (constructor)
	 * @param name the name of the playlist
	 * @param searchPlaylist true if this playlist is a search result, false otherwise
	 * @param pub a boolean, true if the playlist is public, false otherwise.
	 */	
	public Playlist(String name,boolean searchPlaylist,boolean pub) {
		this.id=Integer.MIN_VALUE+"";
		this.searchPlaylist=searchPlaylist;
		init(name, null,pub);
	}
	
	/**
	 * Adds a song to the playlist, without updating it on the server
	 * @param song song to be added
	 */
	public void addSongWithoutUpdating(Song song){
		playlist.add(song);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
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
	
	/**
	 * Id getter
	 * @return id of the playlist
	 */
	public String getId(){
		return id;
	}
	
	/**
	 * Id setter
	 * @param id id of the playlist
	 */
	public void setId(int id){
		this.id=id+"";
	}
	
	/**
	 * Id setter
	 * @param id id of the playlist
	 */
	public void setId(String id){
		this.id=id;
	}
	
	/**
	 * 
	 * @return true if the playlist is public, false if it is private
	 */
	public boolean getPub(){
		return pub;
	}

	/**
	 * 
	 * @param name name of the playlist
	 */
	public void setName(String name) {
		this.name=name;
	}

	/**
	 * @return id of the service
	 */
	public String getServiceId() {return this.serviceId;}
	
}