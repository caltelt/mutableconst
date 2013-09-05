package com.mutableconst.dashboard_manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Date;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.swing.JOptionPane;

import com.mutableconst.protocol.Protocol;

public class Connection {

	private ServerSocket serverSocket;
	private ConcurrentLinkedQueue<String> requests;
	private PrintWriter out;
	private BufferedReader in;
	private final StringBuilder responseBuilder = new StringBuilder();
	private final char NEW_LINE = '\n';
	//private InputStream in;

	public Connection() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					serverSocket = new ServerSocket(9090);
					try {
						while (true) {
							System.out.println("Listening");
							Socket socket = serverSocket.accept();
							System.out.println("Connection Made!");
							requests = new ConcurrentLinkedQueue<String>();
							out = new PrintWriter(socket.getOutputStream(), true);
							in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
							//in = socket.getInputStream();
							while (socket.isClosed() == false) {
								if (requests.isEmpty() == false) {
									out.println(requests.poll());
								}
								while(in.ready()) {
									System.out.println("In is ready");
									char nextChar = (char) in.read();
									responseBuilder.append(nextChar);
									if(nextChar == NEW_LINE) {
										handleResponse(Protocol.getProtocol().decodeRawRequest(responseBuilder.toString()));
										System.out.println(responseBuilder.toString());
										responseBuilder.setLength(0);
									}
								}
								try {
									Thread.sleep(200);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						}
					} finally {
						serverSocket.close();
					}

				} catch (IOException e) {
					System.out.println("Could not listen on port: 9090");
					System.exit(-1);
				}
			}

		}).start();
	}

	private void handleResponse(HashMap<String, String> decodedResponse) {
		if (decodedResponse != null) {
			if (decodedResponse.get(Protocol.TYPE).equals(Protocol.TEXT_MESSAGE_TYPE)) {
				System.out.println("Handling Response");
				EventManager.getEventManager().recieveTextMessage("Doesn't matter", decodedResponse.get(Protocol.PHONE), decodedResponse.get(Protocol.MESSAGE));
			} else {
				System.out.println("Not a Text Mesage Type" + decodedResponse.toString());
			}
		}
	}

	public boolean addRequest(String request) {
		if (requests != null) {
			requests.add(request);
			return true;
		} else {
			JOptionPane.showMessageDialog(null, "Not Connected to Phone. Please start connection from Phone.", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}

}
