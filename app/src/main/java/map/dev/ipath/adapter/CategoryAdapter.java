package map.dev.ipath.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import map.dev.ipath.AddPlaceActivity;
import map.dev.ipath.model.Category;

import java.util.ArrayList;

import map.dev.ipath.R;
//import com.example.mapdemo.R;

/**
 * Created by adrian on 12.03.2017.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryHolder>{

    Context mContext;
    ArrayList<Category> itemData = new ArrayList<>();

    public CategoryAdapter(Context mContext, ArrayList<Category> arrayData) {
        super();
        this.mContext = mContext;
        this.itemData = arrayData;
    }

    @Override
    public CategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.row_item_category, parent, false);
        CategoryHolder viewHolder = new CategoryHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CategoryHolder holder, int position) {
        Category category = itemData.get(position);

        String filterTitle = category.getTitle();
        boolean selectedState = category.isJudgeSelected();

        holder.tvTitle.setText(filterTitle);

        if(selectedState) {
            holder.imgCategory.setImageResource(AddPlaceActivity.images[position]);
        } else {
            holder.imgCategory.setImageResource(AddPlaceActivity.grayImages[position]);
        }
    }

    @Override
    public int getItemCount() {
        return this.itemData.size();
    }

    public class CategoryHolder extends RecyclerView.ViewHolder {
        public ImageView imgCategory;
        public TextView tvTitle;

        public CategoryHolder(View itemView) {
            super(itemView);
            imgCategory = (ImageView) itemView.findViewById(R.id.imgCategory);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
        }
    }
}
