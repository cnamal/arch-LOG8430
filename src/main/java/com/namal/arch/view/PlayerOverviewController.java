package com.namal.arch.view;

import com.namal.arch.controller.PlayerController;
import com.namal.arch.models.Playlist;

import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class PlayerOverviewController {
	
	@FXML
	private ImageView prevView;
	@FXML
	private ImageView playPauseView;
	@FXML
	private ImageView nextView;
	
	private Image play;
	private Image pause;
	private Image next;
	private Image prev;
	
	@FXML
	private Label title;
	@FXML
	private Label timeElapsed;
	@FXML
	private Label timeTotal;
	
	@FXML
	private Slider slider;
	
	//All attributes for the controller to work
	private boolean isPlaying = false;
	private Playlist currPlaylist;
	private int currPos = 0;
	private long timeElapsedInMs = 0;
	private long timeTotalInMs = 0;
	private PlayerController player;
	private Animation animation;
	
	//Constants for brightness
	private final float BLACK = 0.0f;
	private final float GREY = 0.5f;
	

	public PlayerOverviewController() {
	}
	
	public void onLoad(){
		title.setText("");
		timeElapsed.setText("");
		timeTotal.setText("");
		
		isPlaying = false;
		loadAndSetImages();
		resetButton();
		player = PlayerController.getInstance();
	}
	
	public void onPlay(Playlist playlist, int currPos){
		//Setting the variables
		this.currPlaylist = playlist;
		this.currPos = currPos;
		isPlaying = true;
		//UI changes
		newMusicSet();
		//Playing the song
    	player.stop();
    	player.setPlaylist(playlist, currPos);
    	player.play();
	}
	
	private void resetButton(){
		setBrightImage(prevView, (currPlaylist == null || currPos == 0) ? GREY : BLACK);
		prevView.setDisable(currPlaylist == null || currPos == 0);
		setBrightImage(playPauseView, (currPlaylist == null) ? GREY : BLACK);
		playPauseView.setDisable(currPlaylist == null);
		setBrightImage(nextView, (currPlaylist == null || currPos == currPlaylist.getTotalNumberSongs() - 1) ? GREY : BLACK);
		nextView.setDisable(currPlaylist == null || currPos == currPlaylist.getTotalNumberSongs() - 1);
	}
	
	private void setBrightImage(ImageView i, float brightness){
		ColorAdjust colorAdjust = new ColorAdjust();
		colorAdjust.setBrightness(brightness);
		i.setEffect(colorAdjust);
	}
	
	private void changePlayPauseImage(){
		playPauseView.setImage((isPlaying)? pause : play);
	}
	
	private void loadAndSetImages(){
		play = new Image(getClass().getClassLoader().getResource("play.png").toString());
		pause = new Image(getClass().getClassLoader().getResource("pause.png").toString());
		next = new Image(getClass().getClassLoader().getResource("next.png").toString());
		prev = new Image(getClass().getClassLoader().getResource("previous.png").toString());
		nextView.setImage(next);
		prevView.setImage(prev);
		changePlayPauseImage();
	}
	
	private void newMusicSet(){
		title.setText(currPlaylist.getSong(currPos).getTitle());
		timeElapsedInMs = 0;
		timeElapsed.setText(msToMin(timeElapsedInMs));
		timeTotalInMs = currPlaylist.getSong(currPos).getDuration();
		timeTotal.setText(msToMin(timeTotalInMs));
		resetButton();		
		changePlayPauseImage();
		setNewAnimationSlider();
	}
	
	@FXML
	private void playPauseAction(){
		if(isPlaying){
			player.pause();
			animation.pause();
		} else {
			player.resume();
			animation.play();
		}
		isPlaying = !isPlaying;
		changePlayPauseImage();
	}
	
	@FXML
	private void prevAction(){
		player.previousAndPlay();
		if(!isPlaying)
			player.pause();
		currPos = player.getCurrentSongIndex();
		newMusicSet();
	}
	
	@FXML
	private void nextAction(){
		player.nextAndPlay();
		if(!isPlaying)
			player.pause();
		currPos = player.getCurrentSongIndex();
		newMusicSet();
	}
	
	@FXML
	private void sliderAction(){
		//TODO
	}
	
	private String msToMin(long i){
		return String.valueOf(i/(1000*60)) + ":" + String.valueOf(i%(1000*60)/1000);
	}
	
	private void setNewAnimationSlider(){
		slider.setMin(0);
		slider.setMax(timeTotalInMs);
		slider.setValue((double)timeElapsedInMs/timeTotalInMs);
		if(animation != null)
			animation.stop();
		animation = new Transition() {
		     {
		         setCycleDuration(Duration.millis(timeTotalInMs - timeElapsedInMs));
		     }
		 
		     protected void interpolate(double frac) {
		    	 timeElapsedInMs = (long) (frac * timeTotalInMs);
		         slider.setValue(timeElapsedInMs);
		         timeElapsed.setText(msToMin(timeElapsedInMs));
		     }
		 
		 };
		 if(isPlaying)
			 animation.play();
	}

}
