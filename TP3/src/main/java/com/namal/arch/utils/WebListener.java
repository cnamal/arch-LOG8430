package com.namal.arch.utils;

import java.io.InputStream;

/**
 * Web Listener
 * @author namalgac
 *
 */
public interface WebListener {

	/**
	 * 
	 * @param is input stream with result of web query
	 */
	public void done(InputStream is);
}
