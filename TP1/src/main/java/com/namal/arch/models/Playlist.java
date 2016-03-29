package com.namal.arch.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.namal.arch.models.services.AudioServiceProvider;
import com.namal.arch.models.services.cp.CrossPlatform;

/**
 * Model of a playlist
 * @author namalgac
 *
 */
public class Playlist {

	private List<Song> playlist;
	private AudioServiceProvider provider;
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
	 * Finds the provider (or providers) of the songs in the playlist, to determine who are
	 * the providers of the playlist
	 */
	private void findProvider(){
		AudioServiceProvider prev=playlist.get(0).getProvider();
		for(int i=1;i<playlist.size();i++)
			if(prev!=playlist.get(i).getProvider()){
				provider=CrossPlatform.getInstance().getAudioServiceProvider();
				return;
			}
		provider=prev;
	}
	
	/**
	 * Adds the song to the playlist and saves the playlist.
	 * @param index index at which the song should be added (not used currently)
	 * @param song the song to be added
	 */
	public void addSongAndUpdate(int index, Song song){
		playlist.add(index,song);
		if(!searchPlaylist){
			if(id.equals(Integer.MIN_VALUE+"")){
				findProvider();
				provider.createPlaylist(this);
			}
			AudioServiceProvider cp = CrossPlatform.getInstance().getAudioServiceProvider();
			if(!provider.equals(cp)&&!provider.equals(song.getProvider())){
				provider=cp;
				provider.createPlaylist(this);
			}
			provider.addSongToPlaylist(this,song);
		}
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

	private void init(String name,AudioServiceProvider p,boolean pub){
		this.name = name;
		this.pub=pub;
		this.playlist = new ArrayList<>();
		if(p!=null)
			this.provider=p;
	}
	
	/**
	 * Creates a new playlist (constructor)
	 * @param name the name of the playlist
	 * @param id the id of the playlist
	 * @param p the AudioServiceProvider of the playlist
	 * @param pub a boolean, true if the playlist is public, false otherwise.
	 */	
	public Playlist(String name,int id,AudioServiceProvider p,boolean pub) {
		this.id=id+"";
		init(name, p,pub);
	}
	
	public Playlist(String name,String id,AudioServiceProvider p,boolean pub) {
		this.id=id;
		init(name, p,pub);
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
	
}
