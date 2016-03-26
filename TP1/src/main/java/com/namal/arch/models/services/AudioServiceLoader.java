package com.namal.arch.models.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.namal.arch.models.services.soundcloud.Soundcloud;
import com.namal.arch.models.services.spotify.Spotify;


public class AudioServiceLoader implements IAudioServiceLoader{

	private static List<AudioService> audioServices;
	private static AudioServiceLoader instance = new AudioServiceLoader();
	/**
	 * 
	 * @return an iterator on the list of the AudioServices available
	 */
	public Iterator<AudioService> getAudioServices(){
		if(audioServices==null){
			audioServices=new ArrayList<>();
			audioServices.add(Soundcloud.getInstance());
			audioServices.add(Spotify.getInstance());
		}
		return audioServices.iterator();
	}
	
	public static IAudioServiceLoader getInstance(){
		return instance;
	}
	
	private AudioServiceLoader(){};
	
}
