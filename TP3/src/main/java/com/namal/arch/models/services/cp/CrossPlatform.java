package com.namal.arch.models.services.cp;

import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import com.namal.arch.models.ProviderInformation;
import com.namal.arch.models.SongBuilder;
import com.namal.arch.models.SongMalformed;
import com.namal.arch.models.services.AudioService;
import com.namal.arch.models.services.AudioServiceProvider;
import com.namal.arch.models.services.IAuthentification;
import com.namal.arch.utils.Configuration;
import com.namal.arch.utils.MongoDB;
import org.bson.Document;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import java.util.List;

import static com.namal.arch.utils.Constants.*;

/**
 * Cross platform service
 * @author namalgac
 *
 */
public class CrossPlatform implements AudioService {

	private static CrossPlatform instance = new CrossPlatform();


	private CrossPlatformAuthentication authentication;
	private CrossPlatformProvider provider;

	private CrossPlatform() {
		authentication = CrossPlatformAuthentication.getInstance();
		provider = CrossPlatformProvider.getInstance(this);
	}


	/**
	 * 
	 * @return instance of Cross platform service
	 */
	public static CrossPlatform getInstance() {
		return instance;
	}

	private JsonObjectBuilder songBuilder(Document result) throws SongMalformed {
		SongBuilder builder = SongBuilder.songBuilder()
				.setId(result.getString("id"))
				.setTitle(result.getString("title"))
				.setArtist(result.getString("artist"))
				.setProvider(result.getString("provider"))
				.setDuration(result.getLong("duration"))
				.setUri(result.getString("uri"));
		/*if (!result.isNull("artwork_url"))
			builder.setAlbumCoverUrl(result.getString("artwork_url"));*/
		/*if(!result.isNull("preview_url"))
			builder.setUri(result.getString("preview_url"));*/
		
		return builder.build().toJsonObjectBuilder();
	}

	@Override
	public boolean searchAvailable(){
		return false;
	}
	
	@Override
	public ProviderInformation getProviderInformation() {
		return provider.getProviderInformation();
	}

	public String toString() {
		return getProviderInformation().toString();
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
	public JsonArrayBuilder searchTrack(String track) {
		System.err.println("Called search track when searchAvailable is false. Bad programmer !");
		return null;
	}


	@Override
	public JsonArrayBuilder getPlaylists(String token) {
		JsonArrayBuilder res = Json.createArrayBuilder();
		MongoDatabase db = MongoDB.getDatabase();
		FindIterable<Document> playlistColletction=db.getCollection("playlists").find();
		//List<Playlist> playlists = new ArrayList<>();
		//CrossPlatform.this.playlists = playlists;
		playlistColletction.forEach(new Block<Document>() {
		    @Override
		    public void apply(final Document document) {
		    	/*
		    	 * document.getString("title"), document.getObjectId("_id").toString(), provider,
							document.getBoolean("public")
		    	 */
		    	JsonObjectBuilder object = Json.createObjectBuilder();
		    	object.add(TITLE, document.getString("title"));
		    	object.add(ID, document.getObjectId("_id").toString());
		    	object.add(SERVICEID, Configuration.getAudioServiceLoader().getProviderId(instance));
		    	object.add(PUB	, document.getBoolean("public"));
		    	JsonArrayBuilder songsJson = Json.createArrayBuilder();
		        List<Document> songs = (List<Document>)document.get("songs");
		        for(Document song:songs){
		        	try {
						songsJson.add(songBuilder(song));
					} catch (SongMalformed e) {
						e.printStackTrace();
					}
		        }
		        object.add(SONGS, songsJson);
		        res.add(object);
		        //playlists.add(p);
		    }
		});
		return res;
	}
}
