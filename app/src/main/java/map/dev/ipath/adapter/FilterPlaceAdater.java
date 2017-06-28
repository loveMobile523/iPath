package map.dev.ipath.adapter;

import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import map.dev.ipath.AddPlaceActivity;
import map.dev.ipath.R;

//import com.example.mapdemo.R;
import map.dev.ipath.constant.Constant;
import map.dev.ipath.model.DBPlace;
import map.dev.ipath.model.FilterPlaceModel;

/**
 * Created by adrian on 11.03.2017.
 */

public class FilterPlaceAdater extends BaseAdapter{

    Context context;
    ArrayList<DBPlace> arrayList;

    public FilterPlaceAdater(Context context, ArrayList<DBPlace> arrayList){
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(context).inflate(R.layout.row_item_filter_places, null);

        DBPlace dbPlace = (DBPlace) getItem(position);

        ImageView imgCategory = (ImageView) convertView.findViewById(R.id.imgCategory);

        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        RatingBar ratingBar = (RatingBar) convertView.findViewById(R.id.ratingBar);
        TextView tvDistanceDescription = (TextView) convertView.findViewById(R.id.tvDistanceDescription);
        TextView tvDistanceValue = (TextView) convertView.findViewById(R.id.tvDistanceValue);


        imgCategory.setImageResource(AddPlaceActivity.images[getCategoryID(dbPlace.getCategory_name())]);


        tvTitle.setText(dbPlace.getName());
        ratingBar.setRating(Float.parseFloat(dbPlace.getRating()));
        tvDistanceDescription.setText(dbPlace.getAddress());

//        LatLng latLng = new LatLng(Double.parseDouble(dbPlace.getLatitude()), Double.parseDouble(dbPlace.getLongitude()));

        float[] results = new float[1];
        Location.distanceBetween(Constant.CurrentPosition.latitude, Constant.CurrentPosition.longitude,
                Double.parseDouble(dbPlace.getLatitude()), Double.parseDouble(dbPlace.getLongitude()),
                results);
//        Toast.makeText(context, String.valueOf(results[0]) + " m", Toast.LENGTH_SHORT).show();

        tvDistanceValue.setText(String.valueOf(results[0] / Constant.MToMile));

        return convertView;
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
}
