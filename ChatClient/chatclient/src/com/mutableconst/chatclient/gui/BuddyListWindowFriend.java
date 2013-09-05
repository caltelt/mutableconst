package com.mutableconst.chatclient.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;

import com.mutableconst.dashboard_manager.EventManager;
import com.mutableconst.models.Buddy;

public class BuddyListWindowFriend extends JComponent {

	String phoneNumber;

	public BuddyListWindowFriend(final String phoneNumber) {
		super();
		this.phoneNumber = phoneNumber;
		setMinimumSize(new Dimension(50, 35));
		setMaximumSize(new Dimension(50, 35));
		setPreferredSize(new Dimension(50, 35));
		setAlignmentX(Component.LEFT_ALIGNMENT);
		setDoubleBuffered(true);
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					EventManager.getEventManager().pingTextWindow(phoneNumber, null);
				}
			}

		});
	}

	@Override
	public void paint(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		g.setColor(Color.white);
		//g.drawString(buddy.getName(), 0, 25);
		g.drawString(phoneNumber, 95, 25);
		setSize(getParent().getWidth(), getHeight());
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	
	public boolean matchesFilter(String filter) {
		//String lowerCaseName = buddy.getName().toLowerCase();
		String lowerCasePhoneNumber = phoneNumber.toLowerCase();
		return lowerCasePhoneNumber.contains(filter);
	}


}
