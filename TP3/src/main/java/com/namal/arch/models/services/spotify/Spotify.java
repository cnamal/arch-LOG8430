package com.namal.arch.models.services.spotify;

import com.namal.arch.models.ProviderInformation;
import com.namal.arch.models.SongBuilder;
import com.namal.arch.models.SongMalformed;
import com.namal.arch.models.services.AudioService;
import com.namal.arch.models.services.AudioServiceProvider;
import com.namal.arch.models.services.IAuthentification;
import com.namal.arch.utils.Configuration;

import javax.json.*;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import static com.namal.arch.utils.Constants.*;

/**
 * Spotify service
 * @author namalgac
 *
 */
public class Spotify implements AudioService {

	private static Spotify instance = new Spotify();
	static final String BASEURL = "https://api.spotify.com/v1/";
	private static final String SEARCHURL = BASEURL+"search";
	private static final String MYPLAYLISTS = BASEURL + "me/playlists";
	static final String USERURL = BASEURL + "users/";


	private SpotifyAuthentication authentication;
	private SpotifyProvider provider;

	private Spotify() {
		authentication = SpotifyAuthentication.getInstance();
		provider = SpotifyProvider.getInstance(this);
	}

	public static Spotify getInstance() {
		return instance;
	}

	private JsonObjectBuilder songBuilder(JsonObject result) throws SongMalformed {
		SongBuilder builder = SongBuilder.songBuilder()
				.setId(result.getString("id"))
				.setTitle(result.getString("name"))
				.setArtist(result.getJsonArray("artists").getValuesAs(JsonObject.class).get(0).getString("name"))
				.setService(this)
				.setDuration(result.getInt("duration_ms"));
		if(!result.isNull("preview_url"))
			builder.setUri(result.getString("preview_url"));
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
	public JsonArrayBuilder getPlaylists(String authToken)  {
        URL url;
        JsonArrayBuilder res = Json.createArrayBuilder();
        try {
            url = new URL(MYPLAYLISTS);
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setRequestMethod("GET");
            httpCon.setRequestProperty(
                    "Authorization", "Bearer " + authToken);
            JsonReader rdr = Json.createReader(httpCon.getInputStream());
            JsonObject obj = rdr.readObject();
            JsonArray results = obj.getJsonArray("items");
            for (JsonObject playlist : results.getValuesAs(JsonObject.class)) {
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                objectBuilder.add(ID,playlist.getString("id"));
                objectBuilder.add(TITLE,playlist.getString("name"));
                objectBuilder.add(PUB, playlist.getBoolean("public"));
                objectBuilder.add(SERVICEID, Configuration.getAudioServiceLoader().getProviderId(this));
                String trackUrl = playlist.getJsonObject("tracks").getString("href");
                url = new URL(trackUrl);
                httpCon = (HttpURLConnection) url.openConnection();
                httpCon.setRequestMethod("GET");
                httpCon.setRequestProperty(
                        "Authorization", "Bearer "+authToken );
                rdr = Json.createReader(httpCon.getInputStream());
                obj  = rdr.readObject();
                results = obj.getJsonArray("items");
                JsonArrayBuilder arrayBuilder= Json.createArrayBuilder();
                for (JsonObject result : results.getValuesAs(JsonObject.class)) {
                    try {
                        arrayBuilder.add(songBuilder(result.getJsonObject("track")));
                    } catch (SongMalformed e) {
                        e.printStackTrace();
                    }

                }
                objectBuilder.add(SONGS,arrayBuilder);
                res.add(objectBuilder);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
	}

	@Override
	public JsonArrayBuilder searchTrack(String track) {
        URL url;
        JsonArrayBuilder res = Json.createArrayBuilder();
        try {
            url = new URL(SEARCHURL + "?q=" + URLEncoder.encode(track, "UTF-8") + "&type=track");
            JsonReader rdr = Json.createReader(url.openStream());

            JsonObject tracks = rdr.readObject();
            tracks = tracks.getJsonObject("tracks");
            JsonArray results = tracks.getJsonArray("items");
            for (JsonObject result : results.getValuesAs(JsonObject.class)) {
                try {
                    res.add(songBuilder(result));
                } catch (SongMalformed e) {
                    e.printStackTrace();
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
	}
}
