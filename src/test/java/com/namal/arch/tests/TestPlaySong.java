package com.namal.arch.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import com.namal.arch.controller.PlayerController;
import com.namal.arch.models.Playlist;
import com.namal.arch.models.Song;
import com.namal.arch.models.services.AudioService;
import com.namal.arch.models.services.AudioServiceLoader;
import com.namal.arch.utils.PlayerStatus;

public class TestPlaySong {
	
	@Test
	public void testAudioServiceLoader() {
		List<String> audioServices = new ArrayList<String>();
		Iterator<AudioService> it =  AudioServiceLoader.getAudioServices();
		
		while(it.hasNext()) {
			audioServices.add(it.next().getProviderInformation().getName());
		}
		
		assertTrue(audioServices.size()==1);
		assertTrue(audioServices.get(0).equals("Soundcloud"));
		
	}
	
	@Test
	public void testSearchTrack() {
		String query = new String("Hello");
		Playlist results = new Playlist("Search Results",true,true);
		Iterator<AudioService> serv = AudioServiceLoader.getAudioServices();
		while(serv.hasNext()){
			Playlist aux = serv.next().searchTrack(query);			
			Iterator<Song> it = aux.getSongs();
			while(it.hasNext()){
				results.addSongWithoutUpdating(it.next());
			}
		}
		
		assertTrue(results.getSong(0).getTitle().equals("Hello - Adele"));
		
	}
	
	@Test
	public void testPlaySong() {
		String query = new String("Hello");
		Playlist results = new Playlist("Search Results",true,true);
		Iterator<AudioService> serv = AudioServiceLoader.getAudioServices();
		while(serv.hasNext()){
			Playlist aux = serv.next().searchTrack(query);			
			Iterator<Song> it = aux.getSongs();
			while(it.hasNext()){
				results.addSongWithoutUpdating(it.next());
			}
		}
		
		PlayerController pc = PlayerController.getInstance();
		pc.setPlaylist(results);
		assertTrue(pc.getCurrentPlaylist()==results);
		Song song = results.getSong(0);
		pc.play();
		assertTrue(pc.getStatus()==PlayerStatus.PLAYING);
		assertTrue(pc.getCurrentSongIndex()==0);
		pc.pause();
		assertTrue(pc.getStatus()==PlayerStatus.PAUSED);
		pc.resume();
		assertTrue(pc.getStatus()==PlayerStatus.PLAYING);
		pc.nextAndPlay();
		assertTrue(pc.getStatus()==PlayerStatus.PLAYING);
		assertTrue(pc.getCurrentSongIndex()==1);
		pc.stop();
		assertTrue(pc.getStatus()==PlayerStatus.STOPPED);
	}
}
