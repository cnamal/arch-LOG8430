package com.namal.arch.view.wrapper;

import com.namal.arch.models.Playlist;

import javafx.scene.layout.VBox;

public class VBoxPlaylist {
	private VBox vbox;
	private Playlist playlist;
	
	public VBoxPlaylist(VBox vbox, Playlist playlist){
		this.vbox = vbox;
		this.playlist = playlist;
	}

	public VBox getVbox() {
		return vbox;
	}

	public void setVbox(VBox vbox) {
		this.vbox = vbox;
	}

	public Playlist getPlaylist() {
		return playlist;
	}

	public void setPlaylist(Playlist playlist) {
		this.playlist = playlist;
	}
}
