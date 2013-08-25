package com.mutableconst.chatserver.gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import android.os.Bundle;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.mutableconst.protocol.Protocol;

public class MainActivity extends Activity {
	// Connection details
	// public static ConnectionType CONNECTION_TYPE = ConnectionType.ADB;
	// public static String LOCALHOST = "10.0.2.2";

	// TIMEOUT of connection, in seconds
	// public static int TIMEOUT = 10;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button connectButton = (Button) findViewById(R.id.connectButton);
		connectButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//Toast.makeText(MainActivity.this, "Connecting", Toast.LENGTH_SHORT).show();
				createAdbSocketConnection();
			}
		});
		// TODO: SharedPreferences
		// TODO: Runtime shutdown handler thread
		// TODO: Communication protocol
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void createAdbSocketConnection() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String serverAddress = "192.168.1.132";
				Socket socket;
				try {
					socket = new Socket(serverAddress, 9090);
					BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					final SmsManager sms = SmsManager.getDefault();
					while (true) {
						final String rawResponse = in.readLine();
						final HashMap<String, String> decodedResponse = Protocol.getProtocol().decodeRawRequest(rawResponse);
						runOnUiThread(new Runnable() {
							public void run() {
								PendingIntent pi = PendingIntent.getActivity(MainActivity.this, 0, new Intent(MainActivity.this, MainActivity.class), 0);
								Toast.makeText(MainActivity.this, "To: " + decodedResponse.get(Protocol.PHONE) + " " +  "Message:" +  decodedResponse.get(Protocol.MESSAGE) , Toast.LENGTH_LONG).show();
						        sms.sendTextMessage(decodedResponse.get(Protocol.PHONE), null, decodedResponse.get(Protocol.MESSAGE), pi, null);      
							}
						});
					}
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}).start();
	}

}
