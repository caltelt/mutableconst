package com.mutableconst.chatclient.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.mutableconst.models.Buddy;

public class BuddyListWindow extends JFrame {

	private static BuddyListWindow reference;
	private JScrollPane scroll;
	private JPanel friendsArea;
	private Box box;
	
	public static BuddyListWindow getBuddyListWindow() {
		if(reference == null) {
			reference = new BuddyListWindow();
		} 
		return reference;
	}
	
	public static void focusBuddyListWindow() {
		getBuddyListWindow().toFront();
		getBuddyListWindow().setExtendedState(JFrame.NORMAL);
	}
	
	private BuddyListWindow() {
		super("MutableConst");
		setSize(225, 750);
		setMinimumSize(new Dimension(225, 750));
		setLocation((int) (GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getWidth() - getWidth()), 0);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			//Ignore
		}
		friendsArea = new JPanel();
		friendsArea.setBackground(Color.red);
		friendsArea.setBounds(0, 0, getWidth(), getHeight());
		friendsArea.setMaximumSize(friendsArea.getPreferredSize());
		box = Box.createVerticalBox();
		scroll = new JScrollPane(box);
		scroll.setBounds(0, 0, 100, 150);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		add(scroll);
		updateBuddyList();
		setVisible(true);
	}

	private void updateBuddyList() {
		BuddyListWindowFriend[] friends = getCurrentBuddies();
		for (int i = 0; i < friends.length; i++) {
			friends[i].setAlignmentX(Component.LEFT_ALIGNMENT);
			box.add(friends[i]);
			box.add(Box.createRigidArea(new Dimension(0, 5)));
		}
		Dimension j = new Dimension();
		repaint();
	}

	private BuddyListWindowFriend[] getCurrentBuddies() {
		BuddyListWindowFriend[] friends = new BuddyListWindowFriend[2];
		friends[0] = new BuddyListWindowFriend(new Buddy("Casey Foster", "262-994-0732"));
		friends[1] = new BuddyListWindowFriend(new Buddy("Nick Juszczak", "608-397-6053"));
		// for(int i = 0; i < friends.length; i++) {
		// friends[i] = new BuddyListWindowFriend();
		// }
		return friends;
	}

}
