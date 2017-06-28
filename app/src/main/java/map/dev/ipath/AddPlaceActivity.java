package map.dev.ipath;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import map.dev.ipath.adapter.CustomAdapter;
import map.dev.ipath.constant.Constant;
import map.dev.ipath.database.DBPlaceTable;
import map.dev.ipath.model.DBPlace;
import map.dev.ipath.model.RowItem;

/**
 * Created by adrian on 21.03.2017.
 */

public class AddPlaceActivity extends Activity{
    private SharedPreferences.Editor editor;
    private SharedPreferences prefs;

    private Button btnCancel;
    private Button btnSubmit;

    private EditText etPlaceName;
    private EditText etPhone;
    private EditText etEmail;


    private TextView tvLatitude;
    private TextView tvLongitude;
    private TextView tvAddress;

    // ---------------------------------------------------------------------------------------------
    public static final String[] titles = new String[] { "Community Center",
            "Church", "Mosque", "Temple", "Synagogue", "Parking" };

    public static final Integer[] images = { R.drawable.cate_cc100,
            R.drawable.cate_cri100,
            R.drawable.cate_is100,
            R.drawable.cate_t100,
            R.drawable.cate_ju100,
            R.drawable.cate_p100
    };

    public static final Integer[] grayImages = { R.drawable.cate_cc_g,
            R.drawable.cate_cri_g,
            R.drawable.cate_is_g,
            R.drawable.cate_t_g,
            R.drawable.cate_ju_g,
            R.drawable.cate_p_g
    };

    public static final Integer[] pinImages = { R.drawable.pin_cc60,
            R.drawable.pin_cri60,
            R.drawable.pin_is60,
            R.drawable.pin_t60,
            R.drawable.pin_ju60,
            R.drawable.pin_p60
    };

    Spinner spinner;
    List<RowItem> rowItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);

        editor = getSharedPreferences(Constant.MyShared, MODE_PRIVATE).edit();
        prefs = getSharedPreferences(Constant.MyShared, MODE_PRIVATE);

        editor.putString(Constant.FromTag, Constant.FromAddPlace);
        editor.commit();

        initWidget();
        onClickBtn();

        rowItems = new ArrayList<RowItem>();
        for (int i = 0; i < titles.length; i++) {
            RowItem item = new RowItem(titles[i], images[i]);
            rowItems.add(item);
        }

        spinner = (Spinner)findViewById(R.id.spinner);
        CustomAdapter adapter = new CustomAdapter(AddPlaceActivity.this,
                R.layout.row_item_spinner_category, R.id.title, rowItems);
        spinner.setAdapter(adapter);
    }

    private void initWidget() {
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        Typeface typeface = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/lucida_calligraphy_italic.ttf");

        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setTypeface(typeface);
        btnCancel.setTypeface(typeface);
        btnSubmit.setTypeface(typeface);

        // -----------------------------------------------------------------------------------------
        etPlaceName = (EditText) findViewById(R.id.etPlaceName);
        etPhone = (EditText) findViewById(R.id.etPhone);
        etEmail = (EditText) findViewById(R.id.etEmail);

        tvLatitude = (TextView) findViewById(R.id.tvLatitude);
        tvLongitude = (TextView) findViewById(R.id.tvLongitude);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
    }

    private void onClickBtn() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(btnSubmit.getWindowToken(), 0);

                String categoryName = String.valueOf(spinner.getSelectedItem());
//                Toast.makeText(getApplicationContext(), categoryName, Toast.LENGTH_SHORT).show();

                String placeName = etPlaceName.getText().toString();
                String latitude = tvLatitude.getText().toString();
                String longitude = tvLongitude.getText().toString();
                String address = tvAddress.getText().toString();
                String phone = etPhone.getText().toString();
                String email = etEmail.getText().toString();

                if(placeName.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter place name!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(latitude.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please select place position!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(longitude.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please select place position!", Toast.LENGTH_SHORT).show();
                    return;
                }

                DBPlaceTable dbPlaceTable = new DBPlaceTable(AddPlaceActivity.this);


                DBPlace dbPlace = dbPlaceTable.getPlaceByPlaceName(placeName);;

                if(dbPlace != null) {
                    if(dbPlace.getName().equals(placeName)) {
                        Toast.makeText(getApplicationContext(), "This place name already exist, Please enter other place name", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                dbPlace = new DBPlace(placeName, categoryName, latitude, longitude, phone, email, address, "0", "0");
                dbPlaceTable.addPlace(dbPlace);

                finish();
            }
        });

        // -----------------------------------------------------------------------------------------
        tvLatitude.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(AddPlaceActivity.this, PickPlaceActivity.class), 17);
            }
        });

        tvLongitude.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(AddPlaceActivity.this, PickPlaceActivity.class), 17);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            String latitude = String.valueOf(PickPlaceActivity.SelectedLatLng.latitude);
            String longitude = String.valueOf(PickPlaceActivity.SelectedLatLng.longitude);

            if(latitude.length() > 11) {
                latitude = latitude.substring(0, 11);
            }
            if(longitude.length() > 11) {
                longitude = longitude.substring(0, 11);
            }

            tvLatitude.setText(latitude);
            tvLongitude.setText(longitude);

            String address = PickPlaceActivity.SelectedAddress;
            if(address.length() > 35) {
                address = PickPlaceActivity.SelectedAddress.substring(0, 35) + "\n" +
                        PickPlaceActivity.SelectedAddress.substring(35, PickPlaceActivity.SelectedAddress.length());
            }
            tvAddress.setText(address);
        }
    }
}
