package map.dev.ipath.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import map.dev.ipath.helper.MySQLiteHelper;
import map.dev.ipath.model.DBPlace;
import map.dev.ipath.model.DBRate;

/**
 * Created by adrian on 03.04.2017.
 */

public class DBRatingTable extends SQLiteOpenHelper {
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    public static final String DATABASE_NAME = "IPathDBRate";

    // Accounts table name
    public static final String TABLE_RATES= "rates";

    // Accounts Table Columns names
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_RATE_USERNAME = "username";
    public static final String COLUMN_RATE_PLACE_ID = "place_id";
    public static final String COLUMN_RATE_PLACE_NAME = "place_name";
    public static final String COLUMN_RATE_CONTENT = "content";
    public static final String COLUMN_RATE_VALUE = "value";
    public static final String COLUMN_RATE_UPDATED = "updated";

    // Database fields
    private SQLiteDatabase database;
    private String[] allColumns = {
            COLUMN_ID,
            COLUMN_RATE_USERNAME,
            COLUMN_RATE_PLACE_ID,
            COLUMN_RATE_PLACE_NAME,
            COLUMN_RATE_CONTENT,
            COLUMN_RATE_VALUE,
            COLUMN_RATE_UPDATED
    };


    // ---------------------------------------------------------------------------------------------

    public void close() {
        this.close();
    }

    public void open() throws SQLException {
        database = this.getWritableDatabase();
    }

    // ---------------------------------------------------------------------------------------------
    public DBRatingTable(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "create table "
                + TABLE_RATES + "( " + COLUMN_ID
                + " integer primary key autoincrement, " + COLUMN_RATE_USERNAME
                + " text, " + COLUMN_RATE_PLACE_ID
                + " text, " + COLUMN_RATE_PLACE_NAME
                + " text, " + COLUMN_RATE_CONTENT
                + " text, " + COLUMN_RATE_VALUE
                + " text, " + COLUMN_RATE_UPDATED
                + " text);";

        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    public void dropTable() {
        SQLiteDatabase db = this.getWritableDatabase();

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RATES);

        // Create tables again
        onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RATES);

        // Create tables again
        onCreate(db);
    }

    // Adding new rate
    public DBRate addRate(DBRate dbRate) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_RATE_USERNAME, dbRate.getUsername());
        values.put(COLUMN_RATE_PLACE_ID, dbRate.getPlace_id());
        values.put(COLUMN_RATE_PLACE_NAME, dbRate.getPlace_name());
        values.put(COLUMN_RATE_CONTENT, dbRate.getContent());
        values.put(COLUMN_RATE_VALUE, dbRate.getValue());
        values.put(COLUMN_RATE_UPDATED, dbRate.getUpdated());

        long insertId = db/*database*/.insert(TABLE_RATES, null,
                values);
        Cursor cursor = db/*database*/.query(TABLE_RATES,
                allColumns, COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        DBRate newRate = cursorToRate(cursor);
        cursor.close();

        db.close();
        return newRate;
    }

    // Getting single rate
    public DBRate getRate(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_RATES, allColumns, COLUMN_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        DBRate dbRate = new DBRate(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getString(3),
                cursor.getString(4), cursor.getString(5), cursor.getString(6));
        db.close();
        return dbRate;
    }

    // Getting  rates
    public List<DBRate> getRatesByPlaceId(String placeId) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<DBRate> accountList = new ArrayList<DBRate>();

        Cursor cursor = db.query(TABLE_RATES, allColumns, COLUMN_RATE_PLACE_ID + "=?",
                new String[] { placeId }, null, null, null, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DBRate dbRate = new DBRate();
                dbRate = cursorToRate(cursor);
                // Adding place to list
                accountList.add(dbRate);
            } while (cursor.moveToNext());
        }

        // return account list
        return accountList;
    }

    // Getting  rates
    public List<DBRate> getRatesByPlaceName(String placeName) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<DBRate> accountList = new ArrayList<DBRate>();

        Cursor cursor = db.query(TABLE_RATES, allColumns, COLUMN_RATE_PLACE_NAME + "=?",
                new String[] { placeName }, null, null, null, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DBRate dbRate = new DBRate();
                dbRate = cursorToRate(cursor);
                // Adding place to list
                accountList.add(dbRate);
            } while (cursor.moveToNext());
        }

        // return account list
        return accountList;
    }

    // Getting  rates
    public List<DBRate> getRatesByUserName(String userName) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<DBRate> accountList = new ArrayList<DBRate>();

        Cursor cursor = db.query(TABLE_RATES, allColumns, COLUMN_RATE_USERNAME + "=?",
                new String[] { userName }, null, null, null, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DBRate dbRate = new DBRate();
                dbRate = cursorToRate(cursor);
                // Adding place to list
                accountList.add(dbRate);
            } while (cursor.moveToNext());
        }

        // return account list
        return accountList;
    }

    // Getting All rates
    public List<DBRate> getAllRates() {
        List<DBRate> accountList = new ArrayList<DBRate>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_RATES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DBRate dbRate = new DBRate();
                dbRate = cursorToRate(cursor);
                // Adding place to list
                accountList.add(dbRate);
            } while (cursor.moveToNext());
        }

        // return account list
        return accountList;
    }

    // Getting rates Count
    public int getRatesCount() {
        String countQuery = "SELECT  * FROM " + TABLE_RATES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        return cursor.getCount();
    }

    // Updating single rate
    public int updateRate(DBRate dbRate) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, dbRate.getId());
        values.put(COLUMN_RATE_USERNAME, dbRate.getUsername());
        values.put(COLUMN_RATE_PLACE_ID, dbRate.getPlace_id());
        values.put(COLUMN_RATE_PLACE_NAME, dbRate.getPlace_name());
        values.put(COLUMN_RATE_CONTENT, dbRate.getContent());
        values.put(COLUMN_RATE_VALUE, dbRate.getValue());
        values.put(COLUMN_RATE_UPDATED, dbRate.getUpdated());

        // updating row
        return db.update(TABLE_RATES, values, COLUMN_ID + " = ?",
                new String[] { String.valueOf(dbRate.getId()) });
    }

    // Deleting single rate
    public void deleteRate(DBRate dbRate) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RATES, COLUMN_ID + " = ?",
                new String[] { String.valueOf(dbRate.getId()) });
        db.close();
    }

    private DBRate cursorToRate(Cursor cursor) {
        DBRate dbRate = new DBRate();
        dbRate.setId(cursor.getLong(0));
        dbRate.setUsername(cursor.getString(1));
        dbRate.setPlace_id(cursor.getString(2));
        dbRate.setPlace_name(cursor.getString(3));
        dbRate.setContent(cursor.getString(4));
        dbRate.setValue(cursor.getString(5));
        dbRate.setUpdated(cursor.getString(6));

        return dbRate;
    }
}
