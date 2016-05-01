package com.namal.arch.controller;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.namal.arch.utils.PlayerEventType;
import com.namal.arch.utils.PlayerStatus;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.Player;

/**
 * Class managing the playing of songs. 
 * 
 * Source : http://stackoverflow.com/questions/12057214/jlayer-pause-and-resume-song
 *
 * Adapted by Fabien Berquez to add the Observer Design Pattern and the stopped state.
 * The constants describing the possible states of the PausablePlayer have also been moved to be shared
 * by the events, the PlayerController and this class.
 */
public class PausablePlayer {

	/**
	 * List of the observers attached to this PausablePlayer
	 */
	private List<PlayerController> observers;

    // the player actually doing all the work
	/**
	 * The JLayer Player instance used for this song
	 */
    private final Player player;

    // locking object used to communicate with player thread
    /**
     * A lock to ensure the integrity of variables shared with the player thread.
     */
    private final Object playerLock = new Object();

    // status variable what player thread is doing/supposed to do
    /**
     * The current status of the player, with values defined in the PlayerStatus enum.
     */
    private PlayerStatus playerStatus = PlayerStatus.NOTSTARTED;

    /**
     * Constructor using only the InputStream of the song as parameter
     * @param inputStream : the InputStream of the song
     * @throws JavaLayerException
     */
    public PausablePlayer(final InputStream inputStream) throws JavaLayerException {
        this.player = new Player(inputStream);
        this.observers = new ArrayList<PlayerController>();
    }

    /**
     * Constructor taking the InputStream of the song and an AudioDevice as parameters
     * @param inputStream : the InputStream of the song to be played
     * @param audioDevice : the AudioDevice on which the song will be played
     * @throws JavaLayerException
     */
    public PausablePlayer(final InputStream inputStream, final AudioDevice audioDevice) throws JavaLayerException {
        this.player = new Player(inputStream, audioDevice);
        this.observers = new ArrayList<PlayerController>();
    }
    
    /**
     * Attach an observer to this player
     * @param playerController : the PlayerController which will observe the player
     */
    public void attach(PlayerController playerController) {
    	observers.add(playerController);
    }
    /**
     * Detach the observer passed as a parameter
     * @param playerController : the PlayerController which will stop to observe the player.
     */
    public void detach(PlayerController playerController) {
    	observers.remove(playerController);
    }
    
    /**
     * Notify each observer in the observers List. This method is called
     * each time the status is changed, or each time the song changes
     * @param ev : the PausablePlayerEvent to be sent to the observers.
     */
    public void notifyObservers(PausablePlayerEvent ev) {
    	for(PlayerController observer : observers) {
    		observer.update(ev);
    	}
    }

    /**
     * Change the current status in this player. This method also generates
     * a PausablePlayerEvent which encapsulates the data about the status change
     * and calls the notifyObservers method to notify all the observers attached
     * to this object.
     * @param newStatus : the new status of the player, with values defined in the PlayerStatus enum.
     */
    public void changeStatus(PlayerStatus newStatus) {
    	playerStatus = newStatus;
    	PausablePlayerEvent ev = new PausablePlayerEvent(this, PlayerEventType.TYPE_STATECHANGED, newStatus);
    	notifyObservers(ev);
    }
    
    /**
     * Executes when a new song is played. This method also generates
     * a PausablePlayerEvent of type "New Song" and 
     * calls the notifyObservers method to notify all the observers attached
     * to this object.
     */
    public void onNewSong() {
    	PausablePlayerEvent ev = new PausablePlayerEvent(this, PlayerEventType.TYPE_NEWSONG, null);
    	notifyObservers(ev);
    }
    
    /**
     * Starts playback (resumes if the status is PausablePlayerEvent.PAUSED)
     */
    public void play() throws JavaLayerException {
        synchronized (playerLock) {
            switch (playerStatus) {
                case NOTSTARTED:
                    final Runnable r = new Runnable() {
                        public void run() {
                            playInternal();
                        }
                    };
                    final Thread t = new Thread(r);
                    t.setDaemon(true);
                    t.setPriority(Thread.MAX_PRIORITY);
                    changeStatus(PlayerStatus.PLAYING);
                    onNewSong();
                    t.start();
                    break;
                case PAUSED:
                    resume();
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Pauses playback.
     * @return true if new state is PausablePlayerEvent.PAUSED, false otherwise.
     */
    public boolean pause() {
        synchronized (playerLock) {
            if (playerStatus == PlayerStatus.PLAYING) {
                changeStatus(PlayerStatus.PAUSED);
            }
            return playerStatus == PlayerStatus.PAUSED;
        }
    }

    /**
     * Resumes playback.
     * @return true if the new state is PausablePlayerEvent.PLAYING, false otherwise.
     */
    public boolean resume() {
        synchronized (playerLock) {
            if (playerStatus == PlayerStatus.PAUSED) {
                changeStatus(PlayerStatus.PLAYING);
                playerLock.notifyAll();
            }
            return playerStatus == PlayerStatus.PLAYING;
        }
    }

    /**
     * Stops playback. If the currentState is not PausablePlayerEvent.PLAYING, does nothing
     */
    public void stop() {
        synchronized (playerLock) {
            changeStatus(PlayerStatus.STOPPED);
            playerLock.notifyAll();
        }
    }

    /**
     * Internal method implementing how the song will be played.
     */
    private void playInternal() {
        while (playerStatus != PlayerStatus.FINISHED && playerStatus != PlayerStatus.STOPPED) {
            try {
                if (!player.play(1)) {
                    break;
                }
            } catch (final JavaLayerException e) {
                break;
            }
            // check if PausablePlayerEvent.PAUSED or terminated
            synchronized (playerLock) {
                while (playerStatus == PlayerStatus.PAUSED) {
                    try {
                        playerLock.wait();
                    } catch (final InterruptedException e) {
                        // terminate player
                        break;
                    }
                }
            }
        }
        close();
    }

    /**
     * Closes the player, regardless of current state.
     */
    public void close() {
        synchronized (playerLock) {
            changeStatus(PlayerStatus.FINISHED);
        }
        try {
            player.close();
        } catch (final Exception e) {
            // ignore, we are terminating anyway
        }
    }
    
    /**
     * Get the position into the song in ms
     * @return the position in the song (in ms)
     */
    public long getPosition(){
    	if(player != null){
    		return player.getPosition();
    	}
    	return 0;
    }
}