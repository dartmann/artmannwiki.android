package checkIfDeletable;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by David on 28.08.2014.
 */
public class DBManager extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "db_artmannwiki";
    private static final String TABLE_NAME = "table_artmannwiki";
    private static final String KEY_ID = "id";
    private static final String KEY_BENUTZER = "benutzer";
    private static final String KEY_PASSWORT = "passwort";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_DEVICE = "device";
    private static final String KEY_NUMBER = "number";
    private static final String KEY_PIN = "pin";
    private static final String KEY_PUK = "puk";
    private static final String KEY_KATEGORIE = "kategorie";
    private static final String KEY_INFO = "info";
    //Kategorie Strings:
    private static final String KATEGORIE_LOGIN = "Login";
    private static final String KATEGORIE_EMAIL = "E-Mail";
    private static final String KATEGORIE_DEVICE = "Handy/Tablet";

    public DBManager(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        //Bedenke, bei Spaltenänderung, die APP neu auf dem Gerät zu installieren!
        String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                "(" +
                KEY_ID + " INTEGER PRIMARY KEY," +
                KEY_BENUTZER + " VARCHAR(30)," +
                KEY_PASSWORT + " VARCHAR(30)," +
                KEY_EMAIL + " VARCHAR(50)," +
                KEY_DEVICE + " VARCHAR(40)," +
                KEY_NUMBER + " VARCHAR(20)," +
                KEY_PIN + " VARCHAR(4)," +
                KEY_PUK + " VARCHAR(8)," +
                KEY_KATEGORIE + " VARCHAR(20) NOT NULL," +
                KEY_INFO + " VARCHAR(30)" +
                ");";
        db.execSQL(CREATE_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_NAME);
        onCreate(db);
    }

    public void addEntity(Entity entity) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_BENUTZER, entity.get_benutzer());
            values.put(KEY_PASSWORT, entity.get_password());
            values.put(KEY_EMAIL, entity.get_email());
            values.put(KEY_DEVICE, entity.get_device());
            values.put(KEY_NUMBER, entity.get_number());
            values.put(KEY_PIN, entity.get_pin());
            values.put(KEY_PUK, entity.get_puk());
            values.put(KEY_KATEGORIE, entity.get_kategorie());
            values.put(KEY_INFO, entity.get_info());
            db.insert(TABLE_NAME, null, values);
            db.close();
        }
        catch (Exception e) {
            Log.d("@addEntity()", e.getStackTrace().toString());
        }
    }

    public void addLogin(Entity entity) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_BENUTZER, entity.get_benutzer());
            values.put(KEY_PASSWORT, entity.get_password());
            values.put(KEY_KATEGORIE, KATEGORIE_LOGIN);
            values.put(KEY_INFO, entity.get_info());
            db.insert(TABLE_NAME, null, values);
            db.close();
        }
        catch (Exception e) {
            Log.d("@addLogin()", e.getStackTrace().toString());
        }
    }

    public void addDevice(Entity entity) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_DEVICE, entity.get_device());
            values.put(KEY_NUMBER, entity.get_number());
            values.put(KEY_PIN, entity.get_pin());
            values.put(KEY_PUK, entity.get_puk());
            values.put(KEY_KATEGORIE, KATEGORIE_DEVICE);
            db.insert(TABLE_NAME, null, values);
            db.close();
        }
        catch (Exception e) {
            Log.d("@addDevice()", e.getStackTrace().toString());
        }
    }

    public void addEmail(Entity entity) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_EMAIL, entity.get_email());
            values.put(KEY_PASSWORT, entity.get_password());
            values.put(KEY_KATEGORIE, KATEGORIE_EMAIL);
            db.insert(TABLE_NAME, null, values);
            db.close();
        }
        catch (Exception e) {
            Log.d("@addEmail()", e.getStackTrace().toString());
        }
    }

    public Entity getEntity(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[] {KEY_ID, KEY_BENUTZER, KEY_PASSWORT,
                        KEY_EMAIL, KEY_DEVICE, KEY_NUMBER, KEY_PIN, KEY_PUK, KEY_KATEGORIE}, KEY_ID + "=?",
                new String[] {String.valueOf(id)}, null, null, null, null);
        Entity entity = null;
        if(cursor != null) {
            cursor.moveToFirst();
            entity = new Entity(Integer.parseInt(cursor.getString(0)),cursor.getString(1),cursor.getString(2),
                    cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6),
                    cursor.getString(7),cursor.getString(8),cursor.getString(9));
        }
        return entity;
    }

    public ArrayList<Entity> getAllEntities() {
        ArrayList<Entity> entityList = new ArrayList<Entity>();
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()) {
            do {
                Entity entity = new Entity();
                entity.set_id(Integer.parseInt(cursor.getString(0)));
                entity.set_benutzer(cursor.getString(1));
                entity.set_password(cursor.getString(2));
                entity.set_email(cursor.getString(3));
                entity.set_device(cursor.getString(4));
                entity.set_number(cursor.getString(5));
                entity.set_pin(cursor.getString(6));
                entity.set_puk(cursor.getString(7));
                entity.set_kategorie(cursor.getString(8));
                entity.set_info(cursor.getString(9));
                entityList.add(entity);
            } while (cursor.moveToNext());
        }
        return entityList;
    }

    public ArrayList<String> getAllKategories() {
        ArrayList<String> kategorieList = new ArrayList<String>();
        String query = "SELECT DISTINCT "+KEY_KATEGORIE+" FROM "+TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()) {
            do {
                kategorieList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        return kategorieList;
    }

    public void deleteEntity(Entity entity) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " = ?", new String[] {String.valueOf(entity.get_id())});
        db.close();
    }

    public int updateEntity(Entity entity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_BENUTZER, entity.get_benutzer());
        values.put(KEY_PASSWORT, entity.get_password());
        values.put(KEY_EMAIL, entity.get_email());
        values.put(KEY_DEVICE, entity.get_device());
        values.put(KEY_NUMBER, entity.get_number());
        values.put(KEY_PIN, entity.get_pin());
        values.put(KEY_PUK, entity.get_puk());
        values.put(KEY_KATEGORIE, entity.get_kategorie());
        values.put(KEY_INFO, entity.get_info());
        return db.update(TABLE_NAME, values, KEY_ID + " = ?", new String[] {String.valueOf(entity.get_id())});
    }

    public int getEntityAmount() {
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        return cursor.getCount();
    }

    public String getKategorieLogin() {
        return KATEGORIE_LOGIN;
    }

    public String getKategorieEmail() {
        return KATEGORIE_EMAIL;
    }

    public String getKategorieDevice() {
        return KATEGORIE_DEVICE;
    }

}
