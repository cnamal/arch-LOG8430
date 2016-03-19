package com.namal.arch.plugin.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.menu.MToolItem;

public class NextHandler {

	@Execute
	public void execute(MToolItem item){
		System.out.println("next handler");
	}
}
