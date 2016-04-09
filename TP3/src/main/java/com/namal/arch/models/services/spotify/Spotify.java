package com.namal.arch.models.services.spotify;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

import javax.json.*;

import com.namal.arch.models.Playlist;
import com.namal.arch.models.ProviderInformation;
import com.namal.arch.models.Song;
import com.namal.arch.models.SongBuilder;
import com.namal.arch.models.SongMalformed;
import com.namal.arch.models.services.AudioService;
import com.namal.arch.models.services.AudioServiceProvider;
import com.namal.arch.models.services.IAuthentification;
import com.namal.arch.models.services.ServiceEvent;
import com.namal.arch.utils.Configuration;
import com.namal.arch.utils.ServiceListener;
import com.namal.arch.utils.WebListener;
import com.namal.arch.utils.WebThread;

import static com.namal.arch.utils.Constants.*;

/**
 * Spotify service
 * @author namalgac
 *
 */
public class Spotify implements AudioService {

	private static Spotify instance = new Spotify();
	static final String clientId = "87824d0c4d1a4d2c8c72662b3b1a9f6e";
	static final String BASEURL = "https://api.spotify.com/v1/";
	static final String SEARCHURL = BASEURL+"search";
	static final String SONGSURL = BASEURL + "tracks/";
	static final String MYPLAYLISTS = BASEURL + "me/playlists";
	static final String PLAYLISTURL = BASEURL + "playlists/";
	static final String USERURL = BASEURL + "users/";

	private List<Playlist> playlists = null;
	private int nbPlaylists=0;

	private SpotifyAuthentication authentication;
	private SpotifyProvider provider;

	private Spotify() {
		authentication = SpotifyAuthentication.getInstance(this);
		provider = SpotifyProvider.getInstance(this);
	}

	String getAuthToken() {
		return authentication.getAuthToken();
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
		/*if (!result.isNull("artwork_url"))
			builder.setAlbumCoverUrl(result.getString("artwork_url"));*/
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
	
	void update(ServiceEvent ev) {
		if (ev == ServiceEvent.USERPLAYLISTSUPDATED)
			playlists = null;
		else
			throw new UnsupportedOperationException(ev + " is not supported");
	}

	@Override
	public JsonArrayBuilder getPlaylists(String authToken) {
        URL url;
        JsonArrayBuilder res = Json.createArrayBuilder();
        try {
            url = new URL(MYPLAYLISTS);
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setRequestMethod("GET");
            httpCon.setRequestProperty(
                    "Authorization", "Bearer " + getAuthToken());
            JsonReader rdr = Json.createReader(url.openStream());
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
                //System.out.println(getAuthToken());
                httpCon.setRequestProperty(
                        "Authorization", "Bearer "+getAuthToken() );
                rdr = Json.createReader(url.openStream());
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
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
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
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
	}
}
