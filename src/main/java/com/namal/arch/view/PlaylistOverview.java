package com.namal.arch.view;

import java.util.ArrayList;
import java.util.List;

import com.namal.arch.models.Playlist;
import com.namal.arch.models.Song;
import com.namal.arch.models.SongBuilder;
import com.namal.arch.models.SongMalformed;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
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
	private final double PREF_HEIGHT = 30.0;
	
	//List of all the playlist saved
	private List<VBox> playlistList;
	
	/**
	 * Constructor, initialize a empty list
	 */
	public PlaylistOverview(){
		playlistList = new ArrayList<VBox>();
	}
	
	/**
	 * Resizing the AnchorPane after modifying the boxes
	 */
	private void resizePlaylistAnchorPane(){
		playlistAnchorPane.setPrefHeight(playlistList.size() * PREF_HEIGHT);
	}

	/**
	 * Create the VBox (and all of its properties) and return it
	 * @param playlist The playlist associated with this new VBox
	 * @return The VBox freshly created
	 */
	private VBox createVBoxPlaylist(Playlist playlist){	
		//Create the new box
		VBox newBox = new VBox();
		
		// Set the anchors and size and position
		newBox.setPrefHeight(PREF_HEIGHT);
		newBox.setFillWidth(true);
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
		System.out.println(playlist.getName());
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
		Song song;
		try {
			song = SongBuilder.songBuilder()
					.setTitle("J'aime les bulles")
					.setArtist("Bob l'éponge")
					.setUri("")
					.build();
			playlist1.addSong(song);
			song = SongBuilder.songBuilder()
					.setTitle("J'aime les étoiles de mers")
					.setArtist("Bob l'éponge")
					.setUri("")
					.build();
			playlist1.addSong(song);
			song = SongBuilder.songBuilder()
					.setTitle("Y'a de la joie")
					.setArtist("Badoit")
					.setUri("")
					.build();
			playlist1.addSong(song);
			song = SongBuilder.songBuilder()
					.setTitle("Cui Cui")
					.setArtist("Cuiiiii")
					.setUri("")
					.build();
			playlist2.addSong(song);
			song = SongBuilder.songBuilder()
					.setTitle("Quand j'étais petit je n'étais pas grand")
					.setArtist("Me")
					.setUri("")
					.build();
			playlist3.addSong(song);
		} catch (SongMalformed e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		playlists.add(playlist1);
		playlists.add(playlist2);
		playlists.add(playlist3);
		
		//Begin of the real function
		for(Playlist p : playlists){
			VBox v = createVBoxPlaylist(p);
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
    	for(VBox v : playlistList){
	    	v.getStyleClass().removeAll("vboxSelected", "vbox");
	    	v.getStyleClass().add("vbox");
    	}
    }

}
