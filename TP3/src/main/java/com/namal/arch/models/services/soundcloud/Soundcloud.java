package com.namal.arch.models.services.soundcloud;

import com.namal.arch.models.ProviderInformation;
import com.namal.arch.models.SongBuilder;
import com.namal.arch.models.SongMalformed;
import com.namal.arch.models.services.AudioService;
import com.namal.arch.models.services.AudioServiceProvider;
import com.namal.arch.models.services.IAuthentification;
import com.namal.arch.utils.Configuration;

import javax.json.*;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;

import static com.namal.arch.utils.Constants.*;

/**
 * Soundcloud service
 * @author namalgac
 *
 */
public class Soundcloud implements AudioService {

	private static Soundcloud instance = new Soundcloud();
	static final String clientId = "467af8ca6a20d82958569c3c248446f3";
	private static final String BASEURL = "http://api.soundcloud.com/";
	private static final String SONGSURL = BASEURL + "tracks/";
	private static final String MYPLAYLISTS = BASEURL + "me/playlists/";
	static final String PLAYLISTURL = BASEURL + "playlists/";


	private SoundcloudAuthentication authentication;
	private SoundcloudProvider provider;

	private Soundcloud() {
		authentication = SoundcloudAuthentication.getInstance();
		provider = SoundcloudProvider.getInstance(this);
	}

	/**
	 * 
	 * @return instance of Soundcloud service
	 */
	public static Soundcloud getInstance() {
		return instance;
	}

	private JsonObjectBuilder songBuilder(JsonObject result) throws SongMalformed {
		SongBuilder builder = SongBuilder.songBuilder().setId(result.getInt("id")+"").setTitle(result.getString("title"))
				.setArtist(result.getJsonObject("user").getString("username")).setUri(result.getString("stream_url")+"?client_id="+clientId)
				.setService(this).setDuration(result.getInt("duration"));
		return builder.build().toJsonObjectBuilder();
	}

	@Override
	public ProviderInformation getProviderInformation() {
		return provider.getProviderInformation();
	}

	@Override
	public String toString() {
		return getProviderInformation().toString();
	}

	@Override
	public AudioServiceProvider getAudioServiceProvider() {
		return provider;
	}

	@Override
	public IAuthentification getAuthentification() {
		return authentication;
	}

	@Override
	public boolean searchAvailable() {
		return true;
	}

	@Override
	public JsonArrayBuilder searchTrack(String track) {
		URL url;
		JsonArrayBuilder res = Json.createArrayBuilder();
		try {
			url = new URL(SONGSURL + "?client_id=" + clientId + "&q=\"" + URLEncoder.encode(track, "UTF-8") + "\"");
			JsonReader rdr = Json.createReader(url.openStream());

			JsonArray results = rdr.readArray();
			results.getValuesAs(JsonObject.class).stream().filter(result -> result.getBoolean("streamable")).forEach(result -> {
				try {
					res.add(songBuilder(result));
				} catch (SongMalformed e) {
					e.printStackTrace();
				}
			});
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return res;
	}

	@Override
	public JsonArrayBuilder getPlaylists(String authToken) {
		JsonArrayBuilder res = Json.createArrayBuilder();
		try {
			URL url = new URL(MYPLAYLISTS + "?oauth_token=" + authToken);
			JsonReader rdr = Json.createReader(url.openStream());
			JsonArray results = rdr.readArray();
			results.getValuesAs(JsonObject.class).stream().filter(playlist -> !playlist.isNull("streamable") && playlist.getBoolean("streamable")).forEach(playlist -> {
				JsonObjectBuilder object = Json.createObjectBuilder();
				object.add(TITLE, playlist.getString("title"));
				object.add(ID, playlist.getInt("id")+"");
				object.add(SERVICEID, Configuration.getAudioServiceLoader().getServiceId(this));
				object.add(PUB, playlist.getString("sharing").equals("public"));
				JsonArrayBuilder songsJson = Json.createArrayBuilder();
				JsonArray songs = playlist.getJsonArray("tracks");
				songs.getValuesAs(JsonObject.class).stream().filter(song -> song.getBoolean("streamable")).forEach(song -> {
					try {
						songsJson.add(songBuilder(song));
					} catch (SongMalformed e) {
						e.printStackTrace();
					}
				});
				object.add(SONGS, songsJson);
				res.add(object);
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		return res;
	}
}
