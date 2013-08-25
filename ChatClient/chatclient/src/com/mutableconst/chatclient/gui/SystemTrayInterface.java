package com.mutableconst.chatclient.gui;

import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;

import com.mutableconst.dashboard_manager.EventManager;

public class SystemTrayInterface {

	private static boolean setup;

	public static void startSystemTray() {
		if(setup) return; else setup = true;
		SystemTray systemTray;
		TrayIcon trayIcon;
		PopupMenu popupMenu;
		if (SystemTray.isSupported()) {
			try {
				MenuItem item;
				systemTray = SystemTray.getSystemTray();

				popupMenu = new PopupMenu();
				item = new MenuItem("Buddy List");
				item.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						EventManager.getEventManager().focusBuddyListWindow();
					}
				});

				popupMenu.add(item);
				item = new MenuItem("Exit");
				item.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						EventManager.getEventManager().exit();
					}
				});

				popupMenu.add(item);
				trayIcon = new TrayIcon(Toolkit.getDefaultToolkit().getImage("mc.png"), "MutableConst", popupMenu);
				trayIcon.setImageAutoSize(true);
				trayIcon.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						EventManager.getEventManager().focusBuddyListWindow();
					}
				});

				systemTray.add(trayIcon);

			} catch (AWTException e) {
				e.printStackTrace();
			}
		} else {
			//JOptionPane.showMessageDialog(null, "System Tray Icon is not supported on this Operating System.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}
