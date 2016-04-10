package com.namal.arch.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import com.namal.arch.models.services.AudioServiceLoader;

/**
 * Connexion driver
 * @author namalgac
 *
 */
public class Connexion {
	
	private static String uriString;
	public static void init(){
		Properties prop = new Properties();
		String propFileName = "config.properties";

		InputStream inputStream = AudioServiceLoader.getInstance().getClass().getClassLoader().getResourceAsStream(propFileName);
		if(inputStream==null){
			System.err.println("config.properties wasn't found. Please create it and add necessary information using config.properties.dist .");
			System.exit(1);
		}
			
		try {
			
			prop.load(inputStream);
			inputStream.close();
			uriString = "http://";
			if(prop.getProperty("host")==null || prop.getProperty("port") ==null){
				System.err.println("You need to connect to a server. Please follow the tutorial in the report.");
				System.exit(1);
			}
			uriString+=prop.getProperty("host")+":"+prop.getProperty("port");
		} catch (IOException e) {
			e.printStackTrace();
			uriString =" ";
		}
	}
	
	/**
	 * Get the URI
	 * @return The URI
	 */
	public static String getURI(){
		if(uriString == null) {
			Connexion.init();
			return uriString;
		}
		else {
			return uriString;
		}
	}

}

