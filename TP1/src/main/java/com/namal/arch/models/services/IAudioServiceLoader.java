package com.namal.arch.models.services;

import java.util.Iterator;

public interface IAudioServiceLoader {
	
	public Iterator<AudioService> getAudioServices();
	
	public String getProviderId(AudioServiceProvider service);
	
	public AudioServiceProvider getProvider(String serviceId);

}
