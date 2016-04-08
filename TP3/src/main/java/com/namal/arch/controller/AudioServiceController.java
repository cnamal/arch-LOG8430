package com.namal.arch.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.servlet.http.HttpServletResponse;

import com.namal.arch.utils.APIHelper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.namal.arch.models.services.AudioService;
import com.namal.arch.models.services.IAuthentification;
import com.namal.arch.utils.Configuration;
import com.namal.arch.utils.Constants;
import com.namal.arch.utils.ErrorBuilder;

@RestController
@RequestMapping("/services")
public class AudioServiceController {
	
	
	@RequestMapping(method = RequestMethod.GET)
    public String getServices() {
		return APIHelper.dataResponse(Configuration.getAudioServiceLoader().getAudioServicesJson());
    }
	
	@RequestMapping(value="/connect",method = RequestMethod.GET)
    public String connect(@RequestParam(value=Constants.SERVICEID) String serviceId,@RequestParam(value=Constants.URL) String url,@RequestParam(value=Constants.TOKEN,required=false)String token,HttpServletResponse response) {
		if(token == null)
			token =UUID.randomUUID().toString().replace("-", "");
		
		AudioService service = Configuration.getAudioServiceLoader().getService(serviceId);
		if(service==null)
			return ErrorBuilder.error(404, Constants.unfoundServiceError(serviceId),response);
		
		IAuthentification auth = service.getAuthentification();
		String authToken = auth.serverResponse(url);
		Session.addService(token, serviceId, authToken);

        return APIHelper.dataResponse(Json.createObjectBuilder().add(Constants.TOKEN,token));
    }
	
	@RequestMapping(value="/searchTrack",method = RequestMethod.GET)
    public String searchTrack(@RequestParam(Constants.SERVICEID) String[] serviceId,@RequestParam("q") String query, HttpServletResponse response) {
		List<AudioService> services = new ArrayList<>();
		for(String p : serviceId){
			AudioService service =Configuration.getAudioServiceLoader().getService(p);
			if(service!=null)
				services.add(service);
			else
				return ErrorBuilder.error(404, Constants.unfoundServiceError(p),response);
		}
		JsonArrayBuilder builder = Json.createArrayBuilder();
		for(AudioService service:services){
			JsonObjectBuilder object = Json.createObjectBuilder();
			object.add(Constants.SERVICEID, Configuration.getAudioServiceLoader().getProviderId(service));
			object.add(Constants.SONGS, service.searchTrack(query));
			builder.add(object);
		}
		
        return APIHelper.dataResponse(builder);
    }
	
	
}
