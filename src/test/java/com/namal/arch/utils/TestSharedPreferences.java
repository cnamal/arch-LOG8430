package com.namal.arch.utils;

import static org.junit.Assert.assertTrue;

import java.util.UUID;
import java.util.prefs.Preferences;

import org.junit.Test;

public class TestSharedPreferences {

	String key = UUID.randomUUID().toString();
	String value = UUID.randomUUID().toString();
	
	@Test
	public void test() {
		Preferences prefs= SharedPreferences.getPreferences();
		prefs.put(key, value);
		assertTrue(prefs.get(key, "1").equals(value));
		prefs.remove(key);
		assertTrue(prefs.get(key, "1").equals("1"));
	}

}
