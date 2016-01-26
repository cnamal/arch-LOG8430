package com.namal.arch.view;

import java.util.List;

import com.namal.arch.models.Playlist;
import com.namal.arch.view.wrapper.VBoxPlaylist;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class PlaylistOverview {
	
	//The scroll pane for the playlist
	@FXML
	private ScrollPane playlistScrollPane;
	
	//The AnchorPane inside the scroll pane
	@FXML
	private AnchorPane playlistAnchorPane;
	
	//Example playslistBox
	@FXML
	private VBox playlistBoxExample;
	
	//List of all the playlist saved
	private List<VBoxPlaylist> playlistList;
	
	private void resizePlaylistAnchorPane(){
		playlistAnchorPane.setPrefHeight(playlistList.size() * playlistBoxExample.getPrefHeight());
	}
	
	private void copyExample(VBox copied){
		copied.setPrefHeight(playlistBoxExample.getPrefHeight());
		copied.setFillWidth(playlistBoxExample.isFillWidth());
		//Label newLabel = new Label("", playlistBoxExample.getChildren().get(0).getLabelFor());
		//copied.getChildren().add(newLabel);
	}
	
	public void createVBoxPlaylist(Playlist playlist){		
		VBox newBox = new VBox();
		AnchorPane.setLeftAnchor(newBox, 0.0);
		AnchorPane.setRightAnchor(newBox, 0.0);
		//newBox.getChildren().add(new Label(playlist.name));
	}		

}
