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

	Buddy buddy;

	public BuddyListWindowFriend(final Buddy buddy) {
		super();
		this.buddy = buddy;
		setMinimumSize(new Dimension(50, 35));
		setMaximumSize(new Dimension(50, 35));
		setPreferredSize(new Dimension(50, 35));
		setAlignmentX(Component.LEFT_ALIGNMENT);
		setDoubleBuffered(true);
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					EventManager.getEventManager().launchTextWindow(buddy);
				}
			}

		});
	}

	@Override
	public void paint(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		g.setColor(Color.white);
		g.drawString(buddy.getName(), 0, 25);
		g.drawString(buddy.getPhoneNumber(), 95, 25);
		setSize(getParent().getWidth(), getHeight());
	}
	
	public Buddy getBuddy() {
		return buddy;
	}
	
	public boolean matchesFilter(String filter) {
		String lowerCaseName = buddy.getName().toLowerCase();
		String lowerCasePhoneNumber = buddy.getPhoneNumber().toLowerCase();
		return lowerCaseName.contains(filter) || lowerCasePhoneNumber.contains(filter);
	}


}
