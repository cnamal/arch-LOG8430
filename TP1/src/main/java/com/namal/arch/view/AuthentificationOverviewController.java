package com.namal.arch.view;

import java.util.Iterator;

import com.namal.arch.models.services.AudioService;
import com.namal.arch.models.services.AudioServiceLoader;
import com.namal.arch.models.services.IAuthentification;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class AuthentificationOverviewController {
	
	public AuthentificationOverviewController() {
	}
	
	private void onLoadService(Stage stage, IAuthentification auth, Button button, AudioService serv){
		
		stage.setTitle("Authentification " + auth.getProviderInformation().getName());
        stage.setWidth(800);
        stage.setHeight(500);
        stage.setResizable(true);
        Scene scene = new Scene(new Group());

        AnchorPane root = new AnchorPane();     

        final WebView browser = new WebView();
        final WebEngine webEngine = browser.getEngine();

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(browser);
        
        webEngine.getLoadWorker().stateProperty().addListener(
            new ChangeListener<State>() {
              @Override public void changed(ObservableValue<? extends State> ov, State oldState, State newState) {

                  if (newState == Worker.State.SUCCEEDED) {
                	  String url= webEngine.getLocation();
                	  System.out.println(url);
                	  if(url.indexOf(auth.testString())>=0){
                		  boolean test=auth.serverResponse(url);
                		  if(test){
                			  //TODO print success notification
                			  //use auth.getProviderInformation to print stuff if needed
                		  }else{
                			  //TODO error notification
                		  }
                		  updateButton(button, serv);
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
	
	public void onLoadListServices(AnchorPane module, Stage stage){
		Iterator<AudioService> list = AudioServiceLoader.getAudioServices();
		Label text = new Label("Select a service to authenticate");
		text.setLayoutX(module.getWidth()/2 - 50);
		module.getChildren().add(text);
		int layoutY = 40;
		
		while(list.hasNext()){
			AnchorPane child = new AnchorPane();
			AnchorPane.setLeftAnchor(child, 0.0);
			AnchorPane.setRightAnchor(child, 0.0);
			
        	AudioService serv = list.next();
        	Label label = new Label(serv.getProviderInformation().getName());
        	AnchorPane.setLeftAnchor(label, 0.0);
			AnchorPane.setTopAnchor(label, 5.0);
			AnchorPane.setBottomAnchor(label, 5.0);
			
        	Button button = new Button();
        	
        	AnchorPane.setLeftAnchor(label, 100.0);
        	AnchorPane.setTopAnchor(button, 5.0);
			AnchorPane.setBottomAnchor(button, 5.0);
			updateButton(button, serv);
        	button.setOnMouseClicked(new EventHandler<MouseEvent>() {
			    @Override
			    public void handle(MouseEvent mouseEvent) {
			    	if(!serv.isConnected())
			    		onLoadService(stage, serv.getAuthentification(), button, serv);
			    	else{
			    		serv.disconnect();
			    		updateButton(button, serv);
			    	}
			    }
		    });        	
        	
        	child.setLayoutY(layoutY);
        	layoutY += 40 + 10;
        	child.getChildren().addAll(label, button);
        	module.getChildren().add(child);
        }
		
	}
	/*
	public void onLoadListServices(Stage stage){
		stage.setTitle("Authentification");
		stage.setWidth(500);
        Scene scene = new Scene(new Group());

        AnchorPane root = new AnchorPane(); 
        stage.setResizable(false);
        
        Iterator<AudioService> list = AudioServiceLoader.getAudioServices();
        
        Label text = new Label("Select a service to authenticate");
        text.setLayoutX(165);
        
        root.getChildren().add(text);
        
        int layoutY = 40;
        
        while(list.hasNext()){
        	AudioService serv = list.next();
        	Button button = new Button(serv.getProviderInformation().getName());
        	button.setOnMouseClicked(new EventHandler<MouseEvent>() {
			    @Override
			    public void handle(MouseEvent mouseEvent) {
			    	onLoadService(stage, serv.getAuthentification());
			    }
		    });
        	button.setLayoutX((stage.getWidth()-100)/2);
        	button.setLayoutY(layoutY);
        	layoutY += button.getHeight() + 10;
        	root.getChildren().add(button);
        }
        
        scene.setRoot(root);

        stage.setScene(scene);
        stage.showAndWait();
	}*/
	
	private void updateButton(Button button, AudioService serv){
		button.setText((serv.isConnected()?"Disconnect":"Connect"));
    	button.setStyle((serv.isConnected()?"-fx-base: #b6e7c9;":"-fx-base: "));
	}
}
