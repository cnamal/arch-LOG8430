package com.namal.arch.models.services.soundcloud;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.apache.commons.io.IOUtils;

import com.namal.arch.models.Playlist;
import com.namal.arch.models.ProviderInformation;
import com.namal.arch.models.Song;
import com.namal.arch.models.SongBuilder;
import com.namal.arch.models.SongMalformed;
import com.namal.arch.models.services.AudioService;
import com.namal.arch.models.services.AudioServiceProvider;
import com.namal.arch.models.services.IAuthentification;
import com.namal.arch.utils.SharedPreferences;

public class Soundcloud implements AudioService {

	private static Soundcloud instance= new Soundcloud();
	static final String clientId="467af8ca6a20d82958569c3c248446f3";
	 static final String BASEURL = "http://api.soundcloud.com/";
	 static final String SONGSURL = BASEURL+"tracks/";
	 static final String MYPLAYLISTS = BASEURL + "me/playlists/";
	 static final String PLAYLISTURL = BASEURL+"playlists/";
	
	private List<Playlist> playlists = null;
	
	private SoundcloudAuthentication authentication;
	private SoundcloudProvider provider;
	
	private Soundcloud(){
		authentication= SoundcloudAuthentication.getInstance(this);
		provider=SoundcloudProvider.getInstance(this);
	}
	
	String getAuthToken(){
		return authentication.getAuthToken();
	}
	
	public static Soundcloud getInstance(){
		return instance;
	}
	
	
	private Song songBuilder(JsonObject result) throws SongMalformed{
		SongBuilder builder = SongBuilder.songBuilder()
				.setId(result.getInt("id"))
				.setTitle(result.getString("title"))
				.setArtist(result.getJsonObject("user").getString("username"))
				.setUri(result.getString("stream_url"))
				.setProvider(provider)
				.setDuration(result.getInt("duration"));
		if(!result.isNull("artwork_url"))
			builder.setAlbumCoverUrl(result.getString("artwork_url"));
		return builder.build();
	}

	@Override
	public List<Playlist> getPlaylists() {
		// TODO Auto-generated method stub
		if(!authentication.isConnected())
			return null; // TODO add Exception system
		URL url;
		if(playlists != null)
			return playlists;
		try {
			url = new URL(MYPLAYLISTS+"?oauth_token="+authentication.getAuthToken());

			InputStream is = url.openStream();
			JsonReader rdr = Json.createReader(is);
			List<Playlist> playlists = new ArrayList<>();
			JsonArray results = rdr.readArray();
			for (JsonObject playlist : results.getValuesAs(JsonObject.class)) {
				if(!playlist.isNull("streamable") && playlist.getBoolean("streamable")){
					Playlist p = new Playlist(playlist.getString("title"), playlist.getInt("id"),provider,playlist.getString("sharing").equals("public"));
					JsonArray songs = playlist.getJsonArray("tracks");
					for (JsonObject song : songs.getValuesAs(JsonObject.class)){
						if(song.getBoolean("streamable")){
							p.addSongWithoutUpdating(songBuilder(song));
						}
					}
					playlists.add(p);
				}
			}
			this.playlists = playlists;
			return playlists;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SongMalformed e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean authenticationNeeded() {
		return false;
	}

	@Override
	public ProviderInformation getProviderInformation() {
		return provider.getProviderInformation();
	}

	public String toString(){
		return getProviderInformation().toString();
	}
	
	

	@Override
	public Playlist searchTrack(String track) {
		URL url;
		try {
			url = new URL(SONGSURL+"?client_id="+clientId+"&q=\""+URLEncoder.encode(track, "UTF-8")+"\"");

			InputStream is = url.openStream();
			JsonReader rdr = Json.createReader(is);

			JsonArray results = rdr.readArray();
			Playlist playlist = new Playlist("Search results",true,true);
			for (JsonObject result : results.getValuesAs(JsonObject.class)) {
				if(result.getBoolean("streamable")){
					
					playlist.addSongWithoutUpdating(songBuilder(result));
				}
			}
			return playlist;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SongMalformed e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
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
	public boolean isConnected() {
		return authentication.isConnected();
	}

	@Override
	public void disconnect() {
		authentication.disconnect();
	}
	
	void notify(SoundcloudEvent ev){
		if(ev==SoundcloudEvent.USERPLAYLISTSUPDATED)
			playlists=null;
		else
			throw new UnsupportedOperationException(ev +" is not supported");
	}
}
