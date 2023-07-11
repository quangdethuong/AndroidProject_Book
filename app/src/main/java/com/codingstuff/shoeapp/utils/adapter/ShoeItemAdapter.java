package com.codingstuff.shoeapp.utils.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codingstuff.shoeapp.R;
import com.codingstuff.shoeapp.utils.model.Item;

import java.util.List;

public class ShoeItemAdapter extends RecyclerView.Adapter<ShoeItemAdapter.ItemViewHolder> {

    private List<Item> ItemList;
    private ItemClickedListeners itemClickedListeners;
    public ShoeItemAdapter(ItemClickedListeners itemClickedListeners){
        this.itemClickedListeners = itemClickedListeners;
    }
    public void setItemList(List<Item> ItemList){
        this.ItemList = ItemList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.each_shoe , parent , false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = ItemList.get(position);
        holder.shoeNameTv.setText(item.getProductName());
        holder.shoeBrandNameTv.setText(item.getCategory());
        holder.shoePriceTv.setText(String.valueOf(item.getPrice()+"$"));

        Glide.with(holder.itemView.getContext())
                .load(item.getImage())
                .into(holder.shoeImageView);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickedListeners.onCardClicked(item);

            }
        });

    }


    @Override
    public int getItemCount() {
        if (ItemList == null){
            return 0;
        }else{
            return ItemList.size();
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        private ImageView shoeImageView , addToCartBtn;
        private TextView shoeNameTv, shoeBrandNameTv, shoePriceTv;
        private CardView cardView;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.eachShoeCardView);
            shoeNameTv = itemView.findViewById(R.id.eachShoeName);
            shoeImageView = itemView.findViewById(R.id.eachShoeIv);
            shoeBrandNameTv = itemView.findViewById(R.id.eachShoeBrandNameTv);
            shoePriceTv = itemView.findViewById(R.id.eachShoePriceTv);
        }
    }

    public interface ItemClickedListeners{
        void onCardClicked(Item shoe);
    }
}
