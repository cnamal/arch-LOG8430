package com.namal.arch.models.services;

import com.namal.arch.models.ProviderInformation;

public interface IAuthentification {

	public String getAuthentificationUrl();
	public String testString();
	public boolean serverResponse(String response);
	public ProviderInformation getProviderInformation();
	public boolean isConnected();
	public void disconnect();
}
