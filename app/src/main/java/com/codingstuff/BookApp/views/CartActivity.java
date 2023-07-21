package com.codingstuff.BookApp.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.codingstuff.BookApp.R;
import com.codingstuff.BookApp.utils.adapter.CartAdapter;
import com.codingstuff.BookApp.utils.model.ItemCart;
import com.codingstuff.BookApp.viewmodel.CartViewModel;

import java.util.List;

public class CartActivity extends AppCompatActivity implements CartAdapter.CartClickedListeners {

    private RecyclerView recyclerView;
    private CartViewModel cartViewModel;
    private TextView totalCartPriceTv, textView;
    private AppCompatButton checkoutBtn;
    private CardView cardView;
    private CartAdapter cartAdapter;

    public static double price = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        initializeVariables();

        cartViewModel.getAllCartItems().observe(this, new Observer<List<ItemCart>>() {
            @Override
            public void onChanged(List<ItemCart> itemCarts) {
                cartAdapter.setShoeCartList(itemCarts);
                for (int i = 0; i< itemCarts.size(); i++){
                    price = price + itemCarts.get(i).getTotalItemPrice();
                }
                totalCartPriceTv.setText(String.valueOf(String.format("%.2f", price)));
            }
        });

        checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                 cartViewModel.deleteAllCartItems();
//                textView.setVisibility(View.INVISIBLE);
//                checkoutBtn.setVisibility(View.INVISIBLE);
//                totalCartPriceTv.setVisibility(View.INVISIBLE);
//                cardView.setVisibility(View.VISIBLE);
                Intent intent = new Intent(CartActivity.this, CheckOutActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    private void initializeVariables() {

        cartAdapter = new CartAdapter(this);
        textView = findViewById(R.id.textView2);
        cardView = findViewById(R.id.cartActivityCardView);
        totalCartPriceTv = findViewById(R.id.cartActivityTotalPriceTv);
        checkoutBtn = findViewById(R.id.cartActivityCheckoutBtn);
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);
        recyclerView = findViewById(R.id.cartRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(cartAdapter);

    }

    @Override
    public void onDeleteClicked(ItemCart itemCart) {
        cartViewModel.deleteCartItem(itemCart);
    }

    @Override
    public void onPlusClicked(ItemCart itemCart) {
        int quantity = itemCart.getQuantity() + 1;
        cartViewModel.updateQuantity(itemCart.getId() , quantity);
        cartViewModel.updatePrice(itemCart.getId() , quantity* itemCart.getPrice());
        cartAdapter.notifyDataSetChanged();
    }

    @Override
    public void onMinusClicked(ItemCart itemCart) {
        int quantity = itemCart.getQuantity() - 1;
        if (quantity != 0){
            cartViewModel.updateQuantity(itemCart.getId() , quantity);
            cartViewModel.updatePrice(itemCart.getId() , quantity* itemCart.getPrice());
            cartAdapter.notifyDataSetChanged();
        }else{
            cartViewModel.deleteCartItem(itemCart);
        }

    }
}