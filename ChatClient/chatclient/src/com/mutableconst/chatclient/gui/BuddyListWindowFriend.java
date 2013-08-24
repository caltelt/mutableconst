package com.mutableconst.chatclient.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JComponent;
import com.mutableconst.dashboard_manager.EventManager;
import com.mutableconst.models.Buddy;

public class BuddyListWindowFriend extends JComponent implements MouseListener {

	Buddy buddy;

	public BuddyListWindowFriend(Buddy buddy) {
		super();
		setMinimumSize(new Dimension(50, 35));
		setMaximumSize(new Dimension(50, 35));
		addMouseListener(this);
		this.buddy = buddy;
	}

	@Override
	public void paint(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		g.setColor(Color.white);
		g.drawString(buddy.getName(), 0, 25);
		g.drawString(buddy.getPhoneNumber(),95, 25);
		setSize(getParent().getWidth(), getHeight());
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			EventManager.getEventManager().launchTextWindow(buddy);
		}
	}

	public void mousePressed(MouseEvent e) {}

	public void mouseReleased(MouseEvent e) {}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}

}
