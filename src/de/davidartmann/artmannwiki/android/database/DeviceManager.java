package de.davidartmann.artmannwiki.android.database;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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
	
	private static final String CREATE_TABLE_DEVICE = "create table "
		      + TABLE_DEVICE + "(" 
		      + DBManager.COLUMN_ID + " integer primary key autoincrement,"
		      + DBManager.COLUMN_ACTIVE + " integer not null,"
		      + DBManager.COLUMN_CREATETIME + " datetime not null,"
		      + DBManager.COLUMN_LASTUPDATE + " datetime,"
		      + COLUMN_NAME + " text not null,"
		      + COLUMN_NUMBER + " text not null,"
		      + COLUMN_PIN + " text not null,"
		      + COLUMN_PUK + " text not null"
		      +");";
	
	/**
	 * Constructor with the actual context for the DBManager
	 * @param context
	 */
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
	
	/**
	 * Method to get a new {@link Device} by his <u>id</u> from the database.
	 * @param id ({@link Long})
	 * @return {@link Device}
	 */
	public Device getDeviceById(long id) {
		Cursor cursor = db.query(TABLE_DEVICE, null, DBManager.COLUMN_ID + "=?", new String[] {String.valueOf(id)}, null, null, null);
		//always place the cursor to the first element, before accessing
		cursor.moveToFirst();
		Device device = deviceFromCursor(cursor);
		cursor.close();
		return device;
	}
	
	/**
	 * Method to get a new {@link Device} by his <u>number</u> from the database.
	 * @param iban ({@link String})
	 * @return {@link Device}
	 */
	public Device getDeviceByNumber(String number) {
		Cursor cursor = db.query(TABLE_DEVICE, null, COLUMN_NUMBER + "=" + number, null, null, null, null);
		cursor.moveToFirst();
		Device device = deviceFromCursor(cursor);
		cursor.close();
		return device;
	}
	
	/**
	 * Method to store a new {@link Device} in the database.
	 * @param device
	 * @return {@link Device}
	 */
	public Device addDevice(Device device) {
		device.setCreateTime(new Date());
		ContentValues values = fillContenValuesWithNewDeviceData(device);
		long insertId = db.insert(TABLE_DEVICE, null, values);
		Cursor cursor = db.query(TABLE_DEVICE, null, DBManager.COLUMN_ID + "=?", new String[] {String.valueOf(insertId)}, null, null, null);
		cursor.moveToFirst();
		Device toReturn = deviceFromCursor(cursor);
		cursor.close();
		return toReturn;
	}
	
	/**
	 * Method to <u>fully delete</u> an {@link Device}.
	 * @param device
	 * @return true if device could be deleted, false otherwise
	 */
	public boolean fullDeleteDevice(Device device) {
		long id = device.getId();
		return db.delete(TABLE_DEVICE, DBManager.COLUMN_ID + "=" + id, null) > 0;
	}
	
	/**
	 * Method to <u>soft delete</u> an {@link Device}.
	 * This means the active status is set to false.
	 * @param device
	 * @return true if Device could be soft deleted, false otherwise.
	 */
	public boolean softDeleteDevice(Device device) {
		long id = device.getId();
		Device toDeactive = getDeviceById(id);
		if (toDeactive != null) {
			toDeactive.setActive(false);
			updateDevice(toDeactive);
			return true;
		}
		return false;
	}
	
	/**
	 * Method to retrieve all {@link Device}s from the database.
	 * @return {@link List} with {@link Device}s
	 */
	public List<Device> getAllDevices() {
		List<Device> deviceList = new ArrayList<Device>();
		Cursor cursor = db.query(TABLE_DEVICE, null, null, null, null, null, null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast()) {
			Device device = deviceFromCursor(cursor);
			if (device.isActive()) {
				deviceList.add(device);
			}
			cursor.moveToNext();
		}
        cursor.close();
        return deviceList;
	}
	
	/**
	 * Method to update an existing {@link Device}.
	 * @param device
	 * @return {@link Device}
	 */
	public Device updateDevice(Device device) {
		long deviceId = device.getId();
		device.setLastUpdate(new Date());
		ContentValues contentValues = fillContenValuesWithUpdatedDeviceData(device);
		db.update(TABLE_DEVICE, contentValues, DBManager.COLUMN_ID + "=" + deviceId, null);
		Cursor cursor = db.query(TABLE_DEVICE, null, DBManager.COLUMN_ID + "=" + deviceId, null, null, null, null);
		cursor.moveToFirst();
		Device returnDevice = deviceFromCursor(cursor);
		cursor.close();
		return returnDevice;
	}
	
	/**
	 * Method to get a new {@link Device} instance out of a cursor element.
	 * @param cursor
	 * @return {@link Device}
	 */
	public Device deviceFromCursor(Cursor cursor) {
		Device device = new Device();
		device.setId(cursor.getLong(0));
		device.setActive(cursor.getInt(1) == 0 ? false : true);
		device.setCreateTime(new Date(cursor.getLong(2)));
		device.setLastUpdate(new Date(cursor.getLong(3)));
		device.setName(cursor.getString(4));
		device.setNumber(cursor.getString(5));
		device.setPin(cursor.getString(6));
		device.setPuk(cursor.getString(7));
		return device;
	}
	
	/**
	 * Helper method, because the {@link SQLiteDatabase} insert method needs ContentValues.
	 * @param device
	 * @return {@link ContentValues}
	 */
	public ContentValues fillContenValuesWithNewDeviceData(Device device) {
		ContentValues values = new ContentValues();
		values.put(DBManager.COLUMN_ACTIVE, device.isActive() == false ? 0 : 1);
		values.put(DBManager.COLUMN_CREATETIME, device.getCreateTime().getTime());
		values.put(COLUMN_NAME, device.getName());
		values.put(COLUMN_NUMBER, device.getNumber());
		values.put(COLUMN_PIN, device.getPin());
		values.put(COLUMN_PUK, device.getPuk());
		return values;
	}
	
	/**
	 * Helper method, because the {@link SQLiteDatabase} update method needs ContentValues.
	 * @param device
	 * @return {@link ContentValues}
	 */
	public ContentValues fillContenValuesWithUpdatedDeviceData(Device device) {
		ContentValues values = new ContentValues();
		values.put(DBManager.COLUMN_ACTIVE, device.isActive() == false ? 0 : 1);
		values.put(DBManager.COLUMN_LASTUPDATE, device.getLastUpdate().getTime());
		values.put(COLUMN_NAME, device.getName());
		values.put(COLUMN_NUMBER, device.getNumber());
		values.put(COLUMN_PIN, device.getPin());
		values.put(COLUMN_PUK, device.getPuk());
		return values;
	}
}
