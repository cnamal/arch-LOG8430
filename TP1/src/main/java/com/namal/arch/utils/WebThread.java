package com.namal.arch.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebThread implements Runnable {

	private URL url;
	private InputStream is;
	private WebListener wl;
	private HttpURLConnection http;
	
	public WebThread(URL url,WebListener wl){
		this.url=url;
		this.wl=wl;
	}
	
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
