package com.namal.arch.utils;

/**
 * Service listener
 * @author namalgac
 *
 * @param <Result> type of results expected 
 */
public interface ServiceListener<Result> {

	/**
	 * Callback function
	 * @param result results 
	 */
	public void done(Result result);
}
