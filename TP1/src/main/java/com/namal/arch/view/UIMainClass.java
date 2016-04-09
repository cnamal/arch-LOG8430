package com.namal.arch.view;

import java.io.IOException;
import java.util.HashMap;

import com.namal.arch.models.Song;
import com.namal.arch.models.services.AudioServiceProvider;
import com.namal.arch.utils.Configuration;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class UIMainClass extends Application{
	
	
	private Stage primaryStage;
	private Stage authenticateStage;
	private Stage newPlaylistStage;
	private Scene primaryScene;
	private boolean showApp;
	
	private AnchorPane generalLayout;
	private UIController uiController;
	
	//Attribute used to load each image only once
	//Access with getLogoProvider
	private HashMap<AudioServiceProvider, Image> logos;

	
	private final String GENERAL_LAYOUT_FILE = "GeneralLayout.fxml";

	public UIMainClass() {
		uiController = new UIController();
		logos = new HashMap<AudioServiceProvider, Image>();
		showApp = Configuration.getShow();
	}
	
	/**
	 * Initialization
	 * @param args JavaFX arguments
	 */
	public void firstLoad(String[] args){
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Arch-LOG8430 - Streaming playlists gatherer");
        primaryStage.setMinWidth(800);
        //MongoDB.init();
        
        loadGeneraLayout();
    }

	
	public void loadGeneraLayout(){
		FXMLLoader loader = uiController.getLoaderFromFile(GENERAL_LAYOUT_FILE);
		
        try {
			generalLayout = (AnchorPane) loader.load();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

        // Show the scene containing the general layout.
        primaryScene = new Scene(generalLayout);
        if(showApp){
	        primaryStage.setScene(primaryScene);
	        primaryStage.show();
        }
        
        // Give the controller access to the main app.
        GeneralLayoutController controller = loader.getController();
        controller.setMainApp(this);
	}
	
	
	public void loadAuthenticateWindow(AnchorPane module){
		authenticateStage = new Stage();
		authenticateStage.initModality(Modality.WINDOW_MODAL);
		authenticateStage.initOwner(primaryStage);
		AuthentificationOverviewController authenticateController = new AuthentificationOverviewController();
		authenticateController.onLoadListServices(module, authenticateStage);
	}
	
	public Stage getPrimaryStage(){
		return primaryStage;
	}
	
	public Scene getPrimaryScene(){
		return primaryScene;
	}

	
	public void createNewPlaylist(Song song){
		Stage newPlaylistStage = new Stage();
		newPlaylistStage.setTitle("Create new playlist...");
		Scene scene = new Scene(new Group());
		AnchorPane root = new AnchorPane();
		FXMLLoader loader = uiController.loadingModule("NewPlaylistPopup.fxml", root);
		NewPlaylistPopupController controller = loader.getController();
		controller.onLoad(song, newPlaylistStage);
		scene.setRoot(root);
		
		newPlaylistStage.setScene(scene);
		newPlaylistStage.showAndWait();
	}
	
	public Image getLogoProvider(AudioServiceProvider prov){
		if(!logos.containsKey(prov))
			logos.put(prov, new Image(prov.getProviderInformation().getLogoUrl()));
		return logos.get(prov);
	}
}
