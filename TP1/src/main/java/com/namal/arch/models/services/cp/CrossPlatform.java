package com.namal.arch.models.services.cp;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import com.namal.arch.models.Playlist;
import com.namal.arch.models.ProviderInformation;
import com.namal.arch.models.Song;
import com.namal.arch.models.SongBuilder;
import com.namal.arch.models.SongMalformed;
import com.namal.arch.models.services.AudioService;
import com.namal.arch.models.services.AudioServiceProvider;
import com.namal.arch.models.services.IAuthentification;
import com.namal.arch.utils.MongoDB;
import com.namal.arch.utils.ServiceListener;

public class CrossPlatform implements AudioService {

	private static CrossPlatform instance = new CrossPlatform();
	static final String BASEURL = "https://api.deezer.com/";
	static final String SEARCHURL = BASEURL+"search";
	static final String MYPLAYLISTS = BASEURL + "user/me/playlists";
	static final String PLAYLISTURL = BASEURL + "playlist/";

	private List<Playlist> playlists = null;

	private CrossPlatformAuthentication authentication;
	private CrossPlatformProvider provider;

	private CrossPlatform() {
		authentication = CrossPlatformAuthentication.getInstance(this);
		provider = CrossPlatformProvider.getInstance(this);
	}

	

	public static CrossPlatform getInstance() {
		return instance;
	}

	private Song songBuilder(Document result) throws SongMalformed {
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
		
		return builder.build();
	}

	@Override
	public void getPlaylists(ServiceListener<List<Playlist>> callback) {
		// TODO Auto-generated method stub
		if (!authentication.isConnected())
			return; // TODO add Exception system
		if (playlists != null)
			callback.done(playlists);
		else {
			MongoDatabase db = MongoDB.getDatabase();
			FindIterable<Document> playlistColletction=db.getCollection("playlists").find();
			List<Playlist> playlists = new ArrayList<>();
			CrossPlatform.this.playlists = playlists;
			playlistColletction.forEach(new Block<Document>() {
			    @Override
			    public void apply(final Document document) {
			    	Playlist p = new Playlist(document.getString("title"), document.getObjectId("_id").toString(), provider,
							document.getBoolean("public"));
			        List<Document> songs = (List<Document>)document.get("songs");
			        for(Document song:songs){
			        	try {
							p.addSongWithoutUpdating(songBuilder(song));
						} catch (SongMalformed e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			        }
			        playlists.add(p);
			    }
			});
			callback.done(playlists);
		}
	}

	@Override
	public boolean authenticationNeeded() {
		return false;
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
	public void searchTrack(String track, ServiceListener<Playlist> callback) {
		System.err.println("Called search track when searchAvailable is false. Bad programmer !");
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

	void notify(CrossPlatformEvent ev) {
		if (ev == CrossPlatformEvent.USERPLAYLISTSUPDATED)
			playlists = null;
		else
			throw new UnsupportedOperationException(ev + " is not supported");
	}
}
