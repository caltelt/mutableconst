package com.mutableconst.protocol;

import org.json.JSONObject;

/**
 * Singleton class for encoding requests/responses
 * to and from JSON 
 * 
 * Basic commands go <COMMAND>:<DATA>
 * For example, {"MESSAGE":{"1-800-999-9999":"text message :)"}}
 */
public class Protocol 
{
	private static Protocol protocol = null;
	
	private final String MESSAGE = "MESSAGE";
	private final String CONTACTS = "CONTACTS";
	
	private Protocol()
	{
	}
	
	public static Protocol getProtocol()
	{
		if(protocol == null) {
			protocol = new Protocol();
		}
		
		return protocol;
	}
	
	public String sendTextMessage(String number, String message)
	{
		return new JSONObject().put(MESSAGE, 
				new JSONObject().put(number, message).toString()).toString();
	}
	
	public String requestContacts(String request)
	{
		return new JSONObject().put(CONTACTS, request).toString();
	}
}
