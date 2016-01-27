package com.namal.arch.models;

import com.namal.arch.models.services.AudioServiceProvider;

public class SongBuilder {

	String title;
	String artist;
	String uri;
	String art;
	AudioServiceProvider provider;
	
	private SongBuilder() {}
	
	public static SongBuilder songBuilder() {
		return new SongBuilder();
	}
	
	public SongBuilder setTitle(String title){
		this.title=title;
		return this;
	}
	
	public SongBuilder setArtist(String artist){
		this.artist=artist;
		return this;
	}
	
	public SongBuilder setUri(String uri){
		this.uri=uri;
		return this;
	}
	
	public SongBuilder setArtworkUrl(String art){
		this.art=art;
		return this;
	}
	
	public SongBuilder setProvider(AudioServiceProvider provider){
		this.provider=provider;
		return this;
	}
	
	public Song build() throws SongMalformed{
		validate();
		return new Song(this);
	}
	
	
	private void validate() throws SongMalformed{
		if(title==null)
			throw new SongMalformed("Song title is needed");
		if(artist==null)
			throw new SongMalformed("Song artist is needed");
		if(uri==null)
			throw new SongMalformed("Song uri is needed");
		if(provider==null)
			throw new SongMalformed("Song provider is needed");
	}
}
