package com.namal.arch.view;

import java.io.IOException;
import java.net.URL;

import com.namal.arch.Main;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class GeneralLayoutController extends UIController{
	
	@FXML
	private VBox playlistBox;
	
	@FXML
	private VBox searchBox;
	
	@FXML
	private VBox settingsBox;
	
	//Where the modules will be loaded 
	@FXML
	private AnchorPane modulePane;
	
	@FXML
	private AnchorPane playerPane;
	
	private PlayerOverviewController playerOverviewController;
	
	
	
	// Reference to the main application.
    private UIMainClass mainApp;
    
    // The constructor
    public GeneralLayoutController() {
    }
    
    /**
     * Function called at the loading
     * @param mainApp Reference to the mainApp
     */
    public void setMainApp(UIMainClass mainApp){
    	this.mainApp = mainApp;
    	loadPlayer();
    }
    
	private void loadPlayer() {		
		FXMLLoader loader = loadingModule("PlayerOverview.fxml", playerPane);
        
        playerOverviewController = loader.getController();
        playerOverviewController.onLoad();
	}

	/**
	 * Reset all the boxes to the class vbox
	 */
    private void resetAllBoxes(){
    	playlistBox.getStyleClass().removeAll("vboxSelected", "vbox");
    	searchBox.getStyleClass().removeAll("vboxSelected", "vbox");
    	settingsBox.getStyleClass().removeAll("vboxSelected", "vbox");
    	playlistBox.getStyleClass().add("vbox");
    	searchBox.getStyleClass().add("vbox");
    	settingsBox.getStyleClass().add("vbox");
    }
    
    /**
     * Function loaded when the playlist box is clicked
     * Load the layout from the playlist
     * Only done if the playlist isn't the current selected option
     */
    @FXML
    private void playlistLoading(){
    	//If we're not in playlist yet
    	if(!playlistBox.getStyleClass().contains("vboxSelected")){
    		try{
    		resetAllBoxes();
    		playlistBox.getStyleClass().add("vboxSelected");
    		playlistBox.getStyleClass().remove("vbox");
    		//Load the module
    		FXMLLoader loader = loadingModule("./PlaylistOverview.fxml", modulePane);
    		if(loader == null)
    			throw new Exception("Loading failed");
    		//Create the controller if loading ok
    		PlaylistOverview controller = loader.getController();
            controller.onLoad(playerOverviewController);
    		} catch (Exception e){
    			e.printStackTrace();
    		}
    	}
    }
    
    /**
     * Function loaded when the Browse box is clicked
     * Load the layout from Browse
     * Only done if browse isn't the current selected option
     */
    @FXML
    private void searchLoading(){
    	//If we're not in browse yet
    	if(!searchBox.getStyleClass().contains("vboxSelected")){
    		resetAllBoxes();
    		searchBox.getStyleClass().add("vboxSelected");
    		searchBox.getStyleClass().remove("vbox");
    	}
    }
    
    /**
     * Function loaded when the settings box is clicked
     * Load the layout from the settings
     * Only done if the settings aren't the current selected option
     */
    @FXML
    private void settingsLoading(){
    	//If we're not in settings yet
    	if(!settingsBox.getStyleClass().contains("vboxSelected")){
    		resetAllBoxes();
    		settingsBox.getStyleClass().add("vboxSelected");
    		settingsBox.getStyleClass().remove("vbox");
    		mainApp.loadAuthenticateWindow();
    	}
    }  
}
