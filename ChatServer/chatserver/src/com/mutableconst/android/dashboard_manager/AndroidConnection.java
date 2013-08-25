package com.mutableconst.android.dashboard_manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.widget.Toast;

import com.mutableconst.chatserver.gui.MainActivity;
import com.mutableconst.protocol.Protocol;

public class AndroidConnection {

	private String serverAddress = "192.168.1.132";
	private Socket socket;
	private PendingIntent pi;
	private BufferedReader in;
	private Activity mainContext;
	private final SmsManager sms;

	public AndroidConnection(final Activity mainContext) {
		pi = PendingIntent.getActivity(mainContext, 0, new Intent(mainContext, MainActivity.class), 0);
		sms = SmsManager.getDefault();
		this.mainContext = mainContext;
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						if (socket == null || socket.isConnected() == false) {
							socket = new Socket(serverAddress, 9090);
							sendToast("To TOAST MAN");
							in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
						}
						handleResponse(Protocol.getProtocol().decodeRawRequest(in.readLine()));
					} catch (IOException e) {

					}
				}
			}

		}).start();
	}

	private void handleResponse(HashMap<String, String> decodedResponse) {
		if (decodedResponse.get(Protocol.TYPE) == Protocol.TEXT_MESSAGE_TYPE) {
			sms.sendTextMessage(decodedResponse.get(Protocol.PHONE), null, decodedResponse.get(Protocol.MESSAGE), pi, null);
			sendToast("To: " + decodedResponse.get(Protocol.PHONE) + " " + "Message:" + decodedResponse.get(Protocol.MESSAGE));
		}
	}

	private void sendToast(final String message) {
		mainContext.runOnUiThread(new Runnable() {
			public void run() {
				Toast.makeText(mainContext, message, Toast.LENGTH_LONG).show();
			}
		});
	}
}
