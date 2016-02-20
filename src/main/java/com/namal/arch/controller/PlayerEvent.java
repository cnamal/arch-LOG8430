package com.namal.arch.controller;

import com.namal.arch.utils.PlayerEventType;
import com.namal.arch.utils.PlayerStatus;

/**
 * Class representing an event that occurred in the Player (either the PausablePlayer or the PlayerController)
 *
 * @author Fabien Berquez
 */

public class PlayerEvent {
    
    /**
     * Proper attributes of the event object
     */
	private Object eventSource;
    private PlayerEventType eventType;
    private PlayerStatus eventInformation;
    
    /**
     * Constructor allowing easy creation of a PausablePlayerEvent with the needed information
     * @param eventType
     * @param eventInformation
     */
    public PlayerEvent(Object eventSource, PlayerEventType eventType, PlayerStatus eventInformation) {
    	this.eventSource = eventSource;
    	this.eventType = eventType;
    	this.eventInformation = eventInformation;
    }
    
    /**
     * Returns the eventType as an int. Use the constants of this class to compare
     * in conditionnal statements
     * @return the event type (PlayerEventType enum)
     */
	public PlayerEventType getEventType() {
		return eventType;
	}
	
	/**
	 * Sets the eventType. Use the constants of this class as parameters.
	 * Should not be used alone, as the events should logically be created
	 * using the given constructor.
	 * @param eventType a PlayerEventType.
	 */
	public void setEventType(PlayerEventType eventType) {
		this.eventType = eventType;
	}
	
	/**
	 * Returns the eventInformation field value, which gives further information about
	 * the event. Use the constants of this class when writing conditionnal statements.
	 * @return eventInformation
	 */
	public PlayerStatus getEventInformation() {
		return eventInformation;
	}
	
	/**
	 * Sets the eventInformation. Use the constants of this class as parameters.
	 * Should not be used alone, as the events should logically be created
	 * using the given constructor.
	 * @param eventInformation the PlayerStatus (can be null for new song)
	 */
	public void setEventInformation(PlayerStatus eventInformation) {
		this.eventInformation = eventInformation;
	}

	/**
	 * Returns the source object of the event
	 * To be overriden in the subclasses to reflect the concrete class of the source.
	 * @return eventSource a reference to the source of the event.
	 */
	public Object getEventSource() {
		return eventSource;
	}
}
