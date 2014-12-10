package de.davidartmann.artmannwiki.android.database;

import de.davidartmann.artmannwiki.android.model.Account;

/**
 * 
 * This Account model helper class.
 *
 */
public class AccountManager {
	
	private static final String TABLE_ACCOUNTS = "account";
	private static final String COLUMN_ID = "_id";
	private static Account account = new Account();
	
	private static final String DATABASE_CREATE = "create table "
		      + TABLE_ACCOUNTS + "(" 
		      + COLUMN_ID + " integer primary key autoincrement,"
		      + account.isActive() + " integer not null,"
		      + account.getCreateTime() + " datetime not null,"
		      + account.getLastUpdate() + " datetime not null,"
		      + account.getOwner() + " text not null,"
		      + account.getIban() + " text not null,"
		      + account.getBic() + " text not null,"
		      + account.getPin() + " text not null"
		      +");";


	public static String createAccountTable() {
		return DATABASE_CREATE;
	}

	public static String upgradeAccountTable() {
		return "DROP TABLE IF EXISTS" + TABLE_ACCOUNTS;

	}

}
