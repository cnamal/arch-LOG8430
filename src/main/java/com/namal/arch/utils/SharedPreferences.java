package com.namal.arch.utils;

import java.util.prefs.Preferences;

/**
 * Java API's Preferences helper
 * 
 * @author cnamal
 *
 */
public class SharedPreferences {

	private static final String appName= "com/namal/archi";
	
	/**
	 * 
	 * @return Java API's Preferences
	 */
	public static Preferences getPreferences(){
		return Preferences.userRoot().node(appName);
	}
}
