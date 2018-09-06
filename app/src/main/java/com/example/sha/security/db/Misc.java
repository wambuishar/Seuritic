package com.example.sha.security.db;

public class Misc {

	//private variables
	private int _id;
	private String entity,value;

	// Empty constructor
	Misc() {}

	// constructor
	public Misc(String entity, String value) {
		this.entity = entity;
		this.value = value;
	}

	public int getID() {
		return this._id;
	}

	void setID(int id) {
		this._id = id;
	}

	public String getEntity() {
		return entity;
	}

	void setEntity(String entity) {
		this.entity = entity;
	}

	public String getValue() {
		return this.value;
	}

	void setValue(String value) {
		this.value = value;
	}
}