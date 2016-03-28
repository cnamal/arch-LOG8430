package com.namal.arch.plugin.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.menu.MToolItem;

import com.namal.arch.controller.PlayerController;
import com.namal.arch.controller.PlayerEvent;
import com.namal.arch.utils.IPlayerObserver;

public class NextHandler implements IPlayerObserver{

	private PlayerController player;
	
	public NextHandler(){
		player = PlayerController.getInstance();
		player.attach(this);
	}
	
	@Execute
	public void execute(MToolItem item){
		player.nextAndPlay();
		System.out.println("next handler");
	}
	
	private void next(){
		
	}

	@Override
	public void update(PlayerEvent ev) {
	}
}
