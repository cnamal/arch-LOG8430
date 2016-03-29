package com.namal.arch.controller;

import com.namal.arch.utils.PlayerEventType;
import com.namal.arch.utils.PlayerStatus;

/**
 * Class designed to encapsulate data from events occurring in the PlayerController.
 * @author Fabien Berquez
 *
 */

public class PlayerControllerEvent extends PlayerEvent {
	/**
	 * Attribute overriding the eventSource attribute of PlayerEvent.
	 * The concrete type is PlayerController, as the source can only be the PlayerController instance.
	 */
	private PlayerController eventSource;
	
	/**
	 * Constructor overriding the PlayerEvent constructor.
	 * @param eventSource : a reference to the source of the event (a PlayerController instance)
	 * @param eventType : the type of the event (values defined in the PlayerEventType enum) 
	 * @param eventInformation : if relevant, complementary information about the event (the new status). Null otherwise.
	 */
	public PlayerControllerEvent(PlayerController eventSource, PlayerEventType eventType, PlayerStatus eventInformation) {
		super(eventSource, eventType, eventInformation);
	}
	
	/**
	 * Gets the eventSource attribute value.
	 * @return a reference to the event source object (the PlayerController instance).
	 */
	public PlayerController getEventSource() {
		return eventSource;
	}

}
