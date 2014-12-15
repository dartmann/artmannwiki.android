package de.davidartmann.artmannwiki.android.database;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import de.davidartmann.artmannwiki.android.model.Miscellaneous;

/**
 * 
 * {@link Miscellaneous} model helper class.
 *
 */
public class MiscellaneousManager {
	
	private DBManager dbManager;
	private SQLiteDatabase db;
	
	private static final String TABLE_MISCELLANEOUS = "miscellaneous";
	private static final String COLUMN_TEXT = "text";
	private static final String COLUMN_DESCRIPTION = "description";
	
	private static final String CREATE_TABLE_MISCELLANEOUS = "create table "
		      + TABLE_MISCELLANEOUS + "(" 
		      + DBManager.COLUMN_ID + " integer primary key autoincrement,"
		      + DBManager.COLUMN_ACTIVE + " integer not null,"
		      + DBManager.COLUMN_CREATETIME + " datetime not null,"
		      + DBManager.COLUMN_LASTUPDATE + " datetime,"
		      + COLUMN_TEXT + " text not null,"
		      + COLUMN_DESCRIPTION + " text not null"
		      +");";
	
	/**
	 * Constructor with the actual context for the DBManager
	 * @param context
	 */
	public MiscellaneousManager(Context c) {
		dbManager = new DBManager(c);
	}
	
	public void openWritable() {
		db = dbManager.getWritableDatabase();
	}
	
	public void openReadable() {
		db = dbManager.getReadableDatabase();
	}
	
	public void close() {
		dbManager.close();
	}

	//gets called from the DBManager#onCreate()
	public static String createMiscellaneousTable() {
		return CREATE_TABLE_MISCELLANEOUS;
	}
	
	//gets called from the DBManager#onUpdate()
	public static String upgradeMiscellaneousTable() {
		return "DROP TABLE IF EXISTS" + TABLE_MISCELLANEOUS;
	}
	
	/**
	 * Method to get a new {@link Miscellaneous} by his <u>id</u> from the database.
	 * @param id ({@link Long})
	 * @return {@link Miscellaneous}
	 */
	public Miscellaneous getMiscellaneousById(long id) {
		Cursor cursor = db.query(TABLE_MISCELLANEOUS, null, DBManager.COLUMN_ID + "=?", new String[] {String.valueOf(id)}, null, null, null);
		//always place the cursor to the first element, before accessing
		cursor.moveToFirst();
		Miscellaneous miscellaneous = miscellaneousFromCursor(cursor);
		cursor.close();
		return miscellaneous;
	}
	
	/**
	 * Method to store a new {@link Miscellaneous} in the database.
	 * @param miscellaneous
	 * @return {@link Miscellaneous}
	 */
	public Miscellaneous addMiscellaneous(Miscellaneous miscellaneous) {
		miscellaneous.setCreateTime(new Date());
		ContentValues values = fillContenValuesWithNewMiscellaneousData(miscellaneous);
		long insertId = db.insert(TABLE_MISCELLANEOUS, null, values);
		Cursor cursor = db.query(TABLE_MISCELLANEOUS, null, DBManager.COLUMN_ID + "=?", new String[] {String.valueOf(insertId)}, null, null, null);
		cursor.moveToFirst();
		Miscellaneous toReturn = miscellaneousFromCursor(cursor);
		cursor.close();
		return toReturn;
	}
	
	/**
	 * Method to <u>fully delete</u> an {@link Miscellaneous}.
	 * @param miscellaneous
	 * @return true if {@link Miscellaneous} could be deleted, false otherwise
	 */
	public boolean fullDeleteLogin(Miscellaneous miscellaneous) {
		long id = miscellaneous.getId();
		return db.delete(TABLE_MISCELLANEOUS, DBManager.COLUMN_ID + "=" + id, null) > 0;
	}
	
	/**
	 * Method to <u>soft delete</u> an {@link Miscellaneous}.
	 * This means the active status is set to false.
	 * @param miscellaneous
	 * @return true if Miscellaneous could be soft deleted, false otherwise.
	 */
	public boolean softDeleteLogin(Miscellaneous miscellaneous) {
		long id = miscellaneous.getId();
		Miscellaneous toDeactive = getMiscellaneousById(id);
		if (toDeactive != null) {
			toDeactive.setActive(false);
			updateMiscellaneous(toDeactive);
			return true;
		}
		return false;
	}
	
	/**
	 * Method to retrieve all {@link Miscellaneous}s from the database.
	 * @return {@link List} with {@link Miscellaneous}s
	 */
	public List<Miscellaneous> getAllMiscellaneous() {
		List<Miscellaneous> miscellaneousList = new ArrayList<Miscellaneous>();
		Cursor cursor = db.query(TABLE_MISCELLANEOUS, null, null, null, null, null, null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast()) {
			Miscellaneous miscellaneous = miscellaneousFromCursor(cursor);
			if (miscellaneous.isActive()) {
				miscellaneousList.add(miscellaneous);
			}
			cursor.moveToNext();
		}
        cursor.close();
        return miscellaneousList;
	}
	
	/**
	 * Method to update an existing {@link Miscellaneous}.
	 * @param miscellaneous
	 * @return {@link Miscellaneous}
	 */
	public Miscellaneous updateMiscellaneous(Miscellaneous miscellaneous) {
		long loginId = miscellaneous.getId();
		miscellaneous.setLastUpdate(new Date());
		ContentValues contentValues = fillContentValuesWithUpdatedMiscellaneousData(miscellaneous);
		db.update(TABLE_MISCELLANEOUS, contentValues, DBManager.COLUMN_ID + "=" + loginId, null);
		Cursor cursor = db.query(TABLE_MISCELLANEOUS, null, DBManager.COLUMN_ID + "=" + loginId, null, null, null, null);
		cursor.moveToFirst();
		Miscellaneous returnMiscellaneous = miscellaneousFromCursor(cursor);
		cursor.close();
		return returnMiscellaneous;
	}
	
	/**
	 * Method to get a new {@link Miscellaneous} instance out of a cursor element.
	 * @param cursor
	 * @return {@link Miscellaneous}
	 */
	public Miscellaneous miscellaneousFromCursor(Cursor cursor) {
		Miscellaneous miscellaneous = new Miscellaneous();
		miscellaneous.setId(cursor.getLong(0));
		miscellaneous.setActive(cursor.getInt(1) == 0 ? false : true);
		miscellaneous.setCreateTime(new Date(cursor.getLong(2)));
		miscellaneous.setLastUpdate(new Date(cursor.getLong(3)));
		miscellaneous.setText(cursor.getString(4));
		miscellaneous.setDescription(cursor.getString(5));
		return miscellaneous;
	}
	
	/**
	 * Helper method, because the {@link SQLiteDatabase} insert method needs ContentValues.
	 * @param miscellaneous
	 * @return {@link ContentValues}
	 */
	public ContentValues fillContenValuesWithNewMiscellaneousData(Miscellaneous miscellaneous) {
		ContentValues values = new ContentValues();
		values.put(DBManager.COLUMN_ACTIVE, miscellaneous.isActive() == false ? 0 : 1);
		values.put(DBManager.COLUMN_CREATETIME, miscellaneous.getCreateTime().getTime());
		values.put(COLUMN_TEXT, miscellaneous.getText());
		values.put(COLUMN_DESCRIPTION, miscellaneous.getDescription());
		return values;
	}
	
	/**
	 * Helper method, because the {@link SQLiteDatabase} update method needs ContentValues.
	 * @param miscellaneous
	 * @return {@link ContentValues}
	 */
	public ContentValues fillContentValuesWithUpdatedMiscellaneousData(Miscellaneous miscellaneous) {
		ContentValues values = new ContentValues();
		values.put(DBManager.COLUMN_ACTIVE, miscellaneous.isActive() == false ? 0 : 1);
		values.put(DBManager.COLUMN_LASTUPDATE, miscellaneous.getLastUpdate().getTime());
		values.put(COLUMN_TEXT, miscellaneous.getText());
		values.put(COLUMN_DESCRIPTION, miscellaneous.getDescription());
		return values;
	}

}
