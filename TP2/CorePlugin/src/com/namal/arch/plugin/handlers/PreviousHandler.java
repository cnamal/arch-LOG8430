package com.namal.arch.plugin.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.menu.MToolItem;

import com.namal.arch.controller.PlayerController;
import com.namal.arch.controller.PlayerEvent;
import com.namal.arch.utils.IPlayerObserver;
import com.namal.arch.utils.PlayerEventType;

public class PreviousHandler implements IPlayerObserver{

	private PlayerController player;
	private MToolItem item;
	
	public PreviousHandler(){
		player = PlayerController.getInstance();
		player.attach(this);
	}
	
	@Execute
	public void execute(MToolItem item){
		this.item = item;
		player.previousAndPlay();
		System.out.println("prev handler");
	}

	@Override
	public void update(PlayerEvent ev) {
		if(ev.getEventType() == PlayerEventType.TYPE_NEWSONG && item != null){
			if(PlayerController.getInstance().getCurrentSongIndex() == 0)
				item.setEnabled(false);
			else
				item.setEnabled(true);
		}
	}
	
	
}
