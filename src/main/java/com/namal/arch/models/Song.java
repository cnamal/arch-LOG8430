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
	
}
