package com.namal.arch.view;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.namal.arch.models.Playlist;
import com.namal.arch.models.Song;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

public class PlaylistOverview {
	
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
		try {
	    	   //Loading the FXMLFile into an URL
	    	   URL url = getClass().getClassLoader().getResource("SongsOverview.fxml");
	           FXMLLoader loader = new FXMLLoader();
	           loader.setLocation(url);
	           loader.setBuilderFactory(new JavaFXBuilderFactory());
	           //Loading the module and setting the anchors
	           AnchorPane newModule = (AnchorPane) loader.load(url.openStream());
	           AnchorPane.setBottomAnchor(newModule, 0.0);
	           AnchorPane.setTopAnchor(newModule, 0.0);
	           AnchorPane.setLeftAnchor(newModule, 0.0);
	           AnchorPane.setRightAnchor(newModule, 0.0);

	           //Remove the already loaded module (if there is one) and add the new loaded one
	           songsAnchorPane.getChildren().clear();
	           songsAnchorPane.getChildren().add(newModule);
	           
	           SongsOverviewController controller = loader.getController();
	           controller.onLoad(playlist);
	       } 
	       catch (IOException e) {
	           e.printStackTrace();
	       }
	}
	
	/**
	 * Load the controller
	 * Get the playlists saved by the user and display them
	 * Need to be called when the Playlist layout is selected
	 */
	public void onLoad(){
		//Get the list of saved playlist
		// TODO
		List<Playlist> playlists = new ArrayList<Playlist>();
		//For testing, we create a few playlists
		Playlist playlist1 = new Playlist("La chorale de bubulle");
		Playlist playlist2 = new Playlist("Les chants des oiseaux");
		Playlist playlist3 = new Playlist("Throwback Thursday");
		playlist1.addSong(new Song("J'aime les bulles", "Bob l'éponge", "Sonate pour un crabe"));
		playlist1.addSong(new Song("J'aime les étoiles de mers", "Bob l'éponge", "Sonate pour un crabe"));
		playlist1.addSong(new Song("Y'a de la joie", "Badoit", "Classiques de la pub française"));
		playlist2.addSong(new Song("Cui Cui", "Cuiiiii", "Cui cui cui"));
		playlist3.addSong(new Song("Quand j'étais petit je n'étais pas grand", "Me", "Ma vie"));
		playlists.add(playlist1);
		playlists.add(playlist2);
		playlists.add(playlist3);
		
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
