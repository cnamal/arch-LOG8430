package com.namal.arch.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

/**
 * MongoDB driver
 * @author namalgac
 *
 */
public class Connexion {
	
	/**
	 * Get the URI
	 * @return The URI
	 */
	public static String getURI(){
		Properties prop = new Properties();
		String propFileName = "config.properties";

		InputStream inputStream = Configuration.getAudioServiceLoader().getClass().getClassLoader().getResourceAsStream(propFileName);
		if(inputStream==null){
			System.err.println("config.properties wasn't found. Please create it and add necessary information using config.properties.dist .");
			System.exit(1);
		}
			
		try {
			
			prop.load(inputStream);
			inputStream.close();
			String uriString = "http://";
			if(prop.getProperty("host")!=null){
				uriString+=prop.getProperty("host")+":"+prop.getProperty("port");
			}
			return uriString;
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}

}
