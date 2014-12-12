package de.davidartmann.artmannwiki.android.database;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import de.davidartmann.artmannwiki.android.model.Account;

/**
 * 
 * {@link Account} model helper class.
 *
 */
public class AccountManager {
	
	private DBManager dbManager;
	private SQLiteDatabase db;
	
	private static final String TABLE_ACCOUNT = "account";
	private static final String COLUMN_OWNER = "owner";
	private static final String COLUMN_IBAN = "iban";
	private static final String COLUMN_BIC = "bic";
	private static final String COLUMN_PIN = "pin";
//	private String[] ALL_COLUMNS = { DBManager.COLUMN_ID, DBManager.COLUMN_ACTIVE, 
//			DBManager.COLUMN_CREATETIME, DBManager.COLUMN_LASTUPDATE, COLUMN_OWNER, COLUMN_IBAN, COLUMN_BIC, COLUMN_PIN };
	
	private static final String CREATE_TABLE_ACCOUNT = "create table "
		      + TABLE_ACCOUNT + "(" 
		      + DBManager.COLUMN_ID + " integer primary key autoincrement,"
		      + DBManager.COLUMN_ACTIVE + " integer not null,"
		      + DBManager.COLUMN_CREATETIME + " datetime not null,"
		      + DBManager.COLUMN_LASTUPDATE + " datetime,"
		      + COLUMN_OWNER + " text not null,"
		      + COLUMN_IBAN + " text not null,"
		      + COLUMN_BIC + " text not null,"
		      + COLUMN_PIN + " text not null"
		      +");";
	
	/**
	 * Constructor with the actual context for the DBManager
	 * @param context
	 */
	public AccountManager(Context c) {
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
	public static String createAccountTable() {
		return CREATE_TABLE_ACCOUNT;
	}
	
	//gets called from the DBManager#onUpdate()
	public static String upgradeAccountTable() {
		return "DROP TABLE IF EXISTS" + TABLE_ACCOUNT;
	}
	
	/**
	 * Method to get a new {@link Account} by his <u>id</u> from the database.
	 * @param id ({@link Long})
	 * @return {@link Account}
	 */
	public Account getAccountById(long id) {
		Cursor cursor = db.query(TABLE_ACCOUNT, null, DBManager.COLUMN_ID + "=?", new String[] {String.valueOf(id)}, null, null, null);
		//always place the cursor to the first element, before accessing
		cursor.moveToFirst();
		Account account = accountFromCursor(cursor);
		cursor.close();
		return account;
	}
	
	/**
	 * Method to get a new {@link Account} by his <u>iban</u> from the database.
	 * @param iban ({@link String})
	 * @return {@link Account}
	 */
	public Account getAccountByIban(String iban) {
		Cursor cursor = db.query(TABLE_ACCOUNT, null, COLUMN_IBAN + "=" + iban, null, null, null, null);
		cursor.moveToFirst();
		Account account = accountFromCursor(cursor);
		cursor.close();
		return account;
	}
	
	/**
	 * Method to store a new {@link Account} in the database.
	 * @param account
	 * @return {@link Account}
	 */
	public Account addAccount(Account account) {
		account.setCreateTime(new Date());
		ContentValues values = fillContenValuesWithNewAccountData(account);
		long insertId = db.insert(TABLE_ACCOUNT, null, values);
		Cursor cursor = db.query(TABLE_ACCOUNT, null, DBManager.COLUMN_ID + "=?", new String[] {String.valueOf(insertId)}, null, null, null);
		cursor.moveToFirst();
		Account toReturn = accountFromCursor(cursor);
		cursor.close();
		return toReturn;
	}
	
	/**
	 * Method to <u>fully delete</u> an {@link Account}.
	 * @param account
	 * @return true if account could be deleted, false otherwise
	 */
	public boolean fullDeleteAccount(Account account) {
		long id = account.getId();
		return db.delete(TABLE_ACCOUNT, DBManager.COLUMN_ID + "=" + id, null) > 0;
	}
	
	/**
	 * Method to <u>soft delete</u> an {@link Account}.
	 * This means the active status is set to false.
	 * @param account
	 */
	public boolean softDeleteAccount(Account account) {
		long id = account.getId();
		Account toDeactive = getAccountById(id);
		if (toDeactive != null) {
			toDeactive.setActive(false);
			updateAccount(toDeactive);
			return true;
		}
		return false;
	}
	
	/**
	 * Method to retrieve all {@link Account}s from the database.
	 * @return {@link List} with {@link Account}s
	 */
	public List<Account> getAllAccounts() {
		List<Account> accountList = new ArrayList<Account>();
		//columns parameter(second one) is also null because then all columns get returned
		//and thats necessary, so the accountFromCursor() method works correctly
		Cursor cursor = db.query(TABLE_ACCOUNT, null, null, null, null, null, null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast()) {
			Account account = accountFromCursor(cursor);
			if (account.isActive()) {
				accountList.add(account);
			}
			cursor.moveToNext();
		}
        cursor.close();
        return accountList;
	}
	
	/**
	 * Method to update an existing {@link Account}.
	 * @param account
	 * @return {@link Account}
	 */
	public Account updateAccount(Account account) {
		long accountId = account.getId();
		account.setLastUpdate(new Date());
		ContentValues contentValues = fillContentValuesWithUpdatedAccountData(account);
		db.update(TABLE_ACCOUNT, contentValues, DBManager.COLUMN_ID + "=" + accountId, null);
		Cursor cursor = db.query(TABLE_ACCOUNT, null, DBManager.COLUMN_ID + "=" + accountId, null, null, null, null);
		cursor.moveToFirst();
		Account returnAccount = accountFromCursor(cursor);
		cursor.close();
		return returnAccount;
	}
	
	/**
	 * Method to get a new {@link Account} instance out of a cursor element.
	 * @param cursor
	 * @return {@link Account}
	 */
	public Account accountFromCursor(Cursor cursor) {
		Account account = new Account();
		account.setId(cursor.getLong(0));
		account.setActive(cursor.getInt(1) == 0 ? false : true);
		account.setCreateTime(new Date(cursor.getLong(2)));
		account.setLastUpdate(new Date(cursor.getLong(3)));
		account.setOwner(cursor.getString(4));
		account.setIban(cursor.getString(5));
		account.setBic(cursor.getString(6));
		account.setPin(cursor.getString(7));
		return account;
	}
	
	/**
	 * Helper method, because the {@link SQLiteDatabase} insert method needs ContentValues.
	 * @param account
	 * @return {@link ContentValues}
	 */
	public ContentValues fillContenValuesWithNewAccountData(Account account) {
		ContentValues values = new ContentValues();
		values.put(DBManager.COLUMN_ACTIVE, account.isActive() == false ? 0 : 1);
		values.put(DBManager.COLUMN_CREATETIME, account.getCreateTime().getTime());
		values.put(COLUMN_OWNER, account.getOwner());
		values.put(COLUMN_IBAN, account.getIban());
		values.put(COLUMN_BIC, account.getBic());
		values.put(COLUMN_PIN, account.getPin());
		return values;
	}
	
	/**
	 * Helper method, because the {@link SQLiteDatabase} update method needs ContentValues.
	 * @param account
	 * @return {@link ContentValues}
	 */
	public ContentValues fillContentValuesWithUpdatedAccountData(Account account) {
		ContentValues values = new ContentValues();
		values.put(DBManager.COLUMN_ACTIVE, account.isActive() == false ? 0 : 1);
		values.put(DBManager.COLUMN_LASTUPDATE, account.getLastUpdate().getTime());
		values.put(COLUMN_OWNER, account.getOwner());
		values.put(COLUMN_IBAN, account.getIban());
		values.put(COLUMN_BIC, account.getBic());
		values.put(COLUMN_PIN, account.getPin());
		return values;
	}

}
