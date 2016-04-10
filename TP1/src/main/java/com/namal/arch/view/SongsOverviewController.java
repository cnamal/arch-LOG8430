package com.namal.arch.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.namal.arch.models.Playlist;
import com.namal.arch.models.Song;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class SongsOverviewController extends UIController{
	
	@FXML
	private AnchorPane songsPane;
	
	private Playlist playlist;
	
	private List<AnchorPane> songList;
	
	private PlayerOverviewController playerOverviewController;

	public SongsOverviewController() {
		
	}
	
	/**
	 * Called on the initialization of this controller
	 * @param playlist The playlist associated
	 * @param playerOverviewController reference to the FXpart of the PlayerController
	 * @param mainApp reference to the mainApp
	 */
	public void onLoad(Playlist playlist, PlayerOverviewController playerOverviewController, UIMainClass mainApp){
		this.mainApp = mainApp;
		mainApp.setSongsOverviewController(this);
		this.playerOverviewController = playerOverviewController;
		this.playlist = playlist;
		this.songList = new ArrayList<AnchorPane>();
		createHBoxes();
	}
	
	/**
	 * Create the boxes associated.
	 * Synchronized in order to not block the application while loading
	 */
	private void createHBoxes(){
		synchronized(playlist){
			Iterator<Song> it = playlist.getSongs();
			while(it.hasNext()){
				songList.add(createNewBox(it.next()));
			}
			refreshListBox();
		}
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
	           AnchorPane.setLeftAnchor(newBox, 0.0);
	           AnchorPane.setRightAnchor(newBox, 0.0);
	           
	           SongTemplateController controller = loader.getController();
	           controller.onLoad(s, newBox, playlist, playerOverviewController, mainApp);
	           
	           return newBox;
	       } 
	       catch (IOException e) {
	           e.printStackTrace();
	           return null;
	       }		
	}

	public void refresh(){
		onLoad(playlist, playerOverviewController, mainApp);
	}

}
