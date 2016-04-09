package com.namal.arch.utils;

import com.namal.arch.models.services.IAudioServiceLoader;

/**
 * Configuration of the project
 * @author namalgac
 *
 */
public class Configuration {

	private Configuration(){}
	
	private static IAudioServiceLoader serviceLoader;
	private static String urlBase;
	
	/**
	 * 
	 * @param audioSL Audio service loader that is going to be used
	 */
	public static void setAudioServiceLoader(IAudioServiceLoader audioSL){
		serviceLoader = audioSL;
	}
	
	/**
	 * 
	 * @return Audio service loader that is used
	 */
	public static IAudioServiceLoader getAudioServiceLoader(){
		return serviceLoader;
	}

	public static void setUrlBase(String base,int port){
		urlBase = "http://"+base+":"+port;
	}

	public static String getUrlBase(){
		return urlBase;
	}

}
