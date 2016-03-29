package com.namal.arch.models.services.spotify;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
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

	private Song songBuilder(JsonObject result) throws SongMalformed {
		SongBuilder builder = SongBuilder.songBuilder()
				.setId(result.getString("id"))
				.setTitle(result.getString("name"))
				.setArtist(result.getJsonArray("artists").getValuesAs(JsonObject.class).get(0).getString("name"))
				.setProvider(provider)
				.setDuration(result.getInt("duration_ms"));
		/*if (!result.isNull("artwork_url"))
			builder.setAlbumCoverUrl(result.getString("artwork_url"));*/
		if(!result.isNull("preview_url"))
			builder.setUri(result.getString("preview_url"));
		return builder.build();
	}

	@Override
	public void getPlaylists(ServiceListener<List<Playlist>> callback) {
		// TODO Auto-generated method stub
		if (!authentication.isConnected())
			return; // TODO add Exception system
		URL url;
		if (playlists != null)
			callback.done(playlists);
		else {
			try {
				url = new URL(MYPLAYLISTS);
				HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
				httpCon.setRequestMethod("GET");
				httpCon.setRequestProperty(
						"Authorization", "Bearer "+getAuthToken() );
				WebThread webThread = new WebThread(httpCon, new WebListener() {

					@Override
					public void done(InputStream is) {
						if(is!=null){
							JsonReader rdr = Json.createReader(is);
							List<Playlist> playlists = new ArrayList<>();
							Spotify.this.playlists = playlists;
							JsonObject obj = rdr.readObject();
							JsonArray results = obj.getJsonArray("items");
							nbPlaylists=0;
							int max = results.size();
							for (JsonObject playlist : results.getValuesAs(JsonObject.class)) {
								//if (!playlist.isNull("streamable") && playlist.getBoolean("streamable")) {
								Playlist p = new Playlist(playlist.getString("name"), playlist.getString("id"), provider,
										playlist.getBoolean("public"));
								String trackUrl = playlist.getJsonObject("tracks").getString("href");
								playlists.add(p);
								URL url;
								try {
									url = new URL(trackUrl);
									HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
									httpCon.setRequestMethod("GET");
									//System.out.println(getAuthToken());
									httpCon.setRequestProperty(
											"Authorization", "Bearer "+getAuthToken() );
									WebThread webThread = new WebThread(httpCon, new WebListener() {

										@Override
										public void done(InputStream is) {
											if(is!=null){
												JsonReader rdr = Json.createReader(is);
												JsonObject obj  = rdr.readObject();
												JsonArray results = obj.getJsonArray("items");
												for (JsonObject result : results.getValuesAs(JsonObject.class)) {
													try {
														p.addSongWithoutUpdating(songBuilder(result.getJsonObject("track")));
														synchronized (playlist) {
															nbPlaylists++;
															if(nbPlaylists==max)
																callback.done(playlists);
														}
													} catch (SongMalformed e) {
														e.printStackTrace();
													}

												}
											}
										}
										
									});
									new Thread(webThread).start();
								} catch (MalformedURLException e) {
									e.printStackTrace();
								} catch (IOException e) {
									e.printStackTrace();
								}
								
							}
						}else
							callback.done(null);
					}
				});
				new Thread(webThread).start();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	@Override
	public boolean authenticationNeeded() {
		return true;
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
			url = new URL(SEARCHURL + "?q=" + URLEncoder.encode(track, "UTF-8") + "&type=track");
			WebThread webThread = new WebThread(url, new WebListener() {

				@Override
				public void done(InputStream is) {
					if (is != null) {
						JsonReader rdr = Json.createReader(is);

						JsonObject tracks = rdr.readObject();
						tracks = tracks.getJsonObject("tracks");
						JsonArray results = tracks.getJsonArray("items");
						Playlist playlist = new Playlist("Search results", true, true);
						for (JsonObject result : results.getValuesAs(JsonObject.class)) {

							try {
								playlist.addSongWithoutUpdating(songBuilder(result));
							} catch (SongMalformed e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
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
		return provider;
	}

	@Override
	public IAuthentification getAuthentification() {
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
