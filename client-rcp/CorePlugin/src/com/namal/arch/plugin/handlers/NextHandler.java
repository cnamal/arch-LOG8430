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


/**
 * Handler for the next button
 * @author namalgac
 *
 */
public class NextHandler implements IPlayerObserver{

	private PlayerController player;
	private static MToolItem item;
	private static final String ID = "coreplugin.handledtoolitem.1";
	
	@Inject
    private static EModelService modelService;
	
	@Inject
    private static MApplication app;
	
	public NextHandler(){
		player = PlayerController.getInstance();
		player.attach(this);
	}
	
	/**
	 * Init function
	 */
	public static void initItem() {
	    item = (MToolItem) modelService.find(ID, app);
	}
	
	/**
	 * Function called when the button is clicked
	 */
	@Execute
	public void execute(){
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
