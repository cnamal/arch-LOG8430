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
 * Handler for the previous button
 * @author namalgac
 *
 */
public class PreviousHandler implements IPlayerObserver{

	private PlayerController player;
	private static MToolItem item;
	private static final String ID = "coreplugin.handledtoolitem.2";
	
	@Inject
    private static EModelService modelService;
	
	@Inject
    private static MApplication app;
	
	public PreviousHandler(){
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
		player.previousAndPlay();
		System.out.println("prev handler");
	}

	/** {@inheritDoc}
	 */
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
