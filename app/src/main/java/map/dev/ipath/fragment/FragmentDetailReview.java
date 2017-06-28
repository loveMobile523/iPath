package map.dev.ipath.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.test.suitebuilder.TestMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

//import com.example.mapdemo.R;
import map.dev.ipath.adapter.FilterPlaceAdater;
import map.dev.ipath.adapter.ReviewAdapter;
import map.dev.ipath.database.DBPlaceTable;
import map.dev.ipath.database.DBRatingTable;
import map.dev.ipath.model.DBPlace;
import map.dev.ipath.model.DBRate;
import map.dev.ipath.model.ReviewModel;

import java.util.ArrayList;
import java.util.List;

import map.dev.ipath.R;

/**
 * Created by adrian on 12.03.2017.
 */

public class FragmentDetailReview extends Fragment{
    private ListView lvRate;
    ArrayList<DBRate> arrayList;

    private TextView tvTotalRatingValue;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_detail_review, container, false);

        lvRate = (ListView) rootView.findViewById(R.id.lvRate);
        arrayList = new ArrayList<>();

//        DBPlaceTable dbPlaceTable = new DBPlaceTable(getActivity());
//        DBPlace dbPlace = dbPlaceTable.getPlaceByPlaceId()

        DBRatingTable dbRatingTable;
        dbRatingTable = new DBRatingTable(getActivity());

        List<DBRate> dbRates = dbRatingTable.getRatesByPlaceId(FragmentMainList.selectedDBPlace.getPlace_id());
        DBRate dbRate;

        arrayList.clear();

        int len = dbRates.size();
        for (int i = 0; i < len; i++) {
            dbRate = dbRates.get(i);

            arrayList.add(dbRate);
        }

        lvRate.setAdapter(new ReviewAdapter(getContext(), arrayList));

        lvRate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(getContext(), "Okay", Toast.LENGTH_SHORT).show();
            }
        });


        tvTotalRatingValue = (TextView) rootView.findViewById(R.id.tvTotalRatingValue);
        tvTotalRatingValue.setText(FragmentMainList.selectedDBPlace.getRating());
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {

        if(lvRate != null) {
            DBRatingTable dbRatingTable;
            dbRatingTable = new DBRatingTable(getActivity());

            List<DBRate> dbRates = dbRatingTable.getRatesByPlaceId(FragmentMainList.selectedDBPlace.getPlace_id());
            DBRate dbRate;

            arrayList.clear();

            int len = dbRates.size();
            for (int i = 0; i < len; i++) {
                dbRate = dbRates.get(i);

                arrayList.add(dbRate);
            }

            lvRate.setAdapter(new ReviewAdapter(getContext(), arrayList));

            tvTotalRatingValue.setText(FragmentMainList.selectedDBPlace.getRating());
        }

        super.onResume();
    }
}
