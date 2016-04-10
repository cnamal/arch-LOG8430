package com.namal.arch;

import com.namal.arch.models.services.AudioServiceLoader;
import com.namal.arch.utils.Configuration;
import com.namal.arch.utils.Connexion;
import com.namal.arch.view.UIMainClass;

/**
 * Main class.
 * @author namalgac
 *
 */
public class Main{

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//Uncomment/Comment for enable/disable UI
		Configuration.setShow(true);
		Connexion.init();
		UIMainClass uiMainClass = new UIMainClass();
		uiMainClass.firstLoad(args);
	}

}
