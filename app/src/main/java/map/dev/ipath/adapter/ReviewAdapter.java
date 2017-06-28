package map.dev.ipath.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

//import com.example.mapdemo.R;
import map.dev.ipath.model.DBRate;
import map.dev.ipath.model.ReviewModel;

import java.util.ArrayList;

import map.dev.ipath.R;

//import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;

/**
 * Created by adrian on 12.03.2017.
 */

public class ReviewAdapter extends BaseAdapter{

    Context context;
    ArrayList<DBRate> arrayList;

    public ReviewAdapter(Context context, ArrayList<DBRate> arrayList) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.row_item_reviews, null);

        DBRate dbRate = (DBRate) getItem(position);

        TextView tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
        RatingBar ratingBar = (RatingBar) convertView.findViewById(R.id.ratingBar);
        TextView tvReviewContent = (TextView) convertView.findViewById(R.id.tvReviewContent);

        tvUserName.setText(dbRate.getUsername());
        ratingBar.setRating(Float.parseFloat(dbRate.getValue()));
        tvReviewContent.setText(dbRate.getContent());

        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/lucida_calligraphy_italic.ttf");
        tvUserName.setTypeface(typeface);
        tvReviewContent.setTypeface(typeface);

        return convertView;
    }
}
