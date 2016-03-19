package com.namal.arch.plugin.view;

import org.eclipse.fx.ui.workbench3.FXViewPart;

import com.namal.arch.plugin.models.PluginAudioServiceLoader;
import com.namal.arch.utils.Configuration;
import com.namal.arch.view.UIMainClass;

import javafx.scene.Scene;

public class PluginView extends FXViewPart {
	
	@Override
	protected Scene createFxScene() {
		Configuration.setAudioServiceLoader(PluginAudioServiceLoader.getInstance());
		Configuration.setShow(false);
		UIMainClass mainClass = new UIMainClass();
		mainClass.loadGeneraLayout();
		return mainClass.getPrimaryScene();
	}

	@Override
	protected void setFxFocus() {
		
	}
}