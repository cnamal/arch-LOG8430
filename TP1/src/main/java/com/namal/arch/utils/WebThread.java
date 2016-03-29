package com.namal.arch.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Web thread
 * @author namalgac
 *
 */
public class WebThread implements Runnable {

	private URL url;
	private InputStream is;
	private WebListener wl;
	private HttpURLConnection http;
	
	/**
	 * 
	 * @param url url of the query
	 * @param wl callback function
	 */
	public WebThread(URL url,WebListener wl){
		this.url=url;
		this.wl=wl;
	}
	
	/**
	 * 
	 * @param httpCon http connection of the query
	 * @param wl callback function
	 */
	public WebThread(HttpURLConnection httpCon,WebListener wl) {
		http=httpCon;
		this.wl = wl;
	}
	
	@Override
	public void run() {
		try {
			if(url!=null)
				this.is = url.openStream();
			else
				this.is = http.getInputStream();
			wl.done(is);
		} catch (IOException e) {
			e.printStackTrace();
			wl.done(null);
		}
	}

}
