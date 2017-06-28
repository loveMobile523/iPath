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

/**
 * Created by adrian on 30.03.2017.
 */

public class DBPlaceTable extends SQLiteOpenHelper{
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    public static final String DATABASE_NAME = "IPathDB";

    // Accounts table name
    public static final String TABLE_PLACES= "places";

    // Accounts Table Columns names
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PLACE_ID = "place_id";
    public static final String COLUMN_PLACE_NAME = "name";
    public static final String COLUMN_PLACE_CATEGORY_NAME = "category_name";
    public static final String COLUMN_PLACE_LATITUDE = "latitude";
    public static final String COLUMN_PLACE_LONGITUDE = "longitude";
    public static final String COLUMN_PLACE_PHONE = "phone";
    public static final String COLUMN_PLACE_EMAIL = "email";
    public static final String COLUMN_PLACE_ADDRESS = "address";
    public static final String COLUMN_PLACE_RATING = "rating";
    public static final String COLUMN_PLACE_UPDATED = "updated";


    // Database fields
    private SQLiteDatabase database;
    private String[] allColumns = {
            COLUMN_ID,
            COLUMN_PLACE_ID,
            COLUMN_PLACE_NAME,
            COLUMN_PLACE_CATEGORY_NAME,
            COLUMN_PLACE_LATITUDE,
            COLUMN_PLACE_LONGITUDE,
            COLUMN_PLACE_PHONE,
            COLUMN_PLACE_EMAIL,
            COLUMN_PLACE_ADDRESS,
            COLUMN_PLACE_RATING,
            COLUMN_PLACE_UPDATED
    };


    // ---------------------------------------------------------------------------------------------

    public void close() {
        this.close();
    }

    public void open() throws SQLException {
        database = this.getWritableDatabase();
    }

    // ---------------------------------------------------------------------------------------------
    public DBPlaceTable(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "create table "
                + TABLE_PLACES + "( " + COLUMN_ID
                + " integer primary key autoincrement, " + COLUMN_PLACE_ID
                + " text, " + COLUMN_PLACE_NAME
                + " text not null, " + COLUMN_PLACE_CATEGORY_NAME
                + " text not null, " + COLUMN_PLACE_LATITUDE
                + " text, " + COLUMN_PLACE_LONGITUDE
                + " text, " + COLUMN_PLACE_PHONE
                + " text, " + COLUMN_PLACE_EMAIL
                + " text, " + COLUMN_PLACE_ADDRESS
                + " text, " + COLUMN_PLACE_RATING
                + " text, " + COLUMN_PLACE_UPDATED
                + " text);";

        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    public void dropTable() {
        SQLiteDatabase db = this.getWritableDatabase();

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLACES);

        // Create tables again
        onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLACES);

        // Create tables again
        onCreate(db);
    }

    // Adding new place
    public DBPlace addPlace(DBPlace dbPlace) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_PLACE_ID, dbPlace.getPlace_id());
        values.put(COLUMN_PLACE_NAME, dbPlace.getName());
        values.put(COLUMN_PLACE_CATEGORY_NAME, dbPlace.getCategory_name());
        values.put(COLUMN_PLACE_LATITUDE, dbPlace.getLatitude());
        values.put(COLUMN_PLACE_LONGITUDE, dbPlace.getLongitude());
        values.put(COLUMN_PLACE_PHONE, dbPlace.getPhone());
        values.put(COLUMN_PLACE_EMAIL, dbPlace.getEmail());
        values.put(COLUMN_PLACE_ADDRESS, dbPlace.getAddress());
        values.put(COLUMN_PLACE_RATING, dbPlace.getRating());
        values.put(COLUMN_PLACE_UPDATED, dbPlace.getUpdated());

        long insertId = db/*database*/.insert(TABLE_PLACES, null,
                values);
        Cursor cursor = db/*database*/.query(TABLE_PLACES,
                allColumns, COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        DBPlace newPlace = cursorToPlace(cursor);
        cursor.close();

        db.close();
        return newPlace;
    }

    // Getting single place
    public DBPlace getPlace(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PLACES, allColumns, COLUMN_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        DBPlace dbPlace = new DBPlace(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getString(3),
                cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8),
                cursor.getString(9), cursor.getString(10));
        db.close();
        return dbPlace;
    }

    // Getting single place
    public DBPlace getPlaceByPlaceId(String placeId) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PLACES, allColumns, COLUMN_PLACE_ID + "=?",
                new String[] { placeId }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        DBPlace dbPlace = new DBPlace(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getString(3),
                cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8),
                cursor.getString(9), cursor.getString(10));
        db.close();
        return dbPlace;
    }

    // Getting single place
    public DBPlace getPlaceByPlaceName(String placeName) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PLACES, allColumns, COLUMN_PLACE_NAME + "=?",
                new String[] { placeName }, null, null, null, null);

        if (cursor == null) {
            return null;
        }

        if (cursor.getCount() == 0) {
            return null;
        }

        if (cursor != null)
            cursor.moveToFirst();


        DBPlace dbPlace = new DBPlace(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getString(3),
                cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8),
                cursor.getString(9), cursor.getString(10));
        db.close();
        return dbPlace;
    }

    // Getting All Places
    public List<DBPlace> getAllPlaces() {
        List<DBPlace> accountList = new ArrayList<DBPlace>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PLACES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DBPlace dbPlace = new DBPlace();
                dbPlace = cursorToPlace(cursor);
                // Adding place to list
                accountList.add(dbPlace);
            } while (cursor.moveToNext());
        }

        // return account list
        return accountList;
    }

    // Getting places Count
    public int getPlacesCount() {
        String countQuery = "SELECT  * FROM " + TABLE_PLACES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        return cursor.getCount();
    }

    // Updating single place
    public int updatePlace(DBPlace dbPlace) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, dbPlace.getId());
        values.put(COLUMN_PLACE_ID, dbPlace.getPlace_id());
        values.put(COLUMN_PLACE_NAME, dbPlace.getName());
        values.put(COLUMN_PLACE_CATEGORY_NAME, dbPlace.getCategory_name());
        values.put(COLUMN_PLACE_LATITUDE, dbPlace.getLatitude());
        values.put(COLUMN_PLACE_LONGITUDE, dbPlace.getLongitude());
        values.put(COLUMN_PLACE_PHONE, dbPlace.getPhone());
        values.put(COLUMN_PLACE_EMAIL, dbPlace.getEmail());
        values.put(COLUMN_PLACE_ADDRESS, dbPlace.getAddress());
        values.put(COLUMN_PLACE_RATING, dbPlace.getRating());
        values.put(COLUMN_PLACE_UPDATED, dbPlace.getUpdated());

        // updating row
        return db.update(TABLE_PLACES, values, COLUMN_PLACE_ID + " = ?",
                new String[] { /*String.valueOf(dbPlace.getId())*/dbPlace.getPlace_id() });
    }

    // Deleting single place
    public void deletePlace(DBPlace dbPlace) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PLACES, COLUMN_ID + " = ?",
                new String[] { String.valueOf(dbPlace.getId()) });
        db.close();
    }

    private DBPlace cursorToPlace(Cursor cursor) {
        DBPlace dbPlace = new DBPlace();
        dbPlace.setId(cursor.getLong(0));
        dbPlace.setPlace_id(cursor.getString(1));
        dbPlace.setName(cursor.getString(2));
        dbPlace.setCategory_name(cursor.getString(3));
        dbPlace.setLatitude(cursor.getString(4));
        dbPlace.setLongitude(cursor.getString(5));
        dbPlace.setPhone(cursor.getString(6));
        dbPlace.setEmail(cursor.getString(7));
        dbPlace.setAddress(cursor.getString(8));
        dbPlace.setRating(cursor.getString(9));
        dbPlace.setUpdated(cursor.getString(10));
        return dbPlace;
    }
}
