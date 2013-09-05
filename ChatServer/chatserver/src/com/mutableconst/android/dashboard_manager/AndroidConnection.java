package com.mutableconst.android.dashboard_manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.widget.Toast;

import com.mutableconst.chatserver.gui.MainActivity;
import com.mutableconst.protocol.Protocol;

public class AndroidConnection {
	
	private String PREFERENCE_KEY_PREF = "PREFERENCES";
	private String IP_ADDRESS_PREF = "IP_ADDRESS";
	
	private String serverAddress = "192.168.1.100";
	private final char NEW_LINE = '\n';
	private final int PING_COUNTER = 30;
	private final String PING_STRING = "";

	private final int PORT = 7767;
	private Socket socket;
	private int pingCounter = PING_COUNTER;
	private static ConcurrentLinkedQueue<String> requests;
	private static boolean notStarted = true;

	private BufferedReader in;
	private PrintWriter out;
	private final StringBuilder responseBuilder = new StringBuilder();

	private Activity mainContext;
	private PendingIntent pi;
	private final SmsManager sms;

	public static void startAndroidConnection(Activity mainContext) {
		SharedPreferences sharedPreferences = mainContext.getSharedPreferences(PREFERENCE_KEY_PREF, 0);
		serverAddress = sharedPreferences.getString(IP_ADDRESS_PREF, serverAddress);
		
		if (notStarted) {
			notStarted = false;
			new AndroidConnection(mainContext);
		}
	}

	private AndroidConnection(final Activity mainContext) {

		this.mainContext = mainContext;
		pi = PendingIntent.getActivity(mainContext, 0, new Intent(mainContext, MainActivity.class), 0);
		sms = SmsManager.getDefault();
		requests = new ConcurrentLinkedQueue<String>();

		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(500);
						if (socket == null) {
							//sendToast("Creating a New Socket");
							socket = new Socket(serverAddress, PORT);
							in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
							out = new PrintWriter(socket.getOutputStream(), true);
						}
						if (requests.isEmpty() == false) {
							sendToast("Outputing Data: " + requests.peek());
							out.println(requests.poll());
						}
						while (null != in && in.ready()) {
							char nextChar = (char) in.read();
							responseBuilder.append(nextChar);
							if (nextChar == NEW_LINE) {
								handleResponse(Protocol.getProtocol().decodeRawRequest(responseBuilder.toString()));
								sendToast("Incoming request: " + responseBuilder.toString());
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
					} catch (SocketException e) {
						//sendToast("Socket is null");
						System.out.println("1");
						socket = null;
						in = null;
						out = null;
					} catch (UnknownHostException e) {
						e.printStackTrace();
					} catch (IOException e) {
						sendToast(e.getMessage());
						e.printStackTrace();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	private void handleResponse(HashMap<String, String> decodedResponse) {
		if (decodedResponse != null) {
			if (decodedResponse.get(Protocol.TYPE).equals(Protocol.TEXT_MESSAGE_TYPE)) {
				sms.sendTextMessage(decodedResponse.get(Protocol.PHONE), null, decodedResponse.get(Protocol.MESSAGE), pi, null);
				sendToast("To: " + decodedResponse.get(Protocol.PHONE) + " " + "Message:" + decodedResponse.get(Protocol.MESSAGE));
			} else {
				sendToast(decodedResponse.get(Protocol.TYPE));
			}
		}
	}

	private void sendToast(final String message) {
		mainContext.runOnUiThread(new Runnable() {
			public void run() {
				Toast.makeText(mainContext, message, Toast.LENGTH_LONG).show();
			}
		});
	}

	public static boolean addRequest(String request) {
		if (requests != null) {
			requests.add(request);
			return true;
		} else {
			return false;
		}
	}
}
