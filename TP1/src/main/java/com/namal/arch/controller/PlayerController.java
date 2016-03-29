package com.namal.arch.controller;

import java.util.ArrayList;
import java.util.List;

import com.namal.arch.models.Playlist;
import com.namal.arch.models.Song;
import com.namal.arch.utils.IPlayerObserver;
import com.namal.arch.utils.PlayerEventType;
import com.namal.arch.utils.PlayerStatus;

import javazoom.jl.decoder.JavaLayerException;

/**
 * Music player class. Provides the basic functionalities of a player (play, pause, ..).
 * The player always plays a playlist, even if a single Song is played. 
 * @author namalgac
 * @author Fabien Berquez
 *
 */
public class PlayerController {
	
	/**
	 * The song currently being played (or paused)
	 */
	private Song song;
	/**
	 * The current playlist.
	 */
	private Playlist playlist;
	/**
	 * Current position of the song played in the playlist.
	 */
	private int currentPos;
	/**
	 * The PausablePlayer instance currently used to play the song
	 */
	private PausablePlayer pplayer;
	/**
	 * The current status of the player (with values defined in the PlayerStatus enum)
	 */
	private PlayerStatus currentStatus;
	/**
	 * List of observers for this controller (generally views)
	 * The observing views must implement IPlayerObserver.
	 */
	private List<IPlayerObserver> observers;
	/**
	 * The instance of the PlayerController (Singleton pattern).
	 */
	private static PlayerController instance = new PlayerController();
	
	/**
	 * Constructor of this controller, private according to the Singleton pattern
	 */
	private PlayerController() {
		observers = new ArrayList<IPlayerObserver>();
	}
	
	/**
	 * Static method to get the unique instance of PlayerController
	 * @return the unique instance of the PlayerController
	 */
	public static PlayerController getInstance() {
		return instance;
	}
	
	/**
	 * Plays a song. Creates a Playlist with only this song.
	 * @param song : Song played
	 */
	public void play(Song song){
		playlist = new Playlist(song.getTitle(),true,true);
		playlist.addSongWithoutUpdating(song);
		currentPos = 0;
		play();
	}

	/**
	 * Sets the current playlist (the current song is the first one)
	 * @param playlist : the playlist to be played.
	 */
	public void setPlaylist(Playlist playlist){
		this.playlist = playlist;
		currentPos = 0;
	}
	
	/**
	 * Sets the current playlist (the current song is the one chosen)
	 * @param playlist : the playlist to be played
	 * @param songIndex : the index of the chosen song in the playlist.
	 */
	public void setPlaylist(Playlist playlist, int songIndex){
		this.playlist = playlist;
		setCurrentSongIndex(songIndex);
	}
	
	/**
	 * Plays the current song on the playlist
	 */
	public void play(){
		//TODO stop playing song if any 
		//get current song and use song.getInputStream()
		// create a thread and play using JLayer (Player(inputStream).play)
		// probably will need to call stop at the end to cleanup
		song = playlist.getSong(currentPos);
		System.out.println(song);
		try {
			// If pplayer is not null, we ensure that the music
			// will be stopped and the thread will be closed,
			// so we call stop() on the PausablePlayer.
			if(pplayer!=null) {
				pplayer.stop();
				pplayer = null;
			}
			pplayer = new PausablePlayer(song.getInputStream());
			currentStatus = PlayerStatus.NOTSTARTED;
			pplayer.attach(getInstance());
		} catch (JavaLayerException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			pplayer.play();
		} catch (JavaLayerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Pauses the current song
	 */
	public void pause(){
		if(pplayer != null) {
			pplayer.pause();
		}
	}
	
	/**
	 * Stops the current song
	 */
	public void stop(){
		//TODO stop song (player.close()), cleanup (song.cleanup)
		if(pplayer != null) {
			pplayer.stop();
		}
	}
	
	/**
	 *  Resumes the current song
	 */
	public void resume(){
		if(pplayer != null) {
			pplayer.resume();
		}
	}
	
	/**
	 * Sets the current song but does NOT play it.
	 * This method should typically be called when the playlist is paused or stopped.
	 * @param i : if i is within the bounds of the playlist size then it is set to that index
	 * if i&lt;0 then it is set to the last Song of the playlist
	 * if (i&gt;playlist size) it is set to the first Song of the playlist
	 */
	public void setCurrentSongIndex(int i){
		//TODO read carefully the Javadoc
		
		// As multiple calls to getTotalNumberSongs will be made in this method,
		// we store it.
		int sizePlaylist = playlist.getTotalNumberSongs();
		
		if(i<0) {
			currentPos = sizePlaylist - 1;
		}
		else if(i > sizePlaylist - 1) {
			currentPos = 0;
		}
		else {
			currentPos = i;
		}
	}
	
	/**
	 * Sets the current song and plays it right after.
	 * @param i : the new song index
	 */
	public void setCurrentSongIndexAndPlay(int i){
		setCurrentSongIndex(i);
		play();
	}
	
	/**
	 * Gets the index of the current song in the playlist
	 * @return the index of the current song in the playlist
	 */
	public int getCurrentSongIndex(){
		return currentPos;
	}
	
	/**
	 * Gets the number of Songs in the playlist
	 * @return the total number of songs in the current playlist
	 */
	public int getTotalNumberSongs(){
		return playlist.getTotalNumberSongs();
	}
	
	/**
	 * Set the current song to the next one in the playlist but does NOT play it.
	 * If the current song is the last one, the next one is the first Song of the playlist
	 * The current song is NOT stopped.
	 * Therefore this method should typically be called when the playlist is paused or stopped.
	 */
	public void next(){
		setCurrentSongIndex(currentPos+1);
	}
	
	/**
	 * Set the current song to the previous one in the playlist but does NOT play it.
	 * If the current song is the first one, the previous one is the last Song of the playlist
	 * The current song is NOT stopped.
	 * Therefore this method should typically be called when the playlist is paused or stopped.
	 */
	public void previous(){
		setCurrentSongIndex(currentPos-1);
	}
	
	/**
	 * Set the current song to the next one in the playlist and plays it right after.
	 * If the current song is the last one, the next one is the first Song of the playlist.
	 */
	public void nextAndPlay(){
		setCurrentSongIndex(currentPos+1);
		play();
	}
	
	/**
	 * Set the current song to the previous one in the playlist and plays it right after.
	 * If the current song is the first one, the previous one is the last Song of the playlist.
	 */
	public void previousAndPlay(){
		setCurrentSongIndex(currentPos-1);
		play();
	}
	
	/**
	 * Determines if the song is the first of the playlist.
	 * @return true if the current song is the first song of the playlist, false otherwise.
	 */
	public boolean isFirst(){		
		if(playlist == null) {
			return true;
		}
			return currentPos == 0;
	}
	
	/**
	 * Determines if the song is the last of the playlist
	 * @return true if the current song is the last song of the playlist, false otherwise.
	 */
	public boolean isLast(){
		if(playlist == null) {
			return true;
		}
			return currentPos == playlist.getTotalNumberSongs()-1;
	}
	
	/**
	 * Determines if a song is being played.
	 * @return true if a music is playing, false otherwise.
	 */
	public boolean isPlaying(){
		return currentStatus == PlayerStatus.PLAYING;
	}
	
	/**
	 * Gets the current Playlist
	 * @return the playlist field value, representing the playlist currently playing.
	 */
	public Playlist getCurrentPlaylist() {
		return playlist;
	}

	/**
	 * This method is a callback method for the PausablePlayer being observed.
	 * Dispatches the treatment of the event to specialized methods.
	 * @param ev : the PausablePlayerEvent from the PausablePlayer, to be processed.
	 */
	public void update(PausablePlayerEvent ev) {
		switch(ev.getEventType()) {
			case TYPE_STATECHANGED: 
				statusChanged(ev);
				break;
			case TYPE_NEWSONG:
				onNewSong(ev);
				break;
		}
	}
	
	/**
	 * Method treating the events related to a new song being played.
	 * This method should not be called otherwise than through the update method.
	 * @param ev : the PausablePlayerEvent to process.
	 */
	private void onNewSong(PausablePlayerEvent ev) {
		PlayerControllerEvent pcev = new PlayerControllerEvent(getInstance(), PlayerEventType.TYPE_NEWSONG, ev.getEventInformation());
		notifyObservers(pcev);
	}

	/**
	 * Method treating all events related to changes in the status of the player.
	 * This method should not be called otherwise than through the update method.
	 * @param ev : the PausablePlayerEvent to process.
	 */
	private void statusChanged(PlayerEvent ev) {
		if(ev.getEventInformation() == PlayerStatus.FINISHED) {
			if(currentStatus == PlayerStatus.PLAYING) {
				nextAndPlay();
			}
		}
		currentStatus = ev.getEventInformation();
		PlayerControllerEvent pcev = new PlayerControllerEvent(getInstance(), PlayerEventType.TYPE_STATECHANGED, ev.getEventInformation());
		notifyObservers(pcev);
	}
	
	/**
     * Attaches an observer to this player
     * @param observer : an observer of this controller (generally a view) that implements IPlayerObserver.
     */
    public void attach(IPlayerObserver observer) {
    	observers.add(observer);
    }
    /**
     * Detaches the observer passed as a parameter
     * @param observer : an observer that is currently attached to this controller
     */
    public void detach(IPlayerObserver observer) {
    	observers.remove(observer);
    }
    
    /**
     * Notifies all the observers attached to this player
     * @param ev : the PlayerControllerEvent which will be transmitted
     */
    public void notifyObservers(PlayerControllerEvent ev) {
    	for(IPlayerObserver observer : observers) {
    		observer.update(ev);
    	}
    }
    
    /**
     * Gets the current position into the song in ms
     * @return the current position in the song, in ms.
     */
    public long getPosition(){
    	if(pplayer != null){
    		return pplayer.getPosition();
    	}
    	return 0;
    }
    
    /**
     * Gets the current player status
     * @return the current status of the player, which is one of the possible values of the PlayerStatus enum.
     */
    public PlayerStatus getStatus(){
    	
    	return currentStatus;
    }
}
