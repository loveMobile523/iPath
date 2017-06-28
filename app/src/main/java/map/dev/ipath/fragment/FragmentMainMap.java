package map.dev.ipath.fragment;


import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
//import org.jetbrains.annotations.Nullable;
import android.provider.SyncStateContract;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import map.dev.ipath.AddPlaceActivity;
import map.dev.ipath.CreateRouteActivity;
//import com.example.mapdemo.R;
import map.dev.ipath.ForgotPasswordActivity;
import map.dev.ipath.LoginActivity;
import map.dev.ipath.ShowPlaceActivity;
import map.dev.ipath.adapter.CategoryAdapter;
import map.dev.ipath.constant.Constant;
import map.dev.ipath.database.DBPlaceTable;
import map.dev.ipath.helper.PlacesDataSource;
import map.dev.ipath.model.Category;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.util.DirectionConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import map.dev.ipath.R;

import map.dev.ipath.model.DBPlace;
import map.dev.ipath.util.ReadTask;
import map.dev.ipath.util.RecyclerOnItemClickListener;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by adrian on 10.03.2017.
 */

public class FragmentMainMap extends Fragment implements DirectionCallback
        /*implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener*/ {
    private SharedPreferences.Editor editor;
    private SharedPreferences prefs;

    private SupportMapFragment mapFragment;
    private GoogleMap map;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private long UPDATE_INTERVAL = 60000;    /* 60 secs */
    private long FASTEST_INTERVAL = 35000;   /* 35 secs */


    private String serverKey = "AIzaSyDN3CkzIOZNZgM1TNAcgNyooTbu2DNOOls";
    private LatLng camera = new LatLng(37.782437, -122.4281893);
    private LatLng origin = new LatLng(37.7849569, -122.4068855);
    private LatLng destination = new LatLng(37.7814432, -122.4460177);

    // category show recycler view
//    private FloatingActionButton iBtnCategoryShow;
    private ImageButton iBtnCategoryShow;
    private LinearLayout liLayCategoryRecycler;
    private ArrayList<Category> categoryArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private CategoryAdapter adapter;
    private Button btnHide;

    // floating buttons
    private ImageButton iBtnLayer;
    private ImageButton iBtnRoute;

    private ImageButton iBtnSatellite;
    private ImageButton iBtnTraffic;
    private ImageButton iBtnMyLocation;

    private Button btnPlus;
    private Button btnMinus;

    private float initZoomLevel = 12;
    private float currentZoomLevel;
    private float MaxZoomLevel;
    private float MinZoomLevel;


    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    public static double shareLatitude;
    public static double shareLongitude;
    public static String shareAddress;
    public static String shareAddress11;
    public static String shareGoogleMapLink = "";

    // -------------  again   -------------
    MapView mMapView;
    public static GoogleMap googleMap;
    private Marker marker;
    private final double CircleRadius = 50 * Constant.MToMile;

    private RelativeLayout reLayPlacesFilter;
    private LinearLayout liLayDetailPlace;

    private TextView tvPlaceName;
    private TextView tvCityName;
    private TextView tvRating;
    private TextView tvDistance;

    // database
    private PlacesDataSource placesDataSource;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main_map, container, false);

        editor = getActivity().getSharedPreferences(Constant.MyShared, MODE_PRIVATE).edit();
        prefs = getActivity().getSharedPreferences(Constant.MyShared, MODE_PRIVATE);

        initWidget(rootView);
        onBtnClick();

//        placesDataSource = new PlacesDataSource(getContext());
//        placesDataSource.open();

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // For showing a move to my location button
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: permission check
                    ActivityCompat.requestPermissions( getActivity(),
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            Constant.Permission_ACCESS_FINE_LOCATION);
                    return;
                }
                googleMap.setMyLocationEnabled(true);
                googleMap.setOnMyLocationChangeListener(myLocationChangeListener);

                MaxZoomLevel = googleMap.getMaxZoomLevel();
                MinZoomLevel = googleMap.getMinZoomLevel();

                // For dropping a marker at a point on the Map
//                LatLng sydney = new LatLng(-34, 151);
//                googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"));
//
//                // For zooming automatically to the location of the marker
//                CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
//                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


                // todo temp add marker click listener
                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        reLayPlacesFilter.setVisibility(View.GONE);
                        liLayDetailPlace.setVisibility(View.VISIBLE);

                        String placeName = marker.getTitle();
                        DBPlaceTable dbPlaceTable = new DBPlaceTable(getContext());
                        DBPlace dbPlace = dbPlaceTable.getPlaceByPlaceName(placeName);

                        String placeAddress = dbPlace.getAddress();
                        String ratingValue = dbPlace.getRating();


                        float[] results = new float[1];
                        Location.distanceBetween(Constant.CurrentPosition.latitude, Constant.CurrentPosition.longitude,
                                Double.parseDouble(dbPlace.getLatitude()), Double.parseDouble(dbPlace.getLongitude()),
                                results);

                        String distance = String.valueOf(results[0] / Constant.MToMile);
                        if(distance.length() > 6) {
                            distance = distance.substring(0, 6);
                        }


                        tvPlaceName.setText(placeName);
                        tvCityName.setText(placeAddress);
                        tvRating.setText(ratingValue);
                        tvDistance.setText(distance + "mi");

                        return false;
                    }
                });

//               todo temp  Add Pin
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                    @Override
                    public void onMapClick(LatLng point) {
//                        MarkerOptions marker = new MarkerOptions()
//                                .position(new LatLng(point.latitude, point.longitude))
//                                .title("New Marker");
//                        googleMap.addMarker(marker);
////                        Toast.makeText(getContext(), String.valueOf(point.latitude) + " --- " + String.valueOf(point.longitude), Toast.LENGTH_SHORT).show();
//                        showAlert( String.valueOf(point.latitude) + " --- " + String.valueOf(point.longitude));

                        liLayDetailPlace.setVisibility(View.GONE);
                        reLayPlacesFilter.setVisibility(View.VISIBLE);

                    }
                });
            }
        });

        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
//            String fromTag = prefs.getString(Constant.FromTag, "");
//            if(fromTag.equals(Constant.FromDetail)) {
//                return;
//            }

            LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());

            Constant.CurrentPosition = loc;
            // todo temp
            Constant.CurrentPosition = new LatLng(29.2070317, -81.0471095);

//            Toast.makeText(getContext(), "Updated location: " + location.getLatitude() + ", " + location.getLongitude(), Toast.LENGTH_SHORT).show();
            /*mMarker = */
            googleMap.addMarker(new MarkerOptions().position(Constant.CurrentPosition));
            if (googleMap != null) {
//                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(Constant.CurrentPosition, 13.0f));
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: permission check
                    ActivityCompat.requestPermissions( getActivity(),
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            Constant.Permission_ACCESS_FINE_LOCATION);
                    return;
                }
                googleMap.setMyLocationEnabled(false);
            }


        }
    };

    @Override
    public void onResume() {
//        placesDataSource.open();

        super.onResume();
        mMapView.onResume();

        // -----------------------------------------------------------------------------------------
        String fromTag = prefs.getString(Constant.FromTag, "");
        if (fromTag.equals(Constant.FromDetail)) {
            googleMap.addMarker(new MarkerOptions().position(Constant.CurrentPosition));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(Constant.CurrentPosition, 13));

            requestDirection();

            googleMap.setMyLocationEnabled(true);
            return;
        }

        // -----------------------------------------------------------------------------------------
        if (googleMap != null) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: permission check
                ActivityCompat.requestPermissions( getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        Constant.Permission_ACCESS_FINE_LOCATION);
                return;
            }
            googleMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onPause() {
//        placesDataSource.close();
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

    private void initWidget(View view) {
        currentZoomLevel = initZoomLevel;

        iBtnCategoryShow = (ImageButton) view.findViewById(R.id.iBtnCategoryShow);
        liLayCategoryRecycler = (LinearLayout) view.findViewById(R.id.liLayCategoryRecycler);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_horizontal);
        btnHide = (Button) view.findViewById(R.id.btnHide);

        iBtnLayer = (ImageButton) view.findViewById(R.id.iBtnLayer);
        iBtnRoute = (ImageButton) view.findViewById(R.id.iBtnRoute);

        iBtnSatellite = (ImageButton) view.findViewById(R.id.iBtnSatellite);
        iBtnTraffic = (ImageButton) view.findViewById(R.id.iBtnTraffic);
        iBtnMyLocation = (ImageButton) view.findViewById(R.id.iBtnMyLocation);

        btnPlus = (Button) view.findViewById(R.id.btnPlus);
        btnMinus = (Button) view.findViewById(R.id.btnMinus);

        // -----------------------------------------------------------------------------------------
        reLayPlacesFilter = (RelativeLayout) view.findViewById(R.id.reLayPlacesFilter);
        liLayDetailPlace = (LinearLayout) view.findViewById(R.id.liLayDetailPlace);

        tvPlaceName = (TextView) view.findViewById(R.id.tvPlaceName);
        tvCityName = (TextView) view.findViewById(R.id.tvCityName);
        tvRating = (TextView) view.findViewById(R.id.tvRating);
        tvDistance = (TextView) view.findViewById(R.id.tvDistance);
    }

    private void showAlert(String content){
        final Dialog dialog =  new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_alert);

        TextView tvAlert = (TextView)dialog.findViewById(R.id.tvAlert);
        TextView tvClose = (TextView)dialog.findViewById(R.id.tvClose);
        tvAlert.setText(content);

        dialog.setCancelable(false);

        dialog.show();

        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                dialog.dismiss();
//            }
//        },1500);
    }

    private void onBtnClick() {

        // -----------------------------------------------------------------------------------------

        iBtnCategoryShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                liLayCategoryRecycler.setVisibility(View.VISIBLE);
            }
        });

        btnHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                liLayCategoryRecycler.setVisibility(View.GONE);
            }
        });

        // -----------------------------------------------------------------------------------------

        iBtnLayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        iBtnRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CreateRouteActivity.class));
            }
        });

        iBtnSatellite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(googleMap != null) {
                    googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                }
            }
        });

        iBtnTraffic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(googleMap != null) {
                    googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                }
            }
        });

        iBtnMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMapView.onResume();

                if (googleMap != null) {
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: permission check
                        ActivityCompat.requestPermissions( getActivity(),
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                Constant.Permission_ACCESS_FINE_LOCATION);
                        return;
                    }
                    googleMap.setMyLocationEnabled(true);
                }
            }
        });

        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(googleMap != null) {
                    currentZoomLevel = googleMap.getCameraPosition().zoom;
                    LatLng target = googleMap.getCameraPosition().target;
                    if(currentZoomLevel < MaxZoomLevel - 0.5f) {
                        currentZoomLevel += 0.5f;
                    }
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(target).zoom(currentZoomLevel).build();

                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

//                    float[] results = new float[1];
//                    Location.distanceBetween(-34.11, 151,
//                                    -34, 151.22,
//                            results);
//                    Toast.makeText(getContext(), String.valueOf(results[0]) + " m", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(googleMap != null) {
                    currentZoomLevel = googleMap.getCameraPosition().zoom;
                    LatLng target = googleMap.getCameraPosition().target;
                    if(currentZoomLevel > MinZoomLevel + 0.5f) {
                        currentZoomLevel -= 0.5f;
                    }
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(target).zoom(currentZoomLevel).build();

                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
            }
        });

        // -----------------------------------------------------------------------------------------

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addOnItemTouchListener(new RecyclerOnItemClickListener(getContext(), new RecyclerOnItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // TODO ....
                Category category = categoryArrayList.get(position);
                String categoryName = category.getTitle();

                Toast.makeText(getContext(), categoryName + " selected", Toast.LENGTH_SHORT).show();

                // ---------------------------------------------------------------------------------
                categoryArrayList.clear();

                for(int i = 0; i < AddPlaceActivity.titles.length; i++) {
                    if(position == i) {
                        categoryArrayList.add(new Category(i, AddPlaceActivity.titles[i], true));
                    } else {
                        categoryArrayList.add(new Category(i, AddPlaceActivity.titles[i], false));
                    }
                }

                adapter = new CategoryAdapter(getContext(), categoryArrayList);
                recyclerView.setAdapter(adapter);

                recyclerView.scrollToPosition(position);

                // ---------------------------------------------------------------------------------

//                LatLng newyork = new LatLng(40.712783, -74.005941);
//                LatLng sydney = new LatLng(-34, 151);

                googleMap.clear();

                DBPlaceTable dbPlaceTable = new DBPlaceTable(getContext());
//                        dbPlaceTable.open();
                List<DBPlace> values = dbPlaceTable.getAllPlaces();
                DBPlace dbPlace;


                int len = values.size();
                Toast.makeText(getContext(), String.valueOf(len), Toast.LENGTH_SHORT).show();
                LatLng latLng = null;

                switch (category.getCategoryNumber()) {
                    case 0:
//                        googleMap.setMapType(GoogleMap.MAP_TYPE_NONE);

                        for (int i = 0; i < len; i++) {
                            dbPlace = values.get(i);

                            if(dbPlace.getCategory_name().equals(AddPlaceActivity.titles[0])) {
                                latLng = new LatLng(Double.parseDouble(dbPlace.getLatitude()), Double.parseDouble(dbPlace.getLongitude()));

                                marker = googleMap.addMarker(new MarkerOptions().position(latLng).title(dbPlace.getName()).
                                        /*snippet(dbPlace.getAddress()).*/icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_cc60)));
                            }
                        }
                        break;
                    case 1:
                        for (int i = 0; i < len; i++) {
                            dbPlace = values.get(i);

                            if(dbPlace.getCategory_name().equals(AddPlaceActivity.titles[1])) {
                                latLng = new LatLng(Double.parseDouble(dbPlace.getLatitude()), Double.parseDouble(dbPlace.getLongitude()));

                                marker = googleMap.addMarker(new MarkerOptions().position(latLng).title(dbPlace.getName()).
                                        /*snippet(dbPlace.getAddress()).*/icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_cri60)));
                            }
                        }
                        break;
                    case 2:
                        for (int i = 0; i < len; i++) {
                            dbPlace = values.get(i);

                            if(dbPlace.getCategory_name().equals(AddPlaceActivity.titles[2])) {
                                latLng = new LatLng(Double.parseDouble(dbPlace.getLatitude()), Double.parseDouble(dbPlace.getLongitude()));

                                marker = googleMap.addMarker(new MarkerOptions().position(latLng).title(dbPlace.getName()).
                                        /*snippet(dbPlace.getAddress()).*/icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_is60)));
                            }
                        }
                        break;
                    case 3:
                        for (int i = 0; i < len; i++) {
                            dbPlace = values.get(i);

                            if(dbPlace.getCategory_name().equals(AddPlaceActivity.titles[3])) {
                                latLng = new LatLng(Double.parseDouble(dbPlace.getLatitude()), Double.parseDouble(dbPlace.getLongitude()));

                                marker = googleMap.addMarker(new MarkerOptions().position(latLng).title(dbPlace.getName()).
                                        /*snippet(dbPlace.getAddress()).*/icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_t60)));
                            }
                        }
                        break;
                    case 4:
                        for (int i = 0; i < len; i++) {
                            dbPlace = values.get(i);

                            if(dbPlace.getCategory_name().equals(AddPlaceActivity.titles[4])) {
                                latLng = new LatLng(Double.parseDouble(dbPlace.getLatitude()), Double.parseDouble(dbPlace.getLongitude()));

                                marker = googleMap.addMarker(new MarkerOptions().position(latLng).title(dbPlace.getName()).
                                        /*snippet(dbPlace.getAddress()).*/icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_ju60)));
                            }
                        }
                        break;
                    case 5:
                        for (int i = 0; i < len; i++) {
                            dbPlace = values.get(i);

                            if(dbPlace.getCategory_name().equals(AddPlaceActivity.titles[5])) {
                                latLng = new LatLng(Double.parseDouble(dbPlace.getLatitude()), Double.parseDouble(dbPlace.getLongitude()));

                                marker = googleMap.addMarker(new MarkerOptions().position(latLng).title(dbPlace.getName()).
                                        /*snippet(dbPlace.getAddress()).*/icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_p60)));
                            }
                        }
                        break;
                    default:
//                        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                        break;
                }

//                Uri gmmIntentUri = Uri.parse("geo:0,0?q=restaurants");
//                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
//                mapIntent.setPackage("com.google.android.apps.maps");
//                startActivity(mapIntent);

                // For zooming automatically to the location of the marker
                if(latLng != null) {
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(8.3f).build();
                    Circle circle = googleMap.addCircle(new CircleOptions()
                            .center(latLng)
                            .radius(CircleRadius)
                            .strokeColor(Color.BLUE)
                            .strokeWidth(2.0f));
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
            }
        }));

        categoryArrayList.clear();

        for(int i = 0; i < AddPlaceActivity.titles.length; i++) {
            categoryArrayList.add(new Category(i, AddPlaceActivity.titles[i], false));
        }
    }

    public void requestDirection() {
//        Snackbar.make(btnRequestDirection, "Direction Requesting...", Snackbar.LENGTH_SHORT).show();
        Toast.makeText(getContext(), "Direction Requesting...", Toast.LENGTH_SHORT).show();

        origin = Constant.CurrentPosition;

        double lati = Double.parseDouble(FragmentMainList.selectedDBPlace.getLatitude());
        double longi = Double.parseDouble(FragmentMainList.selectedDBPlace.getLongitude());
        destination = new LatLng(lati, longi);

        GoogleDirection.withServerKey(serverKey)
                .from(origin)
                .to(destination)
                .transportMode(TransportMode.DRIVING)
                .execute(this);
    }

    // direction start call back
    @Override
    public void onDirectionSuccess(Direction direction, String rawBody) {
//        Snackbar.make(btnRequestDirection, "Success with status : " + direction.getStatus(), Snackbar.LENGTH_INDEFINITE).show();
        Toast.makeText(getContext(), "Success with status : " + direction.getStatus(), Toast.LENGTH_SHORT).show();
        if (direction.isOK()) {
            googleMap.addMarker(new MarkerOptions().position(origin));
            googleMap.addMarker(new MarkerOptions().position(destination));

            ArrayList<LatLng> directionPositionList = direction.getRouteList().get(0).getLegList().get(0).getDirectionPoint();
            googleMap.addPolyline(DirectionConverter.createPolyline(getActivity(), directionPositionList, 5, Color.RED));
        }
    }

    @Override
    public void onDirectionFailure(Throwable t) {
//        Snackbar.make(btnRequestDirection, t.getMessage(), Snackbar.LENGTH_SHORT).show();
        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
    }
    // direction end call back



    protected void connectClient() {
        // Connect the client.
        if (isGooglePlayServicesAvailable() && mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    /*
     * Called when the Activity becomes visible.
    */
    @Override
    public void onStart() {
        super.onStart();

        adapter = new CategoryAdapter(getContext(), categoryArrayList);
        recyclerView.setAdapter(adapter);
        connectClient();
    }

    /*
     * Called when the Activity is no longer visible.
     */
    @Override
    public void onStop() {
        // Disconnecting the client invalidates it.
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }


    private boolean isGooglePlayServicesAvailable() {
        // Check that Google Play services is available
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            Log.d("Location Updates", "Google Play services is available.");
            return true;
        } else {
            // Get the error dialog from Google Play services
            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(resultCode, getActivity(),
                    CONNECTION_FAILURE_RESOLUTION_REQUEST);

            // If Google Play services can provide an error dialog
            if (errorDialog != null) {
                // Create a new DialogFragment for the error dialog
                ErrorDialogFragment errorFragment = new ErrorDialogFragment();
                errorFragment.setDialog(errorDialog);
                errorFragment.show(getActivity().getSupportFragmentManager(), "Location Updates");
            }

            return false;
        }
    }

    private void getLocationAddress(double latitude, double longitude){

        LatLng latLng = new LatLng(shareLatitude, shareLongitude);//
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 12);
        map.animateCamera(cameraUpdate);

        MarkerOptions markerOptions;
        try {
            Geocoder geo = new Geocoder(getContext(), Locale.getDefault());

            List<Address> addresses = geo.getFromLocation(shareLatitude, shareLongitude, 1);
            if (addresses.isEmpty()) {
                markerOptions  = new MarkerOptions().position(latLng).title("Waiting for Location");
                shareAddress = "Waiting for Location";
                map.addMarker(markerOptions);
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


                    shareAddress11 = "address: \n" + address +
                            "\n \n city: \n" + city +
                            "\n \n state: \n" + state +
                            "\n \n country: \n" + country +
                            "\n \n postalCode: \n" + postalCode +
                            "\n \n knownName: \n" + knownName +
                            "\n \n subAdminArea: \n" + subAdminArea +
                            "\n \n subLocality: \n" + subLocality +
                            "\n \n thoroughfare: \n" + thoroughfare +
                            "\n \n url: \n" + url;


                    shareAddress11 = "";
                    shareAddress = "";
                    if(address != null){
                        shareAddress11 = shareAddress11 + address + "\n";
                        shareAddress = shareAddress + address + ", ";
                    }
                    if(subLocality != null){
                        shareAddress11 = shareAddress11 + subLocality + ", ";
                        shareAddress = shareAddress + subLocality + ", ";
                    }
                    if(city != null){
                        shareAddress11 = shareAddress11 + city + ", ";
                        shareAddress = shareAddress + city + ", ";
                    }
                    if(state != null){
                        shareAddress11 = shareAddress11 + state + " ";
                        shareAddress = shareAddress + state + " ";
                    }
                    if(postalCode != null){
                        shareAddress11 = shareAddress11 + postalCode + " \n ";
                        shareAddress = shareAddress + postalCode + ", ";
                    } else {
                        shareAddress11 = shareAddress11 + " \n ";
                        shareAddress = shareAddress + ", ";
                    }
                    if(country != null){
                        shareAddress11 = shareAddress11 + country + ".";
                        shareAddress = shareAddress + country + ".";
                    }
//						shareAddress11 = shareAddress11 + city + ", " + state + ", " + country;

//						String locationString = location.getLatitude() / 1E6 + "," + location.getLongitude() / 1E6;
                    String locationString = latitude + "," + longitude;

                    String[] addressArray = shareAddress.split(" ");

                    String addressURL; // = addressArray
                    if(addressArray.length == 1){
                        addressURL = shareAddress;
                    } else {
                        addressURL = addressArray[0];
                    }

                    for(int i = 1; i < addressArray.length; i++){
                        addressURL = addressURL + "%20" + addressArray[i];
                    }

                    // TODO my add
//						https://www.google.com/maps/preview/@-15.623037,18.388672,8z
//						shareGoogleMapLink = "http://maps.google.com/maps/preview/@" + locationString + ",8z";
                    shareGoogleMapLink = "http://maps.google.com/maps?q=" + addressURL + "@" + locationString;
//						https://www.google.ro/maps/@43.0185285,21.9170197,20.49z?hl=ro
//						shareGoogleMapLink = "http://maps.google.com/maps?q=" + city + "@" + locationString + ",18z";
//						shareGoogleMapLink = "http://maps.google.com/maps/place/43°01'N+21°55'E/@43.018347,21.9164021,19z";

                    markerOptions  = new MarkerOptions().position(latLng).title(addressString);
                    shareAddress = addressString;
                    map.addMarker(markerOptions);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace(); // getFromLocation() may sometimes fail
        }
    }

//    protected void startLocationUpdates() {
//        mLocationRequest = new LocationRequest();
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
//        mLocationRequest.setInterval(UPDATE_INTERVAL);
//        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
//        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//
//            return;
//        }
//        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
//                mLocationRequest, this);
//    }



    // Define a DialogFragment that displays the error dialog
    public static class ErrorDialogFragment extends DialogFragment {

        // Global field to contain the error dialog
        private Dialog mDialog;

        // Default constructor. Sets the dialog field to null
        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }

        // Set the dialog to display
        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }

        // Return a Dialog to the DialogFragment.
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
    }

}
