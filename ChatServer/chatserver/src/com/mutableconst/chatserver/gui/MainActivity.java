package com.mutableconst.chatserver.gui;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.mutableconst.android.dashboard_manager.AndroidConnection;
import com.mutableconst.android.dashboard_manager.AndroidEventManager;
import com.mutableconst.protocol.ConnectionType;

public class MainActivity extends Activity {
	
	private SharedPreferences sharedPreferences;
	private String PREFERENCE_KEY_PREF = "PREFERENCES";
	private String IP_ADDRESS_PREF = "IP_ADDRESS";
	private ConnectionType connectionType;
	// Reference to ConnectionType Enum class name, used for getting
	// SharedPreferences connection type name (eg. "WIFI")
	private String connectionTypeName = ConnectionType.class.getSimpleName();
	// Reference to ConnectionType Enum class name + Id, used for
	// accessing sharedPreferences RadioButton id (eg. R.id.buttonId)
	private String referenceId = ConnectionType.class.getSimpleName() + "Id";
	
	private String ipAddress = "192.168.1.100";

	
	// Connection details
	// public static ConnectionType CONNECTION_TYPE = ConnectionType.ADB;
	// public static String LOCALHOST = "10.0.2.2";

	// TIMEOUT of connection, in seconds
	// public static int TIMEOUT = 10;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		AndroidEventManager.getAndroidEventManager().setupEnvironment(this);
		new AndroidConnection(this);
		
		// TODO: Runtime shutdown handler thread
		// TODO: Communication protocol
		
		sharedPreferences = getSharedPreferences(PREFERENCE_KEY_PREF, 0);
		connectionType = ConnectionType.valueOf(sharedPreferences.getString(connectionTypeName, ConnectionType.WIFI.name()));
		ipAddress = sharedPreferences.getString(IP_ADDRESS_PREF, "192.168.1.100");

		/**
		 * Get the radio group for selecting the connection type. 
		 * Check if a radio button has been selected, if so, select it.
		 */
		RadioGroup connectionSelection = (RadioGroup) findViewById(R.id.connectionSelection);
		int selectedRadioButtonId = sharedPreferences.getInt(referenceId, -1);
		if(selectedRadioButtonId != -1)
		{
			RadioButton checkedButton = (RadioButton) findViewById(selectedRadioButtonId);
			checkedButton.setChecked(true);
		}

		/**
		 * Create listener to update the pref when a new connection type is selected
		 */
		connectionSelection.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				SharedPreferences.Editor editor = sharedPreferences.edit();
				RadioButton selectedRadioButton = (RadioButton) findViewById(checkedId);
				
				String selectedOption = selectedRadioButton.getText().toString().toUpperCase();
				editor.putString(connectionTypeName, selectedOption);
				editor.putInt(referenceId, checkedId);
				editor.commit();
				
				connectionType = ConnectionType.valueOf(selectedOption);
			}
		});
		
		EditText ipAddressTextField = (EditText) findViewById(R.id.ipAddress);
		ipAddressTextField.setText(ipAddress);
		ipAddressTextField.addTextChangedListener(new TextWatcher() {
			/**
			 * Sets the shared preference, changes the ip address globally
			 */
			@Override
			public void afterTextChanged(Editable arg0) {
				EditText ipAddressTextField = (EditText) findViewById(R.id.ipAddress);
				String newIpAddress = ipAddressTextField.getText().toString();
				
				SharedPreferences.Editor editor = sharedPreferences.edit();
				editor.putString(IP_ADDRESS_PREF, newIpAddress);
				editor.commit();
				
				ipAddress = newIpAddress; 
			}
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			
		});

		// Quit button
		Button quitButton = (Button) findViewById(R.id.quitButton);
		quitButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				System.exit(0);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
