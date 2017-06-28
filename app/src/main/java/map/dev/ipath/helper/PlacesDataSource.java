package map.dev.ipath.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.SQLException;
import android.os.StrictMode;

import java.util.ArrayList;
import java.util.List;

import map.dev.ipath.model.DBPlace;

/**
 * Created by adrian on 29.03.2017.
 */

public class PlacesDataSource {
    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = {
            MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_PLACE_NAME,
            MySQLiteHelper.COLUMN_PLACE_CATEGORY_NAME,
            MySQLiteHelper.COLUMN_PLACE_LATITUDE,
            MySQLiteHelper.COLUMN_PLACE_LONGITUDE,
            MySQLiteHelper.COLUMN_PLACE_PHONE,
            MySQLiteHelper.COLUMN_PLACE_EMAIL,
            MySQLiteHelper.COLUMN_PLACE_ADDRESS,
            MySQLiteHelper.COLUMN_PLACE_RATING
    };

    public PlacesDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public DBPlace createPlace(String name, String category_name, String latitude, String longitude,
                               String phone, String email, String address, String rating) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_PLACE_NAME, name);
        values.put(MySQLiteHelper.COLUMN_PLACE_CATEGORY_NAME, category_name);
        values.put(MySQLiteHelper.COLUMN_PLACE_LATITUDE, latitude);
        values.put(MySQLiteHelper.COLUMN_PLACE_LONGITUDE, longitude);
        values.put(MySQLiteHelper.COLUMN_PLACE_PHONE, phone);
        values.put(MySQLiteHelper.COLUMN_PLACE_EMAIL, email);
        values.put(MySQLiteHelper.COLUMN_PLACE_ADDRESS, address);
        values.put(MySQLiteHelper.COLUMN_PLACE_RATING, rating);
        long insertId = database.insert(MySQLiteHelper.TABLE_PLACES, null,
                values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_PLACES,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        DBPlace newPlace = cursorToPlace(cursor);
        cursor.close();
        return newPlace;
    }

    public void deletePlace(DBPlace dbPlace) {
        long id = dbPlace.getId();
        System.out.println("Place deleted with id: " + id);
        database.delete(MySQLiteHelper.TABLE_PLACES, MySQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public List<DBPlace> getAllPlaces() {
        List<DBPlace> dbPlaces = new ArrayList<DBPlace>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_PLACES,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            DBPlace dbPlace = cursorToPlace(cursor);
            dbPlaces.add(dbPlace);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return dbPlaces;
    }

    private DBPlace cursorToPlace(Cursor cursor) {
        DBPlace dbPlace = new DBPlace();
        dbPlace.setId(cursor.getLong(0));
        dbPlace.setName(cursor.getString(1));
        dbPlace.setCategory_name(cursor.getString(2));
        dbPlace.setLatitude(cursor.getString(3));
        dbPlace.setLongitude(cursor.getString(4));
        dbPlace.setPhone(cursor.getString(5));
        dbPlace.setEmail(cursor.getString(6));
        dbPlace.setAddress(cursor.getString(7));
        dbPlace.setRating(cursor.getString(8));
        return dbPlace;
    }
}
