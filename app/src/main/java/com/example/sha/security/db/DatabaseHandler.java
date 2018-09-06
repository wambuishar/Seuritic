package com.example.sha.security.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "securitik_local";

	// Tables
	private static final String TABLE_CONTACTS = "contacts";
	private static final String TABLE_MISC = "misc";
	private static final String TABLE_USERS = "users";

	// Contacts Table Columns
	private static final String KEY_CON_ID = "_id"; // auto increment integer
	private static final String KEY_CON_NAME = "name";
	private static final String KEY_CON_PHONE = "phone";
	private static final String KEY_CON_EMAIL = "email";

	// Contact Table Columns
	private static final String KEY_MI_ID = "_id"; //auto increment integer
	private static final String KEY_MI_ENTITY = "entity";
	private static final String KEY_MI_VALUE = "value";

	// Contact Table Columns
	private static final String KEY_US_ID = "_id"; //auto increment integer
	private static final String KEY_US_NAME = "usName";
	private static final String KEY_US_PHONE = "usPhone";
	private static final String KEY_US_EMAIL = "usEmail";
	private static final String KEY_US_PASSWORD = "usPassword";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}


	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {

		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
				+ KEY_CON_ID + " INTEGER PRIMARY KEY,"
				+ KEY_CON_NAME + " TEXT,"
				+ KEY_CON_PHONE + " TEXT,"
				+ KEY_CON_EMAIL + " TEXT" + ")";

		String CREATE_MISC_TABLE = "CREATE TABLE " + TABLE_MISC + "("
				+ KEY_MI_ID + " INTEGER PRIMARY KEY,"
				+ KEY_MI_ENTITY + " TEXT,"
				+ KEY_MI_VALUE + " TEXT" + ")";

		String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
				+ KEY_US_ID + " INTEGER PRIMARY KEY,"
				+ KEY_US_NAME + " TEXT,"
				+ KEY_US_PHONE + " TEXT,"
				+ KEY_US_EMAIL + " TEXT,"
				+ KEY_US_PASSWORD + " TEXT" + ")";

		db.execSQL(CREATE_CONTACTS_TABLE);
		db.execSQL(CREATE_MISC_TABLE);
		db.execSQL(CREATE_USERS_TABLE);
	}


	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MISC);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);

		// Create tables again
		onCreate(db);
	}

	// Adding new contact
	public void addContact(Contact con) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_CON_NAME, con.getName());
		values.put(KEY_CON_PHONE, con.getPhone());
		values.put(KEY_CON_EMAIL, con.getEmail());

		// Inserting Row
		db.insert(TABLE_CONTACTS, null, values);
		db.close(); // Closing database connection
	}

	// Adding new misc
	public void addMisc(Misc misc) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_MI_ENTITY, misc.getEntity());
		values.put(KEY_MI_VALUE, misc.getValue());

		// Inserting Row
		db.insert(TABLE_MISC, null, values);
		db.close(); // Closing database connection
	}

	// Adding new user ---> register
	public void addUser(User user) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_US_NAME, user.getName());
		values.put(KEY_US_PHONE, user.getPhone());
		values.put(KEY_US_EMAIL, user.getEmail());
		values.put(KEY_US_PASSWORD, user.getPass());

		// Inserting Row
		db.insert(TABLE_USERS, null, values);
		db.close(); // Closing database connection
	}


    // Getting all contacts
    public List<Contact> getAllContacts() {
        List<Contact> conList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_CONTACTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Contact con = new Contact();
                con.setID(Integer.parseInt(cursor.getString(0)));
                con.setName(cursor.getString(1));
                con.setPhone(cursor.getString(2));
                con.setEmail(cursor.getString(3));
                // Adding area details to list
                conList.add(con);
            } while (cursor.moveToNext());
        }

        cursor.close();

        // return areas list
        return conList;
    }

	// Getting all contacts name
	public List<String> getAllContactsName() {
		List<String> conNameList = new ArrayList<>();
		// Select All Query
		String selectQuery = "SELECT "+ KEY_CON_NAME +" FROM " + TABLE_CONTACTS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Contact con = new Contact();
				con.setName(cursor.getString(0));
				// Adding area details to list
				conNameList.add(con.getName());
			} while (cursor.moveToNext());
		}

		cursor.close();

		// return areas list
		return conNameList;
	}

	// Getting all contacts phone
	public List<String> getAllContactsPhone() {
		List<String> conPhoneList = new ArrayList<>();
		// Select All Query
		String selectQuery = "SELECT "+ KEY_CON_PHONE +" FROM " + TABLE_CONTACTS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Contact con = new Contact();
				con.setPhone(cursor.getString(0));
				// Adding area details to list
				conPhoneList.add(con.getPhone());
			} while (cursor.moveToNext());
		}

		cursor.close();

		// return areas list
		return conPhoneList;
	}

	// Getting all contacts
	public List<User> getAllUsers() {
		List<User> userList = new ArrayList<>();
		// Select All Query
		String selectQuery = "SELECT * FROM " + TABLE_USERS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				User con = new User();
				con.setID(Integer.parseInt(cursor.getString(0)));
				con.setName(cursor.getString(1));
				con.setPhone(cursor.getString(2));
				con.setEmail(cursor.getString(3));
				// Adding area details to list
				userList.add(con);
			} while (cursor.moveToNext());
		}

		cursor.close();

		// return areas list
		return userList;
	}


	// Updating single contact
	public void updateContact(String originalEmail, Contact con) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_CON_NAME, con.getName());
		values.put(KEY_CON_PHONE, con.getPhone());
		values.put(KEY_CON_EMAIL, con.getEmail());

		// updating row
		db.update(TABLE_CONTACTS, values, KEY_CON_EMAIL + " = ?",
				new String[] { originalEmail });
	}

	// Updating single misc
	public void updateMisc(Misc misc) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_MI_ENTITY, misc.getEntity()); // dt entity
		values.put(KEY_MI_VALUE, misc.getValue()); // value

		// updating row
		db.update(TABLE_MISC, values, KEY_MI_ENTITY + " = ?",
				new String[] { String.valueOf(misc.getEntity()) });
	}

	// Updating single user
	public void updateUser(User user) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_US_NAME, user.getName());
		values.put(KEY_US_PHONE, user.getPhone());
		values.put(KEY_US_EMAIL, user.getEmail());
		values.put(KEY_US_PASSWORD, user.getPass());

		// updating row
		db.update(TABLE_USERS, values, KEY_US_ID + " = ?",
				new String[] { String.valueOf(user.getID()) });
	}


	//getting misc
	public String getMisc(String field) {
		String selectQuery = "SELECT "+ KEY_MI_VALUE +" FROM "+TABLE_MISC+" WHERE "+KEY_MI_ENTITY+" = '"+ field+"'";
		SQLiteDatabase db = this.getWritableDatabase();

		Cursor cursor = db.rawQuery(selectQuery, null);

		String result = "";

		if (cursor.moveToFirst()) {
			if (cursor.getString(0) != null) result = cursor.getString(0);
		}

		cursor.close();
		db.close();

		return result;
	}

	//getting user name
	public String getUserName(String email) {
		String selectQuery = "SELECT "+ KEY_US_NAME +" FROM "+TABLE_USERS+" WHERE "+KEY_US_EMAIL+" = '"+ email+"'";
		SQLiteDatabase db = this.getWritableDatabase();

		Cursor cursor = db.rawQuery(selectQuery, null);

		String result = "";

		if (cursor.moveToFirst()) {
			if (cursor.getString(0) != null) result = cursor.getString(0);
		}

		cursor.close();
		db.close();

		return result;
	}

	//getting user phone
	public String getUserPhone(String email) {
		String selectQuery = "SELECT "+ KEY_US_PHONE +" FROM "+TABLE_USERS+" WHERE "+KEY_US_EMAIL+" = '"+ email+"'";
		SQLiteDatabase db = this.getWritableDatabase();

		Cursor cursor = db.rawQuery(selectQuery, null);

		String result = "";

		if (cursor.moveToFirst()) {
			if (cursor.getString(0) != null) result = cursor.getString(0);
		}

		cursor.close();
		db.close();

		return result;
	}


	// Deleting all contacts
	public void deleteContacts() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_CONTACTS, null,
				new String[] {});
		db.close();
	}

	// Deleting all misc
	public void deleteAllMisc() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_MISC, null,
				new String[] {});
		db.close();
	}


	// Deleting single contact
	public void deleteContact(String phone) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_CONTACTS, KEY_CON_PHONE + " = ?",
				new String[] { phone });
		db.close();
	}

	// Deleting single misc
	public void deleteMisc(int mi_id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_MISC, KEY_MI_ID + " = ?",
				new String[] { String.valueOf(mi_id) });
		db.close();
	}


	// Getting count
	public int getContactsCount() {
		String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int count = cursor.getCount();
		cursor.close();

		// return count
		return count;
	}

	// Getting count
	public int getUsersCount() {
		String countQuery = "SELECT * FROM " + TABLE_USERS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int count = cursor.getCount();
		cursor.close();

		// return count
		return count;
	}

	public int getMiscCount() {
		String countQuery = "SELECT  * FROM " + TABLE_MISC;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int count = cursor.getCount();
		cursor.close();

		// return count
		return count;
	}

	public boolean confirmLogin(String email, String password) {
		String selectQuery = "SELECT "+ KEY_US_PASSWORD +" FROM "+TABLE_USERS+" WHERE "+KEY_US_EMAIL+" = '"+ email+"'";
		SQLiteDatabase db = this.getWritableDatabase();

		Cursor cursor = db.rawQuery(selectQuery, null);

		String result = "";

		if (cursor.moveToFirst()) {
			if (cursor.getString(0) != null) result = cursor.getString(0);
		}

		cursor.close();
		db.close();

		return result.equals(password);
	}

}
