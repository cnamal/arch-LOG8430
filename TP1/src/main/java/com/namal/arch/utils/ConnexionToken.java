package com.namal.arch.utils;

/**
 * Created by adrien on 16-04-08.
 */
public class ConnexionToken {

    private static ConnexionToken instance;

    private String token=null;

    static public ConnexionToken getInstance(){
        if(instance==null)
            instance = new ConnexionToken();
        return instance;
    }

    public String getToken(){
        return token;
    }

    public void setToken(String token){
        this.token = token;
    }

}
