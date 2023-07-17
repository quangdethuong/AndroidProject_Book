package com.codingstuff.BookApp.utils.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codingstuff.BookApp.R;
import com.codingstuff.BookApp.utils.model.ItemCart;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHodler> {

    private CartClickedListeners cartClickedListeners;
    private List<ItemCart> itemCartList;

    public CartAdapter(CartClickedListeners cartClickedListeners) {
        this.cartClickedListeners = cartClickedListeners;
    }

    public void setShoeCartList(List<ItemCart> itemCartList) {
        this.itemCartList = itemCartList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CartViewHodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.each_cart_item, parent, false);
        return new CartViewHodler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHodler holder, int position) {

        ItemCart itemCart = itemCartList.get(position);
        Glide.with(holder.itemView.getContext())
                .load(itemCart.getShoeImage())
                .into(holder.shoeImageView);
        holder.shoeNameTv.setText(itemCart.getShoeName());
        holder.shoeBrandNameTv.setText(itemCart.getShoeBrandName());
//        holder.shoeSize.setText("Size: " + shoeCart.getShoeSize());
        holder.shoeQuantity.setText(itemCart.getQuantity() + "");
        holder.shoePriceTv.setText("Price: " + itemCart.getTotalItemPrice() + "$");


        holder.deleteShoeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cartClickedListeners.onDeleteClicked(itemCart);
            }
        });


        holder.addQuantityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cartClickedListeners.onPlusClicked(itemCart);
            }
        });

        holder.minusQuantityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cartClickedListeners.onMinusClicked(itemCart);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (itemCartList == null) {
            return 0;
        } else {
            return itemCartList.size();
        }
    }

    public class CartViewHodler extends RecyclerView.ViewHolder {

        private TextView shoeNameTv, shoeBrandNameTv, shoePriceTv, shoeQuantity, shoeSize;
        private ImageView deleteShoeBtn;
        private ImageView shoeImageView;
        private ImageButton addQuantityBtn, minusQuantityBtn;

        public CartViewHodler(@NonNull View itemView) {
            super(itemView);

            shoeNameTv = itemView.findViewById(R.id.eachCartItemName);
            shoeBrandNameTv = itemView.findViewById(R.id.eachCartItemBrandNameTv);
            shoePriceTv = itemView.findViewById(R.id.eachCartItemPriceTv);
//            shoeSize = itemView.findViewById(R.id.eachCartItemShoeSize);
            deleteShoeBtn = itemView.findViewById(R.id.eachCartItemDeleteBtn);
            shoeImageView = itemView.findViewById(R.id.eachCartItemIV);
            shoeQuantity = itemView.findViewById(R.id.eachCartItemQuantityTV);
            addQuantityBtn = itemView.findViewById(R.id.eachCartItemAddQuantityBtn);
            minusQuantityBtn = itemView.findViewById(R.id.eachCartItemMinusQuantityBtn);
        }
    }

    public interface CartClickedListeners {
        void onDeleteClicked(ItemCart itemCart);

        void onPlusClicked(ItemCart itemCart);

        void onMinusClicked(ItemCart itemCart);
    }
}
