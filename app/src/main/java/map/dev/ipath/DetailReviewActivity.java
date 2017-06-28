package map.dev.ipath;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

//import com.example.mapdemo.R;

import map.dev.ipath.fragment.FragmentDetail;

import java.util.ArrayList;

import map.dev.ipath.R;

import map.dev.ipath.constant.Constant;
import map.dev.ipath.fragment.FragmentDetailReview;
import map.dev.ipath.fragment.FragmentMainList;
import map.dev.ipath.model.DBPlace;

/**
 * Created by adrian on 12.03.2017.
 */

public class DetailReviewActivity extends FragmentActivity implements View.OnClickListener, FragmentManager.OnBackStackChangedListener{

    private ImageButton btnBack;

    private Button btnDetails;
    private Button btnReviews;

    private TextView tvPlaceDetailTitle;

    private View underLineDetails;
    private View underLineReviews;

    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 2;
    private ViewPager mPager;

    private int[] underLineTagId = { R.id.underLineDetails, R.id.underLineReviews };
    private View[] underLineTag;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        initWidget();

        // Determine which type of sheet to create
        FragmentManager manager = getSupportFragmentManager();
        mPager.setAdapter(new DetailReviewActivity.InstallAdapter(manager));


        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                resetWidget();
                selectedPageDisplay(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // init
        if(!Constant.SelectedPlaceName.equals("")){
            String selectedPlaceName = Constant.SelectedPlaceName;
            if(selectedPlaceName.length() > 17) {
                selectedPlaceName = Constant.SelectedPlaceName.substring(0, 15) + " ... #" + Constant.SelectedPlaceRating;
            } else {
                selectedPlaceName = selectedPlaceName + " #" + Constant.SelectedPlaceRating;
            }
            tvPlaceDetailTitle.setText(selectedPlaceName);
        }
    }

    private void initWidget(){
        btnBack = (ImageButton) findViewById(R.id.btnBack);

        btnDetails = (Button) findViewById(R.id.btnDetails);
        btnReviews = (Button) findViewById(R.id.btnReviews);

        tvPlaceDetailTitle = (TextView) findViewById(R.id.tvPlaceDetailTitle);

//        underLineDetails = (View) findViewById(R.id.underLineDetails);
//        underLineReviews = (View) findViewById(R.id.underLineReviews);

        underLineTag = new View[NUM_PAGES];

        for (int i = 0; i < NUM_PAGES; i++)
            underLineTag[i] = (View) findViewById(underLineTagId[i]);

        btnBack.setOnClickListener(this);
        btnDetails.setOnClickListener(this);
        btnReviews.setOnClickListener(this);

        mPager = (ViewPager) findViewById(R.id.pager);
    }

    private void resetWidget() {
        for(int i = 0; i < NUM_PAGES ; i++){
            underLineTag[i].setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackStackChanged() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
//                startActivity(new Intent(DetailReviewActivity.this, MainActivity.class));
                    finish();
                break;
            case R.id.btnDetails:
                mPager.setCurrentItem(0);
                break;
            case R.id.btnReviews:
                mPager.setCurrentItem(1);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    private void selectedPageDisplay (int selectedNum) {
        underLineTag[selectedNum].setVisibility(View.VISIBLE);
    }

    // --------------------------------------------------------
    class InstallAdapter extends FragmentPagerAdapter {

        private final String[]      TITLES      = { "Details Fragment", "Reviews Fragment" };
        private final int           PAGE_COUNT  = TITLES.length;
        private ArrayList<Fragment> FRAGMENTS   = null;


        @Override
        public Fragment getItem(int pos) {
            return FRAGMENTS.get(pos);
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int pos) {
            return TITLES[pos];
        }

        public InstallAdapter(FragmentManager fm) {
            super(fm);

            addFragment0(fm);

        }

        private void addFragment0(FragmentManager fm){
            FRAGMENTS = new ArrayList<Fragment>();

            Fragment frag0 = new FragmentDetail();
            FRAGMENTS.add(frag0);

            Fragment frag1 = new FragmentDetailReview();
            FRAGMENTS.add(frag1);
        }
    }

    private void startAutoCall(String callNumber) {
        String callNumberString = "tel:" + callNumber;
        Intent intentCall = new Intent(Intent.ACTION_CALL);
        intentCall.setData(Uri.parse(callNumberString));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            ActivityCompat.requestPermissions( DetailReviewActivity.this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    Constant.Permission_CALL_PHONE);
            return;
        }
        startActivity(intentCall);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Toast.makeText(getApplicationContext(), "Okay", Toast.LENGTH_SHORT).show();
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay!
                    startAutoCall(FragmentDetail.number);

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    ActivityCompat.requestPermissions( DetailReviewActivity.this,
                            new String[]{Manifest.permission.CALL_PHONE},
                            Constant.Permission_CALL_PHONE);
                }
                return;
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {

        DBPlace dbPlace = FragmentMainList.selectedDBPlace;
        if(tvPlaceDetailTitle != null) {
            String selectedPlaceName = dbPlace.getName();
            if(selectedPlaceName.length() > 17) {
                selectedPlaceName = dbPlace.getName().substring(0, 15) + " ... #" + dbPlace.getRating();
            } else {
                selectedPlaceName = selectedPlaceName + " #" + dbPlace.getRating();
            }
            tvPlaceDetailTitle.setText(selectedPlaceName);
        }

        super.onResume();
    }


}
