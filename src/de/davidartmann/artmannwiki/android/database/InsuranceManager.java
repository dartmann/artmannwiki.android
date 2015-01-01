package de.davidartmann.artmannwiki.android.database;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import de.davidartmann.artmannwiki.android.LoginMain;
import de.davidartmann.artmannwiki.android.model.Email;
import de.davidartmann.artmannwiki.android.model.Insurance;

/**
 * 
 * {@link Insurance} model helper class
 *
 */
public class InsuranceManager {

	private DBManager dbManager;
	private SQLiteDatabase db;
	
	private static final String TABLE_INSURANCE = "insurance";
	private static final String COLUMN_NAME = "name";
	private static final String COLUMN_KIND = "kind";
	private static final String COLUMN_MEMBERSHIPID = "membershipId";

	private static final String CREATE_TABLE_INSURANCE = "create table "
		      + TABLE_INSURANCE + "(" 
		      + DBManager.COLUMN_ID + " integer primary key autoincrement,"
		      + DBManager.COLUMN_ACTIVE + " integer not null,"
		      + DBManager.COLUMN_CREATETIME + " datetime not null,"
		      + DBManager.COLUMN_LASTUPDATE + " datetime,"
		      + DBManager.COLUMN_BACKEND_ID + " integer,"
		      + COLUMN_NAME + " text not null,"
		      + COLUMN_KIND + " text not null,"
		      + COLUMN_MEMBERSHIPID + " text not null"
		      +");";
	
	/**
	 * Constructor with the actual context for the DBManager
	 * @param c
	 */
	public InsuranceManager(Context c) {
		// first init the db libraries with the context
		SQLiteDatabase.loadLibs(c);
		dbManager = new DBManager(c);
	}
	
	public void openWritable(Context c) {
		String s = c.getSharedPreferences(LoginMain.PREFS_NAME, 0).getString(LoginMain.PREFS_ATTR, "");
		//db = dbManager.getWritableDatabase(DBManager.SQLITECIPHER_SECRET);
		db = dbManager.getWritableDatabase(s);
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
	public static String createInsuranceTable() {
		return CREATE_TABLE_INSURANCE;
	}
	
	// gets called from the DBManager#onUpdate()
	public static String upgradeEmailTable() {
		return "DROP TABLE IF EXISTS" + TABLE_INSURANCE;
	}
	
	/**
	 * Method to get a new {@link Insurance} by his <u>id</u> from the database.
	 * @param id ({@link Long})
	 * @return {@link Insurance}
	 */
	public Insurance getInsuranceById(long id) {
		Cursor cursor = db.query(TABLE_INSURANCE, null, DBManager.COLUMN_ID + "=?", new String[] {String.valueOf(id)}, null, null, null);
		// always place the cursor to the first element, before accessing
		cursor.moveToFirst();
		Insurance insurance = insuranceFromCursor(cursor);
		cursor.close();
		return insurance;
	}
	
	/**
	 * Method to get a new {@link Email} by his <u>emailaddress</u> from the database.
	 * @param emailaddress ({@link String})
	 * @return {@link Email}
	 */
	public Insurance getInsuranceByMembershipId(String emailaddress) {
		Cursor cursor = db.query(TABLE_INSURANCE, null, COLUMN_MEMBERSHIPID + "=" + emailaddress, null, null, null, null);
		cursor.moveToFirst();
		Insurance insurance = insuranceFromCursor(cursor);
		cursor.close();
		return insurance;
	}
	
	/**
	 * Method to store a new {@link Insurance} in the database.
	 * @param insurance
	 * @return {@link Insurance}
	 */
	public Insurance addInsurance(Insurance insurance) {
		insurance.setCreateTime(new Date());
		ContentValues values = fillContenValuesWithNewInsuranceData(insurance);
		long insertId = db.insert(TABLE_INSURANCE, null, values);
		Cursor cursor = db.query(TABLE_INSURANCE, null, DBManager.COLUMN_ID + "=?", new String[] {String.valueOf(insertId)}, null, null, null);
		cursor.moveToFirst();
		Insurance toReturn = insuranceFromCursor(cursor);
		cursor.close();
		return toReturn;
	}
	
	/**
	 * Method to <u>fully delete</u> an {@link Insurance}.
	 * @param insurance
	 * @return true if insurance could be deleted, false otherwise
	 */
	public boolean fullDeleteInsurance(Insurance insurance) {
		long id = insurance.getId();
		return db.delete(TABLE_INSURANCE, DBManager.COLUMN_ID + "=" + id, null) > 0;
	}
	
	/**
	 * Method to <u>soft delete</u> an {@link Insurance}.
	 * This means the active status is set to false.
	 * @param insurance
	 * @return true if Insurance could be soft deleted, false otherwise.
	 */
	public boolean softDeleteEmail(Insurance insurance) {
		long id = insurance.getId();
		Insurance toDeactive = getInsuranceById(id);
		if (toDeactive != null) {
			toDeactive.setActive(false);
			updateInsurance(toDeactive);
			return true;
		}
		return false;
	}
	
	/**
	 * Method to retrieve all {@link Insurance}s from the database.
	 * @return {@link List} with {@link Insurance}s
	 */
	public List<Insurance> getAllInsurances() {
		List<Insurance> insuranceList = new ArrayList<Insurance>();
		Cursor cursor = db.query(TABLE_INSURANCE, null, null, null, null, null, null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast()) {
			Insurance insurance = insuranceFromCursor(cursor);
			if (insurance.isActive()) {
				insuranceList.add(insurance);
			}
			cursor.moveToNext();
		}
        cursor.close();
        return insuranceList;
	}
	
	/**
	 * Method to update an existing {@link Insurance}.
	 * @param insurance
	 * @return {@link Insurance}
	 */
	public Insurance updateInsurance(Insurance insurance) {
		long insuranceId = insurance.getId();
		insurance.setLastUpdate(new Date());
		ContentValues contentValues = fillContenValuesWithUpdatedInsuranceData(insurance);
		db.update(TABLE_INSURANCE, contentValues, DBManager.COLUMN_ID + "=" + insuranceId, null);
		Cursor cursor = db.query(TABLE_INSURANCE, null, DBManager.COLUMN_ID + "=" + insuranceId, null, null, null, null);
		cursor.moveToFirst();
		Insurance returnEmail = insuranceFromCursor(cursor);
		cursor.close();
		return returnEmail;
	}
	
	/**
	 * Method to get a new {@link Insurance} instance out of a cursor element.
	 * @param cursor
	 * @return {@link Insurance}
	 */
	public Insurance insuranceFromCursor(Cursor cursor) {
		Insurance insurance = new Insurance();
		insurance.setId(cursor.getLong(0));
		insurance.setActive(cursor.getInt(1) == 0 ? false : true);
		insurance.setCreateTime(new Date(cursor.getLong(2)));
		insurance.setLastUpdate(new Date(cursor.getLong(3)));
		insurance.setName(cursor.getString(4));
		insurance.setKind(cursor.getString(5));
		insurance.setMembershipId(cursor.getString(6));
		return insurance;
	}
	
	/**
	 * Helper method, because the {@link SQLiteDatabase} insert method needs ContentValues.
	 * @param insurance
	 * @return {@link ContentValues}
	 */
	public ContentValues fillContenValuesWithNewInsuranceData(Insurance insurance) {
		ContentValues values = new ContentValues();
		values.put(DBManager.COLUMN_ACTIVE, insurance.isActive() == false ? 0 : 1);
		values.put(DBManager.COLUMN_CREATETIME, insurance.getCreateTime().getTime());
		values.put(COLUMN_NAME, insurance.getName());
		values.put(COLUMN_KIND, insurance.getKind());
		values.put(COLUMN_MEMBERSHIPID, insurance.getMembershipId());
		return values;
	}
	
	/**
	 * Helper method, because the {@link SQLiteDatabase} update method needs ContentValues.
	 * @param insurance
	 * @return {@link ContentValues}
	 */
	public ContentValues fillContenValuesWithUpdatedInsuranceData(Insurance insurance) {
		ContentValues values = new ContentValues();
		values.put(DBManager.COLUMN_ACTIVE, insurance.isActive() == false ? 0 : 1);
		values.put(DBManager.COLUMN_LASTUPDATE, insurance.getLastUpdate().getTime());
		values.put(COLUMN_NAME, insurance.getName());
		values.put(COLUMN_KIND, insurance.getKind());
		values.put(COLUMN_MEMBERSHIPID, insurance.getMembershipId());
		return values;
	}
}
