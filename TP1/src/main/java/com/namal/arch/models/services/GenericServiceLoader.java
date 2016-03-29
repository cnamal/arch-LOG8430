package com.namal.arch.models.services;

import java.util.HashMap;
import java.util.List;

import org.bson.Document;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import com.namal.arch.utils.MongoDB;

/**
 * Generic Audio Service Loader (Helper class)
 * 
 * @author namalgac
 *
 */
public abstract class GenericServiceLoader implements IAudioServiceLoader{
	
	private static BiMap<String, AudioServiceProvider> audioServicesMap;
	protected static List<AudioService> audioServices;
	
	/**
	 * Initializes a bidirectional map
	 */
	protected void initMap(){
		audioServicesMap = HashBiMap.create();
		MongoDatabase db = MongoDB.getDatabase();
		FindIterable<Document> servicesIt=db.getCollection("services").find();
		HashMap<String, String> tmpMap = new HashMap<>();
		servicesIt.forEach(new Block<Document>(){
			@Override
		    public void apply(final Document document) {
				List<Document> providers = (List<Document>) document.get("providers");
				for(Document doc : providers)
					tmpMap.put(doc.getString("name"),doc.getString("id"));
			}
		});
		for(AudioService service :audioServices){
			String id = tmpMap.get(service.getProviderInformation().getName());
			if(id==null){
				System.err.println("Unregistered provider");
				// TODO TP3 -> create the provider
			}else
				audioServicesMap.put(id, service.getAudioServiceProvider());
		}
	}
	
	public String getProviderId(AudioServiceProvider service){
		return audioServicesMap.inverse().get(service);
	}
	
	
	public AudioServiceProvider getProvider(String serviceId){
		return audioServicesMap.get(serviceId);
	}

}
