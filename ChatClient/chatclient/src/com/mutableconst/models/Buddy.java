package com.mutableconst.models;

public class Buddy {

	private String name, phoneNumber;
	
	public Buddy(String name, String phoneNumber) {
		this.name = name;
		this.phoneNumber = phoneNumber;
	}
	
	public String getName() {
		return name;
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	
}
