package com.namal.arch.models;

/**
 * Exception when a song doesn't have the right attributes
 * @author namalgac
 *
 */
public class SongMalformed extends Exception {

	
	private static final long serialVersionUID = -8253705466175853402L;

	public SongMalformed(String string) {
		super(string);
	}

}
