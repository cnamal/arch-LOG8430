package com.namal.arch.view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.namal.arch.models.Playlist;
import com.namal.arch.models.services.AudioService;
import com.namal.arch.utils.PlaylistManager;
import com.namal.arch.utils.ServiceListener;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
	
	private Object mutexLock = new Object();
	
	private List<Playlist> playlists=null;

	private Image dustbin;
	
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
		Button button = new Button();

		button.setMaxSize(20,20);
		button.setPrefSize(20,20);
		button.setMinSize(20,20);
		button.setGraphic(new ImageView(dustbin));
		button.setLayoutX(newBox.getWidth() - 20);
		button.setOnMouseClicked(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent t) {
				PlaylistManager.getInstance().deletePlaylist(playlist);
				refresh(playlist);
			}
		});

		newBox.getChildren().add(button);

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
		dustbin = new Image(getClass().getClassLoader().getResource("dustbin.jpg").toString(), 20,20,true,true);
		this.mainApp = mainApp;
		mainApp.setPlaylistOverview(this);
		this.playerOverviewController = controller;
		//Get the list of saved playlist
		if(playlists != null)
			showResults(playlists);
		else {
			playlists = new ArrayList<>();
			PlaylistManager.getInstance().getPlaylists(new ServiceListener<List<Playlist>>() {

				@Override
				public void done(List<Playlist> result) {
					if (result != null) {
						synchronized (mutexLock) {
							playlists.addAll(result);
							Platform.runLater(new Runnable() {

								@Override
								public void run() {
									showResults(playlists);
								}
							});
						}
					}
				}
			});
		}
			
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

    /**
     * Load the results
     * @param playlists The list of all the results
     */
    private void showResults(List<Playlist> playlists){
		playlistList.clear();
		playlistAnchorPane.getChildren().clear();
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
     * Refresh the list of playlist (when changes occurs).
     * @param playlist Playlist on which the changes occured
     */
	public void refresh(Playlist playlist){
		if(playlist.getTotalNumberSongs() == 0)
			playlists.remove(playlist);
		onLoad(playerOverviewController, mainApp);
	}
}
