package com.namal.arch.models.services.deezer;

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
import com.namal.arch.utils.ServiceListener;
import com.namal.arch.utils.WebListener;
import com.namal.arch.utils.WebThread;

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

	public static Deezer getInstance() {
		return instance;
	}

	private Song songBuilder(JsonObject result) throws SongMalformed {
		SongBuilder builder = SongBuilder.songBuilder()
				.setId(result.getInt("id"))
				.setTitle(result.getString("title"))
				.setArtist(result.getJsonObject("artist").getString("name"))
				.setProvider(provider)
				.setDuration(result.getInt("duration")*1000)
				.setUri(result.getString("preview"));
		/*if (!result.isNull("artwork_url"))
			builder.setAlbumCoverUrl(result.getString("artwork_url"));*/
		/*if(!result.isNull("preview_url"))
			builder.setUri(result.getString("preview_url"));*/
		
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
				url = new URL(MYPLAYLISTS+"?access_token="+authentication.getAuthToken());
				WebThread webThread = new WebThread(url, new WebListener() {

					@Override
					public void done(InputStream is) {
						if(is!=null){
							JsonReader rdr = Json.createReader(is);
							List<Playlist> playlists = new ArrayList<>();
							Deezer.this.playlists = playlists;
							JsonObject obj = rdr.readObject();
							JsonArray results = obj.getJsonArray("data");
							nbPlaylists=0;
							int max = results.size();
							for (JsonObject playlist : results.getValuesAs(JsonObject.class)) {
								//if (!playlist.isNull("streamable") && playlist.getBoolean("streamable")) {
								Playlist p = new Playlist(playlist.getString("title"), playlist.getInt("id"), provider,
										playlist.getBoolean("public"));
								String trackUrl = playlist.getString("link");
								trackUrl=PLAYLISTURL+trackUrl.substring(trackUrl.lastIndexOf("/")+1)+"?access_token="+authentication.getAuthToken();
								playlists.add(p);
								URL url;
								try {
									url = new URL(trackUrl);
									WebThread webThread = new WebThread(url, new WebListener() {

										@Override
										public void done(InputStream is) {
											if(is!=null){
												JsonReader rdr = Json.createReader(is);
												JsonObject obj  = rdr.readObject();
												JsonArray results = obj.getJsonObject("tracks").getJsonArray("data");
												for (JsonObject result : results.getValuesAs(JsonObject.class)) {
													try {
														//System.out.println(result);
														p.addSongWithoutUpdating(songBuilder(result));
														synchronized (playlist) {
															nbPlaylists++;
															if(nbPlaylists==max)
																callback.done(playlists);
														}
													} catch (SongMalformed e) {
														// TODO Auto-generated catch block
														e.printStackTrace();
													}

												}
											}
										}
										
									});
									new Thread(webThread).start();
								} catch (MalformedURLException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} 
								
								/*JsonArray songs = playlist.getJsonArray("tracks");
								for (JsonObject song : songs.getValuesAs(JsonObject.class)) {
									if (song.getBoolean("streamable")) {
										try {
											p.addSongWithoutUpdating(songBuilder(song));
										} catch (SongMalformed e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
								}
								playlists.add(p);*/
								//}
							}
						}else
							callback.done(null);
					}
				});
				new Thread(webThread).start();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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

	public String toString() {
		return getProviderInformation().toString();
	}

	@Override
	public void searchTrack(String track, ServiceListener<Playlist> callback) {
		URL url;
		try {
			url = new URL(SEARCHURL + "?q=track:" + URLEncoder.encode(track, "UTF-8") );
			WebThread webThread = new WebThread(url, new WebListener() {

				@Override
				public void done(InputStream is) {
					if (is != null) {
						JsonReader rdr = Json.createReader(is);

						JsonObject tracks = rdr.readObject();
						JsonArray results = tracks.getJsonArray("data");
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
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

	void notify(DeezerEvent ev) {
		if (ev == DeezerEvent.USERPLAYLISTSUPDATED)
			playlists = null;
		else
			throw new UnsupportedOperationException(ev + " is not supported");
	}
}
