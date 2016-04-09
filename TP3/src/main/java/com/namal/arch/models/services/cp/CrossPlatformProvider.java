package com.namal.arch.models.services.cp;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.namal.arch.utils.Configuration;
import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoDatabase;
import com.namal.arch.models.Playlist;
import com.namal.arch.models.ProviderInformation;
import com.namal.arch.models.Song;
import com.namal.arch.models.services.AudioServiceProvider;
import com.namal.arch.models.services.ServiceEvent;
import com.namal.arch.utils.MongoDB;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

import static com.namal.arch.utils.Constants.*;

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
	public ProviderInformation getProviderInformation() {
		return SpotifyProviderInformation.getInstance();
	}

	@Override
	public void update(ServiceEvent ev) {
		service.update(ev);
	}

	@Override
	public JsonObjectBuilder createPlaylist(String name, Boolean pub, String authToken) {
		MongoDatabase db = MongoDB.getDatabase();
		Document play = new Document()
				.append("title",name)
				.append("public", pub)
				.append("songs",new ArrayList<>());
		db.getCollection("playlists").insertOne(play);
		JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
        objectBuilder.add(ID,play.getObjectId("_id").toString());
		objectBuilder.add(TITLE,name);
		objectBuilder.add(SERVICEID, Configuration.getAudioServiceLoader().getProviderId(service));
		objectBuilder.add(PUB,pub);
		objectBuilder.add(SONGS,Json.createArrayBuilder());
		service.update(ServiceEvent.USERPLAYLISTSUPDATED);
		return objectBuilder;
	}

	@Override
	public void updatePlaylist(String id, Iterator<Song> songs, String authToken) {
        MongoDatabase db = MongoDB.getDatabase();
        List<Document> list = new ArrayList<>();
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
        db.getCollection("playlists").updateOne(new Document("_id", new ObjectId(id)),new Document("$set",new Document("songs",list)));
        service.update(ServiceEvent.USERPLAYLISTSUPDATED);
	}

	@Override
	public void deletePlaylist(String id, String authToken) {
		MongoDatabase db = MongoDB.getDatabase();
		db.getCollection("playlists").deleteOne(new Document("_id", new ObjectId(id)));
		service.update(ServiceEvent.USERPLAYLISTSUPDATED);
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
