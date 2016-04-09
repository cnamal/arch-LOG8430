package com.namal.arch.models.services;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import com.namal.arch.utils.Constants;
import com.namal.arch.utils.MongoDB;
import org.bson.Document;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

/**
 * Generic Audio Service Loader (Helper class)
 * 
 * @author namalgac
 *
 */
abstract class GenericServiceLoader implements IAudioServiceLoader{
	
	private static BiMap<String, AudioService> audioServicesMap;
	static List<AudioService> audioServices;
	private static final String CONNECTURL = "connectUrl";
	private static final String IMAGEURL = "imageUrl";
    private static final String SEARCH = "searchAvailable";
	
	/**
	 * Initializes a bidirectional map
	 */
	protected void initMap(){
		audioServicesMap = HashBiMap.create();
		MongoDatabase db = MongoDB.getDatabase();
		FindIterable<Document> servicesIt=db.getCollection("services").find();
		HashMap<String, String> tmpMap = new HashMap<>();
		servicesIt.forEach((Block<Document>) document -> {
            List<Document> providers = (List<Document>) document.get("providers");
            for(Document doc : providers)
                tmpMap.put(doc.getString("name"),doc.getString("id"));
        });
		for(AudioService service :audioServices){
			String id = tmpMap.get(service.getProviderInformation().getName());
			if(id==null){
				System.err.println("Unregistered provider");
				// TODO TP3 -> create the provider
			}else
				audioServicesMap.put(id, service);
		}
	}
	
	public String getProviderId(AudioService service){
		return audioServicesMap.inverse().get(service);
	}
	
	
	public AudioServiceProvider getProvider(String serviceId){
		return audioServicesMap.get(serviceId).getAudioServiceProvider();
	}

	public AudioService getService(String serviceId){
		return audioServicesMap.get(serviceId);
	}
	
	public JsonArrayBuilder getAudioServicesJson(){
        JsonArrayBuilder res = Json.createArrayBuilder();
		for(Entry<String,AudioService> entry :audioServicesMap.entrySet()){
			JsonObjectBuilder object = Json.createObjectBuilder();
			object.add(Constants.SERVICEID, entry.getKey());
			AudioService service = entry.getValue();
			object.add(Constants.NAME, service.getProviderInformation().getName());
            object.add(SEARCH,service.searchAvailable());
            if(service.getAuthentification().getAuthentificationUrl()!=null)
                object.add(CONNECTURL, service.getAuthentification().getAuthentificationUrl());
            else
                object.add(CONNECTURL, JsonValue.NULL);
            if(service.getProviderInformation().getLogoUrl()!=null)
			    object.add(IMAGEURL, service.getProviderInformation().getLogoUrl());
            else
                object.add(IMAGEURL,JsonValue.NULL);
			res.add(object);
		}
		return res;
	}
	
}
