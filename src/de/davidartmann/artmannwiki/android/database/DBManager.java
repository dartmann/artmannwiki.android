package de.davidartmann.artmannwiki.android.database;

import android.content.Context;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;
import android.util.Log;

/**
 * 
 * This class is the root DBManager.
 * A seperate class per Table is created with static methods,
 * which are called in the corresponding methods of the DBManager.
 *
 */
public class DBManager extends SQLiteOpenHelper {
	
	private static final int DB_VERSION = 1;
    private static final String DB_NAME = "artmannwiki.db";
    
    // public column names, for every entity the same
    public static final String COLUMN_ID = "_id";
	public static final String COLUMN_CREATETIME = "create_time";
	public static final String COLUMN_LASTUPDATE = "last_update";
	public static final String COLUMN_ACTIVE = "active";

	public DBManager(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

	/**
	 * All the static create Methods of the ModelManager classes are called here
	 */
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(AccountManager.createAccountTable());
		db.execSQL(DeviceManager.createDeviceTable());
		db.execSQL(EmailManager.createEmailTable());
		db.execSQL(InsuranceManager.createInsuranceTable());
		db.execSQL(LoginManager.createLoginTable());
		db.execSQL(MiscellaneousManager.createMiscellaneousTable());
	}

	/**
	 * All the static update Methods of the ModelManager classes are called here
	 */
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(DBManager.class.getName(),
		        "Upgrading database from version " + oldVersion + " to "
		            + newVersion + ", which will destroy all old data");
		// only update if new version is higher
		if (oldVersion < newVersion) {
			db.execSQL(AccountManager.upgradeAccountTable());
			db.execSQL(DeviceManager.upgradeDeviceTable());
			db.execSQL(EmailManager.upgradeEmailTable());
			db.execSQL(InsuranceManager.upgradeEmailTable());
			db.execSQL(LoginManager.upgradeLoginTable());
			db.execSQL(MiscellaneousManager.upgradeMiscellaneousTable());
	        onCreate(db);
		}
	}

}
