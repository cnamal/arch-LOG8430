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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void closeInputStream() {
		// TODO Auto-generated method stub
		try {
			inputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
		service.notify(CrossPlatformEvent.USERPLAYLISTSUPDATED);
	}
	
	@Override
	public void addSongToPlaylist(Playlist playlist, Song addedSong) {
		updatePlaylist(playlist);
	}

	@Override
	public void removerSongFromPlaylist(Playlist playlist, Song removedSong) {
		updatePlaylist(playlist);
	}

	/**
	 * Creates a playlist on the provider server from a Playlist of the software
	 * @param playlist the Playlist to save
	 */
	@Override
	public void createPlaylist(Playlist playlist) {
		// TODO Auto-generated method stub
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
		service.notify(CrossPlatformEvent.USERPLAYLISTSUPDATED);
		//FindIterable<Document> playlistColletction=db.getCollection("playlists")
		/*URL url;
		
		try {
			//System.out.println("savePlaylist");
			url = new URL(CrossPlatform.MYPLAYLISTS+"?access_token="+service.getAuthToken()+"&request_method=POST&title="+URLEncoder.encode(playlist.getName(), "UTF-8"));
			
			//httpCon.getInputStream();
			
			//String theString = IOUtils.toString(httpCon.getInputStream(), "UTF-8");
			JsonReader rdr = Json.createReader(url.openConnection().getInputStream());
			JsonObject results = rdr.readObject();
			playlist.setId(results.getInt("id"));
			if(!playlist.getPub()){
				url = new URL(CrossPlatform.PLAYLISTURL+playlist.getId()+"?access_token="+service.getAuthToken()+"&request_method=POST&public=false");
				url.openStream();
			}
			service.notify(CrossPlatformEvent.USERPLAYLISTSUPDATED);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}

	@Override
	public ProviderInformation getProviderInformation() {
		return SpotifyProviderInformation.getInstance();
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
