package com.namal.arch.models.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.namal.arch.models.services.soundcloud.Soundcloud;


public class AudioServiceLoader {

	private static List<AudioService> audioServices;
	
	public static Iterator<AudioService> getAudioServices(){
		if(audioServices==null){
			audioServices=new ArrayList<>();
			audioServices.add(Soundcloud.getInstance());
		}
		return audioServices.iterator();
	}
	
	private AudioServiceLoader(){};
	
}
