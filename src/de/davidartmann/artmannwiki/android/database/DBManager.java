package de.davidartmann.artmannwiki.android.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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


	DBManager(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

	/**
	 * All the static create Methods of the ModelManager classes are called here
	 */
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(AccountManager.createAccountTable());

	}

	/**
	 * All the static update Methods of the ModelManager classes are called here
	 */
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(AccountManager.upgradeAccountTable());
        onCreate(db);
	}

}
