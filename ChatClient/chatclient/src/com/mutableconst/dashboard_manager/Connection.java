package com.mutableconst.dashboard_manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.Date;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.swing.JOptionPane;

import com.mutableconst.protocol.Protocol;

public class Connection {

	private final char NEW_LINE = '\n';
	private final int PING_COUNTER = 60;
	private final String PING_STRING = "";

	private final int PORT = 7767;
	private Socket socket;
	private ServerSocket serverSocket;
	private int pingCounter = PING_COUNTER;
	private static ConcurrentLinkedQueue<String> requests;
	private static boolean notStarted = true;

	private PrintWriter out;
	private BufferedReader in;
	private final StringBuilder responseBuilder = new StringBuilder();

	public static void startConnection() {
		if (notStarted) {
			notStarted = false;
			new Connection();
		}
	}

	private Connection() {

		requests = new ConcurrentLinkedQueue<String>();

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					serverSocket = new ServerSocket(PORT);
					while (true) {
						try {
							if (socket == null) {
								System.out.println("Listening");
								socket = serverSocket.accept();
								in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
								out = new PrintWriter(socket.getOutputStream(), true);
								System.out.println("Connection Made!");
							}
							if (requests.isEmpty() == false) {
								System.out.println("Outputing Data: " + requests.peek());
								out.println(requests.poll());
							}
							while (in.ready()) {
								System.out.println("In is ready");
								char nextChar = (char) in.read();
								responseBuilder.append(nextChar);
								if (nextChar == NEW_LINE) {
									handleResponse(Protocol.getProtocol().decodeRawRequest(responseBuilder.toString()));
									System.out.println("Incoming request: " + responseBuilder.toString());
									responseBuilder.setLength(0);
								}
							}
							pingCounter--;
							if (pingCounter == 0) {
								pingCounter = PING_COUNTER;
								out.println(PING_STRING);
								if (out.checkError()) {
									throw new SocketException();
								}
							}
							Thread.sleep(250);
						} catch (SocketException e) {
							System.out.println("Socket is null");
							socket.close();
							socket = null;
							in = null;
							out = null;
						} catch (UnknownHostException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				} catch (IOException e) {
					System.out.println("Fatal error connecting to socket on port: " + PORT + "!");
				}
			}
		}).start();

	}

	private void handleResponse(HashMap<String, String> decodedResponse) {
		if (decodedResponse != null) {
			if (decodedResponse.get(Protocol.TYPE).equals(Protocol.TEXT_MESSAGE_TYPE)) {
				System.out.println("Handling Response");
				EventManager.getEventManager().recieveTextMessage(decodedResponse.get(Protocol.PHONE), decodedResponse.get(Protocol.MESSAGE));
			} else {
				System.out.println("Not a Text Mesage Type" + decodedResponse.toString());
			}
		}
	}

	public static boolean addRequest(String request) {
		if (requests != null) {
			requests.add(request);
			return true;
		} else {
			JOptionPane.showMessageDialog(null, "Not Connected to Phone. Please start connection from Phone.", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}

}
