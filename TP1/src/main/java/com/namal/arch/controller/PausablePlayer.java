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

	private List<PlayerController> observers;

    // the player actually doing all the work
    private final Player player;

    // locking object used to communicate with player thread
    private final Object playerLock = new Object();

    // status variable what player thread is doing/supposed to do
    private PlayerStatus playerStatus = PlayerStatus.NOTSTARTED;

    public PausablePlayer(final InputStream inputStream) throws JavaLayerException {
        this.player = new Player(inputStream);
        this.observers = new ArrayList<PlayerController>();
    }

    public PausablePlayer(final InputStream inputStream, final AudioDevice audioDevice) throws JavaLayerException {
        this.player = new Player(inputStream, audioDevice);
        this.observers = new ArrayList<PlayerController>();
    }
    
    /**
     * Attach an observer to this player
     */
    public void attach(PlayerController playerController) {
    	observers.add(playerController);
    }
    /**
     * Detach the observer passed as a parameter
     * @param playerController
     */
    public void detach(PlayerController playerController) {
    	observers.remove(playerController);
    }
    
    /**
     * Notify each observer in the observers List. This method is called
     * each time the status is changed. Other events might easily be implemented
     * if needed.
     * @param ev a PausablePlayerEvent
     */
    public void notifyObservers(PausablePlayerEvent ev) {
    	for(PlayerController observer : observers) {
    		observer.update(ev);
    	}
    }

    /**
     * Change the current status in this player. This method also generates
     * a PausablePlayerEvent which encapsulates the data about the status changes
     * and calls the notifyObservers method to notify all the observers attached
     * to this object.
     * @param newStatus
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
     * Starts playback (resumes if PausablePlayerEvent.PAUSED)
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
     * Pauses playback. Returns true if new state is PausablePlayerEvent.PAUSED.
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
     * Resumes playback. Returns true if the new state is PausablePlayerEvent.PLAYING.
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
     * Stops playback. If not PausablePlayerEvent.PLAYING, does nothing
     */
    public void stop() {
        synchronized (playerLock) {
            changeStatus(PlayerStatus.STOPPED);
            playerLock.notifyAll();
        }
    }

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