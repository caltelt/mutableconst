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
							while (socket.isConnected()) {
								if (requests.isEmpty() == false) {
									out.println(requests.poll());
								}
//								if(in.ready()) {
//									handleResponse(Protocol.getProtocol().decodeRawRequest(in.readLine()));
//								}
								while(in.ready()) {
									System.out.println(in.read());
								}
								System.out.println("Falling out of loop");
								if(in.ready()) {
									
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
			if (decodedResponse.get(Protocol.TYPE) == Protocol.TEXT_MESSAGE_TYPE) {
				EventManager.getEventManager().recieveTextMessage("Doesn't matter", decodedResponse.get(Protocol.PHONE), decodedResponse.get(Protocol.MESSAGE));
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
