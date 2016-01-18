package com.namal.arch.models.services;

import java.util.List;

import com.namal.arch.models.Playlist;
import com.namal.arch.models.Song;

public interface AudioService {

	public List<Playlist> getPlaylists();
	public void authenticate();
	public boolean authenticationNeeded();
	public List<Song> searchTrack();
	
}
