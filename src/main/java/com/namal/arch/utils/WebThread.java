package com.namal.arch.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class WebThread implements Runnable {

	private URL url;
	private InputStream is;
	private WebListener wl;
	
	public WebThread(URL url,WebListener wl){
		this.url=url;
		this.wl=wl;
	}
	
	@Override
	public void run() {
		try {
			this.is = url.openStream();
			wl.done(is);
		} catch (IOException e) {
			e.printStackTrace();
			wl.done(null);
		}
	}
	
	

}
