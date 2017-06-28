package map.dev.ipath.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.List;

import map.dev.ipath.AddPlaceActivity;
import map.dev.ipath.CreateRouteActivity;
//import com.example.mapdemo.R;
import map.dev.ipath.RateLocationActivity;

import map.dev.ipath.R;
import map.dev.ipath.ShowPlaceActivity;
import map.dev.ipath.constant.Constant;
import map.dev.ipath.database.DBFavoriteTable;
import map.dev.ipath.database.DBPlaceTable;
import map.dev.ipath.model.DBFavorite;
import map.dev.ipath.model.DBPlace;
import map.dev.ipath.util.GeneralFunc;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by adrian on 12.03.2017.
 */

public class FragmentDetail extends Fragment{
    private SharedPreferences.Editor editor;
    private SharedPreferences prefs;

    public static String contactEmailAddress = "angelmob@outlook.com";
    public static String number;

    private LinearLayout liLayDirections;
    private LinearLayout liLayShowOnTheMap;
    private LinearLayout liLayAddReview;
    private LinearLayout liLayPhone;
    private LinearLayout liLayAddToFavorites;
    private LinearLayout liLayIncorrectInfo;

    private LinearLayout liLayPlaceName;

    // ---------------------------------------------------------------------------------------------
    private ImageView imgPlace;
    private RatingBar ratingBar;

    private TextView tvConnection;

    private TextView tvPhone;
    private TextView tvPlaceName;
    private TextView tvAddress;
    private TextView tvLatitude;
    private TextView tvLongitude;


    private DBPlace selectedDBPlace;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_detail, container, false);

        editor = getActivity().getSharedPreferences(Constant.MyShared, MODE_PRIVATE).edit();
        prefs = getActivity().getSharedPreferences(Constant.MyShared, MODE_PRIVATE);

        initWidget(rootView);
        onBtnClick();

        return rootView;
    }

    private void initWidget(ViewGroup viewGroup) {
        liLayDirections = (LinearLayout) viewGroup.findViewById(R.id.liLayDirections);
        liLayShowOnTheMap = (LinearLayout) viewGroup.findViewById(R.id.liLayShowOnTheMap);
        liLayAddReview = (LinearLayout) viewGroup.findViewById(R.id.liLayAddReview);
        liLayPhone = (LinearLayout) viewGroup.findViewById(R.id.liLayPhone);
        liLayAddToFavorites = (LinearLayout) viewGroup.findViewById(R.id.liLayAddToFavorites);
        liLayIncorrectInfo = (LinearLayout) viewGroup.findViewById(R.id.liLayIncorrectInfo);

        liLayPlaceName = (LinearLayout) viewGroup.findViewById(R.id.liLayPlaceName);

        // -----------------------------------------------------------------------------------------
        imgPlace = (ImageView) viewGroup.findViewById(R.id.imgPlace);
        ratingBar = (RatingBar) viewGroup.findViewById(R.id.ratingBar);

        tvConnection = (TextView) viewGroup.findViewById(R.id.tvConnection);

        tvPhone = (TextView) viewGroup.findViewById(R.id.tvPhone);
        tvPlaceName = (TextView) viewGroup.findViewById(R.id.tvPlaceName);
        tvAddress = (TextView) viewGroup.findViewById(R.id.tvAddress);
        tvLatitude = (TextView) viewGroup.findViewById(R.id.tvLatitude);
        tvLongitude = (TextView) viewGroup.findViewById(R.id.tvLongitude);

        // -----------------------------------------------------------------------------------------
        // init values
        int len;
        String temp;
        selectedDBPlace = FragmentMainList.selectedDBPlace;
        imgPlace.setImageResource(AddPlaceActivity.pinImages[getCategoryID(selectedDBPlace.getCategory_name())]);
        ratingBar.setRating(Float.parseFloat(selectedDBPlace.getRating()));

        tvPhone.setText(selectedDBPlace.getPhone());
        tvPlaceName.setText(stringFormat(selectedDBPlace.getName(), 40));

        tvAddress.setText(stringFormat(selectedDBPlace.getAddress(), 30));
        tvLatitude.setText(selectedDBPlace.getLatitude());
        tvLongitude.setText(selectedDBPlace.getLongitude());

        if(GeneralFunc.isNetworkAvailable(getActivity())) {
            tvConnection.setText("Internet\nconnected");
        } else {
            tvConnection.setText("No internet\nconnection");
        }
    }

    private String stringFormat(String str, int limit) {
        int len = str.length();
        if(len > limit) {
            String temp = str.substring(0, limit) + "\n" + str.substring(limit, len);
            str = temp;
        }

        return str;
    }

    private int getCategoryID(String categoryName) {
        int categoryId = 0;

        for(int i = 0; i < 6; i++) {
            if(categoryName.equals(AddPlaceActivity.titles[i])) {
                categoryId = i;
            }
        }

        return categoryId;
    }

    private void onBtnClick() {
        liLayDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(getActivity(), CreateRouteActivity.class));

                boolean isNetworkConnected = GeneralFunc.isNetworkAvailable(getActivity());
                if(isNetworkConnected) {
                    editor.putString(Constant.FromTag, Constant.FromDetail);
                    editor.commit();

                    getActivity().finish();
                } else {
                    Toast.makeText(getContext(), "No internet connection.", Toast.LENGTH_SHORT).show();
                }


            }
        });

        liLayShowOnTheMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ShowPlaceActivity.class));
            }
        });

        liLayAddReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), RateLocationActivity.class));
//                getActivity().finish();
            }
        });

        liLayPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = selectedDBPlace.getPhone();
                if(!phoneNumber.equals("") && phoneNumber.length() == 14) {
                    number = phoneNumber.substring(2, 5) + phoneNumber.substring(6, 9) + phoneNumber.substring(10, 14);
//                number = "2548835556";
//                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", number, null));
//                startActivity(intent);

                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        ActivityCompat.requestPermissions( getActivity(),
                                new String[]{Manifest.permission.CALL_PHONE},
                                Constant.Permission_CALL_PHONE);
                    } else {
                        startAutoCall(number);
                    }
                } else {
                    // in correct phone number
                }

            }
        });

        liLayAddToFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBPlace selectedDBPlace = FragmentMainList.selectedDBPlace;

                DBFavoriteTable dbFavoriteTable = new DBFavoriteTable(getActivity());
                DBPlaceTable dbPlaceTable = new DBPlaceTable(getActivity());

                List<DBFavorite> dbFavorites = dbFavoriteTable.getAllFavorites();

                int len = dbFavorites.size();
                for ( int i = 0; i< len; i++){
                    if(dbFavorites.get(i).getPlacename().equals(selectedDBPlace.getName())) {
                        Toast.makeText(getContext(), "You have already set favorite for this place.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                DBFavorite dbFavorite = new DBFavorite(selectedDBPlace.getPlace_id(), selectedDBPlace.getName(), "0");
                dbFavoriteTable.addFavorite(dbFavorite);

                Toast.makeText(getContext(), "This place added to Favorites.", Toast.LENGTH_SHORT).show();
            }
        });

        liLayIncorrectInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactEmailAddress = selectedDBPlace.getEmail();
                if(!contactEmailAddress.equals("")) {
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);

                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{ contactEmailAddress });
                    emailIntent.putExtra(Intent.EXTRA_CC, new String[]{""});
                    emailIntent.putExtra(Intent.EXTRA_BCC, new String[]{""});
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Incorrect Info?");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "");

                    emailIntent.setType("message/rfc822");
                    startActivity(/*Intent.createChooser(emailIntent, ChooseEmail)*/emailIntent);
                } else {
                    // in correct email address
                }

            }
        });

        liLayPlaceName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void startAutoCall(String callNumber){
        String callNumberString = "tel:" + callNumber;
        Intent intentCall = new Intent(Intent.ACTION_CALL);
        intentCall.setData(Uri.parse(callNumberString));
        startActivity(intentCall);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
