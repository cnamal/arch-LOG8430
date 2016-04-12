package com.namal.arch.utils;

/**
 * Class with all the constants
 * @author Namal
 */
public class Constants {

	public static final String SERVICEID= "serviceId";
	public static final String NAME= "name";
	public static final String PUB = "pub";
	public static final String TOKEN= "token";
	public static final String ID = "id";
	public static final String SONGS = "songs";
	public static final String PLAYLISTS = "playlists";
	public static final String URL = "url";
	static final String DATA = "data";
	public static final String TITLE = "title";
	public static final String ARTIST = "artist";
	public static final String DURATION = "duration";
	public static final String URI = "uri";

    /**
     *
     * @param serviceId service ID
     * @return Message
     */
	public static String unfoundServiceError(String serviceId){
		return "Service "+serviceId+" doesn't exist";
	}

    /**
     *
     * @param param parameter
     * @return Message
     */
	public static String requiredParamError(String param){
		return param + " parameter is required";
	}

    /**
     *
     * @return Message
     */
	public static String incorrectTypeError(){
		return "A parameter doesn't have the correct type.";
	}
}
