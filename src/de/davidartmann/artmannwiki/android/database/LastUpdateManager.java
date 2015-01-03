package de.davidartmann.artmannwiki.android.database;

import net.sqlcipher.database.SQLiteDatabase;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import de.davidartmann.artmannwiki.android.LoginMain;
import de.davidartmann.artmannwiki.android.model.Account;

public class LastUpdateManager {
	
	private DBManager dbManager;
	private SQLiteDatabase db;
	
	private static final String TABLE_LAST_UPDATE = "last_update";
	private static final String COLUMN_LAST_UPDATE = "last_update";
	
	private static final String CREATE_TABLE_LAST_UPDATE = "create table "
		      + TABLE_LAST_UPDATE + "(" 
		      + DBManager.COLUMN_ID + " integer primary key autoincrement,"
		      + COLUMN_LAST_UPDATE + " datetime"
		      +");";

	/**
	 * Constructor with the actual context for the DBManager
	 * @param context
	 */
	public LastUpdateManager(Context c) {
		// first init the db libraries with the context
		SQLiteDatabase.loadLibs(c);
		dbManager = new DBManager(c);
	}
	
	public void openWritable(Context c) {
		String s = c.getSharedPreferences(LoginMain.PREFS_NAME, 0).getString(LoginMain.PREFS_ATTR, "");
		db = dbManager.getWritableDatabase(s);
	}
	
	public void openReadable(Context c) {
		String s = c.getSharedPreferences(LoginMain.PREFS_NAME, 0).getString(LoginMain.PREFS_ATTR, "");
		db = dbManager.getReadableDatabase(s);
	}
	
	public void close() {
		dbManager.close();
	}
	
	// gets called from the DBManager#onCreate()
	public static String createLastUpdateTable() {
		return CREATE_TABLE_LAST_UPDATE;
	}
	
	// gets called from the DBManager#onUpdate()
	public static String upgradeLastUpdateTable() {
		return "DROP TABLE IF EXISTS" + TABLE_LAST_UPDATE;
	}
	
	/**
	 * Method to get the Timestamp of the last change.
	 * @param id ({@link Long})
	 * @return {@link Account}
	 */
	public Long getLastUpdate() {
		Cursor cursor = db.query(TABLE_LAST_UPDATE, null, DBManager.COLUMN_ID + "=?", new String[] {String.valueOf(1)}, null, null, null);
		// always place the cursor to the first element, before accessing
		cursor.moveToFirst();
		Long l = dateFromCursor(cursor);
		cursor.close();
		return l;
	}
	
	/**
	 * Method to set the timestamp to the actual time, when an entity is created, updated or deleted.
	 * @return the actualized time as {@link Long} or null.
	 */
	public Long setLastUpdate(Long l) {
		Cursor cursor = db.query(TABLE_LAST_UPDATE, null, DBManager.COLUMN_ID + "=?", new String[] {String.valueOf(1)}, null, null, null);
		cursor.moveToFirst();
		if (cursor.getCount() > 0) {
			cursor.close();
			ContentValues contentValues = fillContentValuesWithLong(l);
			db.update(TABLE_LAST_UPDATE, contentValues, DBManager.COLUMN_ID + "=?", new String[] {String.valueOf(1)});
			Cursor cursor1 = db.query(TABLE_LAST_UPDATE, null, DBManager.COLUMN_ID + "=?", new String[] {String.valueOf(1)}, null, null, null);
			cursor1.moveToFirst();
			Long toReturn = dateFromCursor(cursor1);
			cursor1.close();
			return toReturn;
		} else {
			cursor.close();
			ContentValues values = fillContentValuesWithLong(l);
			long insertId = db.insert(TABLE_LAST_UPDATE, null, values);
			Cursor cursor1 = db.query(TABLE_LAST_UPDATE, null, DBManager.COLUMN_ID + "=?", new String[] {String.valueOf(insertId)}, null, null, null);
			cursor1.moveToFirst();
			Long toReturn = dateFromCursor(cursor1);
			cursor1.close();
			return toReturn;
		}
	}
	
	/**
	 * Method to get a new {@link Long} instance out of a cursor element.
	 * This represents the latest change time in the backend.
	 * @param cursor
	 * @return {@link Long}
	 */
	private Long dateFromCursor(Cursor cursor) {
		// id on index 0 is not relevant so we jump directly to index 1 to get the time as Long
		Long l = cursor.getLong(1);
		return l;
	}
	
	/**
	 * Helper method, because the {@link SQLiteDatabase} update method needs ContentValues.
	 * @param l
	 * @return {@link ContentValues}
	 */
	private ContentValues fillContentValuesWithLong(Long l) {
		ContentValues values = new ContentValues();
		values.put(COLUMN_LAST_UPDATE, l);
		return values;
	}
}
