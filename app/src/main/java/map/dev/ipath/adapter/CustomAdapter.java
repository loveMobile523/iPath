package map.dev.ipath.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import map.dev.ipath.R;
import map.dev.ipath.model.RowItem;

/**
 * Created by adrian on 21.03.2017.
 */

public class CustomAdapter extends ArrayAdapter<RowItem>{
    LayoutInflater flater;

    // spinner view

    public CustomAdapter(Activity context, int resouceId, int textviewId, List<RowItem> list){

        super(context,resouceId,textviewId, list);
        flater = context.getLayoutInflater();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        RowItem rowItem = getItem(position);

        View rowview = flater.inflate(R.layout.row_item_spinner_category,null,true);

        TextView tvTitle = (TextView) rowview.findViewById(R.id.tvTitle);
        tvTitle.setText(rowItem.getTitle());

        ImageView imgCategory = (ImageView) rowview.findViewById(R.id.imgCategory);
        imgCategory.setImageResource(rowItem.getImageId());

        return rowview;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = flater.inflate(R.layout.row_item_spinner_category,parent, false);
        }
        RowItem rowItem = getItem(position);

        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        tvTitle.setText(rowItem.getTitle());

        ImageView imgCategory = (ImageView) convertView.findViewById(R.id.imgCategory);
        imgCategory.setImageResource(rowItem.getImageId());

        return convertView;
    }
}
