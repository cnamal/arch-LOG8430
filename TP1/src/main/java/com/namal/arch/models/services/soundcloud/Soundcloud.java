package com.namal.arch.models.services.soundcloud;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import com.namal.arch.models.Playlist;
import com.namal.arch.models.ProviderInformation;
import com.namal.arch.models.Song;
import com.namal.arch.models.SongBuilder;
import com.namal.arch.models.SongMalformed;
import com.namal.arch.models.services.AudioService;
import com.namal.arch.models.services.AudioServiceProvider;
import com.namal.arch.models.services.IAuthentification;
import com.namal.arch.models.services.ServiceEvent;
import com.namal.arch.utils.ServiceListener;
import com.namal.arch.utils.WebListener;
import com.namal.arch.utils.WebThread;

/**
 * Soundcloud service
 * @author namalgac
 *
 */
public class Soundcloud implements AudioService {

	private static Soundcloud instance = new Soundcloud();
	static final String clientId = "467af8ca6a20d82958569c3c248446f3";
	static final String BASEURL = "http://api.soundcloud.com/";
	static final String SONGSURL = BASEURL + "tracks/";
	static final String MYPLAYLISTS = BASEURL + "me/playlists/";
	static final String PLAYLISTURL = BASEURL + "playlists/";

	private List<Playlist> playlists = null;

	private SoundcloudAuthentication authentication;
	private SoundcloudProvider provider;

	private Soundcloud() {
		authentication = SoundcloudAuthentication.getInstance(this);
		provider = SoundcloudProvider.getInstance(this);
	}

	String getAuthToken() {
		return authentication.getAuthToken();
	}

	/**
	 * 
	 * @return instance of Soundcloud service
	 */
	public static Soundcloud getInstance() {
		return instance;
	}

	private Song songBuilder(JsonObject result) throws SongMalformed {
		SongBuilder builder = SongBuilder.songBuilder().setId(result.getInt("id")).setTitle(result.getString("title"))
				.setArtist(result.getJsonObject("user").getString("username")).setUri(result.getString("stream_url"))
				.setProvider(provider).setDuration(result.getInt("duration"));
		if (!result.isNull("artwork_url"))
			builder.setAlbumCoverUrl(result.getString("artwork_url"));
		return builder.build();
	}

	@Override
	public void getPlaylists(ServiceListener<List<Playlist>> callback) {
		if (!authentication.isConnected())
			return; // TODO add Exception system
		URL url;
		if (playlists != null)
			callback.done(playlists);
		else {
			try {
				url = new URL(MYPLAYLISTS + "?oauth_token=" + authentication.getAuthToken());
				WebThread webThread = new WebThread(url, new WebListener() {
					
					@Override
					public void done(InputStream is) {
						if(is!=null){
							JsonReader rdr = Json.createReader(is);
							List<Playlist> playlists = new ArrayList<>();
							JsonArray results = rdr.readArray();
							for (JsonObject playlist : results.getValuesAs(JsonObject.class)) {
								if (!playlist.isNull("streamable") && playlist.getBoolean("streamable")) {
									Playlist p = new Playlist(playlist.getString("title"), playlist.getInt("id"), provider,
											playlist.getString("sharing").equals("public"));
									JsonArray songs = playlist.getJsonArray("tracks");
									for (JsonObject song : songs.getValuesAs(JsonObject.class)) {
										if (song.getBoolean("streamable")) {
											try {
												p.addSongWithoutUpdating(songBuilder(song));
											} catch (SongMalformed e) {
												e.printStackTrace();
											}
										}
									}
									playlists.add(p);
								}
							}
							Soundcloud.this.playlists = playlists;
							callback.done(playlists);
						}else
							callback.done(null);
					}
				});
				new Thread(webThread).start();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean authenticationNeeded() {
		return false;
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
	public void searchTrack(String track, ServiceListener<Playlist> callback) {
		URL url;
		try {
			url = new URL(SONGSURL + "?client_id=" + clientId + "&q=\"" + URLEncoder.encode(track, "UTF-8") + "\"");
			WebThread webThread = new WebThread(url, new WebListener() {

				@Override
				public void done(InputStream is) {
					if (is != null) {
						JsonReader rdr = Json.createReader(is);

						JsonArray results = rdr.readArray();
						Playlist playlist = new Playlist("Search results", true, true);
						for (JsonObject result : results.getValuesAs(JsonObject.class)) {
							if (result.getBoolean("streamable")) {
								try {
									playlist.addSongWithoutUpdating(songBuilder(result));
								} catch (SongMalformed e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
						callback.done(playlist);
					} else
						callback.done(null);
				}
			});

			Thread thread = new Thread(webThread);
			thread.start();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
}
