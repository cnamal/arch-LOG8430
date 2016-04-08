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
	
	
	private static AudioServiceLoader instance ;
	
	public Iterator<AudioService> getAudioServices(){
		return audioServices.iterator();
	}
	
	private void init(){
		audioServices = new ArrayList<>();
		audioServices.add(CrossPlatform.getInstance());
		audioServices.add(Soundcloud.getInstance());
		audioServices.add(Spotify.getInstance());
		audioServices.add(Deezer.getInstance());
		initMap();
	}
	
	public static IAudioServiceLoader getInstance(){
		if(instance==null){
			instance = new AudioServiceLoader();
			instance.init();
		}
		return instance;
	}
	
	private AudioServiceLoader(){};
	
}
