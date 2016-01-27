package com.namal.arch.view;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.namal.arch.models.Playlist;
import com.namal.arch.models.Song;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

public class SongsOverviewController {
	
	@FXML
	private AnchorPane songsPane;
	
	private Playlist playlist;
	
	private List<HBox> hboxList; 

	public SongsOverviewController() {
		
	}
	
	
	public void onLoad(Playlist playlist){
		this.playlist = playlist;
		this.hboxList = new ArrayList<HBox>();
		createHBoxes();
	}
	
	private void createHBoxes(){
		Iterator<Song> it = playlist.getSongs();
		while(it.hasNext()){
			hboxList.add(createNewHBox(it.next()));
		}
		refreshListHBox();
	}

	private void refreshListHBox() {
		songsPane.getChildren().clear();
		if(!hboxList.isEmpty()){
			songsPane.setPrefHeight(hboxList.size() * hboxList.get(0).getPrefHeight());
			songsPane.getChildren().addAll(hboxList);
		}
	}

	private HBox createNewHBox(Song s) {
	       try {
	    	   //Loading the FXMLFile into an URL
	    	   URL url = getClass().getClassLoader().getResource("SongTemplate.fxml");
	           FXMLLoader loader = new FXMLLoader();
	           loader.setLocation(url);
	           loader.setBuilderFactory(new JavaFXBuilderFactory());
	           //Loading the module
	           HBox newBox = (HBox) loader.load(url.openStream());
	           newBox.setPrefWidth(songsPane.getPrefWidth());
	           newBox.setLayoutY(newBox.getPrefHeight() * hboxList.size());
	           SongTemplateController controller = loader.getController();
	           controller.onLoad(s, newBox);
	           return newBox;
	       } 
	       catch (IOException e) {
	           e.printStackTrace();
	           return null;
	       }		
	}

}
