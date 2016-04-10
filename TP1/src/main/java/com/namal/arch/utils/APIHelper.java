package com.namal.arch.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;

import org.apache.commons.io.IOUtils;

import com.namal.arch.models.Playlist;
import com.namal.arch.models.Song;
import com.namal.arch.models.SongBuilder;
import com.namal.arch.models.SongMalformed;
import com.namal.arch.models.services.AudioService;

public class APIHelper {
	
	public static Playlist searchTrack(String query, List<AudioService> servicesList) {
		Playlist playlist = new Playlist("Search results - "+query, true, false);
		String baseUri = Connexion.getURI();
		for(AudioService serv : servicesList) {
			try {
				URL url = new URL(baseUri+"/services/searchTrack?q="+URLEncoder.encode(query, "UTF-8")+"&serviceId="+serv.getServiceId());
				HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
				//httpCon.setDoOutput(true);
				httpCon.setRequestMethod("GET");
				httpCon.setRequestProperty(
					    "Content-Type", "application/json" );
				InputStream is = httpCon.getInputStream();
				JsonReader jsonReader = Json.createReader(is);
				JsonObject object = jsonReader.readObject();
				JsonArray data = object.getJsonArray("data");
				for(JsonObject singleData : data.getValuesAs(JsonObject.class)) {
					JsonArray songs = singleData.getJsonArray("songs");
					for(JsonObject song : songs.getValuesAs(JsonObject.class)) {
						String id = song.getString("id");
						String title = song.getString("title");
						String artist = song.getString("artist");
						String serviceId = song.getString("serviceId");
						int duration = song.getInt("duration");
						String uri = null;
						if(!song.isNull("uri")) {
							uri = song.getString("uri");
						}
						
						Song newSong = SongBuilder.songBuilder().setId(id).setTitle(title).setArtist(artist).setDuration(duration).setUri(uri).setServiceId(serviceId).build();
						playlist.addSongWithoutUpdating(newSong);
					}
				}
					is.close();
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (SongMalformed e) {
					e.printStackTrace();
				}
			}
		return playlist;
	}
}
