package com.mutableconst.chatclient.gui;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

import com.mutableconst.dashboard_manager.EventManager;
import com.mutableconst.models.Buddy;

public class TextWindow extends JFrame implements KeyListener, WindowListener {

	private final String SENDER_PREFIX = "Me: ";
	private final String NEW_LINE = "\n";
	private JScrollPane conversationView, sendMessageView;
	private JTextArea conversationText, sendMessageText;
	private JButton sendButton;
	private Box verticalBox, horizontalBox;
	private Buddy buddy;

	public TextWindow(Buddy buddy) {
		super();
		this.buddy = buddy;
		setTitle(this.buddy.getName());
		setSize(555, 390);
		setMinimumSize(new Dimension(555, 390));
		setLocationRelativeTo(null);
		setVisible(true);
		addKeyListener(this);
		addWindowListener(this);
		setLayout(new BorderLayout());

		conversationText = new JTextArea();
		conversationText.setMinimumSize(new Dimension(495, 310));
		conversationText.setSize(555, 375);
		conversationText.setEditable(false);
		conversationText.setLineWrap(true);

		conversationView = new JScrollPane(conversationText);
		conversationView.setPreferredSize(new Dimension(495, 310));
		conversationView.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		sendMessageText = new JTextArea();
		sendMessageText.setMinimumSize(new Dimension(450, 35));
		sendMessageText.setLineWrap(true);
		sendMessageText.addKeyListener(this);

		sendMessageView = new JScrollPane(sendMessageText);
		sendMessageView.setPreferredSize(new Dimension(450, 35));
		sendMessageView.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		sendButton = new JButton("Send");
		sendButton.setMaximumSize(new Dimension(65, 25));
		sendButton.setMinimumSize(new Dimension(65, 25));
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startSendMessage();
			}
		});

		verticalBox = Box.createVerticalBox();
		horizontalBox = Box.createHorizontalBox();
		horizontalBox.add(sendMessageView);
		horizontalBox.add(Box.createHorizontalGlue());
		horizontalBox.add(sendButton);
		verticalBox.add(conversationView);
		verticalBox.add(horizontalBox);

		add(verticalBox);

	}

	private void startSendMessage() {
		String message = sendMessageText.getText();
		if (message.length() > 0) {
			conversationText.append(SENDER_PREFIX + message + NEW_LINE);
			sendMessageText.setText(null);
			EventManager.getEventManager().sendTextMessage(buddy, message);
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getExtendedKeyCode() == KeyEvent.VK_ENTER) {
			e.consume();
			startSendMessage();
		}
	}

	@Override
	public void windowClosing(WindowEvent e) {
		EventManager.getEventManager().textWindowClose(buddy);
	}

	public void windowClosed(WindowEvent e) {}

	public void keyTyped(KeyEvent e) {}

	public void keyReleased(KeyEvent e) {}

	public void windowOpened(WindowEvent e) {}

	public void windowIconified(WindowEvent e) {}

	public void windowDeiconified(WindowEvent e) {}

	public void windowActivated(WindowEvent e) {}

	public void windowDeactivated(WindowEvent e) {}
	
}
