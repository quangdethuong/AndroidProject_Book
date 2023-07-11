package com.codingstuff.shoeapp.utils.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.codingstuff.shoeapp.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private List<String> categoryList;
    private CategoryClickedListeners categoryClickedListeners;

    public CategoryAdapter(CategoryClickedListeners categoryClickedListeners) {
        this.categoryClickedListeners = categoryClickedListeners;
    }

    public void setCategoryList(List<String> categoryList){
        this.categoryList = categoryList;
        notifyDataSetChanged();
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        String cate = categoryList.get(position);
        int res = 0;
        try {
            res = R.drawable.class.getField(cate).getInt(null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            // if no icon is found
            holder.categoryImage.setImageResource(R.drawable.psychology);
        }
        holder.categoryImage.setImageResource(res);
        holder.categoryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryClickedListeners.onCategoryClicked(cate);
            }
        });
    }
    public static int getImageId(Context context, String imageName) {
        return context.getResources().getIdentifier("drawable/" + imageName, null, context.getPackageName());
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }
    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView categoryImage;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            categoryImage = itemView.findViewById(R.id.categoryIconImageView);
        }

        /*public void bind(String categoryName) {
            categoryImage.setImageResource(Integer.parseInt("R.id."+categoryName));
        }*/
    }
    public interface CategoryClickedListeners{
        void onCategoryClicked(String category);
    }
}

