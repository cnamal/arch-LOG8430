package com.namal.arch.view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.namal.arch.models.Playlist;
import com.namal.arch.models.Song;
import com.namal.arch.models.services.AudioService;
import com.namal.arch.models.services.AudioServiceLoader;
import com.namal.arch.utils.APIHelper;
import com.namal.arch.utils.ServiceListener;

import javafx.application.Platform;
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
	
	private Object mutexLock = new Object();

	public SearchOverviewController() {
		providerListChecked = new ArrayList<AudioService>();
	}
	
	/**
	 * Called on the initialization of this controller
	 * @param mainApp reference to the mainApp
	 * @param playerController reference to the FXpart of the PlayerController
	 */
	public void onLoad(UIMainClass mainApp, PlayerOverviewController playerController){
		this.playerController = playerController;
		this.mainApp = mainApp;
		
		//Providers
		Iterator<AudioService> it = AudioServiceLoader.getInstance().getAudioServices();
		while(it.hasNext()){
			AudioService service = it.next();
			if(service.isSearchAvailable())
				providersListMenu.getItems().add(createMenuCheckProvider(service));
		}		
	}
	
	/**
	 * Create the selection for all the services providers implemented
	 * @param serv the service
	 * @return CheckMenuItem The item created and filled
	 */
	private CheckMenuItem createMenuCheckProvider(AudioService serv){
		CheckMenuItem item = new CheckMenuItem(serv.getName());
		
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
		results = APIHelper.searchTrack(searchText.getText(), providerListChecked);
		showResults();
	}

	/**
	 * Show the results
	 */
	private void showResults() {
		FXMLLoader loader = loadingModule("SongsOverview.fxml", songsPane);
		
		SongsOverviewController controller = loader.getController();
		controller.onLoad(results, playerController, mainApp);
	}

}
