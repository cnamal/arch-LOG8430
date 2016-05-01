package com.namal.arch.view;

import com.namal.arch.models.Playlist;
import com.namal.arch.models.Song;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class NewPlaylistPopupController extends UIController {
	@FXML
	private TextField name;
	
	@FXML
	private CheckBox isPublic;
	
	private Song song;
	private Stage stage;
	
	/**
	 * Function called when the controller is initialized
     */
	public void onLoad(Song song, Stage stage){
		this.song = song;
		this.stage = stage;
	}
	
	@FXML
	private void onOK(){
		Playlist playlist = new Playlist(name.getText(), Integer.MIN_VALUE,  song.getServiceId(), isPublic.isSelected());
		playlist.addSongAndUpdate(0, song);
		stage.close();
	}
	
	@FXML 
	void onCancel(){
		stage.close();
	}
}
