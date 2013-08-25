package com.mutableconst.chatclient.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.mutableconst.dashboard_manager.PrefKeys;
import com.mutableconst.dashboard_manager.Preferences;
import com.mutableconst.models.Buddy;

public class BuddyListWindow extends JFrame {

	private static BuddyListWindow reference;
	private JScrollPane scroll;
	private JTextField filterBox;
	private Box friendsArea, topArea;
	private Box windowBox;
	BuddyListWindowFriend[] friends;

	public static BuddyListWindow getBuddyListWindow() {
		if (reference == null) {
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
		if (Preferences.getPreference(PrefKeys.BuddyX) != null && Preferences.getPreference(PrefKeys.BuddyY) != null) {
			setLocation(new Integer(Preferences.getPreference(PrefKeys.BuddyX)), new Integer(Preferences.getPreference(PrefKeys.BuddyY)));
		} else {
			setLocation((int) (GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getWidth() - getWidth()), 0);
		}
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			// Ignore
		}
		filterBox = new JTextField();
		filterBox.setMinimumSize(new Dimension(getWidth(), 25));
		filterBox.setMaximumSize(new Dimension(getWidth(), 25));
		filterBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		filterBox.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				updateBuddyList();
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				updateBuddyList();
			}
			@Override
			public void changedUpdate(DocumentEvent e) {}
		});

		topArea = Box.createHorizontalBox();
		topArea.add(filterBox);

		friendsArea = Box.createVerticalBox();
		friendsArea.add(Box.createVerticalStrut(5));
		//friendsArea.setDoubleBuffered(true);

		scroll = new JScrollPane(friendsArea);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		windowBox = Box.createVerticalBox();
		windowBox.add(scroll, 0);
		windowBox.add(topArea, 0);

		add(windowBox);
		
		setVisible(true);

		friends = getCurrentBuddies();
		updateBuddyList();
	}

	private void updateBuddyList() {
		Dimension rigidAreaDim = new Dimension(0, 5);
		String filter = filterBox.getText().toLowerCase();
		friendsArea.removeAll();
		for (int i = 0; i < friends.length; i++) {
			if (friends[i].matchesFilter(filter) || filter.length() == 0) {
				friendsArea.add(friends[i]);
				friendsArea.add(Box.createRigidArea(rigidAreaDim));
			}
		}
		friendsArea.revalidate();
		friendsArea.repaint();
	}

	private BuddyListWindowFriend[] getCurrentBuddies() {
		BuddyListWindowFriend[] friends = new BuddyListWindowFriend[3];
		friends[0] = new BuddyListWindowFriend(new Buddy("Casey Foster", "262-994-0732"));
		friends[1] = new BuddyListWindowFriend(new Buddy("Nick Juszczak", "608-555-397-6053"));
		friends[2] = new BuddyListWindowFriend(new Buddy("Alex Hammer", "608-555-3333"));
		// for(int i = 0; i < friends.length; i++) {
		// friends[i] = new BuddyListWindowFriend();
		// }
		return friends;
	}

}
