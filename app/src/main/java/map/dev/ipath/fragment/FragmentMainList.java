package map.dev.ipath.fragment;

        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.support.v4.app.Fragment;
        import android.os.Bundle;
        import android.support.annotation.Nullable;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.ListView;
        import android.widget.SearchView;
        import android.widget.Toast;

        import map.dev.ipath.AddPlaceActivity;
        import map.dev.ipath.DetailReviewActivity;
//        import com.example.mapdemo.R;
        import map.dev.ipath.SignUpActivity;
        import map.dev.ipath.adapter.FilterPlaceAdater;
        import map.dev.ipath.constant.Constant;
        import map.dev.ipath.constant.DataOfDB;
        import map.dev.ipath.database.DBPlaceTable;
        import map.dev.ipath.helper.PlacesDataSource;
        import map.dev.ipath.model.DBPlace;
        import map.dev.ipath.model.FilterPlaceModel;

        import java.util.ArrayList;
        import java.util.List;
        import java.util.Random;

        import map.dev.ipath.R;

        import static android.content.Context.MODE_PRIVATE;
        import static map.dev.ipath.helper.MySQLiteHelper.TABLE_PLACES;

/**
 * Created by adrian on 10.03.2017.
 */

public class FragmentMainList extends Fragment{
    private SharedPreferences.Editor editor;
    private SharedPreferences prefs;

    private ListView lvListPlace;
    ArrayList<DBPlace> arrayList;

    public static DBPlace selectedDBPlace;

    private SearchView searchView;

    // database
//    private PlacesDataSource placesDataSource;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main_list, container, false);

        editor = getActivity().getSharedPreferences(Constant.MyShared, MODE_PRIVATE).edit();
        prefs = getActivity().getSharedPreferences(Constant.MyShared, MODE_PRIVATE);

        lvListPlace = (ListView) rootView.findViewById(R.id.lvListPlace);
        arrayList = new ArrayList<>();

        DBPlaceTable db;
        db = new DBPlaceTable(getActivity());

        List<DBPlace> dbPlaces = db.getAllPlaces();
        DBPlace dbPlace;

        arrayList.clear();

        int len = dbPlaces.size();
        for (int i = 0; i < len; i++) {
            dbPlace = dbPlaces.get(i);

            arrayList.add(dbPlace);
        }

        lvListPlace.setAdapter(new FilterPlaceAdater(getContext(), arrayList));
//        adapter.notifyDataSetChanged();

        initWidget(rootView);
        onSearchViewAction();

        return rootView;
    }

    private void initWidget(ViewGroup viewGroup) {

        searchView = (SearchView) viewGroup.findViewById(R.id.searchView);

    }

    private void onSearchViewAction() {
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // when have focus on/off
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // when click the search button

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // real time
                DBPlaceTable db;
                db = new DBPlaceTable(getActivity());

                List<DBPlace> dbPlaces = db.getAllPlaces();
                DBPlace dbPlace;

                arrayList.clear();

                int len = dbPlaces.size();
                for (int i = 0; i < len; i++) {
                    dbPlace = dbPlaces.get(i);

                    String placeName = dbPlace.getName();
                    if(placeName.toLowerCase().contains(newText.toLowerCase())) {
                        arrayList.add(dbPlace);
                    }
                }

                lvListPlace.setAdapter(new FilterPlaceAdater(getContext(), arrayList));
                return false;
            }
        });
    }



    @Override
    public void onResume() {
        if(lvListPlace != null) {
            DBPlaceTable db;
            db = new DBPlaceTable(getActivity());

            List<DBPlace> dbPlaces = db.getAllPlaces();
            DBPlace dbPlace;

            arrayList.clear();

            int len = dbPlaces.size();
            for (int i = 0; i < len; i++) {
                dbPlace = dbPlaces.get(i);

                arrayList.add(dbPlace);
            }

            lvListPlace.setAdapter(new FilterPlaceAdater(getContext(), arrayList));
        }

//        placesDataSource.open();
        super.onResume();
    }

    @Override
    public void onPause() {
//        placesDataSource.close();
        super.onPause();
    }

    // ---------------------------------------------------------------------------------------------
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lvListPlace.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getActivity(), DetailReviewActivity.class));

                editor.putString(Constant.FromTag, Constant.FromMain);
                editor.commit();

//                TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
//                String placeName = tvTitle.getText().toString();

                selectedDBPlace = arrayList.get(position);
                String placeName = selectedDBPlace.getName();

                Constant.SelectedPlaceName = placeName;
                Constant.SelectedPlaceRating = selectedDBPlace.getRating();

//                getActivity().finish();
            }
        });
    }
}
