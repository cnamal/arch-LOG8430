package com.namal.arch.models;

import static com.namal.arch.utils.Constants.ARTIST;
import static com.namal.arch.utils.Constants.DURATION;
import static com.namal.arch.utils.Constants.ID;
import static com.namal.arch.utils.Constants.SERVICEID;
import static com.namal.arch.utils.Constants.TITLE;
import static com.namal.arch.utils.Constants.URI;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;

/**
 * Model of a song
 * @author namalgac
 *
 */
public class Song {

	private final String serviceId;
	private String id;
	private String title;
	private String artist;

	private String uri;
	private long duration;

	/**
	 * Uses the SongBuilder to create a Song, initializing the attributes
	 * @param builder a SongBuilder
	 */
	Song(SongBuilder builder){
		title=builder.title;
		artist=builder.artist;
		uri=builder.uri;
		duration = builder.duration;
		id=builder.id;
		serviceId= builder.serviceId;
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
	 * @return the id of the song (related to the provider)
	 */
	public String getId(){
		return id;
	}
	
	public String toString(){
		return "Title : "+title + "\n Artist : "+artist+ "\n Uri : "+uri+"\n";
	}
	
	@Override
	public boolean equals(Object o) {
		return o instanceof Song && uri.equals(((Song) o).uri);
	}

	/**
	 * 
	 * @return provider's id
	 */
	public String getServiceId() {
		return serviceId;
	}

	public JsonObjectBuilder toJsonObjectBuilder() {
		JsonObjectBuilder res = Json.createObjectBuilder();
		
		res.add(ID,id)
		.add(TITLE, title)
		.add(ARTIST, artist)
		.add(SERVICEID, serviceId)
		.add(DURATION, duration);
		if(uri!=null)
		    res.add(URI, uri);
        else
            res.add(URI, JsonValue.NULL);
		return res;
	}
}
