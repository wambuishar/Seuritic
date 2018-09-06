package com.example.sha.security.db;

public class User {

	//private variables
	private int _id;
	private String name,phone,email,pass;

	// Empty constructor
	User() {}

	// constructor
	public User(String name, String phone, String email, String pass) {
		this.name = name;
		this.phone = phone;
		this.email = email;
		this.pass = pass;
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

	public String getPass() {
		return this.pass;
	}

	void setPass(String pass) {
		this.pass = pass;
	}
}