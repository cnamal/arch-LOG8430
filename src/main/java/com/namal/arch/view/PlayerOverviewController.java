package com.namal.arch.view;

import javax.naming.TimeLimitExceededException;

import com.namal.arch.controller.PlayerController;
import com.namal.arch.controller.PlayerEvent;
import com.namal.arch.models.Playlist;
import com.namal.arch.utils.IPlayerObserver;
import com.namal.arch.utils.PlayerEventType;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class PlayerOverviewController extends UIController implements IPlayerObserver {
	
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
	private Timeline timeline;
	
	//Constants for brightness
	private final float BLACK = 0.0f;
	private final float GREY = 0.5f;
	

	public PlayerOverviewController() {
	}
	
	public void onLoad(Pane refPane, UIMainClass mainApp){
		this.mainApp = mainApp;
		this.refPane = refPane;
		title.setText("");
		timeElapsed.setText("");
		timeTotal.setText("");
		
		isPlaying = false;
		loadAndSetImages();
		resetButton();
		player = PlayerController.getInstance();
		player.attach(this);
		
		//Resize the player (for the text)
		mainApp.getPrimaryStage().getScene().widthProperty().addListener(new ChangeListener<Number>() {
		    @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
				resizeText(newSceneWidth, oldSceneWidth);
		    }
		});
	}
	
	public void onPlay(Playlist playlist, int currPos){
		//Setting the variables
		this.currPlaylist = playlist;
		this.currPos = currPos;
		isPlaying = true;
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
		currPos = player.getCurrentSongIndex();
		currPlaylist = player.getCurrentPlaylist();
		title.setText(currPlaylist.getSong(currPos).getTitle());
		timeElapsedInMs = 0;
		timeElapsed.setText(msToMin(timeElapsedInMs));
		timeTotalInMs = currPlaylist.getSong(currPos).getDuration();
		timeTotal.setText(msToMin(timeTotalInMs));
		resetButton();		
		changePlayPauseImage();
		resizeText(refPane.getWidth() - 4 * timeTotal.getWidth() - slider.getWidth() - 3 * nextView.getFitWidth(), title.getMaxWidth());
		//setNewAnimationSlider();
		setSlider();
	}
	
	private void resizeText(Number newSceneWidth, Number oldSceneWidth){
		title.setMaxWidth(title.getMaxWidth() + (newSceneWidth.intValue() - oldSceneWidth.intValue()));
	}
	
	@FXML
	private void playPauseAction(){
		if(isPlaying){
			timeline.pause();
			player.pause();			
		} else {
			timeline.play();
			player.resume();
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
	
	public static String msToMin(long i){
		return String.valueOf(i/(1000*60)) + ":" + ((i%(1000*60)/1000 < 10) ? "0" : "") + String.valueOf(i%(1000*60)/1000);
	}
	
	private void setSlider(){
		slider.setMin(0);
		slider.setMax(timeTotalInMs);
		timeElapsedInMs = player.getPosition();
		slider.setValue((double)timeElapsedInMs/timeTotalInMs);
		timeline = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {

		    @Override
		    public void handle(ActionEvent event) {
		    	timeElapsedInMs = player.getPosition();
		    	slider.setValue(timeElapsedInMs);
		         timeElapsed.setText(msToMin(timeElapsedInMs));
		    }
		}));
		timeline.setCycleCount(Timeline.INDEFINITE);
		if(isPlaying)
			timeline.play();
	}
	

	@Override
	public void update(PlayerEvent ev) {
		if(ev.getEventType() == PlayerEventType.TYPE_NEWSONG){
			Platform.runLater(()->{newMusicSet();});
			//newMusicSet();
		}
		
	}

}
