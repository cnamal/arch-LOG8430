package com.namal.arch.models;

import com.namal.arch.models.services.AudioService;
import com.namal.arch.utils.Configuration;

/**
 * Builder class to create a song.
 * @author namalgac
 *
 */
public class SongBuilder {

	String title;
	String artist;
	String uri;
	String id;
	Long duration;
    String serviceId;

    private SongBuilder() {}
	
	/**
	 * Creates a song builder
	 * @return the newly created builder
	 */
	public static SongBuilder songBuilder() {
		return new SongBuilder();
	}
	

	
	/**
	 * 
	 * @param id id of the song
	 * @return the same object with it's id set
	 */
	public SongBuilder setId(String id){
		this.id=id+"";
		return this;
	}
	
	/**
	 * 
	 * @param title song's title
	 * @return the same object with it's id set
	 */
	public SongBuilder setTitle(String title){
		this.title=title;
		return this;
	}
	
	/**
	 * 
	 * @param artist song's artist
	 * @return the same object with it's id set
	 */
	public SongBuilder setArtist(String artist){
		this.artist=artist;
		return this;
	}
	
	/**
	 * 
	 * @param uri link to the audio of the song.
	 * @return the same object with it's id set
	 */
	public SongBuilder setUri(String uri){
		this.uri=uri;
		return this;
	}
	
	/**
	 * 
	 * @param service song's service
	 * @return the same object with it's id set
	 */
	public SongBuilder setService(AudioService service){
		this.serviceId= Configuration.getAudioServiceLoader().getProviderId(service);
		return this;
	}

	public SongBuilder setServiceId(String serviceId) {
        this.serviceId = serviceId;
        return this;
	}

	/**
	 * 
	 * @param duration duration of the song
	 * @return the same object with it's id set
	 */
	public SongBuilder setDuration(long duration){
		this.duration=duration;
		return this;
	}
	
	/**
	 * Verifies the data and creates a song
	 * @return the song 
	 * @throws SongMalformed if the id/title/artist/duration/serviceId isn't set
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
		if(serviceId==null)
			throw new SongMalformed("Song serviceId is needed");
	}


}
