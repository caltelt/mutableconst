package com.mutableconst.android.dashboard_manager;

import android.app.Activity;

import com.mutableconst.protocol.Protocol;

public class AndroidEventManager {

	private static AndroidEventManager reference;
	private AndroidConnection connection;
	
	public static AndroidEventManager getAndroidEventManager() {
		if(reference == null) {
			reference = new AndroidEventManager();
		}
		return reference;
	}
	
	private AndroidEventManager() {
		
	}
	
	public boolean forwardTextToComputer(String phoneNumber, String message) {
		String jsonString = Protocol.getProtocol().encodeSendTextMessage(phoneNumber, message);
		return connection.addRequest(jsonString);
	}

	public void setupEnvironment(Activity mainContext) {
		connection = new AndroidConnection(mainContext);
	}
	
	
	
	
	
}
