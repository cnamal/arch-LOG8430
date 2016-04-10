package com.namal.arch.utils;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;

/**
 * APIHelper class
 *
 * @author Namal
 *         Created on 4/9/16.
 */
public class APIHelper {

    public static String dataResponse(JsonArrayBuilder arrayBuilder){
        String res =Json.createObjectBuilder()
                .add(Constants.DATA,arrayBuilder)
                .build()
                .toString();
        System.out.println("Sending : " +res);
        return res;
    }

    public static String dataResponse(JsonObjectBuilder objectBuilder){
        String res =Json.createObjectBuilder()
                .add(Constants.DATA,objectBuilder)
                .build()
                .toString();
        System.out.println("Sending : " +res);
        return res;
    }
}
