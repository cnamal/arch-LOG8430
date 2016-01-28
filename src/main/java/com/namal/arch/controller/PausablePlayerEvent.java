package com.namal.arch.controller;

/**
 * Class representing an event that occurred in the PausablePlayer 
 *
 * @author Fabien Berquez
 */

public class PausablePlayerEvent {
	
	/**
	 * Constants representing the possible states of the player
	 * These constants are centralized here to be used in the whole package,
	 * preventing the dispersion of information
	 */
	protected final static int NOTSTARTED = 0;
    protected final static int PLAYING = 1;
    protected final static int PAUSED = 2;
    protected final static int FINISHED = 3;
    protected final static int STOPPED = 4;
    
    /**
     * Constants representing the type of the event
     */
    protected final static int TYPE_STATECHANGED = 1;
    
    /**
     * Proper attributes of the event object
     */
    private int eventType;
    private int eventInformation;
    
    /**
     * Constructor allowing easy creation of a PausablePlayerEvent with the needed information
     * @param eventType
     * @param eventInformation
     */
    public PausablePlayerEvent(int eventType, int eventInformation) {
    	this.eventType = eventType;
    	this.eventInformation = eventInformation;
    }
    
    /**
     * Returns the eventType as an int. Use the constants of this class to compare
     * in conditionnal statements
     * @return eventType
     */
	public int getEventType() {
		return eventType;
	}
	
	/**
	 * Sets the eventType. Use the constants of this class as parameters.
	 * Should not be used alone, as the events should logically be created
	 * using the given constructor.
	 * @param eventType
	 */
	public void setEventType(int eventType) {
		this.eventType = eventType;
	}
	
	/**
	 * Returns the eventInformation field value, which gives further information about
	 * the event. Use the constants of this class when writing conditionnal statements.
	 * @return eventInformation
	 */
	public int getEventInformation() {
		return eventInformation;
	}
	
	/**
	 * Sets the eventInformation. Use the constants of this class as parameters.
	 * Should not be used alone, as the events should logically be created
	 * using the given constructor.
	 * @param eventInformation
	 */
	public void setEventInformation(int eventInformation) {
		this.eventInformation = eventInformation;
	}
}
