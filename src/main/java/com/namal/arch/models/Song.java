package com.namal.arch.models;

import java.io.InputStream;

import com.namal.arch.models.services.AudioServiceProvider;

public class Song {

	private String title;
	private String singer;
	private String album;
	private AudioServiceProvider provider;
	
	private String uri;
	
	public InputStream getInputStream(){
		return provider.getInputStream(uri);
	}
	
	public void cleanup(){
		provider.closeInputStream();
	}
	
	/*
	 * TO REMOVE IF NOT OK
	 * By Adrien
	 */
	public Song(String title, String singer, String album){
		this.title = title;
		this.singer = singer;
		this.album = album;
	}

	public String getTitle() {
		return title;
	}

	public String getSinger() {
		return singer;
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
	
}
