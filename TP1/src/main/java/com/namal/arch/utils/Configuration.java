package com.namal.arch.utils;

import com.namal.arch.models.services.IAudioServiceLoader;

/**
 * Configuration of the project
 * @author namalgac
 *
 */
public class Configuration {

	private Configuration(){};
	
	private static IAudioServiceLoader serviceLoader;
	private static boolean show;
	
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

	/**
	 * 
	 * @return true if the main FX app has to be shown, false otherwise
	 */
	public static boolean getShow() {
		return show;
	}

	/**
	 * 
	 * @param show true if the main FX app has to be shown, false otherwise
	 */
	public static void setShow(boolean show) {
		Configuration.show = show;
	}
	
}
