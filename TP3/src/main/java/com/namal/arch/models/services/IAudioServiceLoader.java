package com.namal.arch.models.services;

import java.util.Iterator;

import javax.json.JsonArrayBuilder;

/**
 * AudioService loader interface
 * @author namalgac
 *
 */
public interface IAudioServiceLoader {
	
	/**
	 * 
	 * @return list of all audio services
	 */
	public Iterator<AudioService> getAudioServices();
	
	/**
	 * Get the provider's id given a provider
	 * @param service provider
	 * @return provider's id
	 */
	public String getProviderId(AudioService service);
	
	/**
	 * Get the provider given it's id
	 * @param serviceId provider's id
	 * @return provider
	 */
	public AudioServiceProvider getProvider(String serviceId);

	/**
	 * Get the service given it's id
	 * @param serviceId provider's id
	 * @return service
	 */
	public AudioService getService(String serviceId);
	
	public JsonArrayBuilder getAudioServicesJson();
}
