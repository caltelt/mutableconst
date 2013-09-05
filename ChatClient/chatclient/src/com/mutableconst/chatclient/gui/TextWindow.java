package com.mutableconst.chatclient.gui;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

import com.mutableconst.dashboard_manager.EventManager;
import com.mutableconst.models.Contact;

public class TextWindow extends JFrame {

	private static final String RECEIVER_PREFIX = "Them: ";
	private static final String SENDER_PREFIX = "Me: ";
	private static final String NEW_LINE = "\n";

	private JScrollPane conversationView, sendMessageView;
	private JTextArea conversationText, sendMessageText;
	private JScrollBar scrollBar;
	private JButton sendButton;
	private Box verticalBox, horizontalBox;
	private Contact contact;

	public TextWindow(final Contact contact) {
		super();
		this.contact = contact;
		setIconImage(Resources.MCIcon);
		if(contact.getName() != null && contact.getName().length() > 0) {
			setTitle(contact.getName()); 
		} else {
			setTitle(contact.getPhoneNumber());
		} 
		setSize(555, 390);
		setMinimumSize(new Dimension(555, 390));
		setLocationRelativeTo(null);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				EventManager.textWindowClose(contact.getPhoneNumber());
			}
		});
		setLayout(new BorderLayout());

		conversationText = new JTextArea();
		conversationText.setMinimumSize(new Dimension(495, 310));
		conversationText.setSize(555, 375);
		conversationText.setEditable(false);
		conversationText.setLineWrap(true);

		conversationView = new JScrollPane(conversationText);
		conversationView.setPreferredSize(new Dimension(495, 310));
		conversationView.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollBar = conversationView.getVerticalScrollBar();

		sendMessageText = new JTextArea();
		sendMessageText.setMinimumSize(new Dimension(450, 35));
		sendMessageText.setLineWrap(true);
		sendMessageText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getExtendedKeyCode() == KeyEvent.VK_ENTER) {
					e.consume();
					startSendMessage();
				}
			}
		});

		sendMessageView = new JScrollPane(sendMessageText);
		sendMessageView.setPreferredSize(new Dimension(450, 35));
		sendMessageView.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		sendButton = new JButton("Send");
		sendButton.setMaximumSize(new Dimension(65, 25));
		sendButton.setPreferredSize(new Dimension(65, 25));
		sendButton.setMinimumSize(new Dimension(65, 25));
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startSendMessage();
			}
		});

		verticalBox = Box.createVerticalBox();
		horizontalBox = Box.createHorizontalBox();

		horizontalBox.add(sendMessageView);
		horizontalBox.add(sendButton);
		verticalBox.add(conversationView);
		verticalBox.add(horizontalBox);

		add(verticalBox);
		
		setVisible(true);
	}

	private void startSendMessage() {
		String message = sendMessageText.getText().trim();
		if (message.length() > 0) {
			if (EventManager.sendTextMessage(contact.getPhoneNumber() , message)) {
				conversationText.append(SENDER_PREFIX + message + NEW_LINE);
				sendMessageText.setText(null);
				scrollBar.setValue(scrollBar.getMaximum());
			}
		}
	}

	public void receiveMessage(String message) {
		message = message.trim();
		conversationText.append(RECEIVER_PREFIX + message + NEW_LINE);
	}

}
