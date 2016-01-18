package com.namal.arch.models.services;

import java.io.InputStream;

import com.namal.arch.models.Playlist;
import com.namal.arch.models.ProviderInformation;

public interface AudioServiceProvider {

	public InputStream getInputStream(String uri);
	public void closeInputStream();
	public void savePlaylist(Playlist playlist);
	public ProviderInformation getProviderInformation();
	
}
