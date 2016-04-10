package com.namal.arch.models.services.cp;

import com.mongodb.client.MongoDatabase;
import com.namal.arch.models.ProviderInformation;
import com.namal.arch.models.Song;
import com.namal.arch.models.services.AudioServiceProvider;
import com.namal.arch.utils.Configuration;
import com.namal.arch.utils.MongoDB;
import org.bson.Document;
import org.bson.types.ObjectId;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.namal.arch.utils.Constants.*;

class CrossPlatformProvider implements AudioServiceProvider {
	
	private CrossPlatform service;
	private static CrossPlatformProvider instance;

	
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
	public JsonObjectBuilder createPlaylist(String name, Boolean pub, String authToken) {
		MongoDatabase db = MongoDB.getDatabase();
		Document play = new Document()
				.append(TITLE,name)
				.append("public", pub)
				.append(SONGS,new ArrayList<>());
		db.getCollection("playlists").insertOne(play);
		JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
        objectBuilder.add(ID,play.getObjectId("_id").toString());
		objectBuilder.add(TITLE,name);
		objectBuilder.add(SERVICEID, Configuration.getAudioServiceLoader().getProviderId(service));
		objectBuilder.add(PUB,pub);
		objectBuilder.add(SONGS,Json.createArrayBuilder());
		return objectBuilder;
	}

	@Override
	public void updatePlaylist(String id, Iterator<Song> songs, String authToken) {
        MongoDatabase db = MongoDB.getDatabase();
        List<Document> list = new ArrayList<>();
        while(songs.hasNext()){
            Song song = songs.next();
            list.add(new Document()
                    .append(ID, song.getId())
                    .append(TITLE, song.getTitle())
                    .append(ARTIST, song.getArtist())
                    .append(SERVICEID, song.getServiceId())
                    .append(DURATION, song.getDuration())
                    .append(URI, song.getUri()));
        }
        db.getCollection("playlists").updateOne(new Document("_id", new ObjectId(id)),new Document("$set",new Document("songs",list)));
	}

	@Override
	public void deletePlaylist(String id, String authToken) {
		MongoDatabase db = MongoDB.getDatabase();
		db.getCollection("playlists").deleteOne(new Document("_id", new ObjectId(id)));
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
