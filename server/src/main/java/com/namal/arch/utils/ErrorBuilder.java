package com.namal.arch.utils;

import javax.json.Json;
import javax.servlet.http.HttpServletResponse;


/**
 * Helper class for creating errors
 * @author Namal
 */
public class ErrorBuilder {

	private static void setCode(int code,HttpServletResponse response){
		switch(code){
			case 404:
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			case 400:
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}

    /**
     * Sets error code and return a Json
     * @param code error code
     * @param msg error message
     * @param response server response
     * @return Json
     */
	public static String error(int code, String msg,HttpServletResponse response){
		setCode(code,response);
		return Json.createObjectBuilder()
				.add("code", code)
				.add("message", msg).build().toString();
	}

    /**
     * Helper for unauthorized error
     * @param response server response
     * @return Json
     */
	public static String unauthorizedError(HttpServletResponse response){
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		return Json.createObjectBuilder()
				.add("code", 404)
				.add("message", "Unauthorized").build().toString();
	}
}
