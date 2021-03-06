package de.davidartmann.artmannwiki.android.database;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sqlcipher.database.SQLiteDatabase;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import de.davidartmann.artmannwiki.android.LoginMain;
import de.davidartmann.artmannwiki.android.model.Login;

/**
 * 
 * {@link Login} model helper class.
 *
 */
public class LoginManager {

	private DBManager dbManager;
	private SQLiteDatabase db;
	
	private static final String TABLE_LOGIN = "login";
	private static final String COLUMN_USERNAME = "username";
	private static final String COLUMN_PASSWORD = "password";
	private static final String COLUMN_DESCRIPTION = "description";
	
	private static final String CREATE_TABLE_LOGIN = "create table "
		      + TABLE_LOGIN + "(" 
		      + DBManager.COLUMN_ID + " integer primary key autoincrement,"
		      + DBManager.COLUMN_ACTIVE + " integer not null,"
		      + DBManager.COLUMN_CREATETIME + " datetime not null,"
		      + DBManager.COLUMN_LASTUPDATE + " datetime,"
		      + DBManager.COLUMN_BACKEND_ID + " integer,"
		      + COLUMN_USERNAME + " text not null,"
		      + COLUMN_PASSWORD + " text not null,"
		      + COLUMN_DESCRIPTION + " text not null"
		      +");";
	
	/**
	 * Constructor with the actual context for the DBManager
	 * @param context
	 */
	public LoginManager(Context c) {
		// first init the db libraries with the context
		SQLiteDatabase.loadLibs(c);
		dbManager = new DBManager(c);
	}
	
	public void openWritable(Context c) {
		String s = c.getSharedPreferences(LoginMain.PREFS_NAME, 0).getString(LoginMain.PREFS_ATTR, "");
		//db = dbManager.getWritableDatabase(DBManager.SQLITECIPHER_SECRET);
		db = dbManager.getReadableDatabase(s);
	}
	
	public void openReadable(Context c) {
		String s = c.getSharedPreferences(LoginMain.PREFS_NAME, 0).getString(LoginMain.PREFS_ATTR, "");
		//db = dbManager.getReadableDatabase(DBManager.SQLITECIPHER_SECRET);
		db = dbManager.getReadableDatabase(s);
	}
	
	public void close() {
		dbManager.close();
	}

	// gets called from the DBManager#onCreate()
	public static String createLoginTable() {
		return CREATE_TABLE_LOGIN;
	}
	
	// gets called from the DBManager#onUpdate()
	public static String upgradeLoginTable() {
		return "DROP TABLE IF EXISTS" + TABLE_LOGIN;
	}
	
	/**
	 * Method to get a new {@link Login} by his <u>id</u> from the database.
	 * @param id ({@link Long})
	 * @return {@link Login}
	 */
	public Login getLoginById(long id) {
		Cursor cursor = db.query(TABLE_LOGIN, null, DBManager.COLUMN_ID + "=?", new String[] {String.valueOf(id)}, null, null, null);
		// always place the cursor to the first element, before accessing
		cursor.moveToFirst();
		Login login = loginFromCursor(cursor);
		cursor.close();
		return login;
	}
	
	/**
	 * Method to store a new {@link Login} in the database.
	 * @param login
	 * @return {@link Login}
	 */
	public Login addLogin(Login login) {
		login.setCreateTime(new Date());
		ContentValues values = fillContenValuesWithNewLoginData(login);
		long insertId = db.insert(TABLE_LOGIN, null, values);
		Cursor cursor = db.query(TABLE_LOGIN, null, DBManager.COLUMN_ID + "=?", new String[] {String.valueOf(insertId)}, null, null, null);
		cursor.moveToFirst();
		Login toReturn = loginFromCursor(cursor);
		cursor.close();
		return toReturn;
	}
	
	/**
	 * Method to <u>fully delete</u> an {@link Login}.
	 * @param login
	 * @return true if {@link Login} could be deleted, false otherwise
	 */
	public boolean fullDeleteLogin(Login login) {
		long id = login.getId();
		return db.delete(TABLE_LOGIN, DBManager.COLUMN_ID + "=" + id, null) > 0;
	}
	
	/**
	 * Method to <u>soft delete</u> an {@link Login}.
	 * This means the active status is set to false.
	 * @param login
	 * @return true if Login could be soft deleted, false otherwise.
	 */
	public boolean softDeleteLogin(Login login) {
		long id = login.getId();
		Login toDeactive = getLoginById(id);
		if (toDeactive != null) {
			toDeactive.setActive(false);
			updateLoginById(toDeactive);
			return true;
		}
		return false;
	}
	
	/**
	 * Method to retrieve all {@link Login}s from the database.
	 * @return {@link List} with {@link Login}s
	 */
	public List<Login> getAllLogins() {
		List<Login> loginList = new ArrayList<Login>();
		Cursor cursor = db.query(TABLE_LOGIN, null, null, null, null, null, null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast()) {
			Login login = loginFromCursor(cursor);
			if (login.isActive()) {
				loginList.add(login);
			}
			cursor.moveToNext();
		}
        cursor.close();
        return loginList;
	}
	
	/**
	 * Method to update an existing {@link Login} by its id.
	 * @param login
	 * @return {@link Login}
	 */
	public Login updateLoginById(Login login) {
		login.setLastUpdate(new Date());
		long loginId = login.getId();
		ContentValues contentValues = fillContentValuesWithUpdatedLoginData(login);
		db.update(TABLE_LOGIN, contentValues, DBManager.COLUMN_ID + "=" + loginId, null);
		Cursor cursor = db.query(TABLE_LOGIN, null, DBManager.COLUMN_ID + "=" + loginId, null, null, null, null);
		cursor.moveToFirst();
		Login returnAccount = loginFromCursor(cursor);
		cursor.close();
		return returnAccount;
	}
	
	/**
	 * Method to update an existing {@link Login} by its backendId.
	 * @param login
	 * @return {@link Login}
	 */
	public Login updateLoginByBackendId(Login login) {
		long loginBackendId = login.getBackendId();
		login.setLastUpdate(new Date());
		ContentValues contentValues = fillContentValuesWithUpdatedLoginData(login);
		db.update(TABLE_LOGIN, contentValues, DBManager.COLUMN_BACKEND_ID + "=" + loginBackendId, null);
		Cursor cursor = db.query(TABLE_LOGIN, null, DBManager.COLUMN_BACKEND_ID + "=" + loginBackendId, null, null, null, null);
		cursor.moveToFirst();
		Login returnLogin = loginFromCursor(cursor);
		cursor.close();
		return returnLogin;
	}
	
	/**
	 * Method to get a new {@link Login} instance out of a cursor element.
	 * @param cursor
	 * @return {@link Login}
	 */
	public Login loginFromCursor(Cursor cursor) {
		Login login = new Login();
		login.setId(cursor.getLong(0));
		login.setActive(cursor.getInt(1) == 0 ? false : true);
		login.setCreateTime(new Date(cursor.getLong(2)));
		login.setLastUpdate(new Date(cursor.getLong(3)));
		login.setBackendId(cursor.getLong(4));
		login.setUsername(cursor.getString(5));
		login.setPassword(cursor.getString(6));
		login.setDescription(cursor.getString(7));
		return login;
	}
	
	/**
	 * Helper method, because the {@link SQLiteDatabase} insert method needs ContentValues.
	 * @param login
	 * @return {@link ContentValues}
	 */
	public ContentValues fillContenValuesWithNewLoginData(Login login) {
		ContentValues values = new ContentValues();
		values.put(DBManager.COLUMN_ACTIVE, login.isActive() == false ? 0 : 1);
		values.put(DBManager.COLUMN_CREATETIME, login.getCreateTime().getTime());
		values.put(COLUMN_USERNAME, login.getUsername());
		values.put(COLUMN_PASSWORD, login.getPassword());
		values.put(COLUMN_DESCRIPTION, login.getDescription());
		return values;
	}
	
	/**
	 * Helper method, because the {@link SQLiteDatabase} update method needs ContentValues.
	 * @param login
	 * @return {@link ContentValues}
	 */
	public ContentValues fillContentValuesWithUpdatedLoginData(Login login) {
		ContentValues values = new ContentValues();
		values.put(DBManager.COLUMN_ACTIVE, login.isActive() == false ? 0 : 1);
		values.put(DBManager.COLUMN_LASTUPDATE, login.getLastUpdate().getTime());
		values.put(COLUMN_USERNAME, login.getUsername());
		values.put(COLUMN_PASSWORD, login.getPassword());
		values.put(COLUMN_DESCRIPTION, login.getDescription());
		return values;
	}
	
	/**
	 * Helper method, because the {@link SQLiteDatabase} insert method needs ContentValues.
	 * This only stands for the backendId which is returned from the backend when storing a new entity.
	 * So the synchronization can check relations between local entities and the ones in the backend.
	 * @param login
	 * @return {@link ContentValues}
	 */
	public ContentValues fillContenValuesWithNewLoginBackendId(Login login) {
		ContentValues values = new ContentValues();
		values.put(DBManager.COLUMN_ACTIVE, login.isActive() == false ? 0 : 1);
		values.put(DBManager.COLUMN_BACKEND_ID, login.getBackendId());
		values.put(COLUMN_USERNAME, login.getUsername());
		values.put(COLUMN_PASSWORD, login.getPassword());
		values.put(COLUMN_DESCRIPTION, login.getDescription());
		return values;
	}
	
	
	/**
	 * Method to add the backendId to an {@link Login}, when its stored in the backend.
	 * @param id {@link Long}
	 * @param backendId {@link Long}
	 * @return {@link Login}
	 */
	public Login addBackendId(Long id, Long backendId) {
		Login l = getLoginById(id);
		l.setBackendId(backendId);
		ContentValues contentValues = fillContenValuesWithNewLoginBackendId(l);
		db.update(TABLE_LOGIN, contentValues, DBManager.COLUMN_ID + "=" + id, null);
		Cursor cursor = db.query(TABLE_LOGIN, null, DBManager.COLUMN_ID + "=" + id, null, null, null, null);
		cursor.moveToFirst();
		Login returnLogin = loginFromCursor(cursor);
		cursor.close();
		return returnLogin;
	}
}
