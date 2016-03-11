package com.namal.arch.controller;

import com.namal.arch.utils.PlayerEventType;
import com.namal.arch.utils.PlayerStatus;

public class PausablePlayerEvent extends PlayerEvent {
	private PausablePlayer eventSource;
	
	public PausablePlayerEvent(PausablePlayer eventSource, PlayerEventType eventType, PlayerStatus eventInformation) {
		super(eventSource, eventType, eventInformation);
	}
	
	public PausablePlayer getEventSource() {
		return eventSource;
	}

}
