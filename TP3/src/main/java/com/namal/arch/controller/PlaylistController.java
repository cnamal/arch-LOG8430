package com.namal.arch.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.namal.arch.models.Song;
import com.namal.arch.models.SongBuilder;
import com.namal.arch.models.services.AudioService;
import com.namal.arch.utils.APIHelper;
import com.namal.arch.utils.Configuration;
import com.namal.arch.utils.Constants;
import com.namal.arch.utils.ErrorBuilder;
import org.springframework.web.bind.annotation.*;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@RestController
@RequestMapping("/playlists")
public class PlaylistController {

	private String badRequest(String message, HttpServletResponse response){
		return ErrorBuilder.error(400, message, response);
	}
	
	@RequestMapping(method = RequestMethod.GET)
    public String getPlaylists(@RequestParam(value=Constants.TOKEN) String token,HttpServletResponse response) {
		JsonArrayBuilder builder = Json.createArrayBuilder();
		Iterator<Entry<String, String>> iterator;
		try {
			iterator = Session.getServices(token);
		} catch (UnauthorizedException e) {
			return ErrorBuilder.unauthorizedError(response);
		}
		
		while(iterator.hasNext()){
			JsonObjectBuilder object = Json.createObjectBuilder();
			Entry<String,String> entry = iterator.next();
			object.add(Constants.SERVICEID, entry.getKey());
			AudioService service = Configuration.getAudioServiceLoader().getService(entry.getKey());
			object.add(Constants.PLAYLISTS, service.getPlaylists(entry.getValue()));
			builder.add(object);
		}
		
        return APIHelper.dataResponse(builder);
    }
	
	
	@RequestMapping(method = RequestMethod.POST)
	public String postPlaylists(@RequestBody Map<String, Object> map,HttpServletResponse response){
		try{
			String serviceId = (String)map.get(Constants.SERVICEID);
			if(serviceId == null)
				return badRequest(Constants.requiredParamError(Constants.SERVICEID), response);
			String name = (String) map.get(Constants.NAME);
			if(name == null)
				return badRequest(Constants.requiredParamError(Constants.NAME), response);
			String token = (String) map.get(Constants.TOKEN);
			if(!serviceId.equals("0") && token == null )
				return badRequest(Constants.requiredParamError(Constants.TOKEN), response);
			Boolean pub = (Boolean) map.get(Constants.PUB);
			if(pub == null)
				return badRequest(Constants.requiredParamError(Constants.PUB), response);
			String authToken =null;
            if(!serviceId.equals("0"))
                authToken= Session.getAuthToken(token,serviceId);
            AudioService service = Configuration.getAudioServiceLoader().getService(serviceId);
            if(service==null)
                return ErrorBuilder.error(404, Constants.unfoundServiceError(serviceId),response);
            return APIHelper.dataResponse(service.getAudioServiceProvider().createPlaylist(name,pub,authToken));
		}catch (UnauthorizedException e){
            return ErrorBuilder.unauthorizedError(response);
        }catch(Exception e){
            e.printStackTrace();
			return badRequest(Constants.incorrectTypeError(), response);
		}
	}
	
	@RequestMapping(method = RequestMethod.PUT)
	public String putPlaylists(@RequestBody Data data,HttpServletResponse response){
		try{
			String serviceId = data.getServiceId();
            if(serviceId == null)
                return badRequest(Constants.requiredParamError(Constants.SERVICEID), response);
            String id = data.getId();
            if(id == null)
                return badRequest(Constants.requiredParamError(Constants.ID), response);
            String token = data.getToken();
            if(!serviceId.equals("0") && token == null )
                return badRequest(Constants.requiredParamError(Constants.TOKEN), response);
            List<SongBuilder> songBuilders = data.getSongs();
            if(songBuilders == null)
                return badRequest(Constants.requiredParamError(Constants.SONGS), response);
            List<Song> songs = new ArrayList<>();
            for(SongBuilder s : songBuilders)
                songs.add(s.build());
            String authToken =null;
            if(!serviceId.equals("0"))
                authToken= Session.getAuthToken(token,serviceId);
            AudioService service = Configuration.getAudioServiceLoader().getService(serviceId);
            if(service==null)
                return ErrorBuilder.error(404, Constants.unfoundServiceError(serviceId),response);
            service.getAudioServiceProvider().updatePlaylist(id,songs.iterator(),authToken);
		}catch (UnauthorizedException e){
            return ErrorBuilder.unauthorizedError(response);
        }catch(Exception e){
            e.printStackTrace();
			return badRequest(Constants.incorrectTypeError(), response);
		}
		return "";
	}
	
	@RequestMapping(method = RequestMethod.DELETE)
	public String deletePlaylists(@RequestBody Map<String, Object> map,HttpServletResponse response){
		try{
			String serviceId = (String)map.get(Constants.SERVICEID);
			if(serviceId == null)
				return badRequest(Constants.requiredParamError(Constants.SERVICEID), response);
			String id = (String) map.get(Constants.ID);
			if(id == null)
				return badRequest(Constants.requiredParamError(Constants.ID), response);
			String token = (String) map.get(Constants.TOKEN);
			if(!serviceId.equals("0") && token == null )
				return badRequest(Constants.requiredParamError(Constants.TOKEN), response);
            String authToken =null;
            if(!serviceId.equals("0"))
                authToken= Session.getAuthToken(token,serviceId);
            AudioService service = Configuration.getAudioServiceLoader().getService(serviceId);
            if(service==null)
                return ErrorBuilder.error(404, Constants.unfoundServiceError(serviceId),response);
            service.getAudioServiceProvider().deletePlaylist(id,authToken);
		}catch (UnauthorizedException e){
            return ErrorBuilder.unauthorizedError(response);
        }catch(Exception e){
			System.err.println(e.getMessage());
			return badRequest(Constants.incorrectTypeError(), response);
		}
		return "";
	}

    private static class Data  {

        private String serviceId = null;
        private String id = null;
        private List<SongBuilder> songs = null;
        private String token = null;


        /**
         **/
        @JsonProperty("serviceId")
        public String getServiceId() {
            return serviceId;
        }
        public void setServiceId(String serviceId) {
            this.serviceId = serviceId;
        }


        /**
         **/
        @JsonProperty("id")
        public String getId() {
            return id;
        }
        public void setId(String id) {
            this.id = id;
        }


        /**
         **/
        @JsonProperty("songs")
        public List<SongBuilder> getSongs() {
            return songs;
        }
        public void setSongs(List<SongBuilder> songs) {
            this.songs = songs;
        }


        /**
         **/
        @JsonProperty("token")
        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

    }
}
