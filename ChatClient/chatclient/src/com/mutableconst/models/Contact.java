package com.mutableconst.models;

public class Contact {

	private String name, phoneNumber;

	// MetaData
	// lastTimeContacted, numberOfTexts

	public Contact(String phoneNumber) {
		//Check valid phone number
		this.phoneNumber = phoneNumber;
		this.name = "";
	}

	public Contact(String phoneNumber, String name) {
		this.phoneNumber = phoneNumber;
		this.name = name;
	}

	public void setName(String name) {
		name = name.trim();
		if (name.length() > 0) {
			this.name = name;
		}
	}

	public String getName() {
		return name;
	}

	public void setPhoneNumber(String phoneNumber) {
		//Check valid phone number
		this.phoneNumber = phoneNumber;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

}
