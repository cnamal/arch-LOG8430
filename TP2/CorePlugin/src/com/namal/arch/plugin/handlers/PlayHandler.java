package com.namal.arch.plugin.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.menu.MToolItem;

import com.namal.arch.controller.PlayerController;
import com.namal.arch.controller.PlayerControllerEvent;
import com.namal.arch.controller.PlayerEvent;
import com.namal.arch.utils.IPlayerObserver;
import com.namal.arch.utils.PlayerEventType;
import com.namal.arch.utils.PlayerStatus;

public class PlayHandler implements IPlayerObserver{

	private boolean isPlaying=false;
	private PlayerController player;
	private MToolItem item;
	
	public PlayHandler(){
		player = PlayerController.getInstance();
		player.attach(this);
	}
	
	@Execute
	public void execute(MToolItem item){
		this.item = item;
		if(isPlaying){
			player.pause();			
		} else {
			player.resume();
		}
		changePlayPauseImage();
	}
	
	private void changePlayPauseImage() {
		if(item != null){
			item.setEnabled(true);
			if(isPlaying)
				item.setIconURI("platform:/plugin/CorePlugin/icons/pause.png");
			else
				item.setIconURI("platform:/plugin/CorePlugin/icons/play.png");		
		}
	}

	@Override
	public void update(PlayerEvent ev) {
		if(ev.getEventType() == PlayerEventType.TYPE_STATECHANGED){
			if (ev.getEventInformation() == PlayerStatus.PLAYING)
				isPlaying = true;
			else if (ev.getEventInformation() == PlayerStatus.PAUSED)
				isPlaying = false;
			changePlayPauseImage();
		}
	}

	
	
	
}
