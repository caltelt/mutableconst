package com.mutableconst.dashboard_manager;

import com.mutableconst.chatclient.gui.BuddyListWindow;
import com.mutableconst.chatclient.gui.SystemTrayInterface;
import com.mutableconst.chatclient.gui.TextWindow;
import com.mutableconst.models.Buddy;
import com.mutableconst.protocol.Protocol;

import java.util.HashMap;

import javax.swing.JFrame;

public class EventManager {

	private static EventManager reference;
	private HashMap<String, TextWindow> windows;

	public static void main(String[] args) {
		reference = new EventManager();
	}

	private EventManager() {
		setupEnviornment();
	}
	
	private void setupEnviornment() {
		Preferences.loadPreferences();
		BuddyListWindow.focusBuddyListWindow();
		SystemTrayInterface.startSystemTray();
		Connection.startConnection();
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				Preferences.savePreferences();
			}
		});
		windows = new HashMap<String, TextWindow>();
	}

	public static EventManager getEventManager() {
		return reference;
	}

	public void pingTextWindow(String phoneNumber, String message) {
		TextWindow window = windows.get(phoneNumber);
		if (window == null) {
			window = new TextWindow(phoneNumber);
			windows.put(phoneNumber, window);
		} else {
			window.toFront();
			window.setExtendedState(JFrame.NORMAL);
			window.repaint();
		}
		if(message != null && message.trim().length() > 0) {
			window.receiveMessage(message.trim());
		}
	}

	public void textWindowClose(String phoneNumber) {
		if (windows.containsKey(phoneNumber)) {
			windows.remove(phoneNumber);
		}
	}

	public void exit() {
		System.exit(0);
	}

	public void focusBuddyListWindow() {
		BuddyListWindow.focusBuddyListWindow();
	}

	public boolean sendTextMessage(String phoneNumber, String message) {
		String jsonString = Protocol.getProtocol().encodeSendTextMessage(phoneNumber, message);
		System.out.println(jsonString);
		return Connection.addRequest(jsonString);
	}
	
	public boolean recieveTextMessage(String phoneNumber, String message) {
		System.out.println("Recieving Text Message: " + " " + phoneNumber + " " + message);
		pingTextWindow(phoneNumber, message);
		return true;
	} 

}
