package com.namal.arch.utils;

import javax.json.Json;
import javax.servlet.http.HttpServletResponse;

public class ErrorBuilder {

	private static void setCode(int code,HttpServletResponse response){
		switch(code){
			case 404:
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			case 400:
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}
	
	public static String error(int code, String msg,HttpServletResponse response){
		setCode(code,response);
		return Json.createObjectBuilder()
				.add("c", code)
				.add("message", msg).build().toString();
	}
	
	public static String unauthorizedError(HttpServletResponse response){
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		return Json.createObjectBuilder()
				.add("c", 404)
				.add("message", "Unauthorized").build().toString();
	}
}
