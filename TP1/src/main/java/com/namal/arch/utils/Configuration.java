package com.namal.arch.utils;

import com.namal.arch.models.services.IAudioServiceLoader;

public class Configuration {

	private Configuration(){};
	
	private static IAudioServiceLoader serviceLoader;
	private static boolean show;
	
	public static void setAudioServiceLoader(IAudioServiceLoader audioSL){
		serviceLoader = audioSL;
	}
	
	public static IAudioServiceLoader getAudioServiceLoader(){
		return serviceLoader;
	}

	public static boolean getShow() {
		return show;
	}

	public static void setShow(boolean show) {
		Configuration.show = show;
	}
	
}
