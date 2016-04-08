package com.namal.arch.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import com.namal.arch.Main;

/**
 * MongoDB driver
 * @author namalgac
 *
 */
public class MongoDB {
	
	private static MongoClient mongoClient;
	static MongoDatabase db;
	
	/**
	 * Initialize the driver
	 */
	public static void init(){
		Properties prop = new Properties();
		String propFileName = "config.properties";

		InputStream inputStream = Main.class.getClassLoader().getResourceAsStream(propFileName);
		if(inputStream==null){
			System.err.println("config.properties wasn't found. Please create it and add necessary information using config.properties.dist .");
			System.exit(1);
		}
			
		try {
			
			prop.load(inputStream);
			inputStream.close();
			String uriString = "mongodb://";
			if(prop.getProperty("dbuser")!=null){
				uriString+=prop.getProperty("dbuser")+":"+prop.getProperty("dbpwd")+"@";
			}
			if(prop.getProperty("address")==null|| prop.getProperty("port")==null||prop.getProperty("db")==null){
				System.err.println("For cross platform playlists to work, it needs a proper working mongodb database. Please follow the tutorial in the report.");
				System.exit(1);
			}else
				uriString+=prop.getProperty("address")+":"+prop.getProperty("port")+"/"+prop.getProperty("db");
			MongoClientURI uri = new MongoClientURI(uriString);
			mongoClient = new MongoClient(uri);
			db = mongoClient.getDatabase(uri.getDatabase());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @return true if the mongodb driver is connected
	 */
	public static boolean isConnected(){
		return mongoClient!=null;
	}
	
	/**
	 * Release the driver
	 */
	public static void cleanUp(){
		mongoClient.close();
		db=null;
	}
	
	/**
	 * 
	 * @return the database
	 */
	public static MongoDatabase getDatabase(){
		return db;
	}
}
