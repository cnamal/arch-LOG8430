package com.namal.arch.models.services.cp;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoDatabase;
import com.namal.arch.models.Playlist;
import com.namal.arch.models.ProviderInformation;
import com.namal.arch.models.Song;
import com.namal.arch.models.services.AudioServiceProvider;
import com.namal.arch.models.services.ServiceEvent;
import com.namal.arch.utils.MongoDB;

class CrossPlatformProvider implements AudioServiceProvider {
	
	private CrossPlatform service;
	private static CrossPlatformProvider instance;
	private InputStream inputStream;
	
	
	private CrossPlatformProvider(CrossPlatform service) {
		this.service=service;
	}
	
	static CrossPlatformProvider getInstance(CrossPlatform service){
		if(instance==null)
			instance=new CrossPlatformProvider(service);
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
		System.out.println("Cross plat update playlist");
		MongoDatabase db = MongoDB.getDatabase();
		List<Document> list = new ArrayList<>();
		Iterator<Song> songs = playlist.getSongs();
		while(songs.hasNext()){
			Song song = songs.next();
			list.add(new Document()
					.append("id", song.getId())
					.append("title", song.getTitle())
					.append("artist", song.getArtist())
					.append("provider", song.getProviderId())
					.append("duration", song.getDuration())
					.append("uri", song.getUri()));
		}
		db.getCollection("playlists").updateOne(new Document("_id", new ObjectId(playlist.getId())),new Document("$set",new Document("songs",list)));
		service.update(ServiceEvent.USERPLAYLISTSUPDATED);
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
		MongoDatabase db = MongoDB.getDatabase();
		Document play = new Document()
				.append("title",playlist.getName())
				.append("public", playlist.getPub())
				.append("songs",new ArrayList<>());
		db.getCollection("playlists").insertOne(play);
		playlist.setId(play.getObjectId("_id").toString());
		service.update(ServiceEvent.USERPLAYLISTSUPDATED);
	}

	@Override
	public ProviderInformation getProviderInformation() {
		return SpotifyProviderInformation.getInstance();
	}

	@Override
	public void update(ServiceEvent ev) {
		service.update(ev);
	}
	
	private static class SpotifyProviderInformation extends ProviderInformation{
		
		private static final String name = "Crossplatform";
		private static final String logoURL = null;
		private static final SpotifyProviderInformation instance = new SpotifyProviderInformation();
		
		private SpotifyProviderInformation(){
			super(name,logoURL);
		}
		
		public static SpotifyProviderInformation getInstance(){
			return instance;
		}
	}
}
