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
	 * Get the service's id given a service
	 * @param service service
	 * @return service's id
	 */
	String getServiceId(AudioService service);
	
	/**
	 * Get the service given it's id
	 * @param serviceId service's id
	 * @return service
	 */
	AudioService getService(String serviceId);

	/**
	 *
	 * @return Json of all service information
     */
	JsonArrayBuilder getAudioServicesJson();
}
