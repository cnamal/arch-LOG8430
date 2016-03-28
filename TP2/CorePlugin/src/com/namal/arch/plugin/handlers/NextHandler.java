package com.namal.arch.plugin.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.menu.MToolItem;

import com.namal.arch.controller.PlayerController;
import com.namal.arch.controller.PlayerEvent;
import com.namal.arch.utils.IPlayerObserver;
import com.namal.arch.utils.PlayerEventType;
import com.namal.arch.utils.PlayerStatus;

public class NextHandler implements IPlayerObserver{

	private PlayerController player;
	private MToolItem item;
	
	public NextHandler(){
		player = PlayerController.getInstance();
		player.attach(this);
	}
	
	@Execute
	public void execute(MToolItem item){
		this.item = item;
		player.nextAndPlay();
		System.out.println("next handler");
	}

	@Override
	public void update(PlayerEvent ev) {
		if(ev.getEventType() == PlayerEventType.TYPE_NEWSONG && item != null){
			if(PlayerController.getInstance().getTotalNumberSongs() - PlayerController.getInstance().getCurrentSongIndex() == 1)
				item.setEnabled(false);
			else
				item.setEnabled(true);
		}
	}
}
