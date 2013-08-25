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
	private HashMap<Buddy, TextWindow> windows;
	private Connection connection;

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
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				Preferences.savePreferences();
			}
		});
		windows = new HashMap<Buddy, TextWindow>();
		connection = new Connection();
	}

	public static EventManager getEventManager() {
		return reference;
	}

	public void launchTextWindow(Buddy buddy) {
		TextWindow window = windows.get(buddy);
		if (window == null) {
			windows.put(buddy, new TextWindow(buddy));
		} else {
			window.toFront();
			window.setExtendedState(JFrame.NORMAL);
			window.repaint();
		}
	}

	public void textWindowClose(Buddy buddyObject) {
		if (windows.containsKey(buddyObject)) {
			windows.remove(buddyObject);
		}
	}

	public void exit() {
		System.exit(0);
	}

	public void focusBuddyListWindow() {
		BuddyListWindow.focusBuddyListWindow();
	}

	public boolean sendTextMessage(Buddy buddy, String message) {
		String jsonString = Protocol.getProtocol().encodeSendTextMessage(buddy.getPhoneNumber(), message);
		System.out.println(jsonString);
		return connection.addRequest(jsonString);
	}
	
	public boolean recieveTextMessage(String name, String phoneNumber, String message) {
		System.out.println("Recieving Text Message: " + name + " " + " " + phoneNumber + " " + message);
		Buddy buddy = new Buddy("Testing", phoneNumber);
		launchTextWindow(buddy);
		return true;
	} 

}
