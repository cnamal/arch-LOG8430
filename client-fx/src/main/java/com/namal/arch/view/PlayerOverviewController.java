package com.namal.arch.view;

import com.namal.arch.controller.PlayerController;
import com.namal.arch.controller.PlayerEvent;
import com.namal.arch.models.Playlist;
import com.namal.arch.utils.IPlayerObserver;
import com.namal.arch.utils.PlayerEventType;
import com.namal.arch.utils.PlayerStatus;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
	
	/**
     * Function to call in order to load this service
     * @param refPane The pane where the module will be attached
     * @param mainApp Reference to the main app
     */
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
		mainApp.getPrimaryScene().widthProperty().addListener(new ChangeListener<Number>() {
		    @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
				resizeText(newSceneWidth, oldSceneWidth);
		    }
		});
		
		//Add events
		
	}
	
	/**
     * Play the playlist at the positon
     * @param playlist The playlist played
     * @param currPos At which position
     */
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
	
	/**
	 * Reset all the buttons (and put them grey if needed)
	 * They're grey when the action is not possible
	 */
	private void resetButton(){
		setBrightImage(prevView, (currPlaylist == null || currPos == 0) ? GREY : BLACK);
		prevView.setDisable(currPlaylist == null || currPos == 0);
		setBrightImage(playPauseView, (currPlaylist == null) ? GREY : BLACK);
		playPauseView.setDisable(currPlaylist == null);
		setBrightImage(nextView, (currPlaylist == null || currPos == currPlaylist.getTotalNumberSongs() - 1) ? GREY : BLACK);
		nextView.setDisable(currPlaylist == null || currPos == currPlaylist.getTotalNumberSongs() - 1);
	}
	
	/**
	 * Change the brightness of an image
	 * @param i the image
	 * @param brightness Between 0 and 1
	 */
	private void setBrightImage(ImageView i, float brightness){
		ColorAdjust colorAdjust = new ColorAdjust();
		colorAdjust.setBrightness(brightness);
		i.setEffect(colorAdjust);
	}
	
	/**
	 * Change the play image according to the player is playing or not
	 */
	private void changePlayPauseImage(){
		playPauseView.setImage((isPlaying)? pause : play);
	}
	
	/**
	 * Load all the images
	 */
	private void loadAndSetImages(){
		play = new Image(getClass().getClassLoader().getResource("play.png").toString());
		pause = new Image(getClass().getClassLoader().getResource("pause.png").toString());
		next = new Image(getClass().getClassLoader().getResource("next.png").toString());
		prev = new Image(getClass().getClassLoader().getResource("previous.png").toString());
		nextView.setImage(next);
		prevView.setImage(prev);
		changePlayPauseImage();
	}
	
	/**
	 * Called when a new music is played
	 */
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
	
	/**
	 * Resize the text into the player according to its new size
	 * @param newSceneWidth The new size
	 * @param oldSceneWidth The older size
	 */
	private void resizeText(Number newSceneWidth, Number oldSceneWidth){
		title.setMaxWidth(title.getMaxWidth() + (newSceneWidth.intValue() - oldSceneWidth.intValue()));
	}
	
	@FXML
	private void playPauseAction(){
		if(isPlaying)
			player.pause();			
		else
			player.resume();
	}
	
	@FXML
	private void prevAction(){
		player.previousAndPlay();
		if(!isPlaying)
			player.pause();
	}
	
	@FXML
	private void nextAction(){
		player.nextAndPlay();
		if(!isPlaying)
			player.pause();
	}
	
	@FXML
	private void sliderAction(){
		//TODO
	}
	
	/**
	 * @param i number of millisecond
	 * @return String associated with min:sec
	 */
	public static String msToMin(long i){
		return String.valueOf(i/(1000*60)) + ":" + ((i%(1000*60)/1000 < 10) ? "0" : "") + String.valueOf(i%(1000*60)/1000);
	}
	
	/**
	 * Update the slider
	 */
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
	

	/**
	 * Reaction to events
	 * @param ev the event
	 */
	@Override
	public void update(PlayerEvent ev) {
		if(ev.getEventType() == PlayerEventType.TYPE_NEWSONG){
			Platform.runLater(()->{newMusicSet();});
			//newMusicSet();
		}
		else if(ev.getEventType() == PlayerEventType.TYPE_STATECHANGED){
			if (ev.getEventInformation() == PlayerStatus.PLAYING){
				isPlaying = true;
				if(timeline != null)
					timeline.play();
			} else if (ev.getEventInformation() == PlayerStatus.PAUSED){
				if(timeline != null)
					timeline.pause();
				isPlaying = false;
			}
			changePlayPauseImage();
		}
	}

}
