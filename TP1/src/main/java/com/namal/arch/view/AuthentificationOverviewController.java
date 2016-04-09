package com.namal.arch.view;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import com.namal.arch.utils.ConnexionToken;
import org.apache.commons.io.IOUtils;

import com.google.common.net.UrlEscapers;
import com.namal.arch.models.services.AudioService;
import com.namal.arch.models.services.AudioServiceLoader;
import com.namal.arch.models.services.IAudioServiceLoader;
import com.namal.arch.models.services.IAuthentification;
import com.namal.arch.utils.Connexion;
import com.sun.org.apache.xerces.internal.util.URI;

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

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class AuthentificationOverviewController {
	
	public AuthentificationOverviewController() {
	}
	
	/**
     * Function to call in order to load this service
     * @param stage The stage into which we load the service
     * @param button a button
     * @param serv The audio Service we try to authenticate
     */
	private void onLoadService(Stage stage,  Button button, AudioService serv){
		
		stage.setTitle("Authentification " + serv.getName());
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
                	  if(url.indexOf("https://cnamal.github.io/arch-LOG8430/callback.html")>=0){
                		 
						try {
							 String strUrlServer = Connexion.getURI();
	                		  strUrlServer = strUrlServer+"/services/connect?serviceId="+serv.getServiceId()+"&url="+URLEncoder.encode(url,"UTF-8");
	                		  URL urlServer;
							urlServer = new URL(strUrlServer);
							InputStream serverResponse = urlServer.openStream();
							JsonReader rdr = Json.createReader(serverResponse);
							ConnexionToken.getInstance().setToken(rdr.readObject().getJsonObject("data").getString("token"));
							serv.setConnected(true);
							serverResponse.close();
						} catch (MalformedURLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                		  
                		  //boolean test=auth.serverResponse(url);
                		  /*if(test){
                			  //TODO print success notification
                			  //use auth.getProviderInformation to print stuff if needed
                		  }else{
                			  //TODO error notification
                		  }*/
                		  //TODO Return response to server
                		  updateButton(button, serv);
                		  stage.close();
                	  }
                }
                  
                }
            });
        webEngine.load(serv.getConnectUrl());     

        root.getChildren().addAll(scrollPane);
        scene.setRoot(root);

        stage.setScene(scene);
        stage.showAndWait();
	}
	
	/**
     * Function to call in order to load the list of all services
     * @param module the pane where the the module will be attached
     * @param stage The stage into which we load the service
     */
	public void onLoadListServices(AnchorPane module, Stage stage){
		Iterator<AudioService> list = AudioServiceLoader.getInstance().getAudioServices();
		Label text = new Label("Select a service to authenticate");
		text.setLayoutX(module.getWidth()/2 - 50);
		module.getChildren().add(text);
		int layoutY = 40;
		
		while(list.hasNext()){
			AnchorPane child = new AnchorPane();
			AnchorPane.setLeftAnchor(child, 0.0);
			AnchorPane.setRightAnchor(child, 0.0);
			
        	AudioService serv = list.next();
        	Label label = new Label(serv.getName());
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
			    	if(!serv.isConnected()&&(serv.getConnectUrl()!=null))
			    		onLoadService(stage, button, serv);
			    	else{
			    		//serv.disconnect();
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

	private void updateButton(Button button, AudioService serv){
		button.setText((serv.isConnected()?"Disconnect":"Connect"));
    	button.setStyle((serv.isConnected()?"-fx-base: #b6e7c9;":"-fx-base: "));
	}
}
