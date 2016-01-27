package com.namal.arch.models;

import java.io.InputStream;

import com.namal.arch.models.services.AudioServiceProvider;

import javafx.scene.image.Image;

public class Song {

	private String title;
	private String artist;
	private String album;
	private AudioServiceProvider provider;
	
	
	private String uri;
	private long duration;
	private Image albumCover;
	
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
		albumCover=builder.albumCover;
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
	public Image getAlbumCover(){
		return albumCover;
	}
	
	public String toString(){
		return "Title : "+title + "\n Artist : "+artist+ "\n Uri : "+uri+"\n";
	}
}
