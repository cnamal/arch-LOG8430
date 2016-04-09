package com.namal.arch.models.services.deezer;

import com.namal.arch.models.Playlist;
import com.namal.arch.models.ProviderInformation;
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

import javax.json.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import static com.namal.arch.utils.Constants.*;
import static org.apache.coyote.http11.Constants.a;

/**
 * Deezer service
 * @author namalgac
 *
 */
public class Deezer implements AudioService {

    private static Deezer instance = new Deezer();
    static final String BASEURL = "https://api.deezer.com/";
    static final String SEARCHURL = BASEURL+"search";
    static final String MYPLAYLISTS = BASEURL + "user/me/playlists";
    static final String PLAYLISTURL = BASEURL + "playlist/";

    private List<Playlist> playlists = null;
    private int nbPlaylists=0;

    private DeezerAuthentication authentication;
    private DeezerProvider provider;

    private Deezer() {
        authentication = DeezerAuthentication.getInstance(this);
        provider = DeezerProvider.getInstance(this);
    }

    String getAuthToken() {
        return authentication.getAuthToken();
    }

    /**
     *
     * @return instance of Deezer service
     */
    public static Deezer getInstance() {
        return instance;
    }

    private JsonObjectBuilder songBuilder(JsonObject result) throws SongMalformed {
        SongBuilder builder = SongBuilder.songBuilder()
                .setId(result.getInt("id")+"")
                .setTitle(result.getString("title"))
                .setArtist(result.getJsonObject("artist").getString("name"))
                .setService(this)
                .setDuration(result.getInt("duration")*1000)
                .setUri(result.getString("preview"));
		/*if (!result.isNull("artwork_url"))
			builder.setAlbumCoverUrl(result.getString("artwork_url"));*/
		/*if(!result.isNull("preview_url"))
			builder.setUri(result.getString("preview_url"));*/

        return builder.build().toJsonObjectBuilder();
    }

    @Override
    public ProviderInformation getProviderInformation() {
        return provider.getProviderInformation();
    }

    public String toString() {
        return getProviderInformation().toString();
    }

    @Override
    public AudioServiceProvider getAudioServiceProvider() {
        // TODO Auto-generated method stub
        return provider;
    }

    @Override
    public IAuthentification getAuthentification() {
        // TODO Auto-generated method stub
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
            url = new URL(MYPLAYLISTS+"?access_token="+authToken);
            JsonReader rdr = Json.createReader(url.openStream());
            List<Playlist> playlists = new ArrayList<>();
            Deezer.this.playlists = playlists;
            JsonObject obj = rdr.readObject();
            JsonArray results = obj.getJsonArray("data");
            nbPlaylists=0;
            int max = results.size();
            for (JsonObject playlist : results.getValuesAs(JsonObject.class)) {
                Playlist p = new Playlist(playlist.getString("title"), playlist.getInt("id"), provider,
                        playlist.getBoolean("public"));
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                objectBuilder.add(ID,playlist.getString("id"));
                objectBuilder.add(TITLE,playlist.getString("title"));
                objectBuilder.add(PUB, playlist.getBoolean("public"));
                objectBuilder.add(SERVICEID, Configuration.getAudioServiceLoader().getProviderId(this));
                String trackUrl = playlist.getString("link");
                trackUrl = PLAYLISTURL + trackUrl.substring(trackUrl.lastIndexOf("/") + 1) + "?access_token=" + authToken;
                url = new URL(trackUrl);
                rdr = Json.createReader(url.openStream());
                obj  = rdr.readObject();
                results = obj.getJsonObject("tracks").getJsonArray("data");
                JsonArrayBuilder arrayBuilder= Json.createArrayBuilder();
                for (JsonObject result : results.getValuesAs(JsonObject.class)) {
                    try {
                        arrayBuilder.add(songBuilder(result));

                    } catch (SongMalformed e) {
                        e.printStackTrace();
                    }

                }
                objectBuilder.add(SONGS,arrayBuilder);
                res.add(objectBuilder);
            }
            //URL url;
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
            url = new URL(SEARCHURL + "?q=track:" + URLEncoder.encode(track, "UTF-8"));
            JsonReader rdr = Json.createReader(url.openStream());

            JsonObject tracks = rdr.readObject();
            JsonArray results = tracks.getJsonArray("data");
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
