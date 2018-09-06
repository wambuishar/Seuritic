package com.example.sha.security.db;

public class Contact {

	//private variables
	private int _id;
	private String name,phone,email;

	// Empty constructor
	Contact() {}

	// constructor
	public Contact(String name, String phone, String email) {
		this.name = name;
		this.phone = phone;
		this.email = email;
	}

	public int getID() {
		return this._id;
	}

	void setID(int id) {
		this._id = id;
	}

	public String getName() {
		return name;
	}

	void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return this.email;
	}

	void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return this.phone;
	}

	void setPhone(String phone) {
		this.phone = phone;
	}
}