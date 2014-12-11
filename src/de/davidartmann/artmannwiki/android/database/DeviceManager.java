package de.davidartmann.artmannwiki.android.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import de.davidartmann.artmannwiki.android.model.Device;

/**
 * 
 * {@link Device} model helper class
 *
 */
public class DeviceManager {

	private DBManager dbManager;
	private SQLiteDatabase db;
	
	private static final String TABLE_DEVICE = "device";
	private static final String COLUMN_NAME = "name";
	private static final String COLUMN_NUMBER = "number";
	private static final String COLUMN_PIN = "pin";
	private static final String COLUMN_PUK = "puk";
	private String[] ALL_COLUMNS = { DBManager.COLUMN_ID, DBManager.COLUMN_ACTIVE, 
			DBManager.COLUMN_CREATETIME, DBManager.COLUMN_LASTUPDATE, COLUMN_NAME, COLUMN_NUMBER, COLUMN_PIN, COLUMN_PUK };
	
	private static final String CREATE_TABLE_DEVICE = "create table "
		      + TABLE_DEVICE + "(" 
		      + DBManager.COLUMN_ID + " integer primary key autoincrement,"
		      + DBManager.COLUMN_ACTIVE + " integer not null,"
		      + DBManager.COLUMN_CREATETIME + " datetime not null,"
		      + DBManager.COLUMN_LASTUPDATE + " datetime not null,"
		      + COLUMN_NAME + " text not null,"
		      + COLUMN_NUMBER + " text not null,"
		      + COLUMN_PIN + " text not null,"
		      + COLUMN_PUK + " text not null"
		      +");";
	
	public DeviceManager(Context context) {
		dbManager = new DBManager(context);
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
	public static String createDeviceTable() {
		return CREATE_TABLE_DEVICE;
	}
	
	//gets called from the DBManager#onUpdate()
	public static String upgradeDeviceTable() {
		return "DROP TABLE IF EXISTS" + TABLE_DEVICE;
	}
	
	//TODO: implement the remaining methods...
}
