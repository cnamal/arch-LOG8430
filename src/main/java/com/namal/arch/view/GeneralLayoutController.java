package com.namal.arch.view;

import com.namal.arch.Main;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class GeneralLayoutController {
	
	@FXML
	private VBox playlistBox;
	
	@FXML
	private VBox searchBox;
	
	@FXML
	private VBox settingsBox;
	
	//Where the modules will be loaded 
	@FXML
	private AnchorPane modulePlane;
	
	@FXML
	private AnchorPane currentlyPlayedPane;
	
	
	
	// Reference to the main application.
    private Main mainApp;
    
    // The constructor
    public GeneralLayoutController() {
    }
    
    //Reset the class of all boxes
    public void resetAllBoxes(){
    	playlistBox.getStyleClass().remove("vboxSelected");
    	searchBox.getStyleClass().remove("vboxSelected");
    	settingsBox.getStyleClass().remove("vboxSelected");
    	playlistBox.getStyleClass().remove("vbox");
    	searchBox.getStyleClass().remove("vbox");
    	settingsBox.getStyleClass().remove("vbox");
    	playlistBox.getStyleClass().add("vbox");
    	searchBox.getStyleClass().add("vbox");
    	settingsBox.getStyleClass().add("vbox");
    }
    
    //If we click on Playlist
    public void playlistLoading(){
    	//If we're not in playlist yet
    	if(!playlistBox.getStyleClass().contains("vboxSelected")){
    		resetAllBoxes();
    		playlistBox.getStyleClass().add("vboxSelected");
    		playlistBox.getStyleClass().remove("vbox");
    	}
    }
    
  //If we click on Browse
    public void searchLoading(){
    	//If we're not in browse yet
    	if(!searchBox.getStyleClass().contains("vboxSelected")){
    		resetAllBoxes();
    		searchBox.getStyleClass().add("vboxSelected");
    		searchBox.getStyleClass().remove("vbox");
    	}
    }
    
  //If we click on Settings
    public void settingsLoading(){
    	//If we're not in settings yet
    	if(!settingsBox.getStyleClass().contains("vboxSelected")){
    		resetAllBoxes();
    		settingsBox.getStyleClass().add("vboxSelected");
    		settingsBox.getStyleClass().remove("vbox");
    	}
    }

}
