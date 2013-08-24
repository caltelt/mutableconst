package com.mutableconst.dashboard_manager;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Date;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.swing.JOptionPane;

public class Connection {

	private ServerSocket serverSocket;
	private ConcurrentLinkedQueue<String> requests;

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
							PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
							while (true) {
								if (!requests.isEmpty()) {
									out.println(requests.poll());
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
					System.out.println("Could not listen on port: 4444");
					System.exit(-1);
				}
			}

		}).start();
	}

	public void addRequest(String request) {
		if (requests != null) {
			requests.add(request);
		} else {
			JOptionPane.showMessageDialog(null, "Not Connected to Phone. Please start connection from Phone.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

}
