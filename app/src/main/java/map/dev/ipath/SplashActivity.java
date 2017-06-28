package map.dev.ipath;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.KeyEvent;

import map.dev.ipath.constant.Constant;
import map.dev.ipath.constant.DataOfDB;
import map.dev.ipath.database.DBPlaceTable;
import map.dev.ipath.model.DBPlace;
import map.dev.ipath.model.FilterPlaceModel;

//import com.example.mapdemo.R;

//import map.dev.ipath.R;

/**
 * Created by adrian on 11.03.2017.
 */

public class SplashActivity extends Activity{
    /** Check if the app is running. */
    private boolean isRunning;

    private SharedPreferences.Editor editor;
    private SharedPreferences prefs;

    private boolean judge;

    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        editor = getSharedPreferences(Constant.MyShared, MODE_PRIVATE).edit();
        prefs = getSharedPreferences(Constant.MyShared, MODE_PRIVATE);

        judge = prefs.getBoolean(Constant.firstRun, false);


        editor.putString(Constant.FromTag, Constant.FromSplash);
        editor.commit();

        // -----------------------------------------------------------------------------------------
        isRunning = true;

        startSplash();

    }

    /**
     * Starts the count down timer for 3-seconds. It simply sleeps the thread
     * for 3-seconds.
     */
    private void startSplash()
    {
        new Thread(new Runnable() {
            @Override
            public void run()
            {
                try
                {
                    Thread.sleep(1000);

                } catch (Exception e)
                {
                    e.printStackTrace();
                } finally
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run()
                        {
                            doFinish();
                        }
                    });
                }
            }
        }).start();
    }

    /**
     * If the app is still running than this method will start the Login
     * activity and finish the Splash.
     */
    private synchronized void doFinish()
    {

        if (isRunning)
        {
            Intent i;
            isRunning = false;

            if(judge){
                i = new Intent(SplashActivity.this, MainActivity.class);
            } else {
                i = new Intent(SplashActivity.this, SignUpActivity.class);
//                firstInint();
            }

            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        }
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {

        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            isRunning = false;
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void firstInint(){
        addPlaces(DataOfDB.PlaceNamesOfCommunityCenter, 0);
        addPlaces(DataOfDB.PlaceNamesOfChurch, 1);
        addPlaces(DataOfDB.PlaceNamesOfMosque, 2);

        addPlaces(DataOfDB.PlaceNamesOfSynagogue, 4);
        addPlaces(DataOfDB.PlaceNamesOfParking, 5);
    }

    private void addPlaces(DBPlace[] Places, int indexOfCategory) {
        DBPlaceTable db;
        db = new DBPlaceTable(this);

        /**
         * CRUD Operations
         * */
        // Inserting Contacts

        DBPlace dbPlace = new DBPlace();
        int len = Places.length;
        for (int i = 0; i < len; i++) {

            dbPlace.setName(Places[i].getName());
            dbPlace.setCategory_name(AddPlaceActivity.titles[indexOfCategory]);
            dbPlace.setLatitude(Places[i].getLatitude());
            dbPlace.setLongitude(Places[i].getLongitude());
            dbPlace.setPhone("");
            dbPlace.setEmail("");
            dbPlace.setAddress(Places[i].getAddress());
            dbPlace.setRating("4.7");

            db.addPlace( dbPlace );
        }
    }
}
