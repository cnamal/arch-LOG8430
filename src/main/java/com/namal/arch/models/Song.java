package com.namal.arch.models;

import java.io.InputStream;

import com.namal.arch.models.services.AudioServiceProvider;

import javafx.scene.image.Image;

public class Song {

	private int id;
	private String title;
	private String artist;
	private String album;
	private AudioServiceProvider provider;
	
	
	private String uri;
	private long duration;
	private String albumCoverUrl;
	
	public InputStream getInputStream(){
		return provider.getInputStream(uri);
	}
	
	public void cleanup(){
		provider.closeInputStream();
	}
	
	
	Song(SongBuilder builder){
		title=builder.title;
		artist=builder.artist;
		uri=builder.uri;
		provider=builder.provider;
		duration = builder.duration;
		albumCoverUrl=builder.albumCoverUrl;
		id=builder.id;
	}

	public String getTitle() {
		return title;
	}

	public String getArtist() {
		return artist;
	}
	
	public String getAlbum() {
		return album;
	}

	public AudioServiceProvider getProvider() {
		return provider;
	}


	public String getUri() {
		return uri;
	}

	
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
	
	public int getId(){
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
}
