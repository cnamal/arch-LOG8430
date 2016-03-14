package com.namal.arch.plugin.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

import com.namal.arch.models.services.AudioService;
import com.namal.arch.models.services.IAudioServiceLoader;


public class PluginAudioServiceLoader implements IAudioServiceLoader{

	
	private String ID = "CorePlugin.com.namal.arch.plugin";
	private static List<AudioService> audioServices;
	private static PluginAudioServiceLoader instance = new PluginAudioServiceLoader();
	
	@Override
	public Iterator<AudioService> getAudioServices() {
		if(audioServices==null){
			audioServices = new ArrayList<>();
			loadPlugins();
		}
		return audioServices.iterator();
	}

	private void loadPlugins(){
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IConfigurationElement[] config =
		        registry.getConfigurationElementsFor(ID);
		    try {
		      for (IConfigurationElement e : config) {
		        System.out.println("Evaluating extension");
		        final Object o =
		            e.createExecutableExtension("class");
		        if (o instanceof AudioService) {
		        	audioServices.add((AudioService)o);
		        }
		      }
		    } catch (CoreException ex) {
		      System.out.println(ex.getMessage());
		    }
	}
	
	public static IAudioServiceLoader getInstance(){
		return instance;
	}
	
	private PluginAudioServiceLoader(){};
}
