package com.namal.arch.controller;

import com.namal.arch.utils.PlayerEventType;
import com.namal.arch.utils.PlayerStatus;

public class PlayerControllerEvent extends PlayerEvent {
	private PlayerController eventSource;
	
	public PlayerControllerEvent(PlayerController eventSource, PlayerEventType eventType, PlayerStatus eventInformation) {
		super(eventSource, eventType, eventInformation);
	}
	
	public PlayerController getEventSource() {
		return eventSource;
	}

}
