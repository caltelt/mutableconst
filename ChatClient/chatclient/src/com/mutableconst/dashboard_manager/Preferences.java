package com.mutableconst.dashboard_manager;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import com.mutableconst.chatclient.gui.ContactWindow;

public class Preferences {

	private static final String CONFIG_FILE = "config.pref";

	private static Properties properties = new Properties();
	
	//Keys
	public static final String BUDDYX = "BUDDYX";
	public static final String BUDDYY = "BUDDYY";

	public static boolean loadPreferences() {
		try {
			properties.load(new FileInputStream(CONFIG_FILE));
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	public static boolean savePreferences() {
		setShutDownProperties();
		try {
			properties.store(new FileOutputStream(CONFIG_FILE), null);
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	private static void setShutDownProperties() {
		setPreference(BUDDYX, ContactWindow.getContactWindow().getX() + "");
		setPreference(BUDDYY, ContactWindow.getContactWindow().getY() + "");
	}

	public static String getPreference(String key) {
		return properties.getProperty(key);
	}

	public static void setPreference(String key, String value) {
		if (key != null && value != null) {
			properties.setProperty(key, value);
		}
	}

}
