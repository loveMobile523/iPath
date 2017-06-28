package map.dev.ipath.navigation;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import map.dev.ipath.AddPlaceActivity;
import map.dev.ipath.CreateRouteActivity;
import map.dev.ipath.LoginActivity;
import map.dev.ipath.MainActivity;
//import com.example.mapdemo.R;

import map.dev.ipath.constant.Constant;

import java.util.ArrayList;
import java.util.List;

import map.dev.ipath.R;

/**
 * Created by adrian on 09.03.2017.
 */

public class RightNavigationDrawerFragment extends Fragment /*implements NavigationDrawerCallbacks*/{
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";
    private static final String PREFERENCES_FILE = "my_app_settings"; //TODO: change this to your file
    private NavigationDrawerCallbacks mCallbacks;

    private View mFragmentContainerView;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private boolean mUserLearnedDrawer;
    private boolean mFromSavedInstanceState;
    private int mCurrentSelectedPosition;

    private MainActivity activity;

    private List<NavigationItem> items;
    int page = 2;


    private LinearLayout liLaySearch;
    private LinearLayout liLayTrip;
    private LinearLayout liLayFavorite;

    private LinearLayout liLayInviteFriends;
    private LinearLayout liLayRateApp;
    private LinearLayout liLayLikeOnFacebook;

    private LinearLayout liLayAddNewPlaces;
    private LinearLayout liLayUpdateDB;
    private LinearLayout liLayTalkToSupport;
    private LinearLayout liLayAbout;

    // admob
    private AdView mAdMobAdView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation_drawer_right, container, false);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        initWidget(view);
        onClickItem();

        // admob

        mAdMobAdView = (AdView) view.findViewById(R.id.admob_adview);

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdMobAdView.loadAd(adRequest);

        return view;
    }

    @Override
    public void onResume() {

        super.onResume();
    }



    private void initWidget(View view){
        activity = (MainActivity) getActivity();

        items = new ArrayList<>();

        liLaySearch = (LinearLayout) view.findViewById(R.id.liLaySearch);
        liLayTrip = (LinearLayout) view.findViewById(R.id.liLayTrip);
        liLayFavorite = (LinearLayout) view.findViewById(R.id.liLayFavorite);

        liLayInviteFriends = (LinearLayout) view.findViewById(R.id.liLayInviteFriends);
        liLayRateApp = (LinearLayout) view.findViewById(R.id.liLayRateApp);
        liLayLikeOnFacebook = (LinearLayout) view.findViewById(R.id.liLayLikeOnFacebook);

        liLayAddNewPlaces = (LinearLayout) view.findViewById(R.id.liLayAddNewPlaces);
        liLayUpdateDB = (LinearLayout) view.findViewById(R.id.liLayUpdateDB);
        liLayTalkToSupport = (LinearLayout) view.findViewById(R.id.liLayTalkToSupport);
        liLayAbout = (LinearLayout) view.findViewById(R.id.liLayAbout);
    }

    private void removeCurrentFragment(){
        if(MainActivity.currentFragment.equals(Constant.FragmentMainMap)) {
            MainActivity.manager.beginTransaction().remove(MainActivity.fragmentMainMap).commit();
        }

        if(MainActivity.currentFragment.equals(Constant.FragmentMainList)) {
            MainActivity.manager.beginTransaction().remove(MainActivity.fragmentMainList).commit();
        }

        if(MainActivity.currentFragment.equals(Constant.FragmentMainFavorite)) {
            MainActivity.manager.beginTransaction().remove(MainActivity.fragmentMainFavorite).commit();
        }
    }

    private void resetWidget() {
        for(int i = 0; i < MainActivity.NUM_PAGES ; i++){
            MainActivity.underLineTag[i].setVisibility(View.GONE);
        }
    }

    private void selectedPageDisplay (int selectedNum) {
        MainActivity.underLineTag[selectedNum].setVisibility(View.VISIBLE);
        MainActivity.CurrentSelectedFragment = selectedNum;
    }


    private void onClickItem(){
        liLaySearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDrawer();
                if(MainActivity.CurrentSelectedFragment != 1) {
                    removeCurrentFragment();
                    MainActivity.currentFragment = Constant.FragmentMainList;
                    MainActivity.manager.beginTransaction().add(R.id.container, MainActivity.fragmentMainList).commit();

                    resetWidget();
                    selectedPageDisplay(1);
                }
            }
        });

        liLayTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDrawer();
                startActivity(new Intent(getActivity(), CreateRouteActivity.class));
            }
        });

        liLayFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDrawer();
                if(MainActivity.CurrentSelectedFragment != 2) {
                    removeCurrentFragment();
                    MainActivity.currentFragment = Constant.FragmentMainFavorite;
                    MainActivity.manager.beginTransaction().add(R.id.container, MainActivity.fragmentMainFavorite).commit();

                    resetWidget();
                    selectedPageDisplay(2);
                }
            }
        });

        // like us
        liLayInviteFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDrawer();
            }
        });

        liLayRateApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDrawer();
//                startActivity(new Intent(getActivity(), RateLocationActivity.class));
            }
        });

        liLayLikeOnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDrawer();
            }
        });

        // about
        liLayAddNewPlaces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDrawer();
//                startActivity(new Intent(getActivity(), LoginActivity.class));
                startActivity(new Intent(getActivity(), AddPlaceActivity.class));
            }
        });

        liLayUpdateDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });

        liLayTalkToSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDrawer();
            }
        });

        liLayAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDrawer();
            }
        });

    }

//    void selectItem(int position, String home) {
//        mCurrentSelectedPosition = position;
//        if (mDrawerLayout != null) {
//            mDrawerLayout.closeDrawer(mFragmentContainerView);
//        }
//        if (mCallbacks != null) {
//            mCallbacks.onNavigationDrawerItemSelected(position, home);
//        }
////        ((LeftNavigationDrawerAdapter) mDrawerList.getAdapter()).selectPosition(position);
//    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserLearnedDrawer = Boolean.valueOf(readSharedSetting(getActivity(), PREF_USER_LEARNED_DRAWER, "false"));
        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    public ActionBarDrawerToggle getActionBarDrawerToggle() {
        return mActionBarDrawerToggle;
    }

    public void setActionBarDrawerToggle(ActionBarDrawerToggle actionBarDrawerToggle) {
        mActionBarDrawerToggle = actionBarDrawerToggle;
    }

    public void setup(int fragmentId, DrawerLayout drawerLayout, Toolbar toolbar) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        if(mFragmentContainerView.getParent() instanceof ScrimInsetsFrameLayout){
            mFragmentContainerView = (View) mFragmentContainerView.getParent();
        }
        mDrawerLayout = drawerLayout;
        mDrawerLayout.setStatusBarBackgroundColor(
                getResources().getColor(R.color.myPrimaryDarkColor));

        mActionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(), mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) return;
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) return;
                if (!mUserLearnedDrawer) {
                    mUserLearnedDrawer = true;
                    saveSharedSetting(getActivity(), PREF_USER_LEARNED_DRAWER, "true");
                }

                getActivity().invalidateOptionsMenu();
            }
        };

        if (!mUserLearnedDrawer && !mFromSavedInstanceState)
            mDrawerLayout.openDrawer(mFragmentContainerView);

        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mActionBarDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
    }

    public void openDrawer() {
        mDrawerLayout.openDrawer(mFragmentContainerView);
    }

    public void closeDrawer() {
        mDrawerLayout.closeDrawer(mFragmentContainerView);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    /**
     * Changes the icon of the drawer to back
     */
    public void showBackButton() {
        if (getActivity() instanceof ActionBarActivity) {
            ((ActionBarActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Changes the icon of the drawer to menu
     */
    public void showDrawerButton() {
        if (getActivity() instanceof ActionBarActivity) {
            ((ActionBarActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
        mActionBarDrawerToggle.syncState();
    }

    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mActionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

//    @Override
//    public void onNavigationDrawerItemSelected(int position, String home) {
//        selectItem(position, home);
//    }

    public DrawerLayout getDrawerLayout() {
        return mDrawerLayout;
    }

    public void setDrawerLayout(DrawerLayout drawerLayout) {
        mDrawerLayout = drawerLayout;
    }

    public static void saveSharedSetting(Context ctx, String settingName, String settingValue) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(settingName, settingValue);
        editor.apply();
    }

    public static String readSharedSetting(Context ctx, String settingName, String defaultValue) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        return sharedPref.getString(settingName, defaultValue);
    }
}
