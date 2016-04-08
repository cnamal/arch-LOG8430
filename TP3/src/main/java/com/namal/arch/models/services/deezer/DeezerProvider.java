package com.namal.arch.models.services.deezer;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;

import com.namal.arch.models.Playlist;
import com.namal.arch.models.ProviderInformation;
import com.namal.arch.models.Song;
import com.namal.arch.models.services.AudioServiceProvider;
import com.namal.arch.models.services.ServiceEvent;
import com.namal.arch.utils.Configuration;

import static com.namal.arch.utils.Constants.*;
import static com.namal.arch.utils.Constants.SONGS;

class DeezerProvider implements AudioServiceProvider {
	
	private Deezer service;
	private static DeezerProvider instance;
	private InputStream inputStream;
	
	
	private DeezerProvider(Deezer service) {
		this.service=service;
	}
	
	static DeezerProvider getInstance(Deezer service){
		if(instance==null)
			instance=new DeezerProvider(service);
		return instance;
	}
	
	@Override
	public InputStream getInputStream(String uri) {
		URLConnection urlConnection;
		try {
			//Might be able to refact this code
			urlConnection = new URL ( uri).openConnection();
			urlConnection.connect ();
			return inputStream=urlConnection.getInputStream();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void closeInputStream() {
		try {
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void updatePlaylist(Playlist playlist){
		if(!service.isConnected())
			return; // TODO add Exception system
		URL url;
		
		try {
			String urlString = Deezer.PLAYLISTURL+playlist.getId()+"/tracks"+"?access_token="+service.getAuthToken()+"&request_method=POST&songs=";
			Iterator<Song> it = playlist.getSongs();
			while (it.hasNext()) {
				urlString+=it.next().getId()+",";
			}
			url = new URL(urlString);
			url.openConnection().getInputStream();
			
			service.update(ServiceEvent.USERPLAYLISTSUPDATED);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void addSongToPlaylist(Playlist playlist, Song addedSong) {
		updatePlaylist(playlist);
	}

	@Override
	public void removeSongFromPlaylist(Playlist playlist, Song removedSong) {
		updatePlaylist(playlist);
	}

	@Override
	public void createPlaylist(Playlist playlist) {
		System.out.println("createPlaylist");
		if(!service.isConnected())
			return; // TODO add Exception system
		URL url;
		
		try {
			url = new URL(Deezer.MYPLAYLISTS+"?access_token="+service.getAuthToken()+"&request_method=POST&title="+URLEncoder.encode(playlist.getName(), "UTF-8"));
			
			JsonReader rdr = Json.createReader(url.openConnection().getInputStream());
			JsonObject results = rdr.readObject();
			playlist.setId(results.getInt("id"));
			if(!playlist.getPub()){
				url = new URL(Deezer.PLAYLISTURL+playlist.getId()+"?access_token="+service.getAuthToken()+"&request_method=POST&public=false");
				url.openStream();
			}
			service.update(ServiceEvent.USERPLAYLISTSUPDATED);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public ProviderInformation getProviderInformation() {
		return SpotifyProviderInformation.getInstance();
	}
	
	@Override
	public void update(ServiceEvent ev) {
		service.update(ev);
	}

	@Override
	public JsonObjectBuilder createPlaylist(String name, Boolean pub, String authToken) {
		URL url;
        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
		try {
			url = new URL(Deezer.MYPLAYLISTS+"?access_token="+authToken+"&request_method=POST&title="+URLEncoder.encode(name, "UTF-8"));

			JsonReader rdr = Json.createReader(url.openConnection().getInputStream());
			JsonObject results = rdr.readObject();
			String res= results.getInt("id")+"";
            objectBuilder.add(ID,results.getInt("id"));
            if(!pub){
                url = new URL(Deezer.PLAYLISTURL+res+"?access_token="+authToken+"&request_method=POST&public=false");
                url.openStream();
            }
            objectBuilder.add(TITLE,name);
            objectBuilder.add(SERVICEID, Configuration.getAudioServiceLoader().getProviderId(service));
            objectBuilder.add(PUB,pub);
            objectBuilder.add(SONGS,Json.createArrayBuilder());
			service.update(ServiceEvent.USERPLAYLISTSUPDATED);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return objectBuilder;
	}

	@Override
	public void updatePlaylist(String id, Iterator<Song> songs, String authToken) {
        URL url;

        try {
            String urlString = Deezer.PLAYLISTURL+id+"/tracks"+"?access_token="+authToken+"&request_method=POST&songs=";
            while (songs.hasNext()) {
                urlString+=songs.next().getId()+",";
            }
            url = new URL(urlString);
            url.openConnection().getInputStream();

            service.update(ServiceEvent.USERPLAYLISTSUPDATED);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	@Override
	public void deletePlaylist(String id, String authToken) {

	}

	private static class SpotifyProviderInformation extends ProviderInformation{
		
		private static final String name = "Deezer";
		private static final String logoURL = Configuration.getUrlBase()+"/img/Deezer.png";
		private static final SpotifyProviderInformation instance = new SpotifyProviderInformation();
		
		private SpotifyProviderInformation(){
			super(name,logoURL);
			
		}
		
		public static SpotifyProviderInformation getInstance(){
			return instance;
		}
	}
}
