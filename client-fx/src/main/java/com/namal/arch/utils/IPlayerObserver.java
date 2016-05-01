package com.namal.arch.utils;

import com.namal.arch.controller.PlayerEvent;

/**
 * Interface to be implemented by the views observing the PlayerController.
 * @author Fabien Berquez
 *
 */
public interface IPlayerObserver {
	/**
	 * Method receiving the events of the PlayerController to process them.
	 * @param ev : a PlayerEvent encapsulating the data of the event that occurred in the PlayerController.
	 */
	public void update(PlayerEvent ev);
}
