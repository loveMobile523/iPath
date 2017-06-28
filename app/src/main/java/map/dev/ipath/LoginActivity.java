package map.dev.ipath;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import map.dev.ipath.database.DBFavoriteTable;
import map.dev.ipath.database.DBPlaceTable;
import map.dev.ipath.model.DBFavorite;
import map.dev.ipath.model.DBPlace;
import map.dev.ipath.util.GeneralFunc;
import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by adrian on 11.03.2017.
 */

public class LoginActivity extends Activity{

    private FancyButton btnSignUp;
    private FancyButton btnLogin;

    private TextView tvForgotPassword;
    private TextView tvCancel;

    private EditText etEmail;
    private EditText etPassword;

    // ---------------------------------------------------------------------------------------------
    private SharedPreferences.Editor editor;
    private SharedPreferences prefs;

    public static String userEMAIL;
    public static String userPassword;

    KProgressHUD kpHUD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initWidget();

        onClickBtn();
    }

    private void initWidget() {
        editor = getSharedPreferences(Constant.MyShared, MODE_PRIVATE).edit();
        prefs = getSharedPreferences(Constant.MyShared, MODE_PRIVATE);

        btnSignUp = (FancyButton) findViewById(R.id.btnSignUp);
        btnLogin = (FancyButton) findViewById(R.id.btnLogin);

        tvForgotPassword = (TextView)findViewById(R.id.tvForgotPassword);
        SpannableString content = new SpannableString("FORGOT PASSWORD");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        tvForgotPassword.setText(content);

        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);

        tvCancel = (TextView) findViewById(R.id.tvCancel);

        kpHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true)
//                .setLabel("Login")
                .setAnimationSpeed(1)
                .setDimAmount(0.5f);
    }

    private void onClickBtn() {
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                finish();
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!GeneralFunc.isNetworkAvailable(LoginActivity.this)) {
                    Toast.makeText(getApplicationContext(), "No internet connection, Please check again.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // ---------------------------------------------------------------------------------
                // preparing data to save remote Database
                String userName = prefs.getString(Constant.Username, "");

                int lenOfPlaces = 0;
                int lenOfFavorites = 0;
                int lenOfRates = 0;
                int i = 0;

                JSONArray jaPlaces = new JSONArray();
                JSONArray jaFavorites = new JSONArray();

                // ---------------------------------------------------------------------------------
                // place
                DBPlaceTable dbPlaceTable = new DBPlaceTable(LoginActivity.this);
                List<DBPlace> dbPlaces = new ArrayList<DBPlace>();

                dbPlaces = dbPlaceTable.getAllPlaces();
                if(dbPlaces != null) {
                    lenOfPlaces = dbPlaces.size();
                    if(lenOfPlaces > 0) {
                        DBPlace dbPlace;
                        Map<String, String> placeMap;

                        for (i = 0; i < lenOfPlaces; i++) {
                            dbPlace = dbPlaces.get(i);

                            if(dbPlace.getUpdated().equals("0")) {
                                placeMap = new HashMap<String, String>();

                                placeMap.put("place_id", dbPlace.getPlace_id());
                                placeMap.put("name", dbPlace.getName());
                                placeMap.put("category_name", dbPlace.getCategory_name());
                                placeMap.put("latitude", dbPlace.getLatitude());
                                placeMap.put("longitude", dbPlace.getLongitude());
                                placeMap.put("phone", dbPlace.getPhone());
                                placeMap.put("email", dbPlace.getEmail());
                                placeMap.put("address", dbPlace.getAddress());
                                placeMap.put("rating", dbPlace.getRating());

                                jaPlaces.put(placeMap);

                                // -----------------------------------------------------------------
                                // save updated data
                                dbPlace.setUpdated("1");
                                dbPlaceTable.updatePlace(dbPlace);
                            }
                        }
                    }
                }

                // ---------------------------------------------------------------------------------
                // place
                DBFavoriteTable dbFavoriteTable = new DBFavoriteTable(LoginActivity.this);
                List<DBFavorite> dbFavorites = new ArrayList<DBFavorite>();

                dbFavorites = dbFavoriteTable.getAllFavorites();
                if(dbFavorites != null) {
                    lenOfFavorites = dbFavorites.size();
                    if(lenOfFavorites > 0) {
                        DBFavorite dbFavorite;
                        Map<String, String> favoriteMap;

                        for (i = 0; i < lenOfFavorites; i++) {
                            dbFavorite = dbFavorites.get(i);

                            if(dbFavorite.getUpdated().equals("0")) {
                                favoriteMap = new HashMap<String, String>();

                                favoriteMap.put("place_id", dbFavorite.getPlace_id());
                                favoriteMap.put("username", userName);
                                favoriteMap.put("placename", dbFavorite.getPlacename());


                                jaFavorites.put(favoriteMap);

                                // -----------------------------------------------------------------
                                // save updated data
                                dbFavorite.setUpdated("1");
                                dbFavoriteTable.updateFavorite(dbFavorite);
                            }
                        }
                    }
                }

                // ---------------------------------------------------------------------------------
                // login module
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        kpHUD.show();
                    }
                });

                userEMAIL = etEmail.getText().toString();
                userPassword = etPassword.getText().toString();


                AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                params.put("email", userEMAIL);
                params.put("password", userPassword);



                asyncHttpClient.post(Constant.signInURL, params, new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        kpHUD.dismiss();

                        // todo go to reset password
                        startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
                        finish();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        kpHUD.dismiss();

//                        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();

                        // todo login update
                        editor.putString(Constant.Email, userEMAIL);
                        editor.putString(Constant.Password, userPassword);
                        editor.commit();

                        if(statusCode == 200){
                            startActivity(new Intent(LoginActivity.this, AddPlaceActivity.class));
                            finish();
                        }
                    }
                });
            }
        });


        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
                finish();
            }
        });
    }
}
