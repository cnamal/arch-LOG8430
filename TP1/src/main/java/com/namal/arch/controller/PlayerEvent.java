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
     * A reference to the source of the event
     */
	private Object eventSource;
	/**
	 * The type of the event (see PlayerEventType enum for possible values)
	 */
    private PlayerEventType eventType;
    /**
     * Complementary information about the event (in this case the new PlayerStatus, if relevant)
     */
    private PlayerStatus eventInformation;
    
    /**
     * Constructor allowing easy creation of a PausablePlayerEvent with the needed information
     * @param eventSource : a reference to the source of the event
     * @param eventType : the type of the event (from the PlayerEventType enum)
     * @param eventInformation : if relevant, complementary information about the event, null otherwise.
     */
    public PlayerEvent(Object eventSource, PlayerEventType eventType, PlayerStatus eventInformation) {
    	this.eventSource = eventSource;
    	this.eventType = eventType;
    	this.eventInformation = eventInformation;
    }
    
    /**
     * Returns the eventType as a value of the PlayerEventType enum
     * @return the event type (PlayerEventType enum)
     */
	public PlayerEventType getEventType() {
		return eventType;
	}
	
	/**
	 * Sets the eventType. Use the values in the enum PlayerEventType.
	 * Should not be used alone, as the events should logically be created
	 * using the given constructor.
	 * @param eventType : a value defined in the PlayerEventType enum, representing the event type.
	 */
	public void setEventType(PlayerEventType eventType) {
		this.eventType = eventType;
	}
	
	/**
	 * Returns the eventInformation field value, which gives further information about
	 * the event. The possible values are those of the PlayerStatus enum.
	 * @return eventInformation : if relevant, the status of the player, null otherwise.
	 */
	public PlayerStatus getEventInformation() {
		return eventInformation;
	}
	
	/**
	 * Sets the eventInformation. Use the values of the PlayerStatus enum as parameter.
	 * Should not be used alone, as the events should logically be created
	 * using the given constructor.
	 * @param eventInformation : the new player status, or null in case of a new song event
	 */
	public void setEventInformation(PlayerStatus eventInformation) {
		this.eventInformation = eventInformation;
	}

	/**
	 * Returns the source object of the event
	 * To be overriden in the subclasses to reflect the concrete class of the source.
	 * @return eventSource : a reference to the source of the event.
	 */
	public Object getEventSource() {
		return eventSource;
	}
}
