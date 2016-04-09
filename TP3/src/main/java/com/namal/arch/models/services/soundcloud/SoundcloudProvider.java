package com.namal.arch.models.services.soundcloud;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.json.*;

import com.namal.arch.utils.Configuration;
import org.apache.commons.io.IOUtils;

import com.namal.arch.models.Playlist;
import com.namal.arch.models.ProviderInformation;
import com.namal.arch.models.Song;
import com.namal.arch.models.services.AudioServiceProvider;
import com.namal.arch.models.services.ServiceEvent;

import static com.namal.arch.utils.Constants.*;

class SoundcloudProvider implements AudioServiceProvider {
	
	private Soundcloud service;
	private static SoundcloudProvider instance;
	private InputStream inputStream;
	
	
	private SoundcloudProvider(Soundcloud service) {
		// TODO Auto-generated constructor stub
		this.service=service;
	}
	
	static SoundcloudProvider getInstance(Soundcloud service){
		if(instance==null)
			instance=new SoundcloudProvider(service);
		return instance;
	}

	@Override
	public ProviderInformation getProviderInformation() {
		return SoundcloudProviderInformation.getInstance();
	}

	@Override
	public void update(ServiceEvent ev) {
		service.update(ev);
	}

	@Override
	public JsonObjectBuilder createPlaylist(String name, Boolean pub, String authToken) {
		URL url;
        String res = null;
        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
        try {
            System.out.println("savePlaylist");
            url = new URL(Soundcloud.PLAYLISTURL+"?client_id="+Soundcloud.clientId);
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setDoOutput(true);
            httpCon.setRequestMethod("POST");
            httpCon.setRequestProperty(
					"Content-Type", "application/x-www-form-urlencoded" );
            httpCon.setRequestProperty(
					"Authorization", "OAuth "+authToken );
            OutputStreamWriter out = new OutputStreamWriter(
					httpCon.getOutputStream());
            String data = URLEncoder.encode("playlist[title]", "UTF-8");
            data+="=";
            data+=URLEncoder.encode(name, "UTF-8");
            data+="&";
            data+=URLEncoder.encode("playlist[sharing]", "UTF-8");
            data+="=";
            data+=pub?"public":"private";
            System.out.println(data);
            out.write(data);
            out.close();

            JsonReader rdr = Json.createReader(httpCon.getInputStream());
            JsonObject results = rdr.readObject();
			objectBuilder.add(ID,results.getInt("id"));
			objectBuilder.add(TITLE,results.getString("title"));
            objectBuilder.add(SERVICEID, Configuration.getAudioServiceLoader().getProviderId(service));
            objectBuilder.add(PUB,pub);
            objectBuilder.add(SONGS,Json.createArrayBuilder());
			service.update(ServiceEvent.USERPLAYLISTSUPDATED);
            return objectBuilder;
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
            System.out.println("savePlaylist");
            url = new URL(Soundcloud.PLAYLISTURL+id+"?oauth_token="+authToken);
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setDoOutput(true);
            httpCon.setRequestMethod("PUT");
            httpCon.setRequestProperty(
                    "Content-Type", "application/json" );
            OutputStreamWriter out = new OutputStreamWriter(
                    httpCon.getOutputStream());

            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
            JsonObjectBuilder trackBuilder = Json.createObjectBuilder();
            JsonArrayBuilder songBuilder = Json.createArrayBuilder();
            while (songs.hasNext()){
                JsonObjectBuilder tmp = Json.createObjectBuilder();
                tmp.add(ID,songs.next().getId());
                songBuilder.add(tmp);
            }
            trackBuilder.add("tracks",songBuilder);
            objectBuilder.add("playlist",trackBuilder);
            //objectBuilder.add("playlist")
            out.write(objectBuilder.build().toString());
            out.close();

            String theString = IOUtils.toString(httpCon.getInputStream(), "UTF-8");
            System.out.println(theString);
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

	private static class SoundcloudProviderInformation extends ProviderInformation{
		
		private static final String name = "Soundcloud";
		private static final String logoURL = "https://developers.soundcloud.com/assets/logo_big_black-4fbe88aa0bf28767bbfc65a08c828c76.png";
		private static final SoundcloudProviderInformation instance = new SoundcloudProviderInformation();
		
		private SoundcloudProviderInformation(){
			super(name,logoURL);
			
		}
		
		public static SoundcloudProviderInformation getInstance(){
			return instance;
		}
	}
}
