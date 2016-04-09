package com.namal.arch.models;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Model of a song
 * @author namalgac
 *
 */
public class Song {

	private String id;
	private String title;
	private String artist;
	private String album;
	private String serviceId;
	
	
	private String uri;
	private long duration;
	private String albumCoverUrl;
	
	/**
	 * Uses the SongBuilder to create a Song, initializing the attributes
	 * @param builder a SongBuilder
	 */
	Song(SongBuilder builder){
		title=builder.title;
		artist=builder.artist;
		uri=builder.uri;
		duration = builder.duration;
		albumCoverUrl=builder.albumCoverUrl;
		id=builder.id;
		serviceId=builder.serviceId;
	}

	public InputStream getInputStream() {
		URLConnection urlConnection;
		try {
			//Might be able to refact this code
			urlConnection = new URL (uri).openConnection();
			urlConnection.connect ();
			return urlConnection.getInputStream();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * @return the title of the song
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * 
	 * @return the artist of the song
	 */
	public String getArtist() {
		return artist;
	}
	
	/**
	 * 
	 * @return the album name of the song
	 */
	public String getAlbum() {
		return album;
	}


	/**
	 * 
	 * @return the stream uri of the song
	 */
	public String getUri() {
		return uri;
	}

	/**
	 * 
	 * @return the duration of the song (in s)
	 */
	public long getDuration(){
		return duration;
	}
	
	/**
	 * 
	 * @return Album cover if defined, null otherwise
	 */
	public String getAlbumCoverUrl(){
		return albumCoverUrl;
	}
	
	/**
	 * 
	 * @return the id of the song (related to the provider)
	 */
	public String getId(){
		return id;
	}
	
	public String toString(){
		return "Title : "+title + "\n Artist : "+artist+ "\n Uri : "+uri+"\n";
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof Song){
			return uri.equals(((Song)o).uri);
		}
		return false;
	}

	/**
	 * 
	 * @return provider's id
	 */
	public String getServiceId() {
		return serviceId;
	}
}
