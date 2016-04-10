package com.namal.arch.models.services;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import com.namal.arch.utils.ConnexionToken;
import org.apache.commons.io.IOUtils;

import com.namal.arch.utils.Connexion;

/**
 * Audio Service Loader for 3rd lab
 * @author Fabien Berquez
 *
 */
public class AudioServiceLoader {

    private List<AudioService> audioServices;
    private static AudioServiceLoader instance = new AudioServiceLoader();
    private HashMap<String, String> idToUriMap;

    public Iterator<AudioService> getAudioServices(){
        if(audioServices==null){
            audioServices = new ArrayList<>();
            if(idToUriMap == null){
            	idToUriMap = new HashMap<String, String>();
            }
            String baseUri = Connexion.getURI();
            try {
                URL url = new URL(baseUri+"/services");
                HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
                //httpCon.setDoOutput(true);
                httpCon.setRequestMethod("GET");
                httpCon.setRequestProperty(
                        "Content-Type", "application/json" );
                InputStream is = httpCon.getInputStream();
                JsonReader jsonReader = Json.createReader(is);
                JsonObject object = jsonReader.readObject();
                JsonArray services = object.getJsonArray("data");
                for(JsonObject service : services.getValuesAs(JsonObject.class)) {
                    String serviceId = service.getString("serviceId");
                    String name = service.getString("name");
                    String connectURL = null;
                    String imageURL = null;
                    if(!service.isNull("connectUrl")) {
                        connectURL = service.getString("connectUrl");
                    }
                    if(!service.isNull("imageUrl")) {
                        imageURL = service.getString("imageUrl");
                    }
                    idToUriMap.put(serviceId, imageURL);
                    boolean searchAvailable = service.getBoolean("searchAvailable");

                    AudioService newService = new AudioService(serviceId, name, connectURL, imageURL, searchAvailable);
                    if(newService.getConnectUrl()==null )
                        newService.setConnected(true);
                    audioServices.add(newService);
                }
                is.close();
                String strUrlServer = Connexion.getURI();
                strUrlServer = strUrlServer+"/services/connect?serviceId=0&url=null";
                URL urlServer;
                urlServer = new URL(strUrlServer);
                InputStream serverResponse = urlServer.openStream();
                JsonReader rdr = Json.createReader(serverResponse);
                ConnexionToken.getInstance().setToken(rdr.readObject().getJsonObject("data").getString("token"));

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {

                e.printStackTrace();
            }


        }
        return audioServices.iterator();
    }

    public static AudioServiceLoader getInstance(){
        return instance;
    }
    
    public String getUriFromId(String id){
    	if(idToUriMap == null)
    		getAudioServices();
    	return idToUriMap.get(id);
    }

    private AudioServiceLoader(){};

}
