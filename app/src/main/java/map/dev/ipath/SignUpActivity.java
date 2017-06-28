package map.dev.ipath;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//import com.example.mapdemo.R;

import map.dev.ipath.constant.Constant;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;
import map.dev.ipath.database.DBPlaceTable;
import map.dev.ipath.database.DBRatingTable;
import map.dev.ipath.model.DBPlace;
import map.dev.ipath.model.DBRate;
import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by adrian on 19.03.2017.
 */

public class SignUpActivity extends Activity{
    private SharedPreferences.Editor editor;
    private SharedPreferences prefs;

    // ---------------------------------------------------------------------------------------------
    private FancyButton btnSignUp;
    private TextView tvLogin;

    private EditText etUserName;
    private EditText etEmail;
    private EditText etPassword;

    private String userName;
    private String userEMAIL;
    private String userPassword;

    private boolean firstRun;
    KProgressHUD kpHUD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        editor = getSharedPreferences(Constant.MyShared, MODE_PRIVATE).edit();
        prefs = getSharedPreferences(Constant.MyShared, MODE_PRIVATE);

        firstRun = prefs.getBoolean(Constant.firstRun, false);
        initWidget();

        onClickBtn();


        if(!firstRun) {
            isFindLocationPermissionGranted();
        }
    }

    private void isFindLocationPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG","Permission is granted");
                isPhoneCallPermissionGranted();
            } else {

                Log.v("TAG","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Constant.Permission_ACCESS_FINE_LOCATION);
            }
        }
    }

    public  void isPhoneCallPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG","Permission is granted");
            } else {

                Log.v("TAG","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, Constant.Permission_CALL_PHONE);
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG","Permission is granted");
        }
    }

    private void initWidget() {
        tvLogin = (TextView) findViewById(R.id.tvLogin);

        btnSignUp = (FancyButton) findViewById(R.id.btnSignUp);

        etUserName = (EditText) findViewById(R.id.etUserName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);

        kpHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true)
//                .setLabel("Login")
                .setAnimationSpeed(1)
                .setDimAmount(0.5f);
    }

    private void onClickBtn() {
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!firstRun) {
                    Toast.makeText(getApplicationContext(), "Please first sign up.", Toast.LENGTH_SHORT).show();
                    return;
                }

                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                finish();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        kpHUD.show();
                    }
                });

                userName = etUserName.getText().toString();
                userEMAIL = etEmail.getText().toString();
                userPassword = etPassword.getText().toString();

                if(userPassword.length() < 6) {
                    kpHUD.dismiss();
                    Toast.makeText(getApplicationContext(), "Password require 6 over.", Toast.LENGTH_SHORT).show();
                    return;
                }

                AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                params.put("email", userEMAIL);
                params.put("username", userName);
                params.put("password", userPassword);

                asyncHttpClient.post(Constant.signUpURL, params, new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        kpHUD.dismiss();

                        // todo go to reset password
//                        startActivity(new Intent(SignUpActivity.this, ForgotPasswordActivity.class));
//                        finish();
                        Toast.makeText(getApplicationContext(), "fail", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        kpHUD.dismiss();

                        DBPlaceTable dbPlaceTable;
                        dbPlaceTable = new DBPlaceTable(SignUpActivity.this);
                        dbPlaceTable.dropTable();

                        DBRatingTable dbRatingTable;
                        dbRatingTable = new DBRatingTable(SignUpActivity.this);
                        dbRatingTable.dropTable();

                        editor.putString(Constant.Username, userName);
                        editor.putString(Constant.Email, userEMAIL);
                        editor.putString(Constant.Password, userPassword);

                        editor.putBoolean(Constant.firstRun, true);

                        editor.commit();

                        Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();

                        if(statusCode == 200){
                            JSONObject joRespond = null;
                            try {
                                joRespond = new JSONObject(responseString);
                                String state = joRespond.getString("state");
                                String users = joRespond.getString("users");
                                String places = joRespond.getString("places");
                                String favorites = joRespond.getString("favorites");
                                String rates = joRespond.getString("rates");

                                JSONArray jaUsers = new JSONArray(users);
                                JSONArray jaPlaces = new JSONArray(places);
                                JSONArray jaFavorites = new JSONArray(favorites);
                                JSONArray jaRates = new JSONArray(rates);

                                int len = jaPlaces.length();
                                int i;
                                if(len > 0) {
                                    for(i = 0; i < len; i++) {
                                        String placeItem = jaPlaces.getString(i);
                                        JSONObject joPlaceItem = new JSONObject(placeItem);

                                        String placeId = joPlaceItem.getString("id");
                                        String name = joPlaceItem.getString("name");
                                        String category_id = joPlaceItem.getString("category_id");
                                        String category_name = joPlaceItem.getString("category_name");
                                        String latitude = joPlaceItem.getString("latitude");
                                        String longitude = joPlaceItem.getString("longitude");
                                        String phone = joPlaceItem.getString("phone");
                                        String email = joPlaceItem.getString("email");
                                        String address = joPlaceItem.getString("address");
                                        String rating = joPlaceItem.getString("rating");

                                        double lati = Double.parseDouble(latitude);
                                        double longi = Double.parseDouble(longitude);
                                        if(address.equals("")) {
                                            address = getLocationAddress(lati, longi);
                                        }

                                        DBPlace dbPlace = new DBPlace(placeId, name, category_name,
                                                latitude, longitude, phone, email, address, rating, "1");

                                        DBPlace addedSavePlace = dbPlaceTable.addPlace(dbPlace);
                                    }
                                }

                                len = jaRates.length();
                                if(len > 0) {
                                    for (i = 0; i < len; i++) {
                                        String rateItem = jaRates.getString(i);
                                        JSONObject joRateItem = new JSONObject(rateItem);

                                        String username = joRateItem.getString("username");
                                        String placeID = joRateItem.getString("place_id");
                                        String placeName = joRateItem.getString("place_name");
                                        String content = joRateItem.getString("content");
                                        String value = joRateItem.getString("value");

                                        DBRate dbRate = new DBRate(username, placeID, placeName, content, value, "1");

                                        dbRatingTable.addRate(dbRate);
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                            finish();
                        }
                    }
                });
            }
        });
    }

    private String getLocationAddress(double latitude, double longitude){

        try {
            Geocoder geo = new Geocoder(SignUpActivity.this, Locale.getDefault());

            List<Address> addresses = geo.getFromLocation(latitude, longitude, 1);
            if (addresses.isEmpty()) {
                // Waiting for Location
            }
            else {
                if (addresses.size() > 0) {
                    String addressString = addresses.get(0).getFeatureName();
                    //  + ", getLocality: " + addresses.get(0).getLocality() + ", getAdminArea: "
                    // + addresses.get(0).getAdminArea() + ", getCountryName: " + addresses.get(0).getCountryName();


                    String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                    String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
                    String subAdminArea = addresses.get(0).getSubAdminArea();
                    String subLocality = addresses.get(0).getSubLocality();
                    String thoroughfare = addresses.get(0).getThoroughfare();
                    String url = addresses.get(0).getUrl();

                    String shareAddress = "";
                    if(address != null){
//                        shareAddress11 = shareAddress11 + address + "\n";
                        shareAddress = shareAddress + address + ", ";
                    }
                    if(subLocality != null){
                        shareAddress = shareAddress + subLocality + ", ";
                    }
                    if(city != null){
                        shareAddress = shareAddress + city + ", ";
                    }
                    if(state != null){
                        shareAddress = shareAddress + state + " ";
                    }
                    if(postalCode != null){
                        shareAddress = shareAddress + postalCode + ", ";
                    } else {
                        shareAddress = shareAddress + ", ";
                    }
//                    if(country != null){
////                        shareAddress11 = shareAddress11 + country + ".";
//                        shareAddress = shareAddress + country + ".";
//                    }
//						shareAddress11 = shareAddress11 + city + ", " + state + ", " + country;

                    return shareAddress;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace(); // getFromLocation() may sometimes fail
        }
        return "";
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == Constant.Permission_ACCESS_FINE_LOCATION){

            if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                Log.v("TAG","Permission: "+permissions[0]+ "was "+grantResults[0]);
                //resume tasks needing this permission
                isPhoneCallPermissionGranted();
            } else if(grantResults[0]== PackageManager.PERMISSION_DENIED){
                // re send request
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Constant.Permission_ACCESS_FINE_LOCATION);
            }
        }

        if(requestCode == Constant.Permission_CALL_PHONE) {
            if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                Log.v("TAG","Permission: "+permissions[0]+ "was "+grantResults[0]);

            } else if(grantResults[0]== PackageManager.PERMISSION_DENIED){
                // re send request
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, Constant.Permission_CALL_PHONE);
            }
        }

    }
}
