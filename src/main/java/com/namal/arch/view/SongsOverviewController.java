package com.namal.arch.view;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.namal.arch.models.Playlist;
import com.namal.arch.models.Song;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

public class SongsOverviewController extends UIController{
	
	@FXML
	private AnchorPane songsPane;
	
	private Playlist playlist;
	
	private List<AnchorPane> songList;
	
	private PlayerOverviewController playerOverviewController;

	public SongsOverviewController() {
		
	}
	
	
	public void onLoad(Playlist playlist, PlayerOverviewController playerOverviewController){
		this.playerOverviewController = playerOverviewController;
		this.playlist = playlist;
		this.songList = new ArrayList<AnchorPane>();
		createHBoxes();
	}
	
	private void createHBoxes(){
		Iterator<Song> it = playlist.getSongs();
		while(it.hasNext()){
			songList.add(createNewBox(it.next()));
		}
		refreshListBox();
	}

	private void refreshListBox() {
		songsPane.getChildren().clear();
		if(!songList.isEmpty()){
			songsPane.setPrefHeight(songList.size() * songList.get(0).getPrefHeight());
			songsPane.getChildren().addAll(songList);
		}
	}

	private AnchorPane createNewBox(Song s) {
	       try {
	    	   //Loading the FXMLFile into an URL
	    	   FXMLLoader loader = getLoaderFromFile("SongTemplate.fxml");
	           AnchorPane newBox = (AnchorPane) loader.load();
	           newBox.setPrefWidth(songsPane.getPrefWidth());
	           newBox.setLayoutY(newBox.getPrefHeight() * songList.size());
	           
	           SongTemplateController controller = loader.getController();
	           controller.onLoad(s, newBox, playlist, playerOverviewController);
	           
	           return newBox;
	       } 
	       catch (IOException e) {
	           e.printStackTrace();
	           return null;
	       }		
	}

}
