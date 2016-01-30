package com.namal.arch.view;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class UIMainClass extends Application{
	
	private Stage primaryStage;
	private Stage authenticateStage;
	
	private AnchorPane generalLayout;
	private UIController uiController;

	
	private final String GENERAL_LAYOUT_FILE = "GeneralLayout.fxml";

	public UIMainClass() {
		uiController = new UIController();
	}
	
	public void firstLoad(String[] args){
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Arch-LOG8430 - Streaming playlists gatherer");
        primaryStage.setMinWidth(800);
        
        
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
        Scene scene = new Scene(generalLayout);
        primaryStage.setScene(scene);
        primaryStage.show();
        
     // Give the controller access to the main app.
        GeneralLayoutController controller = loader.getController();
        controller.setMainApp(this);
	}
	
	
	public void loadAuthenticateWindow(){
		authenticateStage = new Stage();
		authenticateStage.initModality(Modality.WINDOW_MODAL);
		authenticateStage.initOwner(primaryStage);
		AuthentificationOverviewController authenticateController = new AuthentificationOverviewController();
		authenticateController.onLoadListServices(authenticateStage);
	}
	
	public Stage getPrimaryStage(){
		return primaryStage;
	}
	

}
