package map.dev.ipath.fragment;

import android.support.v4.app.Fragment;

//import com.mapbox.mapboxsdk.Mapbox;
//import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
//import com.mapbox.mapboxsdk.constants.Style;
//import com.mapbox.mapboxsdk.geometry.LatLng;
//import com.mapbox.mapboxsdk.location.LocationSource;
//import com.mapbox.mapboxsdk.maps.MapView;
//import com.mapbox.mapboxsdk.maps.MapboxMap;
//import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
//import com.mapbox.services.android.telemetry.location.LocationEngine;
//import com.mapbox.services.android.telemetry.location.LocationEngineListener;
//import com.mapbox.services.android.telemetry.permissions.PermissionsListener;
//import com.mapbox.services.android.telemetry.permissions.PermissionsManager;


/**
 * Created by adrian on 13.03.2017.
 */

public class Temp extends Fragment /*implements PermissionsListener*/ {
    // consider

//    private MapView mapView;
//    private MapboxMap map;
//    private FloatingActionButton floatingActionButton;
//    private LocationEngine locationEngine;
//    private LocationEngineListener locationEngineListener;
//    private PermissionsManager permissionsManager;
//
//
//    private RecyclerView recyclerView;
//    private CategoryAdapter adapter;
//    private Button btnHide;
//    private LinearLayout liLayCategoryRecycler;
//
//    private ArrayList<Category> categoryArrayList = new ArrayList<>();
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main_map, container, false);
//
//        Mapbox.getInstance(getActivity(), getString(R.string.access_token));
//
//        // This contains the MapView in XML and needs to be called after the account manager
////        setContentView(R.layout.activity_location_basic);
//
//        // Get the location engine object for later use.
//        locationEngine = LocationSource.getLocationEngine(getActivity());
//        locationEngine.activate();
//
//        mapView = (MapView) rootView.findViewById(R.id.mapView);
//        mapView.setStyleUrl(Style.MAPBOX_STREETS);
//
//        mapView.onCreate(savedInstanceState);
//        mapView.getMapAsync(new OnMapReadyCallback() {
//            @Override
//            public void onMapReady(MapboxMap mapboxMap) {
//                map = mapboxMap;
//
//                if (map != null) {
//                    toggleGps(!map.isMyLocationEnabled());
//                }
//            }
//        });
//
//
//        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_horizontal);
//        btnHide = (Button) rootView.findViewById(R.id.btnHide);
//        liLayCategoryRecycler = (LinearLayout) rootView.findViewById(R.id.liLayCategoryRecycler);
//
//        iBtnCategoryShow = (FloatingActionButton) rootView.findViewById(R.id.iBtnCategoryShow);
//        iBtnCategoryShow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (map != null) {
////                    toggleGps(!map.isMyLocationEnabled());
//
//                    liLayCategoryRecycler.setVisibility(View.VISIBLE);
//                }
//            }
//        });
//
//
//
//        initWidget();
//        return rootView;
//    }
//
//    private void initWidget(){
//        LinearLayoutManager layoutManager
//                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(layoutManager);
//
//        recyclerView.addOnItemTouchListener(new RecyclerOnItemClickListener(getContext(), new RecyclerOnItemClickListener.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                // TODO ....
//                Toast.makeText(getContext(), "Category selected", Toast.LENGTH_SHORT).show();
//            }
//        }));
//
//        categoryArrayList.clear();
//        categoryArrayList.add(new Category(0, "Community Center"));
//        categoryArrayList.add(new Category(0, "Church"));
//        categoryArrayList.add(new Category(0, "Mosque"));
//        categoryArrayList.add(new Category(0, "Temple"));
//        categoryArrayList.add(new Category(0, "Synagogue"));
//        categoryArrayList.add(new Category(0, "Parking"));
//
//        btnHide.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                liLayCategoryRecycler.setVisibility(View.GONE);
//            }
//        });
//
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        mapView.onResume();
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        mapView.onStart();
//
//        adapter = new CategoryAdapter(getContext(), categoryArrayList);
//        recyclerView.setAdapter(adapter);
//    }
//
//
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        mapView.onStop();
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        mapView.onPause();
//    }
//
//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        mapView.onSaveInstanceState(outState);
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        mapView.onDestroy();
//        // Ensure no memory leak occurs if we register the location listener but the call hasn't
//        // been made yet.
//        if (locationEngineListener != null) {
//            locationEngine.removeLocationEngineListener(locationEngineListener);
//        }
//    }
//
//    @Override
//    public void onLowMemory() {
//        super.onLowMemory();
//        mapView.onLowMemory();
//    }
//
//    private void toggleGps(boolean enableGps) {
////        if (enableGps) {
////            // Check if user has granted location permission
////            permissionsManager = new PermissionsManager((PermissionsListener) getActivity());
////            if (!PermissionsManager.areLocationPermissionsGranted(getActivity())) {
////                permissionsManager.requestLocationPermissions(getActivity());
////            } else {
//
//
//        enableLocation(true);
//
//
////            }
////        } else {
////            enableLocation(false);
////        }
//    }
//
//    private void enableLocation(boolean enabled) {
////        if (enabled) {
//        // If we have the last location of the user, we can move the camera to that position.
//        Location lastLocation = locationEngine.getLastLocation();
//        if (lastLocation != null) {
//            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastLocation), 11));
//        }
//
//        locationEngineListener = new LocationEngineListener() {
//            @Override
//            public void onConnected() {
//                // No action needed here.
//            }
//
//            @Override
//            public void onLocationChanged(Location location) {
//                if (location != null) {
//                    // Move the map camera to where the user location is and then remove the
//                    // listener so the camera isn't constantly updating when the user location
//                    // changes. When the user disables and then enables the location again, this
//                    // listener is registered again and will adjust the camera once again.
//                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location), 11));
//                    locationEngine.removeLocationEngineListener(this);
//                }
//            }
//        };
//        locationEngine.addLocationEngineListener(locationEngineListener);
////            floatingActionButton.setImageResource(R.drawable.ic_location_disabled_24dp);
////        } else {
////            floatingActionButton.setImageResource(R.drawable.ic_my_location_24dp);
////        }
//        // Enable or disable the location layer on the map
//        map.setMyLocationEnabled(enabled);
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }
//
//    @Override
//    public void onExplanationNeeded(List<String> permissionsToExplain) {
//        Toast.makeText(getActivity(), "This app needs location permissions in order to show its functionality.",
//                Toast.LENGTH_LONG).show();
//    }
//
//    @Override
//    public void onPermissionResult(boolean granted) {
//        if (granted) {
//            enableLocation(true);
//        } else {
//            Toast.makeText(getActivity(), "You didn't grant location permissions.",
//                    Toast.LENGTH_LONG).show();
////            finish();
//        }
//    }
}
