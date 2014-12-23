package de.davidartmann.artmannwiki.android.database;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import de.davidartmann.artmannwiki.android.model.Email;

/**
 * {@link Email} model helper class
 */
public class EmailManager {
	
	private DBManager dbManager;
	private SQLiteDatabase db;
	
	private static final String TABLE_EMAIL = "email";
	private static final String COLUMN_EMAILADDRESS = "emailaddress";
	private static final String COLUMN_PASSWORD = "password";

	private static final String CREATE_TABLE_EMAIL = "create table "
		      + TABLE_EMAIL + "(" 
		      + DBManager.COLUMN_ID + " integer primary key autoincrement,"
		      + DBManager.COLUMN_ACTIVE + " integer not null,"
		      + DBManager.COLUMN_CREATETIME + " datetime not null,"
		      + DBManager.COLUMN_LASTUPDATE + " datetime,"
		      + COLUMN_EMAILADDRESS + " text not null,"
		      + COLUMN_PASSWORD + " text not null"
		      +");";
	
	/**
	 * Constructor with the actual context for the DBManager
	 * @param c
	 */
	public EmailManager(Context c) {
		// first init the db libraries with the context
		SQLiteDatabase.loadLibs(c);
		dbManager = new DBManager(c);
	}
	
	public void openWritable() {
		db = dbManager.getWritableDatabase(DBManager.SQLITECIPHER_SECRET);
	}
	
	public void openReadable() {
		db = dbManager.getReadableDatabase(DBManager.SQLITECIPHER_SECRET);
	}
	
	public void close() {
		dbManager.close();
	}
	
	// gets called from the DBManager#onCreate()
	public static String createEmailTable() {
		return CREATE_TABLE_EMAIL;
	}
	
	// gets called from the DBManager#onUpdate()
	public static String upgradeEmailTable() {
		return "DROP TABLE IF EXISTS" + TABLE_EMAIL;
	}
	
	/**
	 * Method to get a new {@link Email} by his <u>id</u> from the database.
	 * @param id ({@link Long})
	 * @return {@link Email}
	 */
	public Email getEmailById(long id) {
		Cursor cursor = db.query(TABLE_EMAIL, null, DBManager.COLUMN_ID + "=?", new String[] {String.valueOf(id)}, null, null, null);
		// always place the cursor to the first element, before accessing
		cursor.moveToFirst();
		Email email = emailFromCursor(cursor);
		cursor.close();
		return email;
	}
	
	/**
	 * Method to get a new {@link Email} by his <u>emailaddress</u> from the database.
	 * @param emailaddress ({@link String})
	 * @return {@link Email}
	 */
	public Email getEmailByEmailaddress(String emailaddress) {
		Cursor cursor = db.query(TABLE_EMAIL, null, COLUMN_EMAILADDRESS + "=" + emailaddress, null, null, null, null);
		cursor.moveToFirst();
		Email email = emailFromCursor(cursor);
		cursor.close();
		return email;
	}
	
	/**
	 * Method to store a new {@link Email} in the database.
	 * @param email
	 * @return {@link Email}
	 */
	public Email addEmail(Email email) {
		email.setCreateTime(new Date());
		ContentValues values = fillContenValuesWithNewEmailData(email);
		long insertId = db.insert(TABLE_EMAIL, null, values);
		Cursor cursor = db.query(TABLE_EMAIL, null, DBManager.COLUMN_ID + "=?", new String[] {String.valueOf(insertId)}, null, null, null);
		cursor.moveToFirst();
		Email toReturn = emailFromCursor(cursor);
		cursor.close();
		return toReturn;
	}
	
	/**
	 * Method to <u>fully delete</u> an {@link Email}.
	 * @param email
	 * @return true if email could be deleted, false otherwise
	 */
	public boolean fullDeleteDevice(Email email) {
		long id = email.getId();
		return db.delete(TABLE_EMAIL, DBManager.COLUMN_ID + "=" + id, null) > 0;
	}
	
	/**
	 * Method to <u>soft delete</u> an {@link Email}.
	 * This means the active status is set to false.
	 * @param email
	 * @return true if Email could be soft deleted, false otherwise.
	 */
	public boolean softDeleteEmail(Email email) {
		long id = email.getId();
		Email toDeactive = getEmailById(id);
		if (toDeactive != null) {
			toDeactive.setActive(false);
			updateEmail(toDeactive);
			return true;
		}
		return false;
	}
	
	/**
	 * Method to retrieve all {@link Email}s from the database.
	 * @return {@link List} with {@link Email}s
	 */
	public List<Email> getAllEmails() {
		List<Email> emailList = new ArrayList<Email>();
		Cursor cursor = db.query(TABLE_EMAIL, null, null, null, null, null, null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast()) {
			Email email = emailFromCursor(cursor);
			if (email.isActive()) {
				emailList.add(email);
			}
			cursor.moveToNext();
		}
        cursor.close();
        return emailList;
	}
	
	/**
	 * Method to update an existing {@link Email}.
	 * @param email
	 * @return {@link Email}
	 */
	public Email updateEmail(Email email) {
		long emailId = email.getId();
		email.setLastUpdate(new Date());
		ContentValues contentValues = fillContenValuesWithUpdatedEmailData(email);
		db.update(TABLE_EMAIL, contentValues, DBManager.COLUMN_ID + "=" + emailId, null);
		Cursor cursor = db.query(TABLE_EMAIL, null, DBManager.COLUMN_ID + "=" + emailId, null, null, null, null);
		cursor.moveToFirst();
		Email returnEmail = emailFromCursor(cursor);
		cursor.close();
		return returnEmail;
	}
	
	/**
	 * Method to get a new {@link Email} instance out of a cursor element.
	 * @param cursor
	 * @return {@link Email}
	 */
	public Email emailFromCursor(Cursor cursor) {
		Email email = new Email();
		email.setId(cursor.getLong(0));
		email.setActive(cursor.getInt(1) == 0 ? false : true);
		email.setCreateTime(new Date(cursor.getLong(2)));
		email.setLastUpdate(new Date(cursor.getLong(3)));
		email.setEmailaddress(cursor.getString(4));
		email.setPassword(cursor.getString(5));
		return email;
	}
	
	/**
	 * Helper method, because the {@link SQLiteDatabase} insert method needs ContentValues.
	 * @param email
	 * @return {@link ContentValues}
	 */
	public ContentValues fillContenValuesWithNewEmailData(Email email) {
		ContentValues values = new ContentValues();
		values.put(DBManager.COLUMN_ACTIVE, email.isActive() == false ? 0 : 1);
		values.put(DBManager.COLUMN_CREATETIME, email.getCreateTime().getTime());
		values.put(COLUMN_EMAILADDRESS, email.getEmailaddress());
		values.put(COLUMN_PASSWORD, email.getPassword());
		return values;
	}
	
	/**
	 * Helper method, because the {@link SQLiteDatabase} update method needs ContentValues.
	 * @param email
	 * @return {@link ContentValues}
	 */
	public ContentValues fillContenValuesWithUpdatedEmailData(Email email) {
		ContentValues values = new ContentValues();
		values.put(DBManager.COLUMN_ACTIVE, email.isActive() == false ? 0 : 1);
		values.put(DBManager.COLUMN_LASTUPDATE, email.getLastUpdate().getTime());
		values.put(COLUMN_EMAILADDRESS, email.getEmailaddress());
		values.put(COLUMN_PASSWORD, email.getPassword());
		return values;
	}
}
