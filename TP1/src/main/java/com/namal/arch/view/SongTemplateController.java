package com.namal.arch.view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.namal.arch.models.Playlist;
import com.namal.arch.models.Song;
import com.namal.arch.models.services.AudioService;
import com.namal.arch.models.services.AudioServiceLoader;
import com.namal.arch.utils.PlaylistManager;
import com.namal.arch.utils.ServiceListener;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class SongTemplateController extends UIController{
	
	//Resizing UI
	private double titlePurcent;
	private double singerPurcent;
	private double albumPurcent;
	private double timePurcent;
	private double menuButtonPurcent;
	
	@FXML
	private AnchorPane songBox;
	
	@FXML
	private ImageView imageView;
	
	@FXML
	private Label  title;
	@FXML
	private Label  singer;
	@FXML
	private Label  album;
	@FXML
	private Label  time;
	
	@FXML
	private MenuButton menuButton;
	
	@FXML
	private Menu addPlaylistMenu;
	
	private Song song;
	private Playlist playlist;
	private PlayerOverviewController playerOverviewController;

	public SongTemplateController() {		
	}

	/**
	 * Called on the initialization of this controller
	 * @param song The song associated
	 * @param songBox Where the box will be attached
	 * @param playlist The playlist where the song belongs to
	 * @param playerOverviewController reference to the FXpart of the PlayerController
	 * @param mainApp reference to the mainApp
	 */
	public void onLoad(Song song, AnchorPane songBox, Playlist playlist, PlayerOverviewController playerOverviewController, UIMainClass mainApp){
		this.mainApp = mainApp;
		this.playerOverviewController = playerOverviewController;
		this.song = song;
		this.title.setText(this.song.getTitle());
		this.singer.setText(this.song.getArtist());
		this.album.setText(this.song.getAlbum());
		this.time.setText(PlayerOverviewController.msToMin(this.song.getDuration()));
		// TODO
		String imageURI = AudioServiceLoader.getInstance().getUriFromId(song.getServiceId());
		if(song.getServiceId().equals("0")){
			//this.imageView.setImage(mainApp.getLogoProvider(song.getProvider()));
		}else if(imageURI == null){
			//this.imageView.setImage(mainApp.getLogoProvider(song.getProvider()));
		}else
			this.imageView.setImage(new Image(imageURI, songBox.getHeight(),songBox.getHeight(), true, true));
		this.songBox = songBox;
		this.playlist = playlist;
		menuButton.getItems().get(0).setDisable(song.getUri()==null);
		connectAction();
		createAddPlaylistMenu();
		generatePurcentageBasedOnFile();
		
		//Resize the player (for the text)
		mainApp.getPrimaryScene().widthProperty().addListener(new ChangeListener<Number>() {
		    @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
		    	resizeComponents(newSceneWidth, oldSceneWidth);
		    }
		});
	}
	
	private void connectAction() {
		songBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent mouseEvent) {
		        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
		            if(mouseEvent.getClickCount() == 2){
		            	// TODO Connect the player to the song
		            	int pos = playlist.getPos(song);
		            	if(pos != -1){
			                System.out.println("PLAY");
			                play();
		            	}
		            }
		        }
		    }
		});
	}
	
	private void createAddPlaylistMenu(){
		PlaylistManager.getInstance().getPlaylists(new ServiceListener<List<Playlist>>() {
			@Override
			public void done(List<Playlist> result) {
				if(result != null){
					for(Playlist p : result){
							addMenuItem(p);
					}
				}
			}
		});
		//New playlist
		MenuItem menuItem = new MenuItem("New playlist...");
		menuItem.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		        mainApp.createNewPlaylist(song);
		    }
		});
		addPlaylistMenu.getItems().add(menuItem);

	}
	
	private void addMenuItem(Playlist p){
		MenuItem menuItem = new MenuItem(p.getName());
		menuItem.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		        p.addSongAndUpdate(p.getTotalNumberSongs(), song);
		    }
		});
		addPlaylistMenu.getItems().add(menuItem);
	}
	
	@FXML
	private void play(){
		playerOverviewController.onPlay(playlist, playlist.getPos(song));
	}

	@FXML
	private void delete(){ playlist.deleteSong(song); mainApp.refresh(playlist);}

	
	private void resizeComponents(Number newSceneWidth, Number oldSceneWidth){
		double diff = newSceneWidth.doubleValue() - oldSceneWidth.doubleValue();
		title.setMaxWidth(title.getMaxWidth() + diff * titlePurcent);
		singer.setMaxWidth(singer.getMaxWidth() + diff * singerPurcent);
		album.setMaxWidth(album.getMaxWidth() + diff * albumPurcent);
		time.setMaxWidth(time.getMaxWidth() + diff * timePurcent);
		menuButton.setMaxWidth(menuButton.getMaxWidth() + diff * menuButtonPurcent);
	}
	
	private void generatePurcentageBasedOnFile(){
		double prefSize = songBox.getPrefWidth() - imageView.getFitWidth();
		titlePurcent = title.getMaxWidth() / prefSize;
		singerPurcent = singer.getMaxWidth()/ prefSize;
		albumPurcent = album.getMaxWidth()/ prefSize;
		timePurcent = time.getMaxWidth() / prefSize;
		menuButtonPurcent = menuButton.getMaxWidth() / prefSize;
	}

}
