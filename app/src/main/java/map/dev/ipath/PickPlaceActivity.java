package map.dev.ipath;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import map.dev.ipath.constant.Constant;
import map.dev.ipath.fragment.FragmentMainList;
import map.dev.ipath.model.DBPlace;
import map.dev.ipath.util.GeneralFunc;
import mehdi.sakout.fancybuttons.FancyButton;

import static java.security.AccessController.getContext;

/**
 * Created by adrian on 07.04.2017.
 */

public class PickPlaceActivity extends Activity{

    private ImageButton btnBack;

    private FancyButton btnCancel;
    private FancyButton btnSelect;

    MapView mMapView;
    private GoogleMap googleMap;
//    private Marker marker;

    //
    public static String SelectedAddress;
    public static LatLng SelectedLatLng;

    private String workingAddress;
    private LatLng workingLatLng;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_place);


        mMapView = (MapView) findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;


                if (ActivityCompat.checkSelfPermission(PickPlaceActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(PickPlaceActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    ActivityCompat.requestPermissions( PickPlaceActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            Constant.Permission_ACCESS_FINE_LOCATION);
                    return;
                }
//                googleMap.setMyLocationEnabled(true);
                googleMap.setOnMyLocationChangeListener(myLocationChangeListener);

                // For dropping a marker at a point on the Map
//                DBPlace dbPlace = FragmentMainList.selectedDBPlace;
//                LatLng selectedPlaceLL = new LatLng(Double.parseDouble(dbPlace.getLatitude()),
//                        Double.parseDouble(dbPlace.getLongitude()));
//                googleMap.addMarker(new MarkerOptions().position(selectedPlaceLL).title(dbPlace.getName()).snippet(dbPlace.getAddress()).
//                        icon(BitmapDescriptorFactory.fromResource(AddPlaceActivity.pinImages[getCategoryID(dbPlace.getCategory_name())])));
//
//                // For zooming automatically to the location of the marker
//                CameraPosition cameraPosition = new CameraPosition.Builder().target(selectedPlaceLL).zoom(12).build();
//                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));



                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                    @Override
                    public void onMapClick(LatLng point) {
                        workingAddress = GeneralFunc.getLocationAddress(PickPlaceActivity.this, point.latitude, point.longitude);
                        workingLatLng = point;

                        MarkerOptions marker = new MarkerOptions()
                                .position(new LatLng(point.latitude, point.longitude))
                                .title("New Marker")
                                .snippet(workingAddress);



                        googleMap.addMarker(marker);
//                        Toast.makeText(PickPlaceActivity.this, String.valueOf(point.latitude) + " --- " + String.valueOf(point.longitude), Toast.LENGTH_SHORT).show();
//                        showAlert( String.valueOf(point.latitude) + " --- " + String.valueOf(point.longitude));
                    }
                });
            }
        });

        SelectedAddress = "";
        SelectedLatLng = new LatLng(0, 0);
        initWidget();

        onBtnClick();
    }

    private void initWidget() {
        btnBack = (ImageButton) findViewById(R.id.btnBack);

        btnCancel = (FancyButton) findViewById(R.id.btnCancel);
        btnSelect = (FancyButton) findViewById(R.id.btnSelect);
    }

    private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
            /*mMarker = */
//            googleMap.addMarker(new MarkerOptions().position(loc));
            if (googleMap != null) {
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
                if (ActivityCompat.checkSelfPermission(PickPlaceActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(PickPlaceActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: permission check
                    ActivityCompat.requestPermissions( PickPlaceActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            Constant.Permission_ACCESS_FINE_LOCATION);
                    return;
                }
                googleMap.setMyLocationEnabled(false);
            }
        }
    };

    private void onBtnClick() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(googleMap != null) {
                    googleMap.clear();
                }
            }
        });

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectedAddress = workingAddress;
                SelectedLatLng = workingLatLng;
                Intent returnIntent = new Intent();
//                returnIntent.putExtra("result",result);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();

        if (googleMap != null) {
            if (ActivityCompat.checkSelfPermission(PickPlaceActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(PickPlaceActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: permission check
                ActivityCompat.requestPermissions( PickPlaceActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        Constant.Permission_ACCESS_FINE_LOCATION);
                return;
            }
            googleMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

//    /*
// * Called when the Activity becomes visible.
//*/
//    @Override
//    public void onStart() {
//        super.onStart();
//
//        adapter = new CategoryAdapter(getContext(), categoryArrayList);
//        recyclerView.setAdapter(adapter);
//        connectClient();
//    }
//
//    /*
//     * Called when the Activity is no longer visible.
//     */
//    @Override
//    public void onStop() {
//        // Disconnecting the client invalidates it.
//        if (mGoogleApiClient != null) {
//            mGoogleApiClient.disconnect();
//        }
//        super.onStop();
//    }
}
