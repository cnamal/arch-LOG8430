package com.namal.arch.models;

import com.namal.arch.models.services.AudioServiceProvider;

/**
 * Builder class to create a song.
 * @author namalgac
 *
 */
public class SongBuilder {

	String title;
	String artist;
	String uri;
	String albumCoverUrl = null;
	String id;
	String providerId;
	AudioServiceProvider provider;
	Long duration;
	
	private SongBuilder() {}
	
	/**
	 * Creates a song builder
	 * @return the newly creater builder
	 */
	public static SongBuilder songBuilder() {
		return new SongBuilder();
	}
	
	/**
	 * 
	 * @param id id of the song
	 * @return the same objet with it's id set
	 */
	public SongBuilder setId(int id){
		this.id=id+"";
		return this;
	}
	
	/**
	 * 
	 * @param id id of the song
	 * @return the same objet with it's id set
	 */
	public SongBuilder setId(String id){
		this.id=id+"";
		return this;
	}
	
	/**
	 * 
	 * @param title song's title
	 * @return the same objet with it's id set
	 */
	public SongBuilder setTitle(String title){
		this.title=title;
		return this;
	}
	
	/**
	 * 
	 * @param artist song's artist
	 * @return the same objet with it's id set
	 */
	public SongBuilder setArtist(String artist){
		this.artist=artist;
		return this;
	}
	
	/**
	 * 
	 * @param uri link to the audio of the song.
	 * @return the same objet with it's id set
	 */
	public SongBuilder setUri(String uri){
		this.uri=uri;
		return this;
	}
	
	/**
	 * 
	 * @param albumCoverUrl link to the song's cover
	 * @return the same objet with it's id set
	 */
	public SongBuilder setAlbumCoverUrl(String albumCoverUrl){
		this.albumCoverUrl=albumCoverUrl;
		return this;
	}
	
	/**
	 * 
	 * @param provider song provider
	 * @return the same objet with it's id set
	 */
	public SongBuilder setProvider(AudioServiceProvider provider){
		this.provider=provider;
		return this;
	}
	
	/**
	 * 
	 * @param providerId song provider's id
	 * @return the same objet with it's id set
	 */
	public SongBuilder setProvider(String providerId){
		this.providerId=providerId;
		return this;
	}
	
	/**
	 * 
	 * @param duration duration of the song
	 * @return the same objet with it's id set
	 */
	public SongBuilder setDuration(long duration){
		this.duration=duration;
		return this;
	}
	
	/**
	 * Verifies the data and creates a song
	 * @return the song 
	 * @throws SongMalformed if the id/title/artist/duration/provider isn't set
	 * Note : for the provider, it's id (String) or the provider (AudioServiceProvider) has to be set. 
	 * Setting both is useless.
	 */
	public Song build() throws SongMalformed{
		validate();
		return new Song(this);
	}
	
	private void validate() throws SongMalformed{
		if(id==null)
			throw new SongMalformed("Song id is needed");
		if(title==null)
			throw new SongMalformed("Song title is needed");
		if(artist==null)
			throw new SongMalformed("Song artist is needed");
		if(duration==null)
			throw new SongMalformed("Song duration is needed");
		if(provider==null&&providerId==null)
			throw new SongMalformed("Song provider is needed");
	}
}
