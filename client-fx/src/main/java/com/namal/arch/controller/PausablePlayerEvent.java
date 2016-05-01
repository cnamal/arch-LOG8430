package com.namal.arch.controller;

import com.namal.arch.utils.PlayerEventType;
import com.namal.arch.utils.PlayerStatus;

/**
 * Class designed to encapsulate data from events occurring in the PausablePlayer.
 * @author Fabien Berquez
 *
 */
public class PausablePlayerEvent extends PlayerEvent {
	/**
	 * Attribute overriding the eventSource attribute of PlayerEvent.
	 * The concrete type is PausablePlayer, as the source can only be a PausablePlayer instance.
	 */
	private PausablePlayer eventSource;
	
	/**
	 * Constructor overriding the PlayerEvent constructor.
	 * @param eventSource : a reference to the source of the event (a PausablePlayer instance)
	 * @param eventType : the type of the event (values defined in the PlayerEventType enum) 
	 * @param eventInformation : if relevant, complementary information about the event (the new status). Null otherwise.
	 */
	public PausablePlayerEvent(PausablePlayer eventSource, PlayerEventType eventType, PlayerStatus eventInformation) {
		super(eventSource, eventType, eventInformation);
	}
	
	/**
	 * Gets the eventSource attribute value.
	 * @return a reference to the event source object (a PausablePlayer instance).
	 */
	public PausablePlayer getEventSource() {
		return eventSource;
	}

}
