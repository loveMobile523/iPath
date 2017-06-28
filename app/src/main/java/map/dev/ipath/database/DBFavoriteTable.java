package map.dev.ipath.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import map.dev.ipath.model.DBFavorite;
import map.dev.ipath.model.DBRate;

/**
 * Created by adrian on 03.04.2017.
 */

public class DBFavoriteTable extends SQLiteOpenHelper {
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    public static final String DATABASE_NAME = "IPathDBFavorite";

    // Accounts table name
    public static final String TABLE_FAVORITES= "favorites";

    // Accounts Table Columns names
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_FAVORITE_PLACE_ID = "place_id";
    public static final String COLUMN_FAVORITE_PLACE_NAME = "placename";
    public static final String COLUMN_FAVORITE_UPDATED = "updated";

    // Database fields
    private SQLiteDatabase database;
    private String[] allColumns = {
            COLUMN_ID,
            COLUMN_FAVORITE_PLACE_ID,
            COLUMN_FAVORITE_PLACE_NAME,
            COLUMN_FAVORITE_UPDATED
    };


    // ---------------------------------------------------------------------------------------------

    public void close() {
        this.close();
    }

    public void open() throws SQLException {
        database = this.getWritableDatabase();
    }

    // ---------------------------------------------------------------------------------------------
    public DBFavoriteTable(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "create table "
                + TABLE_FAVORITES + "( " + COLUMN_ID
                + " integer primary key autoincrement, " + COLUMN_FAVORITE_PLACE_ID
                + " text, " + COLUMN_FAVORITE_PLACE_NAME
                + " text, " + COLUMN_FAVORITE_UPDATED
                + " text);";

        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);

        // Create tables again
        onCreate(db);
    }

    // Adding new favorite
    public DBFavorite addFavorite(DBFavorite dbFavorite) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_FAVORITE_PLACE_ID, dbFavorite.getPlace_id());
        values.put(COLUMN_FAVORITE_PLACE_NAME, dbFavorite.getPlacename());
        values.put(COLUMN_FAVORITE_UPDATED, dbFavorite.getUpdated());

        long insertId = db/*database*/.insert(TABLE_FAVORITES, null,
                values);
        Cursor cursor = db/*database*/.query(TABLE_FAVORITES,
                allColumns, COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        DBFavorite newFavorite = cursorToFavorite(cursor);
        cursor.close();

        db.close();
        return newFavorite;
    }

    // Getting single favorite
    public DBFavorite getFavorite(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_FAVORITES, allColumns, COLUMN_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        DBFavorite dbFavorite = new DBFavorite(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
        db.close();
        return dbFavorite;
    }

    // Getting  single favorite
    public DBFavorite getFavoriteByPlaceId(String placeId) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_FAVORITES, allColumns, COLUMN_FAVORITE_PLACE_ID + "=?",
                new String[] { placeId }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        DBFavorite dbFavorite = new DBFavorite(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
        db.close();
        return dbFavorite;
    }


    // Getting All favorites
    public List<DBFavorite> getAllFavorites() {
        List<DBFavorite> accountList = new ArrayList<DBFavorite>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_FAVORITES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DBFavorite dbFavorite = new DBFavorite();
                dbFavorite = cursorToFavorite(cursor);
                // Adding place to list
                accountList.add(dbFavorite);
            } while (cursor.moveToNext());
        }

        // return account list
        return accountList;
    }

    // Getting favorites Count
    public int getFavoritesCount() {
        String countQuery = "SELECT  * FROM " + TABLE_FAVORITES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        return cursor.getCount();
    }

    // Updating single favorite
    public int updateFavorite(DBFavorite dbFavorite) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, dbFavorite.getId());
        values.put(COLUMN_FAVORITE_PLACE_ID, dbFavorite.getPlace_id());
        values.put(COLUMN_FAVORITE_PLACE_NAME, dbFavorite.getPlacename());
        values.put(COLUMN_FAVORITE_UPDATED, dbFavorite.getUpdated());

        // updating row
        return db.update(TABLE_FAVORITES, values, COLUMN_ID + " = ?",
                new String[] { String.valueOf(dbFavorite.getId()) });
    }

    // Deleting single favorite
    public void deleteRate(DBFavorite dbFavorite) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FAVORITES, COLUMN_ID + " = ?",
                new String[] { String.valueOf(dbFavorite.getId()) });
        db.close();
    }

    private DBFavorite cursorToFavorite(Cursor cursor) {
        DBFavorite dbFavorite = new DBFavorite();
        dbFavorite.setId(cursor.getLong(0));
        dbFavorite.setPlace_id(cursor.getString(1));
        dbFavorite.setPlacename(cursor.getString(2));
        dbFavorite.setUpdated(cursor.getString(3));

        return dbFavorite;
    }
}
