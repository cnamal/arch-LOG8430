package com.namal.arch.utils;

import com.namal.arch.controller.PlayerEvent;

public interface IPlayerObserver {
	
	public void update(PlayerEvent ev);
}
