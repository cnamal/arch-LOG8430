package com.namal.arch.plugin.handlers;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.menu.MToolItem;
import org.eclipse.e4.ui.workbench.modeling.EModelService;

import com.namal.arch.controller.PlayerController;
import com.namal.arch.controller.PlayerEvent;
import com.namal.arch.utils.IPlayerObserver;
import com.namal.arch.utils.PlayerEventType;
import com.namal.arch.utils.PlayerStatus;

public class PlayHandler implements IPlayerObserver{

	private boolean isPlaying=false;
	private PlayerController player;
	private static MToolItem item;
	
	@Inject
    private static EModelService modelService;
	
	@Inject
    private static MApplication app;
	
	public PlayHandler(){
		player = PlayerController.getInstance();
		player.attach(this);
	}
	
	
	public static void initItem() {
	    item = (MToolItem) modelService.find("coreplugin.handledtoolitem.0", app);
	}
	
	@Execute
	public void execute(){
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
