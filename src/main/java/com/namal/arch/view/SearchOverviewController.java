package com.namal.arch.view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.namal.arch.models.Playlist;
import com.namal.arch.models.Song;
import com.namal.arch.models.services.AudioService;
import com.namal.arch.models.services.AudioServiceLoader;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class SearchOverviewController extends UIController{
	
	@FXML
	private AnchorPane songsPane;
	
	@FXML
	private TextField searchText;
	
	@FXML
	private MenuButton providersListMenu;
	
	private List<AudioService> providerListChecked;
	private Playlist results;
	private PlayerOverviewController playerController;

	public SearchOverviewController() {
		providerListChecked = new ArrayList<AudioService>();
	}
	
	public void onLoad(UIMainClass mainApp, PlayerOverviewController playerController){
		this.playerController = playerController;
		this.mainApp = mainApp;
		
		//Providers
		Iterator<AudioService> it = AudioServiceLoader.getAudioServices();
		while(it.hasNext()){
			providersListMenu.getItems().add(createMenuCheckProvider(it.next()));
		}		
	}
	
	private CheckMenuItem createMenuCheckProvider(AudioService serv){
		CheckMenuItem item = new CheckMenuItem(serv.getProviderInformation().getName());
		
		//Comment these if you want no selected items at the beginning
		item.setSelected(true);
		providerListChecked.add(serv);
		//---
		
		item.setOnAction(new EventHandler<ActionEvent>() {			
			@Override
			public void handle(ActionEvent event) {
				if(item.isSelected()){
					providerListChecked.add(serv);
				} else {
					providerListChecked.remove(serv);
				}
			}
		});
		return item;
	}
	
	@FXML
	private void search(){
		//TODO When we'll have other providers
		results = new Playlist("Search Results",true,true);
		for(AudioService serv : providerListChecked){
			Playlist aux = serv.searchTrack(searchText.getText());			
			Iterator<Song> it = aux.getSongs();
			while(it.hasNext()){
				results.addSongWithoutUpdating(it.next());
			}
		}
		showResults();
	}

	private void showResults() {
		FXMLLoader loader = loadingModule("SongsOverview.fxml", songsPane);
		
		SongsOverviewController controller = loader.getController();
		controller.onLoad(results, playerController, mainApp);
	}

}
