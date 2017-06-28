package map.dev.ipath;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import map.dev.ipath.adapter.CategoryAdapter;
import map.dev.ipath.constant.Constant;
import map.dev.ipath.fragment.FragmentMainList;
import map.dev.ipath.model.DBPlace;

/**
 * Created by adrian on 22.03.2017.
 */

public class ShowPlaceActivity extends Activity {

    private ImageButton btnBack;
    private TextView tvPlaceDetailTitle;

    MapView mMapView;
    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_place);

        mMapView = (MapView) findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;


                if (ActivityCompat.checkSelfPermission(ShowPlaceActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(ShowPlaceActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    ActivityCompat.requestPermissions( ShowPlaceActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            Constant.Permission_ACCESS_FINE_LOCATION);
                    return;
                }
//                googleMap.setMyLocationEnabled(true);
                googleMap.setOnMyLocationChangeListener(myLocationChangeListener);



                // For dropping a marker at a point on the Map
                DBPlace dbPlace = FragmentMainList.selectedDBPlace;
                LatLng selectedPlaceLL = new LatLng(Double.parseDouble(dbPlace.getLatitude()),
                        Double.parseDouble(dbPlace.getLongitude()));
                googleMap.addMarker(new MarkerOptions().position(selectedPlaceLL).title(dbPlace.getName()).snippet(dbPlace.getAddress()).
                        icon(BitmapDescriptorFactory.fromResource(AddPlaceActivity.pinImages[getCategoryID(dbPlace.getCategory_name())])));

                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(selectedPlaceLL).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });

        initWidget();

        onBtnClick();
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

    private void initWidget() {
        btnBack = (ImageButton) findViewById(R.id.btnBack);
        tvPlaceDetailTitle = (TextView) findViewById(R.id.tvPlaceDetailTitle);

        DBPlace dbPlace = FragmentMainList.selectedDBPlace;
        String selectedPlaceName = dbPlace.getName();
        if(selectedPlaceName.length() > 20) {
            selectedPlaceName = Constant.SelectedPlaceName.substring(0, 15) + " ... #" + FragmentMainList.selectedDBPlace.getRating();
        }

        tvPlaceDetailTitle.setText(selectedPlaceName);
    }

    private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
            /*mMarker = */
            googleMap.addMarker(new MarkerOptions().position(loc));
            if (googleMap != null) {
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
                if (ActivityCompat.checkSelfPermission(ShowPlaceActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(ShowPlaceActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: permission check
                    ActivityCompat.requestPermissions( ShowPlaceActivity.this,
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
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();

        if (googleMap != null) {
            if (ActivityCompat.checkSelfPermission(ShowPlaceActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(ShowPlaceActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: permission check
                ActivityCompat.requestPermissions( ShowPlaceActivity.this,
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
