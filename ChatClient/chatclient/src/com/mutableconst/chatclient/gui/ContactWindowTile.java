package com.mutableconst.chatclient.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.mutableconst.dashboard_manager.EventManager;
import com.mutableconst.models.Contact;

public class ContactWindowTile extends JPanel {

	Contact contact;

	public ContactWindowTile(final Contact contact) {
		super();
		this.contact = contact;
		
		//setLayout(new GridBagLayout());
		
		setMaximumSize(new Dimension(500,50));
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JLabel name = new JLabel(contact.getName());
		
		JLabel phoneNumber = new JLabel(contact.getPhoneNumber());
		
		setBackground(Color.red);

		add(name);
		add(phoneNumber);
		
//		setMaximumSize(new Dimension(250,50));
		
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					EventManager.pingTextWindow(contact.getPhoneNumber(), null);
				}
			}
		});
	}
	
	public Contact getContact() {
		return contact;
	}
	
	public boolean matchesFilter(String filter) {
		String lowerCaseName = contact.getName().toLowerCase();
		String lowerCasePhoneNumber = contact.getPhoneNumber().toLowerCase();
		return lowerCasePhoneNumber.contains(filter) || lowerCaseName.contains(filter);
	}


}
