package com.namal.arch.view;

import com.namal.arch.models.Song;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class SongTemplateController {
	
	@FXML
	private HBox songBox;
	
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
	
	private Song song;

	public SongTemplateController() {		
	}

	public void onLoad(Song song, HBox songBox){
		this.song = song;
		this.title.setText(this.song.getTitle());
		this.singer.setText(this.song.getArtist());
		this.album.setText(this.song.getAlbum());
		this.time.setText("Unknown");
		this.songBox = songBox;
		connectAction();
	}
	
	private void connectAction() {
		songBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent mouseEvent) {
		        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
		            if(mouseEvent.getClickCount() == 2){
		            	// TODO Connect the player to the song
		                System.out.println("PLAY");
		            }
		        }
		    }
		});
	}

}
