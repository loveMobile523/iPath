package map.dev.ipath.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

//import com.example.mapdemo.R;

import java.util.ArrayList;
import java.util.List;

import map.dev.ipath.DetailReviewActivity;
import map.dev.ipath.R;
import map.dev.ipath.adapter.FavoriteAdapter;
import map.dev.ipath.adapter.FilterPlaceAdater;
import map.dev.ipath.constant.Constant;
import map.dev.ipath.database.DBFavoriteTable;
import map.dev.ipath.database.DBPlaceTable;
import map.dev.ipath.model.DBFavorite;
import map.dev.ipath.model.DBPlace;

/**
 * Created by adrian on 10.03.2017.
 */

public class FragmentMainFavorite extends Fragment{

    private ListView lvFavorite;
    ArrayList<DBPlace> arrayList;

    public static DBPlace selectedFavorite;

    public FragmentMainFavorite(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main_favorite, container, false);

        lvFavorite = (ListView) rootView.findViewById(R.id.lvFavorite);
        arrayList = new ArrayList<>();


        DBFavoriteTable dbFavoriteTable = new DBFavoriteTable(getActivity());
        List<DBFavorite> dbFavorites = dbFavoriteTable.getAllFavorites();

        DBPlaceTable dbPlaceTable;
        dbPlaceTable = new DBPlaceTable(getActivity());

        DBPlace dbPlace;

        arrayList.clear();

        int len = dbFavorites.size();
        for (int i = 0; i < len; i++) {
            String favoritePlaceName = dbFavorites.get(i).getPlacename();

            dbPlace = dbPlaceTable.getPlaceByPlaceName(favoritePlaceName);
            arrayList.add(dbPlace);
        }

        lvFavorite.setAdapter(new FavoriteAdapter(getContext(), arrayList));

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lvFavorite.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                startActivity(new Intent(getActivity(), DetailReviewActivity.class));

//                selectedFavorite = arrayList.get(position);
//                String placeName = selectedFavorite.getName();
//
//                Constant.SelectedPlaceName = placeName;
//                Constant.SelectedPlaceRating = selectedFavorite.getRating();
            }
        });
    }
}
