package com.mutableconst.dashboard_manager;

import com.mutableconst.chatclient.gui.ContactWindow;
import com.mutableconst.chatclient.gui.SystemTrayInterface;
import com.mutableconst.chatclient.gui.TextWindow;
import com.mutableconst.models.Contact;
import com.mutableconst.models.ContactManager;
import com.mutableconst.protocol.Protocol;

import java.util.HashMap;

import javax.swing.JFrame;

public class EventManager {

	private static HashMap<String, TextWindow> windows;

	public static void main(String[] args) {
		setupEnviornment();
	}

	private static void setupEnviornment() {
		Preferences.loadPreferences();
		ContactWindow.focusContactWindow();
		SystemTrayInterface.startSystemTray();
		Connection.startConnection();
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				Preferences.savePreferences();
			}
		});
		windows = new HashMap<String, TextWindow>();
	}

	public static void pingTextWindow(String phoneNumber, String message) {
		TextWindow window = windows.get(phoneNumber);
		if (window == null) {
			window = new TextWindow(ContactManager.getContact(phoneNumber));
			windows.put(phoneNumber, window);
		} else {
			window.toFront();
			window.setExtendedState(JFrame.NORMAL);
			window.repaint();
		}
		if (message != null && message.trim().length() > 0) {
			window.receiveMessage(message.trim());
		}
	}

	public static void textWindowClose(String phoneNumber) {
		if (windows.containsKey(phoneNumber)) {
			windows.remove(phoneNumber);
		}
	}

	public static void focusContactWindow() {
		ContactWindow.focusContactWindow();
	}

	public static boolean sendTextMessage(String phoneNumber, String message) {
		String jsonString = new Protocol(Protocol.TEXT_MESSAGE_HEADER, Protocol.PHONE, phoneNumber, Protocol.MESSAGE, message).getEncodedJSONString();
		System.out.println("Sending Text: " + jsonString);
		return Connection.addRequest(jsonString);
	}

	public static boolean recieveTextMessage(String phoneNumber, String message) {
		System.out.println("Recieving Text Message: " + " " + phoneNumber + " " + message);
		pingTextWindow(phoneNumber, message);
		return true;
	}

	public static void exit() {
		System.exit(0);
	}

}
