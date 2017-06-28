package map.dev.ipath;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import map.dev.ipath.constant.Constant;
import map.dev.ipath.database.DBPlaceTable;
import map.dev.ipath.database.DBRatingTable;
import map.dev.ipath.fragment.FragmentMainList;
import map.dev.ipath.model.DBPlace;
import map.dev.ipath.model.DBRate;

//import com.example.mapdemo.R;

//import map.dev.ipath.R;

/**
 * Created by adrian on 12.03.2017.
 */

public class RateLocationActivity extends Activity implements View.OnClickListener{
    private SharedPreferences.Editor editor;
    private SharedPreferences prefs;

    private RatingBar ratingBar;

    private EditText etRateContent;

    private Button btnCancel;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_location);

        editor = getSharedPreferences(Constant.MyShared, MODE_PRIVATE).edit();
        prefs = getSharedPreferences(Constant.MyShared, MODE_PRIVATE);

        initWidget();
    }

    private void initWidget() {
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        etRateContent = (EditText) findViewById(R.id.etRateContent);

        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        // -----------------------------------------------------------------------------------------

        Typeface typeface = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/lucida_calligraphy_italic.ttf");
        TextView tvRateLocation = (TextView) findViewById(R.id.tvRateLocation);
        tvRateLocation.setTypeface(typeface);

        TextView tvWriteComment = (TextView) findViewById(R.id.tvWriteComment);
        tvWriteComment.setTypeface(typeface);

        etRateContent.setTypeface(typeface);

        btnCancel.setTypeface(typeface);
        btnSubmit.setTypeface(typeface);

        // -----------------------------------------------------------------------------------------

        btnCancel.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCancel:
                finish();
                break;
            case R.id.btnSubmit:
                DBRatingTable dbRatingTable;
                dbRatingTable = new DBRatingTable(RateLocationActivity.this);
                String username = prefs.getString(Constant.Username, "");
                DBPlace dbPlace = FragmentMainList.selectedDBPlace;

                List<DBRate> dbRates = dbRatingTable.getRatesByUserName(username);
                int len = dbRates.size();
                DBRate dbRate;

                if(len > 0) {
                    for (int i = 0; i < len; i++) {
                        dbRate = dbRates.get(i);

                        if(dbRate.getPlace_id().equals(dbPlace.getPlace_id())) {
                            Toast.makeText(getApplicationContext(), "You have already given rate to this place.", Toast.LENGTH_SHORT).show();
//                            startActivity(new Intent(RateLocationActivity.this, DetailReviewActivity.class));
                            finish();
                            return;
                        }
                    }
                }

                dbRate = new DBRate(username, dbPlace.getPlace_id(), dbPlace.getName(), etRateContent.getText().toString(),
                        String.valueOf(ratingBar.getRating()), "0");

                dbRatingTable.addRate(dbRate);

                dbRates = dbRatingTable.getRatesByPlaceId(dbPlace.getPlace_id());

                len = dbRates.size();
                float sum = 0f;

                if(len > 0) {
                    for (int i = 0; i < len; i++) {
                        dbRate = dbRates.get(i);
                        sum += Float.parseFloat(dbRate.getValue());
                    }
                }

                sum /= len;

                DBPlaceTable dbPlaceTable = new DBPlaceTable(RateLocationActivity.this);
                DBPlace updatedDBPlace = dbPlaceTable.getPlaceByPlaceName(dbPlace.getName());

                String ratingValue = String.valueOf(sum);
                if (ratingValue.length() > 5) {
                    ratingValue = ratingValue.substring(0, 5);
                }

                updatedDBPlace.setRating(ratingValue);
                int n = dbPlaceTable.updatePlace(updatedDBPlace);

                FragmentMainList.selectedDBPlace = updatedDBPlace;

//                startActivity(new Intent(RateLocationActivity.this, MainActivity.class));
//                startActivity(new Intent(RateLocationActivity.this, DetailReviewActivity.class));
                finish();

                break;
        }
    }
}
