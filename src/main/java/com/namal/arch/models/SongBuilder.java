package com.namal.arch.models;

import com.namal.arch.models.services.AudioServiceProvider;

import javafx.scene.image.Image;

public class SongBuilder {

	String title;
	String artist;
	String uri;
	Image albumCover = null;
	AudioServiceProvider provider;
	long duration;
	boolean durationDefined;
	
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
	
	public SongBuilder setAlbumCover(Image albumCover){
		this.albumCover=albumCover;
		return this;
	}
	
	public SongBuilder setProvider(AudioServiceProvider provider){
		this.provider=provider;
		return this;
	}
	
	public SongBuilder setDuration(long duration){
		this.duration=duration;
		durationDefined=true;
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
		if(!durationDefined)
			throw new SongMalformed("Song duration is needed");
		if(provider==null)
			throw new SongMalformed("Song provider is needed");
	}
}
