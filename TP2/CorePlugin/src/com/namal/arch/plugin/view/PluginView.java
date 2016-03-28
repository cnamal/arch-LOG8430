package com.namal.arch.plugin.view;

import org.eclipse.fx.ui.workbench3.FXViewPart;

import com.namal.arch.plugin.handlers.NextHandler;
import com.namal.arch.plugin.handlers.PlayHandler;
import com.namal.arch.plugin.handlers.PreviousHandler;
import com.namal.arch.view.UIMainClass;

import javafx.scene.Scene;

public class PluginView extends FXViewPart {
	
	@Override
	protected Scene createFxScene() {
		PlayHandler.initItem();
		NextHandler.initItem();
		PreviousHandler.initItem();
		UIMainClass mainClass = new UIMainClass();
		mainClass.loadGeneraLayout();
		return mainClass.getPrimaryScene();
	}

	@Override
	protected void setFxFocus() {
		
	}
	
}