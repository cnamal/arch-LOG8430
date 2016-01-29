package com.namal.arch.models;

import java.io.InputStream;

import com.namal.arch.models.services.AudioServiceProvider;

public class Song {

	private String title;
	private String artist;
	private String album;
	private AudioServiceProvider provider;
	
	private String uri;
	
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

	public void setProvider(AudioServiceProvider provider) {
		this.provider = provider;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
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
