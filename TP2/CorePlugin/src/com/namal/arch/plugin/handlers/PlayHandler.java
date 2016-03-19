package com.namal.arch.plugin.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.menu.MToolItem;

public class PlayHandler {

	private boolean isPlaying=false;
	
	@Execute
	public void execute(MToolItem item){
		/*item.setEnabled(true);
		if(isPlaying)
			item.setIconURI("platform:/plugin/CorePlugin/icons/pause.png");
		else
			item.setIconURI("platform:/plugin/CorePlugin/icons/play.png");*/
		System.out.println("play/pause handler");
		isPlaying = !isPlaying;
	}
}
