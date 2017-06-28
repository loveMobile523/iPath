package map.dev.ipath.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by adrian on 29.03.2017.
 */

public class MySQLiteHelper extends SQLiteOpenHelper {
    public static final String TABLE_PLACES= "places";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PLACE_NAME = "name";
    public static final String COLUMN_PLACE_CATEGORY_NAME = "category_name";
    public static final String COLUMN_PLACE_LATITUDE = "latitude";
    public static final String COLUMN_PLACE_LONGITUDE = "longitude";
    public static final String COLUMN_PLACE_PHONE = "phone";
    public static final String COLUMN_PLACE_EMAIL = "email";
    public static final String COLUMN_PLACE_ADDRESS = "address";
    public static final String COLUMN_PLACE_RATING = "rating";

    private static final String DATABASE_NAME = "ipath.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_PLACES + "( " + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_PLACE_NAME
            + " text not null, " + COLUMN_PLACE_CATEGORY_NAME
            + " text not null, " + COLUMN_PLACE_LATITUDE
            + " text, " + COLUMN_PLACE_LONGITUDE
            + " text, " + COLUMN_PLACE_PHONE
            + " text, " + COLUMN_PLACE_EMAIL
            + " text, " + COLUMN_PLACE_ADDRESS
            + " text, " + COLUMN_PLACE_RATING
            + " text);";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLACES);
        onCreate(db);
    }
}
