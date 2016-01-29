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
	
	private Song song;
	private Playlist playlist;
	private int currentPos;
	private PausablePlayer pplayer;
	private PlayerStatus currentStatus;
	private List<IPlayerObserver> observers;
	private static PlayerController instance = new PlayerController();
	
	private PlayerController() {
		observers = new ArrayList<IPlayerObserver>();
	}
	
	public static PlayerController getInstance() {
		return instance;
	}
	
	/**
	 * Plays a song
	 * @param song Song played
	 */
	public void play(Song song){
		playlist = new Playlist(song.getTitle());
		playlist.addSong(song);
		currentPos = 0;
		play();
	}

	/**
	 * Sets the current playlist (the current song is the first one)
	 * @param playlist
	 */
	public void setPlaylist(Playlist playlist){
		this.playlist = playlist;
		currentPos = 0;
	}
	
	/**
	 * Sets the current playlist (the current song is the one chosen)
	 * @param playlist
	 * @param songIndex
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
	 * @param i if i is within the bounds of the playlist size then it is set to that index
	 * if i<0 then it is set to the last Song of the playlist
	 * if (i>playlist size) it is set to the first Song of the playlist
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
	 * @param i
	 */
	public void setCurrentSongIndexAndPlay(int i){
		setCurrentSongIndex(i);
		// TODO call play
	}
	
	/**
	 * Index of the current song in the playlist
	 * @return 
	 */
	public int getCurrentSongIndex(){
		return currentPos;
	}
	
	/**
	 * Number of Songs in the playlist
	 * @return 
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
	 * 
	 * @return true if the current song is the first song of the playlist, false otherwise.
	 */
	public boolean isFirst(){		
		if(playlist == null) {
			return true;
		}
			return currentPos == 0;
	}
	
	/**
	 * 
	 * @return true if the current song is the last song of the playlist, false otherwise.
	 */
	public boolean isLast(){
		if(playlist == null) {
			return true;
		}
			return currentPos == playlist.getTotalNumberSongs()-1;
	}
	
	/**
	 * 
	 * @return true if a music is playing, false otherwise.
	 */
	public boolean isPlaying(){
		return currentStatus == PlayerStatus.PLAYING;
	}
	
	/**
	 * 
	 * @return the playlist field value, representing the playlist currently playing.
	 */
	public Playlist getCurrentPlaylist() {
		return playlist;
	}

	/**
	 * This method is a callback method for the PausablePlayer being observed.
	 * Dispatches the treatment of the event to specialized methods.
	 * @param ev
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
	 * @param ev the PausablePlayerEvent to process.
	 */
	private void onNewSong(PausablePlayerEvent ev) {
		PlayerControllerEvent pcev = new PlayerControllerEvent(getInstance(), PlayerEventType.TYPE_NEWSONG, ev.getEventInformation());
		notifyObservers(pcev);
	}

	/**
	 * Method treating all events related to changes in the status of the player.
	 * This method should not be called otherwise than through the update method.
	 * @param ev the PausablePlayerEvent to process.
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
     * Attach an observer to this player
     */
    public void attach(IPlayerObserver observer) {
    	observers.add(observer);
    }
    /**
     * Detach the observer passed as a parameter
     * @param observer
     */
    public void detach(IPlayerObserver observer) {
    	observers.remove(observer);
    }
    
    /**
     * Notifies all the observers attached to this player
     * @param ev the PlayerControllerEvent which will be transmitted
     */
    public void notifyObservers(PlayerControllerEvent ev) {
    	for(IPlayerObserver observer : observers) {
    		observer.update(ev);
    	}
    }
    
    /**
     * Get the current position into the song in ms
     */
    public long getPosition(){
    	if(pplayer != null){
    		return pplayer.getPosition();
    	}
    	return 0;
    }
}
