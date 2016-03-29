package com.namal.arch.models.services;

import java.util.ArrayList;
import java.util.Iterator;

import com.namal.arch.models.services.cp.CrossPlatform;
import com.namal.arch.models.services.deezer.Deezer;
import com.namal.arch.models.services.soundcloud.Soundcloud;
import com.namal.arch.models.services.spotify.Spotify;

/**
 * Audio Service Loader for 1st lab
 * @author namalgac
 *
 */
public class AudioServiceLoader extends GenericServiceLoader{
	
	
	private static AudioServiceLoader instance = new AudioServiceLoader();
	
	public Iterator<AudioService> getAudioServices(){
		if(audioServices==null){			
			audioServices = new ArrayList<>();
			audioServices.add(CrossPlatform.getInstance());
			audioServices.add(Soundcloud.getInstance());
			audioServices.add(Spotify.getInstance());
			audioServices.add(Deezer.getInstance());
			initMap();
		}
		return audioServices.iterator();
	}
	
	public static IAudioServiceLoader getInstance(){
		return instance;
	}
	
	private AudioServiceLoader(){};
	
}
