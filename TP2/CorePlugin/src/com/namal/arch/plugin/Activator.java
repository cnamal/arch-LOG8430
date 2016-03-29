package com.namal.arch.plugin;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.namal.arch.plugin.models.PluginAudioServiceLoader;
import com.namal.arch.utils.Configuration;
import com.namal.arch.utils.MongoDB;

/**
 * Activator class that controls the life cycle of the plugin.
 * @author namalgac
 *
 */
public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		
		// setup for our plugin
		Configuration.setAudioServiceLoader(PluginAudioServiceLoader.getInstance());
		Configuration.setShow(false);
		MongoDB.init();
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
		
		//release the mongodb driver
		MongoDB.cleanUp();
	}

}

