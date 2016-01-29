package com.namal.arch.view;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.concurrent.Worker;
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
                	  System.out.println(url);
                	  if(url.indexOf("access_token")>=0){
                		  Pattern p = Pattern.compile("(access_token=)([a-z|A-Z|0-9|-]*)(&expires_in=)([0-9]*)(&scope=)([%|0-9|A-Z|a-z]*)");
                		  Matcher m = p.matcher(url.substring(url.indexOf("access_token")));
                		  if(m.matches()) {
                			  for(int i=0; i <= m.groupCount(); i++) {
                			        // affichage de la sous-chaîne capturée
                			        System.out.println("Groupe " + i + " : " + m.group(i));
                			    }
                		  }else
                			  System.out.println("no match");
                	  }else{
                		  System.out.println("no index");
                	  }
                }
                  
                }
            });
        webEngine.load("https://accounts.spotify.com/authorize?client_id=87824d0c4d1a4d2c8c72662b3b1a9f6e&redirect_uri=namal-arch%3A%2F%2Fspotify&response_type=token");        
        
        //webEngine.loadContent("<b>asdf</b>");

        root.getChildren().addAll(scrollPane);
        scene.setRoot(root);

        stage.setScene(scene);
        stage.showAndWait();
        }
}
