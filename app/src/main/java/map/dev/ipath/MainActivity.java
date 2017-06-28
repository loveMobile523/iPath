package map.dev.ipath;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

//import com.example.mapdemo.R;

import map.dev.ipath.fragment.FragmentMainFavorite;
import map.dev.ipath.fragment.FragmentMainMap;
import map.dev.ipath.navigation.NavigationDrawerCallbacks;

//import map.dev.ipath.R;

import map.dev.ipath.constant.Constant;
import map.dev.ipath.fragment.FragmentMainList;
import map.dev.ipath.navigation.RightNavigationDrawerFragment;

public class MainActivity extends FragmentActivity implements NavigationDrawerCallbacks, View.OnClickListener, FragmentManager.OnBackStackChangedListener {

    private SharedPreferences.Editor editor;
    private SharedPreferences prefs;

    private Toolbar mToolbar;
    private RightNavigationDrawerFragment rNavigationDrawerFragment;

    private ImageButton rMenu;

    private TextView tvMap;
    private TextView tvList;
    private TextView tvFavorite;

    public static FragmentManager manager;
    public static String currentFragment;

    public static FragmentMainMap fragmentMainMap;
    public static FragmentMainList fragmentMainList;
    public static FragmentMainFavorite fragmentMainFavorite;

    public static void setCurrentFragment(String currentFragment) {
        MainActivity.currentFragment = currentFragment;
    }

    public static String getCurrentFragment() {
        return currentFragment;
    }

    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    public static final int NUM_PAGES = 3;
//    private ViewPager mPager;

    private int[] underLineTagId = { R.id.underLineMap, R.id.underLineList,  R.id.underLineFavorite };
    public static View[] underLineTag;
    public static int CurrentSelectedFragment;

    private final int sdk = android.os.Build.VERSION.SDK_INT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editor = getSharedPreferences(Constant.MyShared, MODE_PRIVATE).edit();
        prefs = getSharedPreferences(Constant.MyShared, MODE_PRIVATE);

        initeWidget();


    }

    private void initeWidget(){
        mToolbar = new Toolbar(this);

//        getActionBar().hide();


        rNavigationDrawerFragment = (RightNavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.fragment_drawer_right);
        rNavigationDrawerFragment.setup(R.id.fragment_drawer_right, (DrawerLayout) findViewById(R.id.drawer), mToolbar);
        rNavigationDrawerFragment.closeDrawer();

        manager = getSupportFragmentManager();
//        mFragment = new MaleFragment();
//        fFragment = new FemaleFragment();
//        manager.beginTransaction().replace(R.id.container,mFragment).commit();

        rMenu = (ImageButton) findViewById(R.id.btn_right_menu);

        tvMap = (TextView) findViewById(R.id.tvMap);
        tvList = (TextView) findViewById(R.id.tvList);
        tvFavorite = (TextView) findViewById(R.id.tvFavorite);

        underLineTag = new View[NUM_PAGES];

        for (int i = 0; i < NUM_PAGES; i++)
            underLineTag[i] = (View) findViewById(underLineTagId[i]);

        rMenu.setOnClickListener(this);

        tvMap.setOnClickListener(this);
        tvList.setOnClickListener(this);
        tvFavorite.setOnClickListener(this);

        // -----------------------------------------------------------------------------------------
        Typeface typeface = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/lucida_calligraphy_italic.ttf");

        tvMap.setTypeface(typeface);
        tvFavorite.setTypeface(typeface);
        tvList.setTypeface(typeface);
        // -----------------------------------------------------------------------------------------

        manager = getSupportFragmentManager();

        fragmentMainMap = new FragmentMainMap();
        fragmentMainList = new FragmentMainList();
        fragmentMainFavorite = new FragmentMainFavorite();

        manager.beginTransaction().replace(R.id.container, fragmentMainMap).commit();
        CurrentSelectedFragment = 0;
        currentFragment = Constant.FragmentMainMap;
    }

    private void resetWidget() {
        for(int i = 0; i < NUM_PAGES ; i++){
            underLineTag[i].setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position, String home) {

    }

    @Override
    public void onBackPressed() {
//        if (mPager.getCurrentItem() == 0) {
//            // If the user is currently looking at the first step, allow the system to handle the
//            // Back button. This calls finish() on this activity and pops the back stack.
//            super.onBackPressed();
//        } else {
//            // Otherwise, select the previous step.
//            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
//        }
        finish();
    }

    @Override
    public void onClick(View v) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(tvFavorite.getWindowToken(), 0);
        switch (v.getId()) {
            case R.id.btn_right_menu:
//                manager.beginTransaction().remove()
                rNavigationDrawerFragment.openDrawer();

//                RightNavigationDrawerFragment rightNavigationDrawerFragment = new RightNavigationDrawerFragment();
//                manager.beginTransaction().replace(R.id.fragment_drawer_right, fragmentMainMap, "map").commit();

                break;
            case R.id.tvMap:
//                FragmentMainMap fragmentMainMap = new FragmentMainMap();
//                manager.beginTransaction().replace(R.id.fragment_drawer_right, fragmentMainMap, "map").commit();
//                openFragment(fragmentMainMap);

                if(CurrentSelectedFragment != 0) {
                    removeCurrentFragment();
                    currentFragment = Constant.FragmentMainMap;
                    manager.beginTransaction().add(R.id.container, fragmentMainMap).commit();

                    resetWidget();
                    selectedPageDisplay(0);
                }
//                mPager.setCurrentItem(0);
                break;
            case R.id.tvList:
                if(CurrentSelectedFragment != 1) {
                    removeCurrentFragment();
                    currentFragment = Constant.FragmentMainList;
                    manager.beginTransaction().add(R.id.container, fragmentMainList).commit();

                    resetWidget();
                    selectedPageDisplay(1);
                }
                break;
            case R.id.tvFavorite:
                if(CurrentSelectedFragment != 2) {
                    removeCurrentFragment();
                    currentFragment = Constant.FragmentMainFavorite;
                    manager.beginTransaction().add(R.id.container, fragmentMainFavorite).commit();

                    resetWidget();
                    selectedPageDisplay(2);
                }
                break;
        }
    }

    private void removeCurrentFragment(){
        if(currentFragment.equals(Constant.FragmentMainMap)) {
            manager.beginTransaction().remove(fragmentMainMap).commit();
        }

        if(currentFragment.equals(Constant.FragmentMainList)) {
            manager.beginTransaction().remove(fragmentMainList).commit();
        }

        if(currentFragment.equals(Constant.FragmentMainFavorite)) {
            manager.beginTransaction().remove(fragmentMainFavorite).commit();
        }
    }

    @Override
    public void onBackStackChanged() {

    }

    public void openFragment(Fragment fragment) {
        String fragmentTag = fragment.getClass().getName();
        FragmentManager fragmentManager= (this).getSupportFragmentManager();

        boolean fragmentPopped = fragmentManager.popBackStackImmediate(fragmentTag , 0);

        if (!fragmentPopped && fragmentManager.findFragmentByTag(fragmentTag) == null) {

            FragmentTransaction ftx = fragmentManager.beginTransaction();
            ftx.addToBackStack(fragment.getClass().getSimpleName());
//            ftx.add(R.id.container, fragment);
            ftx.commit();
        }
    }

    private void selectedPageDisplay (int selectedNum) {
        underLineTag[selectedNum].setVisibility(View.VISIBLE);
        CurrentSelectedFragment = selectedNum;
    }

    @Override
    protected void onResume() {
        String fromTag = prefs.getString(Constant.FromTag, "");
        if(fromTag.equals(Constant.FromDetail)) {
            if(CurrentSelectedFragment != 0) {
                removeCurrentFragment();
                currentFragment = Constant.FragmentMainMap;
                manager.beginTransaction().add(R.id.container, fragmentMainMap).commit();

                resetWidget();
                selectedPageDisplay(0);
            }
        } else if (fromTag.equals(Constant.FromAddPlace)) {
            if(CurrentSelectedFragment != 1) {
                removeCurrentFragment();
                currentFragment = Constant.FragmentMainList;
                manager.beginTransaction().add(R.id.container, fragmentMainList).commit();

                resetWidget();
                selectedPageDisplay(1);
            }
        }

        editor.putString(Constant.FromTag, Constant.FromMain);
        editor.commit();

        super.onResume();
    }
}
