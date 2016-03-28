package com.namal.arch.models;

import java.io.InputStream;

import com.namal.arch.models.services.AudioServiceProvider;
import com.namal.arch.utils.Configuration;

public class Song {

	private String id;
	private String title;
	private String artist;
	private String album;
	private AudioServiceProvider provider;
	private String providerId;
	
	
	private String uri;
	private long duration;
	private String albumCoverUrl;
	
	/**
	 * 
	 * @return the inputStream of the song, to be used in the player
	 */
	public InputStream getInputStream(){
		return provider.getInputStream(uri);
	}
	
	/**
	 * Closes the inputStream
	 */
	public void cleanup(){
		provider.closeInputStream();
	}
	
	/**
	 * Uses the SongBuilder to create a Song, initialising the attributes
	 * @param builder a SongBuilder
	 */
	Song(SongBuilder builder){
		title=builder.title;
		artist=builder.artist;
		uri=builder.uri;
		duration = builder.duration;
		albumCoverUrl=builder.albumCoverUrl;
		id=builder.id;
		if(builder.provider!=null){
			provider=builder.provider;
			providerId= Configuration.getAudioServiceLoader().getProviderId(provider);
		}else{
			providerId= builder.providerId;
			provider = Configuration.getAudioServiceLoader().getProvider(providerId);
		}
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
	 * @return the AudioServiceProvider of the song
	 */
	public AudioServiceProvider getProvider() {
		return provider;
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

	public String getProviderId() {
		return providerId;
	}
}
