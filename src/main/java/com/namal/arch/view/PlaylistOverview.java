package com.namal.arch.view;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.namal.arch.models.Playlist;
import com.namal.arch.models.Song;
import com.namal.arch.models.SongBuilder;
import com.namal.arch.models.SongMalformed;
import com.namal.arch.models.services.Soundcloud;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

public class PlaylistOverview extends UIController{
	
	//The scroll pane for the playlist
	@FXML
	private ScrollPane playlistScrollPane;
	
	//The AnchorPane inside the scroll pane
	@FXML
	private AnchorPane playlistAnchorPane;
	
	//The AnchorPane where the songs will be displayed
	@FXML
	private AnchorPane songsAnchorPane;
	
	//Example playslistBox
	private final double PREF_HEIGHT = 30.0;
	
	//List of all the playlist saved
	private List<HBox> playlistList;
	
	private PlayerOverviewController playerOverviewController;
	
	/**
	 * Constructor, initialize a empty list
	 */
	public PlaylistOverview(){
		playlistList = new ArrayList<HBox>();
	}
	
	/**
	 * Resizing the AnchorPane after modifying the boxes
	 */
	private void resizePlaylistAnchorPane(){
		playlistAnchorPane.setPrefHeight(playlistList.size() * PREF_HEIGHT);
	}

	/**
	 * Create the HBox (and all of its properties) and return it
	 * @param playlist The playlist associated with this new HBox
	 * @return The HBox freshly created
	 */
	private HBox createHBoxPlaylist(Playlist playlist){	
		//Create the new box
		HBox newBox = new HBox();
		
		// Set the anchors and size and position
		newBox.setPrefHeight(PREF_HEIGHT);
		AnchorPane.setLeftAnchor(newBox, 0.0);
		AnchorPane.setRightAnchor(newBox, 0.0);
		newBox.setLayoutX(0.0);
		newBox.setLayoutY(playlistList.size()*PREF_HEIGHT);
		
		//Add a label for the playlist
		newBox.getChildren().add(new Label(playlist.getName()));

		//Connect the signal to this playlist
		newBox.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent t) {
                loadPlaylistUI(playlist);
                resetAllBoxes();
                newBox.getStyleClass().add("vboxSelected");
            }
        });
		
		return newBox;
	}
	
	/**
	 * Load the UI to display the songs
	 * @param playlist Playlist from which songs will be displayed
	 */
	private void loadPlaylistUI(Playlist playlist){
		FXMLLoader loader = loadingModule("SongsOverview.fxml", songsAnchorPane);
	           
		SongsOverviewController controller = loader.getController();
		controller.onLoad(playlist, playerOverviewController, mainApp);
	}
	
	/**
	 * Load the controller
	 * Get the playlists saved by the user and display them
	 * Need to be called when the Playlist layout is selected
	 */
	public void onLoad(PlayerOverviewController controller, UIMainClass mainApp){
		this.mainApp = mainApp;
		this.playerOverviewController = controller;
		//Get the list of saved playlist
		// TODO
		List<Playlist> playlists = new ArrayList<Playlist>();
		//For testing, we create a few playlists
		List<Playlist> playlist4 = Soundcloud.getInstance().getPlaylists();
		Playlist playlist1 = Soundcloud.getInstance().searchTrack("lucky");
		Playlist playlist2 = Soundcloud.getInstance().searchTrack("bass");
		Playlist playlist3 = Soundcloud.getInstance().searchTrack("hello");

		playlists.add(playlist1);
		playlists.add(playlist2);
		playlists.add(playlist3);
		if(playlist4!=null)
			playlists.addAll(playlist4);
		
		//Begin of the real function
		for(Playlist p : playlists){
			HBox v = createHBoxPlaylist(p);
			playlistList.add(v);
			playlistAnchorPane.getChildren().add(v);
		}
		resizePlaylistAnchorPane();
		resetAllBoxes();
	}
	
	/**
	 * Reset all the boxes to the class vbox
	 * (Can be perhaps refactored with the same method in GeneralLayoutController)
	 */
    public void resetAllBoxes(){
    	for(HBox v : playlistList){
	    	v.getStyleClass().removeAll("vboxSelected", "vbox");
	    	v.getStyleClass().add("vbox");
    	}
    }

}
