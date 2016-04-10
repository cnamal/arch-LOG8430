package com.namal.arch.utils;

import com.namal.arch.models.Playlist;
import com.namal.arch.models.Song;
import com.namal.arch.models.SongBuilder;
import com.namal.arch.models.SongMalformed;

import javax.json.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by adrien on 16-04-08.
 */
public class PlaylistManager {

    static private PlaylistManager instance;

    static public PlaylistManager getInstance(){
        if(instance == null){
            instance = new PlaylistManager();
        }
        return instance;
    }

    /**
     * Get all the playlists of all services authenicated
     * @param callback Callback function when the request is over and the list of playlist has been got
     */
    public void getPlaylists(ServiceListener<List<Playlist>> callback){
        try {
            String token = ConnexionToken.getInstance().getToken();
            if(token == null){
                //TODO : Manage if the token is null
            }
            URL url = new URL(Connexion.getURI()+"/playlists?token=" + token);
            WebThread webThread = new WebThread(url, new WebListener() {

                @Override
                public void done(InputStream is){
                    if(is!=null) {
                        JsonReader rdr = Json.createReader(is);

                        List<Playlist> res_playlists = new ArrayList<Playlist>();
                        JsonArray data = rdr.readObject().getJsonArray("data");
                        for (JsonObject services : data.getValuesAs(JsonObject.class)) {
                            JsonArray playlists = services.getJsonArray("playlists");
                            for (JsonObject playlist : playlists.getValuesAs(JsonObject.class)) {
                                Playlist aux_playlist = new Playlist(playlist.getString("title"),
                                        playlist.getString("id"),
                                        playlist.getString("serviceId"),
                                        playlist.getBoolean("pub"));
                                JsonArray songs = playlist.getJsonArray("songs");
                                for (JsonObject song : songs.getValuesAs(JsonObject.class)) {
                                    try {
                                        SongBuilder builder = SongBuilder.songBuilder().setId(song.getString("id"))
                                                .setTitle(song.getString("title"))
                                                .setArtist(song.getString("artist"))
                                                .setServiceId(song.getString("serviceId"))
                                                .setDuration(song.getInt("duration"));
                                        if(!song.isNull("uri"))
                                            builder.setUri(song.getString("uri"));
                                        Song song_aux = builder.build();
                                        aux_playlist.addSongWithoutUpdating(song_aux);
                                    } catch (SongMalformed e) {
                                        e.printStackTrace();
                                    }
                                }
                                res_playlists.add(aux_playlist);
                            }
                        }
                        callback.done(res_playlists);
                    } else
                        callback.done(null);
                }
            });
            new Thread(webThread).start();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void createPlaylist(Playlist playlist){
        try {
            String token = ConnexionToken.getInstance().getToken();
            if(token == null){
                //TODO : Manage if the token is null
            }
            URL url = new URL(Connexion.getURI()+"/playlists");

            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setDoOutput(true);
            httpCon.setRequestMethod("POST");
            httpCon.setRequestProperty(
                    "Content-Type", "application/json" );
            OutputStreamWriter out = new OutputStreamWriter(
                    httpCon.getOutputStream());
            JsonObjectBuilder object =Json.createObjectBuilder();
            object.add("serviceId", playlist.getServiceId());
            object.add("name", playlist.getName());
            object.add("pub", playlist.getPub());
            object.add("token", token);
            out.write(object.build().toString());
            out.close();

            //System.out.println(org.apache.commons.io.IOUtils.toString(httpCon.getInputStream()));

            JsonReader rdr = Json.createReader(httpCon.getInputStream());
            JsonObject results = rdr.readObject().getJsonObject("data");
            playlist.setId(results.getString("id"));

            updatePlaylist(playlist);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updatePlaylist(Playlist playlist){
        try {
            String token = ConnexionToken.getInstance().getToken();
            if(token == null){
                //TODO : Manage if the token is null
            }
            URL url = new URL(Connexion.getURI()+"/playlists");

            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setDoOutput(true);
            httpCon.setRequestMethod("PUT");
            httpCon.setRequestProperty(
                    "Content-Type", "application/json" );
            OutputStreamWriter out = new OutputStreamWriter(
                    httpCon.getOutputStream());

            JsonArrayBuilder songs = Json.createArrayBuilder();
            Iterator<Song> it = playlist.getSongs();
            while(it.hasNext()){
                JsonObjectBuilder songObject = Json.createObjectBuilder();
                Song aux = it.next();
                songObject.add("id", aux.getId())
                        .add("title", aux.getTitle())
                        .add("artist", aux.getArtist())
                        .add("serviceId", aux.getServiceId())
                        .add("duration", aux.getDuration())
                        .add("uri", aux.getUri());
                songs.add(songObject);
            }

            JsonObjectBuilder object = Json.createObjectBuilder();
            object.add("serviceId", playlist.getServiceId())
                    .add("id", playlist.getId())
                    .add("songs", songs)
                    .add("token", token);
            out.write(object.build().toString());
            out.close();
            httpCon.getInputStream();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deletePlaylist(Playlist playlist){
        try {
            String token = ConnexionToken.getInstance().getToken();
            if(token == null){
                //TODO : Manage if the token is null
            }
            URL url = new URL(Connexion.getURI()+"/playlists");

            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setDoOutput(true);
            httpCon.setRequestMethod("DELETE");
            httpCon.setRequestProperty(
                    "Content-Type", "application/json" );
            OutputStreamWriter out = new OutputStreamWriter(
                    httpCon.getOutputStream());

            JsonObjectBuilder object = Json.createObjectBuilder();
            object.add("serviceId", playlist.getServiceId())
                    .add("id", playlist.getId())
                    .add("token", token);
            out.write(object.build().toString());
            out.close();
            httpCon.getInputStream();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
