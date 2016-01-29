package com.namal.arch.view;

import com.namal.arch.models.services.IAuthentification;
import com.namal.arch.models.services.Soundcloud;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class AuthentificationOverviewController {
	
	public AuthentificationOverviewController() {
	}
	
	public void onLoad(Stage stage){
		//TODO remove this auth variable
		IAuthentification auth = Soundcloud.getInstance().getAuthentification();
		
		stage.setTitle("Authentification");
        stage.setWidth(800);
        stage.setHeight(500);
        Scene scene = new Scene(new Group());

        AnchorPane root = new AnchorPane();     

        final WebView browser = new WebView();
        final WebEngine webEngine = browser.getEngine();

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(browser);
        
        webEngine.getLoadWorker().stateProperty().addListener(
            new ChangeListener<State>() {
              @Override public void changed(ObservableValue ov, State oldState, State newState) {

                  if (newState == Worker.State.SUCCEEDED) {
                	  String url= webEngine.getLocation();
                	  if(url.indexOf(auth.testString())>=0){
                		  boolean test=auth.serverResponse(url);
                		  if(test){
                			  //TODO print success notification
                			  //use auth.getProviderInformation to print stuff if needed
                		  }else{
                			  //TODO error notification
                		  }
                		  stage.close();
                	  }
                }
                  
                }
            });
        webEngine.load(auth.getAuthentificationUrl());     

        root.getChildren().addAll(scrollPane);
        scene.setRoot(root);

        stage.setScene(scene);
        stage.showAndWait();
        }
}
