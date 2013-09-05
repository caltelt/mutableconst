package com.mutableconst.android.dashboard_manager;

import android.app.Activity;

import com.mutableconst.protocol.Protocol;

public class AndroidEventManager {

	private static AndroidEventManager reference;

	public static AndroidEventManager getAndroidEventManager() {
		if (reference == null) {
			reference = new AndroidEventManager();
		}
		return reference;
	}

	private AndroidEventManager() {

	}

	public boolean forwardTextToComputer(String phoneNumber, String message) {
		String jsonString = new Protocol(Protocol.TEXT_MESSAGE_HEADER, Protocol.PHONE, phoneNumber, Protocol.MESSAGE, message).getEncodedJSONString();
		return AndroidConnection.addRequest(jsonString);
	}

	public void setupEnvironment(Activity mainContext) {
		AndroidConnection.startAndroidConnection(mainContext);
	}

}
