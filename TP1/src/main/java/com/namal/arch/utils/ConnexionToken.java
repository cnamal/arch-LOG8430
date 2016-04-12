package com.namal.arch.utils;

/**
 * Created by adrien on 16-04-08.
 */
public class ConnexionToken {

	/**
	 * Classe singleton, keep the reference to the instance
	 */
    private static ConnexionToken instance;

    
    private String token=null;

    /**
     * 
     * @return The instance of the singleton
     */
    static public ConnexionToken getInstance(){
        if(instance==null)
            instance = new ConnexionToken();
        return instance;
    }

    /**
     * 
     * @return The connexion token usefull for the server
     */
    public String getToken(){
        return token;
    }

    /**
     * Set the token at the beginning of the program
     * @param token the token
     */
    public void setToken(String token){
        this.token = token;
    }

}
