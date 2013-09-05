package com.mutableconst.models;

import java.util.HashMap;

/*
 * A class to store and allow access to all Contacts in the system
 */
public class ContactManager {

	private static HashMap<String, Contact> contacts = new HashMap<String, Contact>();
	
	public static Contact addContact(Contact contact) {
		contacts.put(contact.getPhoneNumber(), contact);
		return contact; //TODO probably get rid of this later
	} 
	
	public static Contact getContact(String phoneNumber) {
		return contacts.get(phoneNumber);
	}

}
